package com.example.quizapp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

public class Graph_M {
    public static class Vertex {
        public HashMap<String, Float> nbrs = new HashMap<>();
        public String line;
    }

    public static HashMap<String, Vertex> vtces;

    public Graph_M() {
        vtces = new HashMap<>();
    }

    public int numVertex() {
        return this.vtces.size();
    }

    public boolean containsVertex(String vname) {
        return this.vtces.containsKey(vname);
    }

    public void addVertex(String vname, String line) {
        Vertex vtx = new Vertex();
        vtx.line = line;
        vtces.put(vname, vtx);
    }

    public void removeVertex(String vname) {
        Vertex vtx = vtces.get(vname);
        ArrayList<String> keys = new ArrayList<>(vtx.nbrs.keySet());

        for (String key : keys) {
            Vertex nbrVtx = vtces.get(key);
            nbrVtx.nbrs.remove(vname);
        }

        vtces.remove(vname);
    }

    public int numEdges() {
        ArrayList<String> keys = new ArrayList<>(vtces.keySet());
        int count = 0;

        for (String key : keys) {
            Vertex vtx = vtces.get(key);
            count += vtx.nbrs.size();
        }

        return count / 2;
    }

    public boolean containsEdge(String vname1, String vname2) {
        Vertex vtx1 = vtces.get(vname1);
        Vertex vtx2 = vtces.get(vname2);

        return vtx1 != null && vtx2 != null && vtx1.nbrs.containsKey(vname2);
    }

    public void addEdge(String vname1, String vname2, float value) {
        Vertex vtx1 = vtces.get(vname1);
        Vertex vtx2 = vtces.get(vname2);

        if (vtx1 == null || vtx2 == null || vtx1.nbrs.containsKey(vname2)) {
            return;
        }

        vtx1.nbrs.put(vname2, value);
        vtx2.nbrs.put(vname1, value);
    }

    public void removeEdge(String vname1, String vname2) {
        Vertex vtx1 = vtces.get(vname1);
        Vertex vtx2 = vtces.get(vname2);

        if (vtx1 == null || vtx2 == null || !vtx1.nbrs.containsKey(vname2)) {
            return;
        }

        vtx1.nbrs.remove(vname2);
        vtx2.nbrs.remove(vname1);
    }

    public void display_Map() {
        System.out.println("\t Delhi Metro Map");
        System.out.println("\t------------------");
        System.out.println("----------------------------------------------------\n");
        ArrayList<String> keys = new ArrayList<>(vtces.keySet());

        for (String key : keys) {
            String str = key + " (" + vtces.get(key).line + ") =>\n";
            Vertex vtx = vtces.get(key);
            ArrayList<String> vtxnbrs = new ArrayList<>(vtx.nbrs.keySet());

            for (String nbr : vtxnbrs) {
                str = str + "\t" + nbr + " (" + vtces.get(nbr).line + ")\t";
                if (nbr.length() < 16)
                    str = str + "\t";
                if (nbr.length() < 8)
                    str = str + "\t";
                str = str + vtx.nbrs.get(nbr) + "\n";
            }
            System.out.println(str);
        }
        System.out.println("\t------------------");
        System.out.println("---------------------------------------------------\n");
    }

    public void display_Stations() {
        System.out.println("\n***********************************************************************\n");
        ArrayList<String> keys = new ArrayList<>(vtces.keySet());
        int i = 1;
        for (String key : keys) {
            System.out.println(i + ". " + key + " (" + vtces.get(key).line + ")");
            i++;
        }
        System.out.println("\n***********************************************************************\n");
    }

    public boolean hasPath(String vname1, String vname2, HashMap<String, Boolean> processed) {
        if (containsEdge(vname1, vname2)) {
            return true;
        }

        processed.put(vname1, true);

        Vertex vtx = vtces.get(vname1);
        ArrayList<String> nbrs = new ArrayList<>(vtx.nbrs.keySet());

        for (String nbr : nbrs) {
            if (!processed.containsKey(nbr)) {
                if (hasPath(nbr, vname2, processed)) {
                    return true;
                }
            }
        }

        return false;
    }

    // Define the DijkstraPair class
    private static class DijkstraPair implements Comparable<DijkstraPair> {
        String vname;
        float cost;
        String psf;
        int lineChanges;

        public DijkstraPair() {}

        public DijkstraPair(String vname, float cost) {
            this.vname = vname;
            this.cost = cost;
            this.psf = "";
            this.lineChanges = 0;
        }

        @Override
        public int compareTo(DijkstraPair o) {
            return Float.compare(this.cost, o.cost);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            DijkstraPair that = (DijkstraPair) o;

            return vname.equals(that.vname);
        }

        @Override
        public int hashCode() {
            return vname.hashCode();
        }
    }

    // Dijkstra method implementation
    public float dijkstra(String src, String des, boolean nan) {
        float val = 0;
        HashMap<String, DijkstraPair> map = new HashMap<>();
        Heap<DijkstraPair> heap = new Heap<>();
        String finalPath = "";

        // Initialize the heap and the map with all vertices
        for (String key : vtces.keySet()) {
            DijkstraPair np = new DijkstraPair(key, Float.MAX_VALUE);

            if (key.equals(src)) {
                np.cost = 0;
                np.psf = key;
            }

            heap.add(np);
            map.put(key, np);
        }

        while (!heap.isEmpty()) {
            DijkstraPair rp = heap.remove();

            if (rp.vname.equals(des)) {
                val = rp.cost;
                finalPath = rp.psf;
                break;
            }

            map.remove(rp.vname);

            Vertex v = vtces.get(rp.vname);
            for (String nbr : v.nbrs.keySet()) {
                if (map.containsKey(nbr)) {
                    float oc = map.get(nbr).cost;
                    float nc = nan ? rp.cost + 120 + 40 * v.nbrs.get(nbr) : rp.cost + v.nbrs.get(nbr);

                    if (nc < oc) {
                        DijkstraPair gp = map.get(nbr);
                        gp.cost = nc;
                        gp.psf = rp.psf + " -> " + nbr;

                        heap.updatePriority(gp);
                    }
                }
            }
        }

        int finalLineChanges = calculateLineExchanges(finalPath);
        System.out.println("Minimum Distance Path: " + finalPath);
        System.out.println("Line Changes: " + finalLineChanges);

        return val;
    }

    private String getLine(String station) {
        Vertex vtx = vtces.get(station);
        return vtx != null ? vtx.line : "Unknown";
    }

    public Set<String> getLines(String station) {
        String Lines = getLine(station);
        String[] split1 = Lines.split("");
        Set<String> ans = new HashSet<>();
        ans.addAll(Arrays.asList(split1));
        return(ans);
    }

    public int calculateLineExchanges(String path) {
        String[] stations = path.split(" -> ");
        int lineExchanges = 0;

        for (int i = 1; i < stations.length -1; i++) {
            String currentStation = stations[i - 1];
            String nextStation = stations[i];
            String nextStation1=stations[i+1];

            Set<String> currentLines = getLines(currentStation);
            Set<String> nextLines = getLines(nextStation);
            Set<String> nextLines1 = getLines(nextStation1);

            String prev = null;
            String next = null;

            // Check if there's any line common between currentStation and nextStation
            for (String line : currentLines) {
                if (nextLines.contains(line)) {
                    prev= line;
                    break;
                }
            }

            for (String line : nextLines1) {
                if (line.equals(prev)) {
                    next = line;
                    break;
                }
            }

            if (!prev.equals(next)) {
                lineExchanges++;
            }
        }

        return lineExchanges;
    }

    // Pair class for minimum distance and time calculations
    private class Pair {
        String vname;
        String psf;
        float min_dis;
        float min_time;
        int lineChanges;
    }

    public String Get_Minimum_Distance(String src, String dst) {
        float minDistance = Float.MAX_VALUE;
        String minPath = "";
        int minLineChanges = 0;

        // Queue for BFS traversal
        LinkedList<Pair> queue = new LinkedList<>();

        // HashMap to track processed vertices and their minimum distance pairs
        HashMap<String, Pair> processed = new HashMap<>();

        // Initialize the starting vertex
        Pair startPair = new Pair();
        startPair.vname = src;
        startPair.psf = src + " -> ";
        startPair.min_dis = 0;
        startPair.lineChanges = 0;
        queue.addLast(startPair);

        while (!queue.isEmpty()) {
            Pair currentPair = queue.removeFirst();

            // Check if the current vertex is already processed with shorter distance
            if (processed.containsKey(currentPair.vname) && processed.get(currentPair.vname).min_dis < currentPair.min_dis) {
                continue;
            }

            // Mark the current vertex as processed
            processed.put(currentPair.vname, currentPair);

            // If we reach the destination vertex
            if (currentPair.vname.equals(dst)) {
                float currentDistance = currentPair.min_dis;

                // Update minimum distance path if found shorter path
                if (currentDistance < minDistance) {
                    minDistance = currentDistance;
                    minPath = currentPair.psf; // Update minimum path string
                    minLineChanges = currentPair.lineChanges; // Update line changes count
                }
                continue; // Continue to explore other neighbors
            }

            // Get the vertex information from graph data structure
            Vertex currentVertex = vtces.get(currentPair.vname);

            // Iterate through all neighbors of the current vertex
            for (Map.Entry<String, Float> neighborEntry : currentVertex.nbrs.entrySet()) {
                String neighborName = neighborEntry.getKey();
                float weight = neighborEntry.getValue();

                // Create new pair for the neighbor vertex
                Pair nextPair = new Pair();
                nextPair.vname = neighborName;
                nextPair.psf = currentPair.psf + neighborName + " -> ";
                nextPair.min_dis = currentPair.min_dis + weight;

                // Check if there's a line change at this transition
                if (!currentVertex.line.equals(vtces.get(neighborName).line)) {
                    nextPair.lineChanges = currentPair.lineChanges + 1; // Increment line changes
                } else {
                    nextPair.lineChanges = currentPair.lineChanges; // Maintain current line changes
                }

                // Add the new pair to the queue for further exploration
                queue.addLast(nextPair);
            }
        }

        // Construct the final result string
        String result = minPath;
        System.out.println("Minimum Distance Path: " + result + "  Line Changes " + calculateLineExchanges(result) + "  Minimum Distance " + minDistance);
        return result;
    }

    public String Get_Minimum_LineChanges(String src, String dst) {
        float minDistance = Float.MAX_VALUE;
        String minPath = "";
        int minLineChanges = Integer.MAX_VALUE;

        // Queue for BFS traversal
        LinkedList<Pair> queue = new LinkedList<>();

        // HashMap to track processed vertices and their minimum line changes
        HashMap<String, Pair> processed = new HashMap<>();

        // Initialize the starting vertex
        Pair startPair = new Pair();
        startPair.vname = src;
        startPair.psf = src + " -> ";
        startPair.lineChanges = 0; // Start with 0 line changes
        startPair.min_dis = 0; // Start with 0 distance
        queue.addLast(startPair);

        while (!queue.isEmpty()) {
            Pair currentPair = queue.removeFirst();

            // Check if the current vertex is already processed with fewer line changes
            if (processed.containsKey(currentPair.vname) && processed.get(currentPair.vname).lineChanges <= currentPair.lineChanges) {
                continue;
            }

            // Mark the current vertex as processed with its minimum line changes
            processed.put(currentPair.vname, currentPair);

            // If we reach the destination vertex
            if (currentPair.vname.equals(dst)) {
                // Update minimum distance path if found fewer line changes
                if (currentPair.lineChanges < minLineChanges ||
                        (currentPair.lineChanges == minLineChanges && currentPair.min_dis < minDistance)) {
                    minLineChanges = currentPair.lineChanges;
                    minDistance = currentPair.min_dis;
                    minPath = currentPair.psf; // Update minimum path string
                }
                continue; // Continue to explore other neighbors
            }

            // Get the vertex information from graph data structure
            Vertex currentVertex = vtces.get(currentPair.vname);

            // Iterate through all neighbors of the current vertex
            for (Map.Entry<String, Float> neighborEntry : currentVertex.nbrs.entrySet()) {
                String neighborName = neighborEntry.getKey();
                float weight = neighborEntry.getValue();

                // Create new pair for the neighbor vertex
                Pair nextPair = new Pair();
                nextPair.vname = neighborName;
                nextPair.psf = currentPair.psf + neighborName + " -> ";
                nextPair.min_dis = currentPair.min_dis + weight;
                nextPair.lineChanges = currentPair.lineChanges;

                // Check if there's a line change at this transition
                if (!currentVertex.line.equals(vtces.get(neighborName).line)) {
                    nextPair.lineChanges++;
                }

                // Add the new pair to the queue for further exploration
                queue.addLast(nextPair);
            }
        }

        // Construct the final result string with minimum line changes and distance information
        String result = minPath + "Line Changes: " + minLineChanges + " Distance: " + minDistance;
        System.out.println("Minimum LineChange Path: " + result);
        return result;
    }

    public String Get_Minimum_Time(String src, String dst) {
        float minTime = Float.MAX_VALUE;
        String minPath = "";
        int minLineChanges = 0;

        // Queue for BFS traversal
        LinkedList<Pair> queue = new LinkedList<>();

        // HashMap to track processed vertices and their line changes
        HashMap<String, Integer> processedLineChanges = new HashMap<>();

        // Initialize the starting vertex
        Pair startPair = new Pair();
        startPair.vname = src;
        startPair.psf = src + " -> ";
        startPair.min_dis = 0;
        startPair.lineChanges = 0;
        queue.addLast(startPair);

        while (!queue.isEmpty()) {
            Pair currentPair = queue.removeFirst();

            // Check if the current vertex is already processed with fewer line changes
            if (processedLineChanges.containsKey(currentPair.vname) &&
                    processedLineChanges.get(currentPair.vname) <= currentPair.lineChanges) {
                continue;
            }

            // Mark the current vertex as processed with its minimum line changes
            processedLineChanges.put(currentPair.vname, currentPair.lineChanges);

            // If we reach the destination vertex
            if (currentPair.vname.equals(dst)) {
                float currentTime = currentPair.min_dis * 100 + currentPair.lineChanges * 140;

                // Update minimum time path if found shorter path
                if (currentTime < minTime) {
                    minTime = currentTime;
                    minPath = currentPair.psf; // Update minimum path string
                    minLineChanges = currentPair.lineChanges; // Update line changes count
                }
                continue; // Continue to explore other neighbors
            }

            // Get the vertex information from graph data structure
            Vertex currentVertex = vtces.get(currentPair.vname);

            // Iterate through all neighbors of the current vertex
            for (Map.Entry<String, Float> neighborEntry : currentVertex.nbrs.entrySet()) {
                String neighborName = neighborEntry.getKey();
                float weight = neighborEntry.getValue();

                // Create new pair for the neighbor vertex
                Pair nextPair = new Pair();
                nextPair.vname = neighborName;
                nextPair.psf = currentPair.psf + neighborName + " -> ";
                nextPair.min_dis = currentPair.min_dis + weight;
                nextPair.lineChanges = currentPair.lineChanges;

                // Check if there's a line change at this transition
                if (!currentVertex.line.equals(vtces.get(neighborName).line)) {
                    nextPair.lineChanges++;
                }

                // Add the new pair to the queue for further exploration
                queue.addLast(nextPair);
            }
        }

        // Construct the final result string
        String result = minPath;
        System.out.println("Minimum Time Path : " + result + "  Line Changes " + calculateLineExchanges(result) + "  Minimum Time " + minTime);
        return result;
    }
}