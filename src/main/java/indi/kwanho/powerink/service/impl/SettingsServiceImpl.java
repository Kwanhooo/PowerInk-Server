package indi.kwanho.powerink.service.impl;

import indi.kwanho.powerink.cache.VersionControl;
import indi.kwanho.powerink.common.DeviceMode;
import indi.kwanho.powerink.common.ResponseCode;
import indi.kwanho.powerink.entity.mysql.Device;
import indi.kwanho.powerink.exception.BusinessException;
import indi.kwanho.powerink.persistence.mysql.DeviceMapper;
import indi.kwanho.powerink.service.SettingsService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.imgscalr.Scalr;
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
        deviceMapper.updateById(device);
        return device;
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
            // 读取图片文件
            BufferedImage srcImage = ImageIO.read(file.getInputStream());
            if (srcImage == null) {
                throw new IOException("无法解码图片文件");
            }

            // 裁剪图片为400x300
            BufferedImage scaledImage = Scalr.resize(srcImage, Scalr.Method.QUALITY, Scalr.Mode.FIT_EXACT, 400, 300);

            // 确定保存图片的路径
            String directoryPath = "/home/kwanho/" + id;
            File directory = new File(directoryPath);
            if (!directory.exists() && !directory.mkdirs()) {
                throw new IOException("无法创建目录: " + directoryPath);
            }

            // 保存裁剪后的图片为BMP格式
            File outputfile = new File(directoryPath, "frame.bmp");
            boolean result = ImageIO.write(scaledImage, "BMP", outputfile);
            if (!result) {
                throw new IOException("写入BMP文件失败");
            }

            return "图片上传并裁剪成功";

        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException(ResponseCode.ERROR, "图片上传失败: " + e.getMessage());
        }
    }

}
