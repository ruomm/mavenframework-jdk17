/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年9月10日 下午2:09:18
 */
package com.ruomm.webx.springservletx;

public interface SpringServletAware {
    public Object getBeanByName(String beanName);

    public Object getBeanByClass(Class<?> cls);
}