package poet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import graph.Graph;

public class GraphPoet {
    
    private final Graph<String> graph = Graph.empty();

    // Abstraction function:
    //   Represents an affinity graph for poetry generation. Vertices are words (case-insensitive),
    //   and edges indicate the number of times a word follows another in the input corpus.
    // Representation invariant:
    //   - Graph is not null.
    //   - Vertices are non-null, non-empty strings.
    // Safety from rep exposure:
    //   - The graph is private and final. Methods do not leak direct references to it.

    /**
     * Create a new poet with the graph from corpus (as described above).
     * 
     * @param corpus text file from which to derive the poet's affinity graph
     * @throws IOException if the corpus file cannot be found or read
     */
    public GraphPoet(File corpus) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(corpus))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] words = line.toLowerCase().split("\\s+");
                for (int i = 0; i < words.length - 1; i++) {
                    String word1 = words[i];
                    String word2 = words[i + 1];
                    graph.add(word1);
                    graph.add(word2);
                    int currentWeight = graph.set(word1, word2, 0);
                    graph.set(word1, word2, currentWeight + 1);
                }
            }
        }
        checkRep();
    }

    
    /**
     * Generate a poem.
     * 
     * @param input string from which to create the poem
     * @return poem (as described above)
     */
    public String poem(String input) {
        String[] words = input.split("\\s+");
        StringBuilder poemBuilder = new StringBuilder();

        for (int i = 0; i < words.length - 1; i++) {
            String word1 = words[i].toLowerCase();
            String word2 = words[i + 1].toLowerCase();
            poemBuilder.append(words[i]).append(" ");

            String bridgeWord = findBridgeWord(word1, word2);
            if (bridgeWord != null) {
                poemBuilder.append(bridgeWord).append(" ");
            }
        }

        poemBuilder.append(words[words.length - 1]);
        return poemBuilder.toString().trim();
    }

    
    private String findBridgeWord(String word1, String word2) {
        int maxWeight = 0;
        String bestBridge = null;

        for (String potentialBridge : graph.vertices()) {
            int weight1 = graph.sources(potentialBridge).getOrDefault(word1, 0);
            int weight2 = graph.targets(potentialBridge).getOrDefault(word2, 0);
            int totalWeight = weight1 + weight2;

            // Prefer bridges with the highest weight
            if (totalWeight > maxWeight) {
                maxWeight = totalWeight;
                bestBridge = potentialBridge;
            }
        }
        return bestBridge;
    }



    
    private void checkRep() {
        assert graph != null : "Graph should not be null";
        for (String vertex : graph.vertices()) {
            assert vertex != null && !vertex.isEmpty() : "Vertex should be a non-empty string";
        }
    }
}
