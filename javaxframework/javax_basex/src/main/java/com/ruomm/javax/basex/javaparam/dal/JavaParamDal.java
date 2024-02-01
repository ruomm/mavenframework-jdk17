package com.ruomm.javax.basex.javaparam.dal;

/**
 * @author 牛牛-wanruome@163.com
 * @version 1.0
 * @copyright 像衍科技-idr.ai
 * @create 2022/12/1 11:27
 */
public class JavaParamDal<T> {
    /**
     * 解析是否成功
     */
    private boolean result;
    /**
     * 解析的结果字符串
     */
    private String valString;

    private T valObject;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getValString() {
        return valString;
    }

    public void setValString(String valString) {
        this.valString = valString;
    }

    public T getValObject() {
        return valObject;
    }

    public void setValObject(T valObject) {
        this.valObject = valObject;
    }

    @Override
    public String toString() {
        return "JavaParamDal{" +
                "result=" + result +
                ", valString='" + valString + '\'' +
                ", valObject=" + valObject +
                '}';
    }

    /**
     * 验证shell是否包含值
     *
     * @return 是否包含值
     */
    public boolean validResult() {
        return this.result;
    }

    /**
     * 验证shell是否包含值并且解析成功了
     *
     * @return 是否包含值
     */
    public boolean validResultNoNull() {
        return this.result && null != valObject;
    }

    /**
     * 返回shell字符串参数，shell不包含返回null，包含返回非null，包含但是没有值返回空字符串“”
     */
    public String toValString() {
        if (!this.result) {
            return null;
        } else {
            return null == this.valString ? "" : this.valString;
        }
    }

    /**
     * 返回shell对象内容，shell不包含返回指定默认值，包含返回但是解析失败返回默认对象，解析成功则返回真实对象
     *
     * @param hasKeyT 包含参数但是解析失败的返回对象
     * @param noKeyT  不包含参数的返回对象
     * @return 返回shell对象内容
     */
    public T toValObject(T hasKeyT, T noKeyT) {
        if (!this.result) {
            return noKeyT;
        } else {
            return null == this.valObject ? hasKeyT : this.valObject;
        }
    }

    /**
     * 返回shell对象内容，shell不包含返回指定默认值，包含返回但是解析失败返回默认对象，解析成功则返回真实对象
     *
     * @param hasKeyT 包含参数但是解析失败的返回对象
     * @return 返回shell对象内容
     * @paramDefalut noKeyT 不包含参数的返回对象，此处默认值null
     */
    public T toValObject(T hasKeyT) {
        return toValObject(hasKeyT, null);
    }

    /**
     * 返回shell对象内容，shell不包含返回指定默认值，包含返回但是解析失败返回默认对象，解析成功则返回真实对象
     *
     * @return 返回shell对象内容
     * @paramDefalut hasKeyT 包含参数但是解析失败的返回对象，此处默认值null
     * @paramDefalut noKeyT 不包含参数的返回对象，此处默认值null
     */
    public T toValObject() {
        return toValObject(null, null);
    }

}
