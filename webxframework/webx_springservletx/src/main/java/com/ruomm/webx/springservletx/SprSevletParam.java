/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年9月10日 下午3:48:12
 */
package com.ruomm.webx.springservletx;

import java.util.List;

class SprSevletParam {
    public List<XmlSprServletSpace> lstServletSpace;
    public int uriLevel = 0;
    public SpringServletAware beanFactory = null;
    public boolean isDebug = false;
    public String reloadUri = null;
    public String reloadPath = null;

}