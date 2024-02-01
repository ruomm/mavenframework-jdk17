/**
 * @copyright www.ruomm.com
 * @author 牛牛-wanruome@163.com
 * @create 2020年9月4日 上午11:49:30
 */
package com.ruomm.javax.propertiesx;

import java.util.Map;

public interface PropertyReaderCommonHelper {
    /**
     * 获取对应配置值
     *
     * @param key 配置名
     * @return
     */
    public String getProperty(String key);

    /**
     * 获取对应配置值
     *
     * @param key          配置名
     * @param defaultValue 默认值
     * @return
     */
    public String getProperty(String key, String defaultValue);

    /**
     * 配置转换为String类型
     *
     * @return
     */
    public String propertyToString();

    /**
     * 配置转换为Map类型
     *
     * @return
     */
    public Map<String, String> propertyToMap();

    /**
     * 读取配置文件
     *
     * @param isForce 是否强制读取
     */
    public void loadProps(boolean isForce);

    /**
     * 添加配置，XML模式不支持
     * @param key 配置Key
     * @param value 配置值
     * @return 添加结果
     */
    public boolean putProperty(String key, String value);

    /**
     * 删除配置，XML模式不支持
     * @param key 配置Key
     * @return 删除结果
     */
    public boolean removeProperty(String key);

    /**
     * 存储配置，XML模式不支持，JSON模式不支持注释
     * @param  comments 注释
     * @return 存储结果
     */
    public boolean storeProperty(String comments);

}
