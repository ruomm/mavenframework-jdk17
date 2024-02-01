package com.ruomm.javax.corex.dal;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author 牛牛-wanruome@163.com
 * @version 1.0
 * @copyright 像衍科技-idr.ai
 * @create 2023/5/26 1:07
 */
@Getter
@Setter
@ToString
public class CmdxParam {
    private String paramStr;
    private String quoteTag;

    public CmdxParam(String paramStr, String fuhaoTag) {
        this.paramStr = paramStr;
        this.quoteTag = fuhaoTag;
    }
}
