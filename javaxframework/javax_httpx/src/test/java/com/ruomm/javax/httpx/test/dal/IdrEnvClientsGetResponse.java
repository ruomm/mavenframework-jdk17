package com.ruomm.javax.httpx.test.dal;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author 牛牛-wanruome@163.com
 * @version 1.0
 * @copyright 像衍科技-idr.ai
 * @create 2022/5/10 14:25
 */
@Getter
@Setter
@ToString
public class IdrEnvClientsGetResponse{
    private String respCode;
    private String respMsg;
    private List<IdrEnvClientItem> items;
}
