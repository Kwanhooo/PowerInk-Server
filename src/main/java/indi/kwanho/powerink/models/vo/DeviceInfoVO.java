package indi.kwanho.powerink.models.vo;

import indi.kwanho.powerink.entity.mysql.Device;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class DeviceInfoVO extends Device {
    private boolean isOnline;
}
