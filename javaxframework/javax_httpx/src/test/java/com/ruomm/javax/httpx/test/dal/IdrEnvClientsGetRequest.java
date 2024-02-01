package com.ruomm.javax.httpx.test.dal;

import com.ruomm.javax.corex.annotation.DefRemoteApiUrl;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author 牛牛-wanruome@163.com
 * @version 1.0
 * @copyright 像衍科技-idr.ai
 * @create 2022/5/10 15:02
 */
@Getter
@Setter
@ToString
@DefRemoteApiUrl(apiUrl = "device/envClientsGet", apiDesc = "依据环境获取终端信息列表", targetClass = IdrEnvClientsGetResponse.class)
public class IdrEnvClientsGetRequest {
    private String deviceId;
    private String env;
}
