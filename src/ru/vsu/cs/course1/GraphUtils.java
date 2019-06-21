package ru.vsu.cs.course1;

import javax.swing.plaf.nimbus.State;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Stack;

public class GraphUtils {
    public static RelationshipGraph generate(int vertexCount) {
        RelationshipGraph graph = new RelationshipGraph(vertexCount);
        int maxFriends = vertexCount / 2;

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
                if(graph.areFriends(a, b))
                    continue;
                graph.makeFriends(a, b);
                if(++edges[a] == maxFriends)
                    list.remove((Integer)a);
                if(++edges[b] == maxFriends)
                    list.remove((Integer)b);
                continue there;
            }
            list.removeFirst();
        }

        return graph;
    }

    public static Integer[] findWay(RelationshipGraph graph, int from) {
        int vertexCount = graph.vertexCount();
        Stack<Integer> wayStack = new Stack<>();

        int current = from;
        int last = -1;
        int counter = 0;
        while(!wayStack.empty()) {
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
