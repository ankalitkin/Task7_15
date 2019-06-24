package ru.vsu.cs.course1;

import com.intellij.uiDesigner.core.GridLayoutManager;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.util.Enumeration;

public class FormUtils  {
    private static double scaleFactor = getScaleFactorInternal();

    public static void prepare() {
        setUIFont(new Font("Segoe UI", Font.PLAIN, 12));
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }
        UIManager.put("Panel.background", Color.WHITE);
        UIManager.put("RadioButton.background", Color.WHITE);
        UIManager.put("Spinner.background", Color.WHITE);
        UIManager.put("Slider.background", Color.WHITE);
        UIManager.put("CheckBox.background", Color.WHITE);
    }

    public static void processFormAfter(JPanel rootPanel, JFrame frame, boolean setMin) {
        processContainer(rootPanel);
        SwingUtilities.updateComponentTreeUI(rootPanel);
        frame.pack();
        if(setMin)
            setMinimumWindowSize(frame);
        frame.setVisible(true);
    }

    private static void setMinimumWindowSize(JFrame frame) {
        int width = (int)(1000 * scaleFactor);
        int height = (int)(800 * scaleFactor);
        //int width = frame.getWidth();
        //int height = frame.getHeight();
        frame.setMinimumSize(new Dimension(width, height));
    }

    private static void setUIFont(Font font) {
        FontUIResource f = new FontUIResource(font);
        Enumeration keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof FontUIResource)
                UIManager.put(key, f);
        }
    }

    private static void processContainer(JComponent container) {
        processComponent(container);
        Component[] components = container.getComponents();
        for (Component nextComponent : components)
            if (nextComponent instanceof JComponent)
                processContainer((JComponent) nextComponent);
    }

    private static void processComponent(JComponent component) {
        if (component.getLayout() instanceof GridLayoutManager) {
            GridLayoutManager layout = (GridLayoutManager) component.getLayout();
            Insets inset = layout.getMargin();
            inset.top *= scaleFactor;
            inset.right *= scaleFactor;
            inset.bottom *= scaleFactor;
            inset.left *= scaleFactor;
            layout.setMargin(inset);
            int hGap = (layout.getHGap() >= 0) ? layout.getHGap() : 10;
            int vGap = (layout.getVGap() >= 0) ? layout.getVGap() : 5;
            layout.setHGap((int) Math.round(hGap * scaleFactor));
            layout.setVGap((int) Math.round(vGap * scaleFactor));
        }
        if (component.getFont() != null) {
            Font old = component.getFont();
            component.setFont(new Font(old.getName(), old.getStyle(), (int) (old.getSize() * scaleFactor)));
        }
        if (component.getBorder() instanceof TitledBorder) {
            TitledBorder border = (TitledBorder) component.getBorder();
            Font old = border.getTitleFont();
            border.setTitleFont(new Font(old.getName(), old.getStyle(), (int) (old.getSize() * scaleFactor)));
        }
    }

    private static double getScaleFactorInternal() {
        //return 1;
        return Math.round((new com.sun.java.swing.plaf.windows.WindowsLookAndFeel().getDefaults().getFont("Label.font").getSize()) / 11.0 / 0.96 * 8) / 8.0;
    }
}
