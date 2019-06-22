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
            BufferedImage img = getResizedImage();
            int left = Math.max((width - img.getWidth()) / 2, 0);
            int top = Math.max((height - img.getHeight()) / 2, 0);
            g.drawImage(img, left, top, null);
        }
    }

    void update(BufferedImage image) {
        this.originalImage = image;
        validate();
        paintImmediately(0, 0, getParent().getWidth(), getParent().getHeight());
    }

    private BufferedImage getResizedImage() {
        //System.out.println(getWidth()+" "+getHeight());
        int width  = originalImage.getWidth();
        int height  = originalImage.getHeight();
        double scale = Math.min(getWidth()/(double)width, getHeight()/(double)height);
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