package indi.kwanho.powerink.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class ImageProcessor {

    public static BufferedImage processImage(InputStream inputStream) throws IOException {
        BufferedImage originalImage = ImageIO.read(inputStream);
        BufferedImage croppedImage = cropImageCenter(originalImage, 400, 300);
        return convertToGrayscale(croppedImage);
    }

    private static BufferedImage cropImageCenter(BufferedImage src, int width, int height) {
        int x = (src.getWidth() - width) / 2;
        int y = (src.getHeight() - height) / 2;

        if (x < 0 || y < 0 || width > src.getWidth() || height > src.getHeight()) {
            throw new IllegalArgumentException("裁剪尺寸超出原始图像范围");
        }

        return src.getSubimage(x, y, width, height);
    }

    private static BufferedImage convertToGrayscale(BufferedImage src) {
        BufferedImage grayImage = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D g2d = grayImage.createGraphics();
        g2d.drawImage(src, 0, 0, null);
        g2d.dispose();
        return grayImage;
    }
}
