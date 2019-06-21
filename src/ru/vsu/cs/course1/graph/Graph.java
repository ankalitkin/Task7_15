package ru.vsu.cs.course1.graph;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
import javax.management.Query;

/**
 * Интерфейс для описания неориентированного графа (н-графа)
 * с реализацией некоторых методов графа
 */
public interface Graph {
    /**
     * Кол-во вершин в графе
     * @return 
     */
    int vertexCount();
    
    /**
     * Кол-во ребер в графе
     * @return 
     */
    int edgeCount();
    
    /**
     * Добавление ребра между вершинами с номерами v1 и v2
     * @param v1
     * @param v2
     */
    void addAdge(int v1, int v2);
    
    /**
     * Удаление ребра/ребер между вершинами с номерами v1 и v2
     * @param v1
     * @param v2
     */
    void removeAdge(int v1, int v2);
    
    /**
     * @param v Номер вершины, смежные с которой необходимо найти
     * @return Объект, поддерживающий итерацию по номерам связанных с v вершин
     */
    Iterable<Integer> adjacencies(int v);
    
    /**
     * Проверка смежности двух вершин
     * @param v1
     * @param v2
     * @return 
     */
    default boolean isAdj(int v1, int v2) {
        for (Integer adj : adjacencies(v1)) {
            if (adj == v2) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Поиск в глубину в виде итератора, который проходит по связным вершинам
     * (начальная вершина также включена)
     * @param v Вершина, с которой начинается поиск
     * @return Итератор
     */
    default Iterable<Integer> dfs(int v) {
        return new Iterable<Integer>() {
            private Stack<Integer> stack = null;
            private boolean[] visited = null;

            @Override
            public Iterator<Integer> iterator() {
                stack = new Stack<Integer>();
                stack.push(v);
                visited = new boolean[Graph.this.vertexCount()];
                visited[v] = true;
                
                return new Iterator<Integer>() {
                    @Override
                    public boolean hasNext() {
                        return ! stack.isEmpty();
                    }

                    @Override
                    public Integer next() {
                        Integer result = stack.pop();
                        for (Integer adj : Graph.this.adjacencies(result)) {
                            if (!visited[adj]) {
                                visited[adj] = true;
                                stack.add(adj);
                            }
                        }
                        return result;
                    }
                };
            }
        };
    }
    
    /**
     * Поиск в ширину в виде итератора, который проходит по связным вершинам
     * (начальная вершина также включена)
     * @param v Вершина, с которой начинается поиск
     * @return Итератор
     */
    default Iterable<Integer> bfs(int v) {
        return new Iterable<Integer>() {
            private Queue<Integer> queue = null;
            private boolean[] visited = null;

            @Override
            public Iterator<Integer> iterator() {
                queue = new LinkedList<Integer>();
                queue.add(v);
                visited = new boolean[Graph.this.vertexCount()];
                visited[v] = true;
                
                return new Iterator<Integer>() {
                    @Override
                    public boolean hasNext() {
                        return ! queue.isEmpty();
                    }

                    @Override
                    public Integer next() {
                        Integer result = queue.poll();
                        for (Integer adj : Graph.this.adjacencies(result)) {
                            if (!visited[adj]) {
                                visited[adj] = true;
                                queue.add(adj);
                            }
                        }
                        return result;
                    }
                };
            }
        };
    }
    
    /**
     * Получение dot-описяния графа (для GraphViz)
     * @return 
     */
    default String toDot() {
        StringBuilder sb = new StringBuilder();
        String nl = System.getProperty("line.separator");
        boolean isDigraph = this instanceof Digraph;
        sb.append(isDigraph ? "digraph" : "strict graph").append(" {").append(nl);
        for (int v1 = 0; v1 < vertexCount(); v1++) {
            int count = 0;
            for (Integer v2 : this.adjacencies(v1)) {
                sb.append(String.format("  %d %s %d", v1, (isDigraph ? "->" : "--"), v2)).append(nl);
                count++;
            }
            if (count == 0) {
                sb.append(v1).append(nl);
            }
        }
        sb.append("}").append(nl);
        
        return sb.toString();
    }
}
