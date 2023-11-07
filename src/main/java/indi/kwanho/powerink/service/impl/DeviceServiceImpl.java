package indi.kwanho.powerink.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import indi.kwanho.powerink.common.*;
import indi.kwanho.powerink.entity.mysql.Device;
import indi.kwanho.powerink.models.vo.CommandVO;
import indi.kwanho.powerink.persistence.mysql.DeviceMapper;
import indi.kwanho.powerink.service.DeviceService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 设备服务类-实现
 *
 * @author kwanho
 */
@Service
public class DeviceServiceImpl extends ServiceImpl<DeviceMapper, Device>
        implements DeviceService {
    @Override
    public CommonResponse<CommandVO> giveCommand(String id) {
        Device device = this.getById(id);
        // 如果设备没注册过
        if (device == null) {
            device = this.registerDevice(id);
        }
        // 发送图片指令
        CommandVO commandVO = new CommandVO();
        commandVO.setType(CommandType.REFRESH);
        commandVO.setCommand(PathConfig.BASE_URL + "/" + id + "/frame.bmp");
        return CommonResponse.createForSuccess(commandVO);
    }

    @Override
    public Device registerDevice(String id) {
        Device newDevice = new Device();
        newDevice.setId(id);
        newDevice.setName("还没有起名字");
        newDevice.setDescription("还没有描述");
        newDevice.setOwner(null);
        newDevice.setMode(DeviceMode.CLEAR_MODE);
        newDevice.setStatus(DeviceStatus.REGISTERED);
        newDevice.setLastSeen(LocalDateTime.now());
        this.save(newDevice);
        return newDevice;
    }
}




