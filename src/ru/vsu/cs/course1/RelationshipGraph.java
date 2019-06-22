package ru.vsu.cs.course1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class RelationshipGraph {
    private interface Dotter {
        boolean add(StringBuilder sb, int v1, int v2);
    }

    enum Relationship {
        None(0),
        Friend(1),
        Enemy(2);

        private int value;
        Relationship(int i) {
            value = i;
        }

        public int getValue() {
            return value;
        }
    }

    private SymmetricGraph graph;

    public RelationshipGraph(int vertexCount) {
        graph = new SymmetricGraph(vertexCount);
    }

    public boolean areStrangers(int i, int j) {
        return graph.get(i, j) == Relationship.None.value;
    }

    public boolean areFriends(int i, int j) {
        return graph.get(i, j) == Relationship.Friend.value;
    }

    public boolean areEnemies(int i, int j) {
        return graph.get(i, j) == Relationship.Enemy.value;
    }

    public void makeStrangers(int i, int j) {
        graph.set(i, j, Relationship.None.value);
    }

    public void makeFriends(int i, int j) {
        graph.set(i, j, Relationship.Friend.value);
    }

    public void makeEnemies(int i, int j) {
        graph.set(i, j, Relationship.Enemy.value);
    }

    public SymmetricGraph getGraph() {
        return graph;
    }

    public int vertexCount() {
        return graph.vertexCount();
    }

    private String toDot(Dotter dotter) {
        StringBuilder sb = new StringBuilder();
        String nl = System.lineSeparator();
        sb.append("strict graph").append(" {").append(nl);
        for (int v1 = 0; v1 < vertexCount(); v1++) {
            int count = 0;
            for(int v2 = 0; v2 < v1; v2++) {
                if(dotter.add(sb, v1, v2))
                    count++;
            }
            if (count == 0) {
                sb.append(v1).append(nl);
            }
        }
        sb.append("}").append(nl);

        return sb.toString();
    }

    public String toDot() {
        return toDot(((sb, v1, v2) -> {
            if (areFriends(v1, v2)) {
                sb.append(String.format("  %d %s %d [color=blue]", v1, "--", v2)).append(System.lineSeparator());
                return true;
            }
            if (areEnemies(v1, v2)) {
                sb.append(String.format("  %d %s %d [color=red,style=dashed]", v1, "--", v2)).append(System.lineSeparator());
                return true;
            }
            return false;
        }));
    }

    public String toDotWay() {
        Integer[] way = GraphUtils.findWay(this, 0);
        if(way == null)
            return null;
        HashSet<Integer> edges = new HashSet<>(way.length*2);
        for (int i = 0; i < way.length; i++) {
            int j = (i+1)%way.length;
            System.out.println(way[i]+" --"+way[j]);
            edges.add(way[i] << 16 | way[j]);
            edges.add(way[j] << 16 | way[i]);
        }

        return toDot(((sb, v1, v2) -> {
            if (areFriends(v1, v2)) {
                if(edges.contains(v1 << 16 | v2))
                    sb.append(String.format("  %d -- %d [color=blue]", v1, v2)).append(System.lineSeparator());
                else
                    sb.append(String.format("  %d -- %d ", v1, v2)).append(System.lineSeparator());
                return true;
            }
            if (areEnemies(v1, v2)) {
                sb.append(String.format("  %d -- %d [style=dashed]", v1, v2)).append(System.lineSeparator());
                return true;
            }
            return false;
        }));
    }

    public String toDotGroups(int groupNumber, int maxEnemies) {
        int[] split = GraphUtils.splitIntoGroups(this, groupNumber, maxEnemies);

        return toDot(((sb, v1, v2) -> {
            if (split[v1] != split[v2])
                return false;
            if (areFriends(v1, v2))
                sb.append(String.format("  %d -- %d [color=blue]", v1, v2)).append(System.lineSeparator());
            else if (areEnemies(v1, v2))
                sb.append(String.format("  %d -- %d [color=red, style=dashed]", v1, v2)).append(System.lineSeparator());
            else
                sb.append(String.format("  %d -- %d [color=darkGreen,style=dashed]", v1, v2)).append(System.lineSeparator());
            return true;
        }));

    }

}
