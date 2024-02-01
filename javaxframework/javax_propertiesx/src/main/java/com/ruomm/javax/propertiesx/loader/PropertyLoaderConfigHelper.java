/**
 * @copyright www.ruomm.com
 * @author 牛牛-wanruome@163.com
 * @create 2020年9月2日 下午5:40:33
 */
package com.ruomm.javax.propertiesx.loader;

public interface PropertyLoaderConfigHelper {
    /**
     *
     * @return 激活的环境配置文件环境名称，如是{xxxxx}或${xxxxxx}设置为dev
     */
    public String activeKey();

    /**
     *
     * @return 环境配置文件路径属性
     */
    public String activePathKey();

    /**
     *
     * @return 入口配置文件和环境配置文件额外加载配置文件的路径
     */
    public String incluesKey();

    /**
     *
     * @return 环境配置文件自定义扩展名，若是为空则和入口配置文件的扩展名一致
     */
    public String extensionKey();

}
