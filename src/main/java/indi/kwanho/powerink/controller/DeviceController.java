package indi.kwanho.powerink.controller;

import indi.kwanho.powerink.common.CommonResponse;
import indi.kwanho.powerink.entity.mysql.Device;
import indi.kwanho.powerink.models.vo.CommandVO;
import indi.kwanho.powerink.service.DeviceService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/device")
public class DeviceController {
    @Resource
    private DeviceService deviceService;

    @RequestMapping("/getCommand/{id}")
    public CommonResponse<CommandVO> getCommand(@PathVariable String id, Long version, HttpServletRequest request) {
        String clientIp = request.getHeader("x-forwarded-for");

        if (StringUtils.isEmpty(clientIp)) {
            clientIp = request.getRemoteAddr();
        }
        return deviceService.giveCommand(id, version, clientIp);
    }

    @GetMapping("/getDeviceInfo/{id}")
    public CommonResponse<? extends Device> getDeviceInfo(@PathVariable String id) {
        return deviceService.getDeviceInfo(id);
    }
}
