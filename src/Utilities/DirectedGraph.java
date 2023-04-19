package Utilities;

import java.io.*;
import java.util.*;

public class DirectedGraph {
    private int numVertices; // Anzahl der Knoten im Graphen
    private Map<Integer, List<Integer>> adjacencyList; // Adjazenzliste des Graphen

    public DirectedGraph(int numVertices) {
        this.numVertices = numVertices;
        adjacencyList = new HashMap<>();
        for (int i = 0; i < numVertices; i++) {
            adjacencyList.put(i, new ArrayList<>());
        }
    }

    public DirectedGraph(String filePath) {
        try (Scanner scanner = new Scanner(new File(filePath))) {
            numVertices = scanner.nextInt();
            adjacencyList = new HashMap<>();
            for (int i = 0; i < numVertices; i++) {
                adjacencyList.put(i, new ArrayList<>());
            }
            scanner.nextLine();
            int vertex = 0;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] tokens = line.split(" ");
                for (String token : tokens) {
                    int neighbor = Integer.parseInt(token);
                    adjacencyList.get(vertex).add(neighbor);
                }
                vertex++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void AddEdge(int source, int destination) {
        adjacencyList.get(source).add(destination);
    }

    public List<Integer> GetAdjacentVertices(int vertex) {
        return adjacencyList.get(vertex);
    }

    public void SaveToFile(String filePath) {
        Utilities.Log(filePath);
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                file.createNewFile();
            }
            PrintWriter writer = new PrintWriter(new FileWriter(file));
            writer.println(numVertices);
            for (int i = 0; i < numVertices; i++) {
                List<Integer> neighbors = adjacencyList.get(i);
                StringBuilder sb = new StringBuilder();
                for (int j : neighbors) {
                    sb.append(j).append(" ");
                }
                writer.println(sb.toString().trim());
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
