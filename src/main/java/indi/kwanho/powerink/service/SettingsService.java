package indi.kwanho.powerink.service;

import indi.kwanho.powerink.entity.mysql.Device;
import org.springframework.web.multipart.MultipartFile;

public interface SettingsService {
    Device switchMode(String id, int mode);

    String uploadImage(MultipartFile file, String id);
}
