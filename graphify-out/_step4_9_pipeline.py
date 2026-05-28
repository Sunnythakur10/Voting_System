import json
import re
from pathlib import Path
from datetime import datetime, timezone
from graphify.build import build_from_json
from graphify.cluster import cluster, score_all
from graphify.analyze import god_nodes, surprising_connections, suggest_questions
from graphify.report import generate
from graphify.export import to_json, to_html
from graphify.detect import save_manifest

input_path = '.'
extraction = json.loads(Path('graphify-out/.graphify_extract.json').read_text())
detection = json.loads(Path('graphify-out/.graphify_detect.json').read_text())

G = build_from_json(extraction)
communities = cluster(G)
cohesion = score_all(G, communities)
tokens = {'input': extraction.get('input_tokens', 0), 'output': extraction.get('output_tokens', 0)}
gods = god_nodes(G)
surprises = surprising_connections(G, communities)
labels_placeholder = {cid: 'Community ' + str(cid) for cid in communities}
questions = suggest_questions(G, communities, labels_placeholder)

report = generate(G, communities, cohesion, labels_placeholder, gods, surprises, detection, tokens, input_path, suggested_questions=questions)
Path('graphify-out/GRAPH_REPORT.md').write_text(report)
to_json(G, communities, 'graphify-out/graph.json')

analysis = {
    'communities': {str(k): v for k, v in communities.items()},
    'cohesion': {str(k): v for k, v in cohesion.items()},
    'gods': gods,
    'surprises': surprises,
    'questions': questions,
}
Path('graphify-out/.graphify_analysis.json').write_text(json.dumps(analysis, indent=2))

if G.number_of_nodes() == 0:
    print('ERROR: Graph is empty - extraction produced no nodes.')
    raise SystemExit(1)
print('Graph:', G.number_of_nodes(), 'nodes,', G.number_of_edges(), 'edges,', len(communities), 'communities')

# Step 5: label communities automatically from dominant tokens in node labels
node_labels = {n: (d.get('label') or '') for n, d in G.nodes(data=True)}
stop = {'the','and','for','with','from','this','that','into','using','user','users','service','controller','repository','model','config'}
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
        phrase = ' '.join([w.capitalize() for w, _ in top])
        labels[cid] = phrase if phrase else f'Community {cid}'
    else:
        labels[cid] = f'Community {cid}'

questions = suggest_questions(G, communities, labels)
report = generate(G, communities, cohesion, labels, gods, surprises, detection, tokens, input_path, suggested_questions=questions)
Path('graphify-out/GRAPH_REPORT.md').write_text(report)
Path('graphify-out/.graphify_labels.json').write_text(json.dumps({str(k): v for k, v in labels.items()}))
print('Report updated with community labels')

# Step 6: HTML
if G.number_of_nodes() > 5000:
    print('Graph has', G.number_of_nodes(), 'nodes - too large for HTML viz. Use Obsidian vault instead.')
else:
    to_html(G, communities, 'graphify-out/graph.html', community_labels=labels or None)
    print('graph.html written - open in any browser, no server needed')

# Step 9 manifest + cost
save_manifest(detection.get('files', {}))
input_tok = extraction.get('input_tokens', 0)
output_tok = extraction.get('output_tokens', 0)

cost_path = Path('graphify-out/cost.json')
if cost_path.exists():
    cost = json.loads(cost_path.read_text())
else:
    cost = {'runs': [], 'total_input_tokens': 0, 'total_output_tokens': 0}

cost['runs'].append({
    'date': datetime.now(timezone.utc).isoformat(),
    'input_tokens': input_tok,
    'output_tokens': output_tok,
    'files': detection.get('total_files', 0),
})
cost['total_input_tokens'] += input_tok
cost['total_output_tokens'] += output_tok
cost_path.write_text(json.dumps(cost, indent=2))
print(f'This run: {input_tok:,} input tokens, {output_tok:,} output tokens')
print(f'All time: {cost["total_input_tokens"]:,} input, {cost["total_output_tokens"]:,} output ({len(cost["runs"])} runs)')
