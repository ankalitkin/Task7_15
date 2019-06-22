package ru.vsu.cs.course1;

import java.util.*;

public class GraphUtils {
    private static Random random = new Random();
    private interface Getter {
        boolean get(RelationshipGraph graph, int i, int j);
    }
    private interface Setter {
        void set(RelationshipGraph graph, int i, int j);
    }

    public static RelationshipGraph generate(int vertexCount, int k) {
        RelationshipGraph graph = new RelationshipGraph(vertexCount);
        int maxFriends = vertexCount / 2;
        addEdges(vertexCount, graph, maxFriends, RelationshipGraph::areFriends, RelationshipGraph::makeFriends);
        addEdges(vertexCount, graph, k, (graph1, i, j) -> !graph1.areStrangers(i, j), RelationshipGraph::makeEnemies);
        return graph;
    }

    private static void addEdges(int vertexCount, RelationshipGraph graph, int max, Getter getter, Setter setter) {
        if(max <= 0)
            return;
        int[] edges = new int[vertexCount];

        LinkedList<Integer> list = new LinkedList<>();
        for (int i = 0; i < vertexCount; i++)
            list.add(i);

        there: while(!list.isEmpty()) {
            list.sort(Comparator.comparingInt(a -> edges[a]));
            int a = list.getFirst();
            for(int b : list) {
                if(a == b)
                    continue;
                if(getter.get(graph, a, b))
                    continue;
                setter.set(graph, a, b);
                if(++edges[a] == max)
                    list.remove((Integer)a);
                if(++edges[b] == max)
                    list.remove((Integer)b);
                continue there;
            }
            list.removeFirst();
        }
    }

    public static Integer[] findWay(RelationshipGraph graph, int from) {
        int vertexCount = graph.vertexCount();
        Stack<Integer> wayStack = new Stack<>();

        int current = from;
        int last = -1;
        int counter = 0;
        while(true) {
            int next = -1;
            for (int i = last+1; i < vertexCount; i++) {
                if (i == current) continue;
                if (wayStack.contains(i) && i != from) continue;
                if(graph.areFriends(current, i)) {
                    next = i;
                    break;
                }
            }
            if(next < 0) {
                last = current;
                current = wayStack.pop();
                continue;
            }

            wayStack.push(current);
            current = next;
            if(current == from) {
                if (wayStack.size() == vertexCount) {
                    return wayStack.toArray(new Integer[0]);
                }
                else {
                    last = current;
                    current = wayStack.pop();
                }
            } else {
                last = -1;
            }
            if(counter++ > 100000)
                break;
        }
        return null;
    }

    public static void printGraph(SymmetricGraph graph) {
        for (int i = 0; i < graph.vertexCount(); i++) {
            for (int j = 0; j < i; j++) {
                System.out.print(graph.get(i, j));
            }
            System.out.println();
        }
    }

    public static int[] splitIntoGroups(RelationshipGraph graph, int groupNumber, int maxEnemies) {
        long initSeed = Math.abs(random.nextLong());
        int vertexCount = graph.vertexCount();
        there: for(long seed = initSeed;seed < initSeed+10000; seed++) {
            long currentSeed = seed;
            int[] split = new int[vertexCount];
            for (int i = 0; i < vertexCount; i++) {
                split[i] = (int) (currentSeed % groupNumber);
                currentSeed /= groupNumber;
            }
            for (int i = 0; i < groupNumber; i++) {
                for (int j = 0; j < vertexCount; j++) {
                    int counter = 0;
                    for (int k = 0; k < vertexCount; k++) {
                        if(j == k || split[j] != split[k])
                            continue;
                        if (graph.areEnemies(j, k))
                            counter++;
                    }
                    if (counter >= maxEnemies)
                        continue there;
                }
            }
            /*for (int i = 0; i < vertexCount; i++) {
                System.out.print(split[i]);
            }
            System.out.println();*/
            return split;
        }
        throw new RuntimeException("Can't generate");
    }

    /*public static void main(String[] args) {
        RelationshipGraph graph = generate(100);
        printGraph(graph.getGraph());
        System.out.println();
        for (int a: findWay(graph, 0)) {
            System.out.print(a);
            System.out.print(" ");
        }
    }*/


}
