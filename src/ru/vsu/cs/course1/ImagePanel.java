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
        if (originalImage != null) {
            BufferedImage img = getResizedImage();
            g.drawImage(getResizedImage(), 0, 0, null);
        }
    }

    void update(BufferedImage image) {
        this.originalImage = image;
        setSize(image.getWidth(), image.getHeight());
        validate();
        image = getResizedImage();
        paintImmediately(0, 0, image.getWidth(), image.getHeight());
    }


    private BufferedImage getResizedImage() {
        System.out.println(getWidth()+" "+getHeight());
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