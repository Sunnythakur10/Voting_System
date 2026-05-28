import json
import re
from pathlib import Path
from datetime import datetime, timezone

from graphify.detect import detect, save_manifest
from graphify.extract import collect_files, extract
from graphify.build import build_from_json
from graphify.cluster import cluster, score_all
from graphify.analyze import god_nodes, surprising_connections, suggest_questions
from graphify.report import generate
from graphify.export import to_json, to_html

ROOT = Path('.')
OUT = ROOT / 'graphify-out'
OUT.mkdir(exist_ok=True)

# Step 2: detect
result = detect(Path('.'))
(OUT / '.graphify_detect.json').write_text(json.dumps(result), encoding='utf-8')

# Step 3A: AST extraction
detect_data = result
code_files = []
for f in detect_data.get('files', {}).get('code', []):
    p = Path(f)
    code_files.extend(collect_files(p) if p.is_dir() else [p])
if code_files:
    ast = extract(code_files)
else:
    ast = {'nodes': [], 'edges': [], 'input_tokens': 0, 'output_tokens': 0}
(OUT / '.graphify_ast.json').write_text(json.dumps(ast, indent=2), encoding='utf-8')

# Step 3B/3C: semantic from chunk and merge
chunk = OUT / '.graphify_chunk_01.json'
if chunk.exists():
    sem = json.loads(chunk.read_text())
else:
    sem = {'nodes': [], 'edges': [], 'hyperedges': [], 'input_tokens': 0, 'output_tokens': 0}

seen = {n.get('id') for n in ast.get('nodes', []) if n.get('id')}
merged_nodes = list(ast.get('nodes', []))
for n in sem.get('nodes', []):
    nid = n.get('id')
    if nid and nid not in seen:
        merged_nodes.append(n)
        seen.add(nid)

extract_json = {
    'nodes': merged_nodes,
    'edges': ast.get('edges', []) + sem.get('edges', []),
    'hyperedges': sem.get('hyperedges', []),
    'input_tokens': sem.get('input_tokens', 0),
    'output_tokens': sem.get('output_tokens', 0),
}
(OUT / '.graphify_extract.json').write_text(json.dumps(extract_json, indent=2), encoding='utf-8')

# Step 4: build and analyze
G = build_from_json(extract_json)
if G.number_of_nodes() == 0:
    raise SystemExit('ERROR: Graph is empty - extraction produced no nodes.')

communities = cluster(G)
cohesion = score_all(G, communities)
gods = god_nodes(G)
surprises = surprising_connections(G, communities)

# Step 5: label communities automatically
node_labels = {n: (d.get('label') or '') for n, d in G.nodes(data=True)}
stop = {
    'the', 'and', 'for', 'with', 'from', 'this', 'that', 'into', 'using',
    'user', 'users', 'service', 'controller', 'repository', 'model', 'config'
}
labels = {}
for cid, nodes in communities.items():
    toks = []
    for nid in nodes:
        text = re.sub(r'[^a-zA-Z0-9 ]+', ' ', node_labels.get(nid, '')).lower()
        toks.extend([t for t in text.split() if len(t) > 2 and t not in stop])
    if toks:
        freq = {}
        for t in toks:
            freq[t] = freq.get(t, 0) + 1
        top = sorted(freq.items(), key=lambda x: (-x[1], x[0]))[:3]
        labels[cid] = ' '.join([w.capitalize() for w, _ in top]) or f'Community {cid}'
    else:
        labels[cid] = f'Community {cid}'

questions = suggest_questions(G, communities, labels)

report = generate(
    G,
    communities,
    cohesion,
    labels,
    gods,
    surprises,
    detect_data,
    {'input': extract_json.get('input_tokens', 0), 'output': extract_json.get('output_tokens', 0)},
    '.',
    suggested_questions=questions,
)
(OUT / 'GRAPH_REPORT.md').write_text(report, encoding='utf-8')

analysis = {
    'communities': {str(k): v for k, v in communities.items()},
    'cohesion': {str(k): v for k, v in cohesion.items()},
    'gods': gods,
    'surprises': surprises,
    'questions': questions,
}
(OUT / '.graphify_analysis.json').write_text(json.dumps(analysis, indent=2), encoding='utf-8')
(OUT / '.graphify_labels.json').write_text(json.dumps({str(k): v for k, v in labels.items()}, indent=2), encoding='utf-8')

# Step 6: html
if G.number_of_nodes() <= 5000:
    to_html(G, communities, str(OUT / 'graph.html'), community_labels=labels or None)

to_json(G, communities, str(OUT / 'graph.json'))

# Step 9: manifest + cost
save_manifest(detect_data.get('files', {}))
input_tok = extract_json.get('input_tokens', 0)
output_tok = extract_json.get('output_tokens', 0)
cost_path = OUT / 'cost.json'
if cost_path.exists():
    cost = json.loads(cost_path.read_text())
else:
    cost = {'runs': [], 'total_input_tokens': 0, 'total_output_tokens': 0}

cost['runs'].append({
    'date': datetime.now(timezone.utc).isoformat(),
    'input_tokens': input_tok,
    'output_tokens': output_tok,
    'files': detect_data.get('total_files', 0),
})
cost['total_input_tokens'] += input_tok
cost['total_output_tokens'] += output_tok
cost_path.write_text(json.dumps(cost, indent=2), encoding='utf-8')

print('Graph:', G.number_of_nodes(), 'nodes,', G.number_of_edges(), 'edges,', len(communities), 'communities')
print('This run:', f'{input_tok:,}', 'input tokens,', f'{output_tok:,}', 'output tokens')
print('All time:', f"{cost['total_input_tokens']:,}", 'input,', f"{cost['total_output_tokens']:,}", 'output (', len(cost['runs']), 'runs)')
