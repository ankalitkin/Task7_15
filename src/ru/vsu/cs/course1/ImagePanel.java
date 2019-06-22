package ru.vsu.cs.course1;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class ImagePanel extends JPanel {
    private BufferedImage originalImage;

    ImagePanel() {
        super();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.WHITE);
        int width = getParent().getWidth();
        int height = getParent().getHeight();
        g.drawRect(0, 0, width, height);
        if (originalImage != null) {
            double scale = getScale();
            BufferedImage img = getResizedImage(scale);
            //int left = Math.max((width - img.getWidth()) / 2, 0);
            //int top = Math.max((height - img.getHeight()) / 2, 0);
            int left = (int)(width - originalImage.getWidth()*scale)/2;
            int top = (int)(height - originalImage.getHeight()*scale)/2;
            g.drawImage(img, left, top, null);
        }
    }

    void update(BufferedImage image) {
        this.originalImage = image;
        validate();
        paintImmediately(0, 0, getParent().getWidth(), getParent().getHeight());
    }

    private double getScale() {
        int width  = originalImage.getWidth();
        int height  = originalImage.getHeight();
        double scale = Math.min(getWidth()/(double)width, getHeight()/(double)height);
        return Math.min(scale, 1);
    }

    private BufferedImage getResizedImage(double scale) {
        int width  = originalImage.getWidth();
        int height  = originalImage.getHeight();
        if(scale > 1)
            return originalImage;
        BufferedImage tmp = new BufferedImage(width, height, originalImage.getType());
        AffineTransform affineTransform = new AffineTransform();
        affineTransform.scale(scale, scale);
        AffineTransformOp scaleOp = new AffineTransformOp(affineTransform, AffineTransformOp.TYPE_BICUBIC);
        tmp = scaleOp.filter(originalImage, tmp);
        return tmp;
    }
}