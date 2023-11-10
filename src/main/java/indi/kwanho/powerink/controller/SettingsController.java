package indi.kwanho.powerink.controller;

import indi.kwanho.powerink.common.CommonResponse;
import indi.kwanho.powerink.entity.mysql.Device;
import indi.kwanho.powerink.service.SettingsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@RestController
@RequestMapping("/settings")
public class SettingsController {
    @Resource
    private SettingsService settingsService;

    /**
     * 切换设备模式
     *
     * @param id   设备ID
     * @param mode 模式
     * @return 切换后的设备信息
     */
    @RequestMapping("/switchMode/{id}")
    public CommonResponse<Device> switchMode(@PathVariable String id, @RequestParam("mode") int mode) {
        return CommonResponse.createForSuccess(settingsService.switchMode(id, mode));
    }

    @PostMapping("/uploadImage/{id}")
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file, @PathVariable String id) {
        if (file.isEmpty()) {
            return ResponseEntity.status(400).body("文件为空");
        }
        return ResponseEntity.ok(settingsService.uploadImage(file, id));
    }
}
