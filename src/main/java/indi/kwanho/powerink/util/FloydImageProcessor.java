import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class FloydImageProcessor {

    public static void main(String[] args) {
        String inputImagePath = "/home/kwanho/test.jpg"; // 输入JPEG图像的路径
        String outputImagePath = "/home/kwanho/frame.bmp"; // 输出BMP图像的路径

        try {
            // 读取图像
            BufferedImage originalImage = ImageIO.read(new File(inputImagePath));

            // 裁剪图像的中央部分
            BufferedImage croppedImage = cropImageCenter(originalImage, 400, 300);

            // 转换为灰度图
            BufferedImage grayImage = convertToGrayscale(croppedImage);

            // 应用Floyd-Steinberg抖动
            BufferedImage ditheredImage = applyFloydSteinbergDithering(grayImage);

            // 保存为BMP
            ImageIO.write(ditheredImage, "bmp", new File(outputImagePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    private static BufferedImage applyFloydSteinbergDithering(BufferedImage grayImage) {
        int width = grayImage.getWidth();
        int height = grayImage.getHeight();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int oldPixel = grayImage.getRGB(x, y);
                int newPixel = oldPixel < -8388608 ? 0xFF000000 : 0xFFFFFFFF; // 选择最近的颜色（黑色或白色）
                grayImage.setRGB(x, y, newPixel);

                int error = oldPixel - newPixel;

                if (x + 1 < width) {
                    grayImage.setRGB(x + 1, y, limitColor(grayImage.getRGB(x + 1, y) + error * 5 / 16));
                }
                if (x - 1 >= 0 && y + 1 < height) {
                    grayImage.setRGB(x - 1, y + 1, limitColor(grayImage.getRGB(x - 1, y + 1) + error * 3 / 16));
                }
                if (y + 1 < height) {
                    grayImage.setRGB(x, y + 1, limitColor(grayImage.getRGB(x, y + 1) + error * 7 / 16));
                }
                if (x + 1 < width && y + 1 < height) {
                    grayImage.setRGB(x + 1, y + 1, limitColor(grayImage.getRGB(x + 1, y + 1) + error / 16));
                }
            }
        }
        return grayImage;
    }


    private static int limitColor(int color) {
        if (color < 0xFF000000) {
            color = 0xFF000000;
        } else if (color > 0xFFFFFFFF) {
            color = 0xFFFFFFFF;
        }
        return color;
    }
}
