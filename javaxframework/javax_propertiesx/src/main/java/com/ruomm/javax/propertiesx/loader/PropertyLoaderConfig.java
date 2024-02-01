/**
 * @copyright www.ruomm.com
 * @author 牛牛-wanruome@163.com
 * @create 2020年9月2日 下午3:31:01
 */
package com.ruomm.javax.propertiesx.loader;

public class PropertyLoaderConfig implements PropertyLoaderConfigHelper {

    @Override
    public String activeKey() {
        // TODO Auto-generated method stub
        return "profiles.active";
    }

    @Override
    public String activePathKey() {
        // TODO Auto-generated method stub
        return "profiles.activePath";
    }

    @Override
    public String incluesKey() {
        // TODO Auto-generated method stub
        return "profiles.inclues";
    }

    @Override
    public String extensionKey() {
        // TODO Auto-generated method stub
        return "profiles.extension";
    }

}
