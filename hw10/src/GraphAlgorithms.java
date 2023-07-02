import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

/**
 * Your implementation of various different graph algorithms.
 *
 * @author Parker Hyde
 * @version 1.0
 */
public class GraphAlgorithms {

    /**
     * Performs a breadth first search (bfs) on the input graph, starting at
     * {@code start} which represents the starting vertex.
     *
     * When exploring a vertex, make sure to explore in the order that the
     * adjacency list returns the neighbors to you. Failure to do so may cause
     * you to lose points.
     *
     * You may import/use {@code java.util.Set}, {@code java.util.List},
     * {@code java.util.Queue}, and any classes that implement the
     * aforementioned interfaces, as long as it is efficient.
     *
     * The only instance of {@code java.util.Map} that you may use is the
     * adjacency list from {@code graph}. DO NOT create new instances of Map
     * for BFS (storing the adjacency list in a variable is fine).
     *
     * DO NOT modify the structure of the graph. The graph should be unmodified
     * after this method terminates.
     *
     * @throws IllegalArgumentException if any input
     *  is null, or if {@code start} doesn't exist in the graph
     * @param <T> the generic typing of the data
     * @param start the vertex to begin the bfs on
     * @param graph the graph to search through
     * @return list of vertices in visited order
     */
    public static <T> List<Vertex<T>> bfs(Vertex<T> start, Graph<T> graph) {
        checkArgs(start, graph);
        List<Vertex<T>> visited = new ArrayList<>();
        Set<Vertex<T>> seen = new HashSet<>();
        Queue<Vertex<T>> queue = new LinkedList<>();
        queue.add(start);
        while (!queue.isEmpty()) {
            Vertex<T> next = queue.remove();
            visited.add(next);
            for (VertexDistance<T> vd : graph.getAdjList().get(next)) {
                Vertex<T> neighbor = vd.getVertex();
                if (!seen.contains(neighbor)) {
                    seen.add(neighbor);
                    queue.add(neighbor);
                }
            }
        }
        return visited;
    }

    /**
     * Performs a depth first search (dfs) on the input graph, starting at
     * {@code start} which represents the starting vertex.
     *
     * When exploring a vertex, make sure to explore in the order that the
     * adjacency list returns the neighbors to you. Failure to do so may cause
     * you to lose points.
     *
     * *NOTE* You MUST implement this method recursively, or else you will lose
     * most if not all points for this method.
     *
     * You may import/use {@code java.util.Set}, {@code java.util.List}, and
     * any classes that implement the aforementioned interfaces, as long as it
     * is efficient.
     *
     * The only instance of {@code java.util.Map} that you may use is the
     * adjacency list from {@code graph}. DO NOT create new instances of Map
     * for DFS (storing the adjacency list in a variable is fine).
     *
     * DO NOT modify the structure of the graph. The graph should be unmodified
     * after this method terminates.
     *
     * @throws IllegalArgumentException if any input
     *  is null, or if {@code start} doesn't exist in the graph
     * @param <T> the generic typing of the data
     * @param start the vertex to begin the dfs on
     * @param graph the graph to search through
     * @return list of vertices in visited order
     */
    public static <T> List<Vertex<T>> dfs(Vertex<T> start, Graph<T> graph) {
        checkArgs(start, graph);
        List<Vertex<T>> visited = new ArrayList<>();
        Set<Vertex<T>> seen = new HashSet<>();
        dfsHelper(start, graph, visited, seen);
        return new ArrayList<>(visited);
    }

    /*
     * dfs helper method
     * recursively visits each vertex in the graph beginning with start vertex
     * adds each visited node to visited set
     *
     * @param curr the current vertex we are visiting
     * @param graph the graph to search through
     * @param visited the ordered list of visited vertices to populate
     * @param seen the O(1) lookup set of seen vertices to populate
     * @return set of vertices in visited order
     */
    private static <T> void dfsHelper(
        Vertex<T> curr, 
        Graph<T> graph, 
        List<Vertex<T>> visited,
        Set<Vertex<T>> seen
    ) {
        if (seen.contains(curr)) {
            return;
        }
        visited.add(curr);
        seen.add(curr);
        for (VertexDistance neighbor : graph.getAdjList().get(curr)) {
            dfsHelper(neighbor.getVertex(), graph, visited, seen);
        }
    }


    /**
     * Finds the single-source shortest distance between the start vertex and
     * all vertices given a weighted graph (you may assume non-negative edge
     * weights).
     *
     * Return a map of the shortest distances such that the key of each entry
     * is a node in the graph and the value for the key is the shortest distance
     * to that node from {@code start}, or Integer.MAX_VALUE (representing
     * infinity) if no path exists.
     *
     * You may import/use {@code java.util.PriorityQueue},
     * {@code java.util.Map}, and {@code java.util.Set} and any class that
     * implements the aforementioned interfaces, as long as your use of it
     * is efficient as possible.
     *
     * You should implement the version of Dijkstra's where you use two
     * termination conditions in conjunction.
     *
     * 1) Check that not all vertices have been visited.
     * 2) Check that the PQ is not empty yet.
     *
     * DO NOT modify the structure of the graph. The graph should be unmodified
     * after this method terminates.
     *
     * @throws IllegalArgumentException if any input is null, or if start
     *  doesn't exist in the graph.
     * @param <T> the generic typing of the data
     * @param start the vertex to begin the Dijkstra's on (source)
     * @param graph the graph we are applying Dijkstra's to
     * @return a map of the shortest distances from {@code start} to every
     *          other node in the graph
     */
    public static <T> Map<Vertex<T>, Integer> dijkstras(Vertex<T> start, Graph<T> graph) {
        checkArgs(start, graph);
        Map<Vertex<T>, Integer> distances = initDistances(graph);
        Set<Vertex<T>> visited = new HashSet<>();
        Queue<VertexDistance<T>> pq = new PriorityQueue<>();
        pq.add(new VertexDistance<>(start, 0));
        int vertexCount = graph.getVertices().size();
        while (!pq.isEmpty() && visited.size() < vertexCount) {
            VertexDistance<T> next = pq.remove();
            Vertex<T> v = next.getVertex();
            if (visited.contains(v)) {
                continue;
            }
            int distance = next.getDistance();
            distances.put(v, distance);
            visited.add(v);
            for (VertexDistance<T> vd : graph.getAdjList().get(v)) {
                Vertex<T> neighbor = vd.getVertex();
                if (!visited.contains(neighbor)) {
                    int neighborDistance = vd.getDistance();
                    pq.add(new VertexDistance<>(
                        neighbor, 
                        distance + neighborDistance
                    ));
                }
            }
        }
        return distances;
    }

    /*
     * Dijkstra helper method to initialize vertex distances to Integer.MAX_VALUE
     *
     * @param graph the graph containing the vertices to init
     * @return the vertex distances map
     */
    private static <T> Map<Vertex<T>, Integer> initDistances(Graph<T> graph) {
        Map<Vertex<T>, Integer> distances = new HashMap<>();
        for (Vertex<T> v : graph.getVertices()) {
            distances.put(v, Integer.MAX_VALUE);
        }
        return distances;
    }

    /**
     * Runs Prim's algorithm on the given graph and returns the Minimum
     * Spanning Tree (MST) in the form of a set of Edges. If the graph is
     * disconnected and therefore no valid MST exists, return null.
     *
     * You may assume that the passed in graph is undirected. In this framework,
     * this means that if (u, v, 3) is in the graph, then the opposite edge
     * (v, u, 3) will also be in the graph, though as a separate Edge object.
     *
     * The returned set of edges should form an undirected graph. This means
     * that every time you add an edge to your return set, you should add the
     * reverse edge to the set as well. This is for testing purposes. This
     * reverse edge does not need to be the one from the graph itself; you can
     * just make a new edge object representing the reverse edge.
     *
     * You may assume that there will only be one valid MST that can be formed.
     *
     * You should NOT allow self-loops or parallel edges in the MST.
     *
     * You may import/use {@code java.util.PriorityQueue},
     * {@code java.util.Set}, and any class that implements the aforementioned
     * interface.
     *
     * DO NOT modify the structure of the graph. The graph should be unmodified
     * after this method terminates.
     *
     * The only instance of {@code java.util.Map} that you may use is the
     * adjacency list from {@code graph}. DO NOT create new instances of Map
     * for this method (storing the adjacency list in a variable is fine).
     *
     * @throws IllegalArgumentException if any input
     *  is null, or if {@code start} doesn't exist in the graph
     * @param <T> the generic typing of the data
     * @param start the vertex to begin Prims on
     * @param graph the graph we are applying Prims to
     * @return the MST of the graph or null if there is no valid MST
     */ 
    public static <T> Set<Edge<T>> prims(Vertex<T> start, Graph<T> graph) {
        checkArgs(start, graph);
        Set<Vertex<T>> visited = new HashSet<>();
        Set<Edge<T>> mst = new HashSet<>();
        Queue<Edge<T>> pq = new PriorityQueue<>();
        // Initialize Priority Queue
        for (VertexDistance<T> vd : graph.getAdjList().get(start)) {
            Vertex<T> neighbor = vd.getVertex();
            int weight = vd.getDistance();
            pq.add(new Edge<T>(start, neighbor, weight));
        }
        // Initialize visited set
        visited.add(start);
        int vertexCount = graph.getVertices().size();
        while (!pq.isEmpty() && visited.size() < vertexCount) {
            Edge<T> next = pq.remove();
            if (visited.contains(next.getV())) {
                continue;
            }
            visited.add(next.getV());
            mst.add(next);
            mst.add(reverseEdge(next));
            for (VertexDistance<T> vd : graph.getAdjList().get(next.getV())) {
                Vertex<T> u = next.getV();
                Vertex<T> v = vd.getVertex();
                int weight = vd.getDistance();
                if (!visited.contains(v)) {
                    pq.add(new Edge<>(u, v, weight));
                }
            }
        }
        return visited.size() == vertexCount ? mst : null;
    }

    /*
     * prims helper method
     * takes input (u,v,weight) edge and returns the reversed (v,u,weight) edge
     *
     * @param edge the edge to reverse
     * @return the reversed edge
     */
    private static <T> Edge<T> reverseEdge(Edge<T> edge) {
        return new Edge<>(edge.getV(), edge.getU(), edge.getWeight());
    }

    /*
     * Helper method to sanitize inputs
     * @throws IllegalArgumentException if any input 
     * is null, or if start doesn't exist in the graph
     * @param start the start vertex for the search algorithm
     * @param graph the graph we are searching
     */
    private static <T> void checkArgs(Vertex<T> start, Graph<T> graph) {
        if (start == null || graph == null) {
            throw new IllegalArgumentException("Inputs cannot be null");
        }
        if (!graph.getVertices().contains(start)) {
            throw new IllegalArgumentException("graph must contain start vertex");
        }
    }
}
