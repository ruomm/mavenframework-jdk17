package com.ruomm.javax.propertiesx;

/**
 * @author 牛牛-wanruome@163.com
 * @version 1.0
 * @copyright www.ruomm.com
 * @create 2020/12/16 16:12
 */
enum ItemMode {
    // 注释行，多行注释，键值对行，换行拼接行，空行，未知行
    COMMIT, COMMIT_MULTI, KEY_VAL, APPEND, EMPTY, NO_DATA
}
