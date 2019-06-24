package ru.vsu.cs.course1;

import java.awt.*;
import java.io.PrintWriter;

public class Utils {

    public static void saveToFile(String ext, String res) {
        String filename = browseSaveFile(ext);
        if(filename == null)
            return;
        try (PrintWriter out = new PrintWriter(filename)) {
            out.print(res);
        } catch (Exception ignored) {}
    }

    private static String browseSaveFile(String ext) {
        FileDialog fileDialog = new FileDialog((Frame) null, "Save file");
        fileDialog.setMode(FileDialog.SAVE);
        fileDialog.setFile(ext);
        fileDialog.setVisible(true);

        String dir = fileDialog.getDirectory();
        String file = fileDialog.getFile();
        if (dir != null && file != null) {
            return dir + file;
        }
        return null;
    }


}
