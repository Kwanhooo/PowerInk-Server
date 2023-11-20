package indi.kwanho.powerink.service.impl;

import indi.kwanho.powerink.cache.VersionControl;
import indi.kwanho.powerink.common.DeviceMode;
import indi.kwanho.powerink.common.ResponseCode;
import indi.kwanho.powerink.entity.mysql.Device;
import indi.kwanho.powerink.exception.BusinessException;
import indi.kwanho.powerink.persistence.mysql.DeviceMapper;
import indi.kwanho.powerink.service.SettingsService;
import indi.kwanho.powerink.util.ImageProcessor;
import indi.kwanho.powerink.util.TextImageGenerator;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

@Service
public class SettingsServiceImpl implements SettingsService {
    @Resource
    private DeviceMapper deviceMapper;

    @Override
    public Device switchMode(String id, int mode) {
        Device device = deviceMapper.selectById(id);
        if (device == null) {
            throw new BusinessException(ResponseCode.ERROR, "设备不存在");
        }
        device.setMode(mode);
        applyMode(device);
        deviceMapper.updateById(device);
        return device;
    }

    private void applyMode(Device device) {
        // 更新服务端版本号
        VersionControl.getInstance().putValue(device.getId(), System.currentTimeMillis());
    }

    @Override
    public String uploadImage(MultipartFile file, String id) {
        // 确认设备存在并且在图像模式
        Device device = deviceMapper.selectById(id);
        if (device == null) {
            throw new BusinessException(ResponseCode.ERROR, "设备不存在");
        }
        if (!Objects.equals(device.getMode(), DeviceMode.IMAGE_MODE)) {
            throw new BusinessException(ResponseCode.ERROR, "设备不在图像模式");
        }
        VersionControl.getInstance().putValue(id, System.currentTimeMillis());

        try {
            // 使用ImageProcessor处理图像
            BufferedImage processedImage = ImageProcessor.processImage(file.getInputStream());

            // 构建文件保存路径
            String outputImagePath = "/www/wwwroot/asset.0xcafebabe.cn/" + id + "/frame.bmp";

            // 保存为BMP格式
            File outputFile = new File(outputImagePath);
            ImageIO.write(processedImage, "bmp", outputFile);

            return "图像上传并转换成功，保存于：" + outputImagePath;

        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException(ResponseCode.ERROR, "图片上传失败: " + e.getMessage());
        }
    }

    @Override
    public String uploadText(String text, String id) {
        // 确认设备存在并且在文本模式
        Device device = deviceMapper.selectById(id);
        if (device == null) {
            throw new BusinessException(ResponseCode.ERROR, "设备不存在");
        }
        if (!Objects.equals(device.getMode(), DeviceMode.TEXT_MODE)) {
            throw new BusinessException(ResponseCode.ERROR, "设备不在文本模式");
        }
        VersionControl.getInstance().putValue(id, System.currentTimeMillis());

        try {
            // 构建文件保存路径
            String outputImagePath = "/www/wwwroot/asset.0xcafebabe.cn/" + id + "/frame.bmp";

            // 使用TextImageGenerator生成图像
            TextImageGenerator.generateImageFromText(text, outputImagePath);

            return "文本图像上传成功，保存于：" + outputImagePath;

        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException(ResponseCode.ERROR, "文本图像上传失败: " + e.getMessage());
        }
    }

}
