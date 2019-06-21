package ru.vsu.cs.course1;

import java.util.NoSuchElementException;

public class SymmetricGraph {
    private int[][] matrix;
    int vertexCount;

    public SymmetricGraph(int count) {
        vertexCount = count;
        matrix = new int[count][];
        for (int i = 0; i < count; i++)
            matrix[i] = new int[i];
    }

    public void set(int i, int j, int value) {
        int row = Math.max(i, j);
        int col = Math.min(i, j);
        if (row <= 0 || row >= matrix.length || col < 0 || col >= matrix[row].length)
            throw new NoSuchElementException();
        matrix[row][col] = value;
    }

    public int get(int i, int j) {
        int row = Math.max(i, j);
        int col = Math.min(i, j);
        if (row <= 0 || row >= matrix.length || col < 0 || col >= matrix[row].length)
            throw new NoSuchElementException();
        return matrix[row][col];
    }

    public int vertexCount() {
        return vertexCount;
    }
}
