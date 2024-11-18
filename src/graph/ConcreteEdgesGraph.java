package graph;

import java.util.*;

/**
 * A mutable weighted directed graph implementation using edges.
 * 
 * Representation:
 * - The graph is represented by a set of vertices and a list of edges.
 * - Each edge is an immutable object representing a directed, weighted edge.
 * 
 * @param <L> type of vertex labels in this graph, must be immutable
 */
public class ConcreteEdgesGraph<L> implements Graph<L> {
    
    private final Set<L> vertices = new HashSet<>();
    private final List<Edge<L>> edges = new ArrayList<>();
    
    // Abstraction Function:
    //   - vertices represents the set of vertices in the graph.
    //   - edges represents all the directed edges between the vertices.
    //
    // Representation Invariant:
    //   - vertices contains no duplicates and only valid vertex labels.
    //   - edges contains no duplicate edges, and each edge connects vertices in `vertices`.
    //   - All edge weights are positive (> 0).
    //
    // Safety from Representation Exposure:
    //   - The vertices and edges collections are private and final.
    //   - Defensive copies are used when returning mutable data.

    private void checkRep() {
        for (Edge<L> edge : edges) {
            assert vertices.contains(edge.getSource());
            assert vertices.contains(edge.getTarget());
            assert edge.getWeight() > 0;
        }
    }
    
    @Override
    public boolean add(L vertex) {
        if (vertices.add(vertex)) {
            checkRep();
            return true;
        }
        return false;
    }
    
    @Override
    public int set(L source, L target, int weight) {
        if (!vertices.contains(source)) vertices.add(source);
        if (!vertices.contains(target)) vertices.add(target);
        
        for (Edge<L> edge : edges) {
            if (edge.getSource().equals(source) && edge.getTarget().equals(target)) {
                int oldWeight = edge.getWeight();
                edges.remove(edge);
                if (weight > 0) {
                    edges.add(new Edge<>(source, target, weight));
                }
                checkRep();
                return oldWeight;
            }
        }
        
        if (weight > 0) {
            edges.add(new Edge<>(source, target, weight));
        }
        checkRep();
        return 0;
    }
    
    @Override
    public boolean remove(L vertex) {
        if (!vertices.remove(vertex)) return false;
        edges.removeIf(edge -> edge.getSource().equals(vertex) || edge.getTarget().equals(vertex));
        checkRep();
        return true;
    }
    
    @Override
    public Set<L> vertices() {
        return new HashSet<>(vertices);
    }
    
    @Override
    public Map<L, Integer> sources(L target) {
        Map<L, Integer> result = new HashMap<>();
        for (Edge<L> edge : edges) {
            if (edge.getTarget().equals(target)) {
                result.put(edge.getSource(), edge.getWeight());
            }
        }
        return result;
    }
    
    @Override
    public Map<L, Integer> targets(L source) {
        Map<L, Integer> result = new HashMap<>();
        for (Edge<L> edge : edges) {
            if (edge.getSource().equals(source)) {
                result.put(edge.getTarget(), edge.getWeight());
            }
        }
        return result;
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Vertices: ").append(vertices).append("\n");
        builder.append("Edges: ").append(edges);
        return builder.toString();
    }
}

/**
 * Immutable class representing a directed, weighted edge.
 */
class Edge<L> {
    private final L source;
    private final L target;
    private final int weight;

    // Abstraction Function:
    //   - Represents a directed edge from `source` to `target` with a weight `weight`.
    //
    // Representation Invariant:
    //   - weight > 0, source != null, target != null.
    //
    // Safety from Representation Exposure:
    //   - All fields are private and final.

    public Edge(L source, L target, int weight) {
        this.source = source;
        this.target = target;
        this.weight = weight;
        checkRep();
    }

    private void checkRep() {
        assert source != null && target != null;
        assert weight > 0;
    }

    public L getSource() {
        return source;
    }

    public L getTarget() {
        return target;
    }

    public int getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return String.format("%s -> %s (weight: %d)", source, target, weight);
    }
}
