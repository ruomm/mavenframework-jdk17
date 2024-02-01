/**
 * @copyright www.ruomm.com
 * @author 牛牛-wanruome@163.com
 * @create 2020年6月23日 上午10:37:15
 */
package com.ruomm.javax.tuominx.dal;

public class TuominFieldConfig {
    private String tag;
    private boolean emptyTuomi;
    private String fieldClear;
    private String fieldMask;
    private String fieldEncrypt;
    private String fieldDigest;
    private String methodEncrypt;
    private String methodDecrypt;
    private String methodDecryptWithMask;
    private String methodMask;
    private String methodDigest;
    boolean ignoreDecryptWithMask = false;
    boolean decryptWithDigest = false;
    private TuominHelper fieldHelper;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public boolean isEmptyTuomi() {
        return emptyTuomi;
    }

    public void setEmptyTuomi(boolean emptyTuomi) {
        this.emptyTuomi = emptyTuomi;
    }

    public String getFieldClear() {
        return fieldClear;
    }

    public void setFieldClear(String fieldClear) {
        this.fieldClear = fieldClear;
    }

    public String getFieldMask() {
        return fieldMask;
    }

    public void setFieldMask(String fieldMask) {
        this.fieldMask = fieldMask;
    }

    public String getFieldEncrypt() {
        return fieldEncrypt;
    }

    public void setFieldEncrypt(String fieldEncrypt) {
        this.fieldEncrypt = fieldEncrypt;
    }

    public String getFieldDigest() {
        return fieldDigest;
    }

    public void setFieldDigest(String fieldDigest) {
        this.fieldDigest = fieldDigest;
    }

    public String getMethodEncrypt() {
        return methodEncrypt;
    }

    public void setMethodEncrypt(String methodEncrypt) {
        this.methodEncrypt = methodEncrypt;
    }

    public String getMethodDecrypt() {
        return methodDecrypt;
    }

    public void setMethodDecrypt(String methodDecrypt) {
        this.methodDecrypt = methodDecrypt;
    }

    public String getMethodDecryptWithMask() {
        return methodDecryptWithMask;
    }

    public void setMethodDecryptWithMask(String methodDecryptWithMask) {
        this.methodDecryptWithMask = methodDecryptWithMask;
    }

    public String getMethodMask() {
        return methodMask;
    }

    public void setMethodMask(String methodMask) {
        this.methodMask = methodMask;
    }

    public String getMethodDigest() {
        return methodDigest;
    }

    public void setMethodDigest(String methodDigest) {
        this.methodDigest = methodDigest;
    }

    public boolean isIgnoreDecryptWithMask() {
        return ignoreDecryptWithMask;
    }

    public void setIgnoreDecryptWithMask(boolean ignoreDecryptWithMask) {
        this.ignoreDecryptWithMask = ignoreDecryptWithMask;
    }

    public boolean isDecryptWithDigest() {
        return decryptWithDigest;
    }

    public void setDecryptWithDigest(boolean decryptWithDigest) {
        this.decryptWithDigest = decryptWithDigest;
    }

    public TuominHelper getFieldHelper() {
        return fieldHelper;
    }

    public void setFieldHelper(TuominHelper fieldHelper) {
        this.fieldHelper = fieldHelper;
    }

    @Override
    public String toString() {
        return "TuominFieldConfig{" +
                "tag='" + tag + '\'' +
                ", emptyTuomi=" + emptyTuomi +
                ", fieldClear='" + fieldClear + '\'' +
                ", fieldMask='" + fieldMask + '\'' +
                ", fieldEncrypt='" + fieldEncrypt + '\'' +
                ", fieldDigest='" + fieldDigest + '\'' +
                ", methodEncrypt='" + methodEncrypt + '\'' +
                ", methodDecrypt='" + methodDecrypt + '\'' +
                ", methodDecryptWithMask='" + methodDecryptWithMask + '\'' +
                ", methodMask='" + methodMask + '\'' +
                ", methodDigest='" + methodDigest + '\'' +
                ", ignoreDecryptWithMask=" + ignoreDecryptWithMask +
                ", decryptWithDigest=" + decryptWithDigest +
                ", fieldHelper=" + fieldHelper +
                '}';
    }
}
