package indi.kwanho.powerink.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import indi.kwanho.powerink.cache.VersionControl;
import indi.kwanho.powerink.common.*;
import indi.kwanho.powerink.entity.mysql.Device;
import indi.kwanho.powerink.models.vo.CommandVO;
import indi.kwanho.powerink.models.vo.DeviceInfoVO;
import indi.kwanho.powerink.persistence.mysql.DeviceMapper;
import indi.kwanho.powerink.service.DeviceService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 设备服务类-实现
 *
 * @author kwanho
 */
@Service
public class DeviceServiceImpl extends ServiceImpl<DeviceMapper, Device>
        implements DeviceService {
    @Override
    public CommonResponse<CommandVO> giveCommand(String id, Long version, String clientIp) {
        Device device = this.getById(id);
        // 如果设备没注册过
        if (device == null) {
            device = this.registerDevice(id);
        }
        // 处理设备在线信息
        handleAliveInfo(device, clientIp);
        CommandVO commandVO = new CommandVO();
        // 检查服务端版本号
        if (VersionControl.getInstance().getValue(id) == null) {
            VersionControl.getInstance().putValue(id, System.currentTimeMillis());
        }
        // 比对版本号
        if (version != null && version.equals(VersionControl.getInstance().getValue(id))) {
            commandVO.setType(CommandType.KEEP);
            commandVO.setCommand(null);
            commandVO.setVersion((Long) VersionControl.getInstance().getValue(id));
            return CommonResponse.createForSuccess(commandVO);
        }
        // 处理清屏模式
        if (Objects.equals(device.getMode(), DeviceMode.CLEAR_MODE)) {
            commandVO.setType(CommandType.CLEAR);
            commandVO.setCommand(null);
            commandVO.setVersion((Long) VersionControl.getInstance().getValue(id));
            return CommonResponse.createForSuccess(commandVO);
        }
        // 发送拉取指令
        commandVO.setType(CommandType.REFRESH);
        commandVO.setCommand(PathConfig.BASE_URL + "/" + id + "/frame.bmp");
        commandVO.setVersion((Long) VersionControl.getInstance().getValue(id));
        return CommonResponse.createForSuccess(commandVO);
    }

    private void handleAliveInfo(Device device, String clientIp) {
        device.setLastSeen(LocalDateTime.now());
        device.setIp(clientIp);
        this.updateById(device);
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

    @Override
    public CommonResponse<Device> getDeviceInfo(String id) {
        Device device = this.getById(id);
        if (device == null) {
            return CommonResponse.createForError("设备不存在");
        }
        DeviceInfoVO vo = new DeviceInfoVO();
        BeanUtils.copyProperties(device, vo);
        // 计算当前时间和lastSeen的时间差，如果超过5分钟，认为设备离线
        vo.setOnline(!vo.getLastSeen().plusMinutes(5).isBefore(LocalDateTime.now()));
        return CommonResponse.createForSuccess(vo);
    }
}




