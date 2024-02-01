/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年9月10日 上午10:03:09
 */
package com.ruomm.webx.springservletx;

class UriSprServletNode {
    private String servletName;
    private String springName;
    private String methodName;
    private String mode;
    private String packageSpace;

    public String getServletName() {
        return servletName;
    }

    public void setServletName(String servletName) {
        this.servletName = servletName;
    }

    public String getSpringName() {
        return springName;
    }

    public void setSpringName(String springName) {
        this.springName = springName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getPackageSpace() {
        return packageSpace;
    }

    public void setPackageSpace(String packageSpace) {
        this.packageSpace = packageSpace;
    }

    @Override
    public String toString() {
        return "SpringServletNode [servletName=" + servletName + ", springName=" + springName + ", methodName="
                + methodName + ", mode=" + mode + ", packageSpace=" + packageSpace + "]";
    }

}