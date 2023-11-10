package indi.kwanho.powerink.cache;

import java.util.HashMap;
import java.util.Map;

public class VersionControl {

    // 静态变量存储单例实例
    private static VersionControl instance;

    // HashMap实例
    private Map<String, Object> map;

    // 私有构造函数，防止外部实例化
    private VersionControl() {
        map = new HashMap<>();
    }

    // 公共静态方法提供全局访问点
    public static synchronized VersionControl getInstance() {
        if (instance == null) {
            instance = new VersionControl();
        }
        return instance;
    }

    // 获取HashMap的方法
    public Map<String, Object> getMap() {
        return map;
    }

    public void putValue(String key, Object value) {
        map.put(key, value);
    }

    // 以获取项为例
    public Object getValue(String key) {
        return map.get(key);
    }
}
