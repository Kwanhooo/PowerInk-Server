package indi.kwanho.powerink.service;

import com.baomidou.mybatisplus.extension.service.IService;
import indi.kwanho.powerink.common.CommonResponse;
import indi.kwanho.powerink.entity.mysql.Device;
import indi.kwanho.powerink.models.vo.CommandVO;

/**
 * 设备服务类
 *
 * @author kwanho
 */
public interface DeviceService extends IService<Device> {

    public CommonResponse<CommandVO> giveCommand(String id);

    public Device registerDevice(String id);
}
