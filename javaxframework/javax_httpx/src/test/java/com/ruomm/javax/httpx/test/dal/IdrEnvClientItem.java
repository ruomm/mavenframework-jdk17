package com.ruomm.javax.httpx.test.dal;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author 牛牛-wanruome@163.com
 * @version 1.0
 * @copyright 像衍科技-idr.ai
 * @create 2022/5/10 15:04
 */
@Getter
@Setter
@ToString
public class IdrEnvClientItem {
    private String deviceId;
    private String env;
    private String deviceNo;
    private Integer masterStatus;
    private String osType;
    private String clientHost;
    private String lastOnlineTime;
}
