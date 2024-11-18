package graph;

import java.util.*;

/**
 * A mutable weighted directed graph implementation using vertices.
 * 
 * Representation:
 * - Each vertex is represented by a `Vertex` object, which stores its outgoing edges.
 * - The graph maintains a set of all vertices.
 * 
 * @param <L> type of vertex labels in this graph, must be immutable
 */
public class ConcreteVerticesGraph<L> implements Graph<L> {
    
    private final Set<Vertex<L>> vertices = new HashSet<>();
    
    // Abstraction Function:
    //   - Represents a directed graph where each vertex tracks its own outgoing edges.
    //
    // Representation Invariant:
    //   - No duplicate vertex labels in `vertices`.
    //   - All edge weights are positive (> 0).
    //
    // Safety from Representation Exposure:
    //   - The vertices set is private and final.
    //   - Defensive copies are used when returning mutable data.

    private void checkRep() {
        for (Vertex<L> vertex : vertices) {
            assert vertex != null;
            for (Map.Entry<L, Integer> edge : vertex.getTargets().entrySet()) {
                assert edge.getValue() > 0; // Positive weights
            }
        }
    }
    
    @Override
    public boolean add(L vertexLabel) {
        if (vertices.stream().anyMatch(v -> v.getLabel().equals(vertexLabel))) {
            return false;
        }
        vertices.add(new Vertex<>(vertexLabel));
        checkRep();
        return true;
    }
    
    @Override
    public int set(L source, L target, int weight) {
        Vertex<L> sourceVertex = getOrCreateVertex(source);
        Vertex<L> targetVertex = getOrCreateVertex(target);

        int previousWeight = sourceVertex.setTarget(target, weight);

        if (weight == 0 && sourceVertex.getTargets().isEmpty()) {
            vertices.remove(sourceVertex); // Remove source if it has no edges
        }
        checkRep();
        return previousWeight;
    }
    
    @Override
    public boolean remove(L vertexLabel) {
        Vertex<L> vertexToRemove = getVertex(vertexLabel);
        if (vertexToRemove == null) return false;

        vertices.remove(vertexToRemove);

        for (Vertex<L> vertex : vertices) {
            vertex.removeTarget(vertexLabel);
        }
        checkRep();
        return true;
    }
    
    @Override
    public Set<L> vertices() {
        Set<L> labels = new HashSet<>();
        for (Vertex<L> vertex : vertices) {
            labels.add(vertex.getLabel());
        }
        return labels;
    }
    
    @Override
    public Map<L, Integer> sources(L target) {
        Map<L, Integer> result = new HashMap<>();
        for (Vertex<L> vertex : vertices) {
            Integer weight = vertex.getTargets().get(target);
            if (weight != null) {
                result.put(vertex.getLabel(), weight);
            }
        }
        return result;
    }
    
    @Override
    public Map<L, Integer> targets(L source) {
        Vertex<L> vertex = getVertex(source);
        return vertex == null ? Collections.emptyMap() : new HashMap<>(vertex.getTargets());
    }
    
    private Vertex<L> getVertex(L label) {
        return vertices.stream()
                .filter(vertex -> vertex.getLabel().equals(label))
                .findFirst()
                .orElse(null);
    }
    
    private Vertex<L> getOrCreateVertex(L label) {
        Vertex<L> vertex = getVertex(label);
        if (vertex == null) {
            vertex = new Vertex<>(label);
            vertices.add(vertex);
        }
        return vertex;
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (Vertex<L> vertex : vertices) {
            builder.append(vertex).append("\n");
        }
        return builder.toString();
    }
}

/**
 * Mutable class representing a vertex and its outgoing edges.
 */
class Vertex<L> {
    private final L label;
    private final Map<L, Integer> targets = new HashMap<>();
    
    // Abstraction Function:
    //   - Represents a vertex with outgoing edges stored in `targets`.
    //
    // Representation Invariant:
    //   - All weights in `targets` are positive (> 0).
    //
    // Safety from Representation Exposure:
    //   - `targets` is private and not directly exposed.

    public Vertex(L label) {
        this.label = label;
        checkRep();
    }

    private void checkRep() {
        assert label != null;
        for (int weight : targets.values()) {
            assert weight > 0;
        }
    }
    
    public L getLabel() {
        return label;
    }
    
    public Map<L, Integer> getTargets() {
        return new HashMap<>(targets);
    }
    
    public int setTarget(L target, int weight) {
        if (weight == 0) {
            return targets.remove(target) == null ? 0 : targets.get(target);
        }
        return targets.put(target, weight) == null ? 0 : targets.get(target);
    }
    
    public void removeTarget(L target) {
        targets.remove(target);
    }
    
    @Override
    public String toString() {
        return label + " -> " + targets;
    }
}
