package ru.vsu.cs.course1;

public class RelationshipGraph {
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

    public String toDot() {
        StringBuilder sb = new StringBuilder();
        String nl = System.lineSeparator();
        sb.append("strict graph").append(" {").append(nl);
        for (int v1 = 0; v1 < vertexCount(); v1++) {
            int count = 0;
            for(int v2 = 0; v2 < v1; v2++) {
                if(areFriends(v1, v2)) {
                    sb.append(String.format("  %d %s %d [color=blue]", v1, "--", v2)).append(nl);
                    count++;
                }
                if(areEnemies(v1, v2)) {
                    sb.append(String.format("  %d %s %d [color=red,style=dashed]", v1, "--", v2)).append(nl);
                    count++;
                }
            }
            if (count == 0) {
                sb.append(v1).append(nl);
            }
        }
        sb.append("}").append(nl);

        return sb.toString();
    }
}
