package indi.kwanho.powerink.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TextImageGenerator {

    public static void generateImageFromText(String text, String outputPath) throws IOException {
        BufferedImage image = new BufferedImage(400, 300, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D g2d = image.createGraphics();

        // 设置背景颜色
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, 400, 300);

        // 设置文字属性
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Segoe UI", Font.BOLD, 22));
//        g2d.setFont(new Font("WenQuanYi Zen Hei", Font.BOLD, 20));
//        g2d.setFont(new Font("WenQuanYi Micro Hei", Font.BOLD, 20));

        // 自动换行处理（适用于中文）
        List<String> lines = wrapTextForChinese(text, 380, g2d.getFontMetrics());

        // 计算文本块的高度
        int textHeight = lines.size() * g2d.getFontMetrics().getHeight();

        // 绘制每一行文本
        int startY = (300 - textHeight) / 2 + g2d.getFontMetrics().getAscent();
        for (String line : lines) {
            int textWidth = g2d.getFontMetrics().stringWidth(line);
            int x = (400 - textWidth) / 2;
            g2d.drawString(line, x, startY);
            startY += g2d.getFontMetrics().getHeight();
        }

        g2d.dispose();
        ImageIO.write(image, "bmp", new File(outputPath));
    }

    private static List<String> wrapTextForChinese(String text, int maxWidth, FontMetrics metrics) {
        List<String> lines = new ArrayList<>();
        StringBuilder currentLine = new StringBuilder();
        for (char ch : text.toCharArray()) {
            if (metrics.stringWidth(currentLine.toString() + ch) < maxWidth) {
                currentLine.append(ch);
            } else {
                lines.add(currentLine.toString());
                currentLine = new StringBuilder(String.valueOf(ch));
            }
        }
        if (currentLine.length() > 0) {
            lines.add(currentLine.toString());
        }
        return lines;
    }
}
