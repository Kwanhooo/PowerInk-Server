package indi.kwanho.powerink.controller;

import indi.kwanho.powerink.common.CommonResponse;
import indi.kwanho.powerink.models.vo.CommandVO;
import indi.kwanho.powerink.service.DeviceService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/device")
public class DeviceController {
    @Resource
    private DeviceService deviceService;

    @RequestMapping("/getCommand/{id}")
    public CommonResponse<CommandVO> getCommand(@PathVariable String id) {
        return deviceService.giveCommand(id);
    }
}
