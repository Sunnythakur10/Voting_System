import json
from pathlib import Path
from graphify.extract import collect_files, extract
from graphify.cache import save_semantic_cache

# Ensure semantic_new exists (from chunk file if needed)
chunk = Path('graphify-out/.graphify_chunk_01.json')
sem_new_path = Path('graphify-out/.graphify_semantic_new.json')
if (not sem_new_path.exists()) and chunk.exists():
    sem_new_path.write_text(chunk.read_text())

# AST extraction
detect = json.loads(Path('graphify-out/.graphify_detect.json').read_text())
code_files = []
for f in detect.get('files', {}).get('code', []):
    p = Path(f)
    code_files.extend(collect_files(p) if p.is_dir() else [p])

if code_files:
    result = extract(code_files)
    Path('graphify-out/.graphify_ast.json').write_text(json.dumps(result, indent=2))
    print('AST:', len(result.get('nodes', [])), 'nodes,', len(result.get('edges', [])), 'edges')
else:
    Path('graphify-out/.graphify_ast.json').write_text(json.dumps({'nodes': [], 'edges': [], 'input_tokens': 0, 'output_tokens': 0}))
    print('No code files - skipping AST extraction')

# Save semantic cache and merge semantic
new = json.loads(sem_new_path.read_text()) if sem_new_path.exists() else {'nodes': [], 'edges': [], 'hyperedges': []}
saved = save_semantic_cache(new.get('nodes', []), new.get('edges', []), new.get('hyperedges', []))
print('Cached', saved, 'files')

cached_path = Path('graphify-out/.graphify_cached.json')
cached = json.loads(cached_path.read_text()) if cached_path.exists() else {'nodes': [], 'edges': [], 'hyperedges': []}
all_nodes = cached.get('nodes', []) + new.get('nodes', [])
all_edges = cached.get('edges', []) + new.get('edges', [])
all_hyperedges = cached.get('hyperedges', []) + new.get('hyperedges', [])

seen = set()
deduped = []
for n in all_nodes:
    nid = n.get('id')
    if nid and nid not in seen:
        seen.add(nid)
        deduped.append(n)

sem_merged = {
    'nodes': deduped,
    'edges': all_edges,
    'hyperedges': all_hyperedges,
    'input_tokens': new.get('input_tokens', 0),
    'output_tokens': new.get('output_tokens', 0),
}
Path('graphify-out/.graphify_semantic.json').write_text(json.dumps(sem_merged, indent=2))
print('Extraction complete -', len(deduped), 'nodes,', len(all_edges), 'edges (', len(cached.get('nodes', [])), 'from cache,', len(new.get('nodes', [])), 'new)')

# Merge AST + semantic
ast = json.loads(Path('graphify-out/.graphify_ast.json').read_text())
sem = sem_merged
seen = {n.get('id') for n in ast.get('nodes', []) if n.get('id')}
merged_nodes = list(ast.get('nodes', []))
for n in sem.get('nodes', []):
    nid = n.get('id')
    if nid and nid not in seen:
        merged_nodes.append(n)
        seen.add(nid)

merged_edges = ast.get('edges', []) + sem.get('edges', [])
merged_hyperedges = sem.get('hyperedges', [])
merged = {
    'nodes': merged_nodes,
    'edges': merged_edges,
    'hyperedges': merged_hyperedges,
    'input_tokens': sem.get('input_tokens', 0),
    'output_tokens': sem.get('output_tokens', 0),
}
Path('graphify-out/.graphify_extract.json').write_text(json.dumps(merged, indent=2))
print('Merged:', len(merged_nodes), 'nodes,', len(merged_edges), 'edges (', len(ast.get('nodes', [])), 'AST +', len(sem.get('nodes', [])), 'semantic)')
