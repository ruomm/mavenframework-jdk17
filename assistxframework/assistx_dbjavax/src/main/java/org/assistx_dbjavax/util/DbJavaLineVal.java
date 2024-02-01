/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年8月14日 上午9:23:07
 */
package org.assistx_dbjavax.util;

public class DbJavaLineVal {

    private int index;
    private String key;
    private String keyType;
    private String commit;
    private String dbCommit;
    private String val;
    private boolean keyFlag;
    private boolean commitFlag;
    private boolean columnAnnotation;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKeyType() {
        return keyType;
    }

    public void setKeyType(String keyType) {
        this.keyType = keyType;
    }

    public String getCommit() {
        return commit;
    }

    public void setCommit(String commit) {
        this.commit = commit;
    }

    public String getDbCommit() {
        return dbCommit;
    }

    public void setDbCommit(String dbCommit) {
        this.dbCommit = dbCommit;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public boolean isKeyFlag() {
        return keyFlag;
    }

    public void setKeyFlag(boolean keyFlag) {
        this.keyFlag = keyFlag;
    }

    public boolean isCommitFlag() {
        return commitFlag;
    }

    public void setCommitFlag(boolean commitFlag) {
        this.commitFlag = commitFlag;
    }

    public boolean isColumnAnnotation() {
        return columnAnnotation;
    }

    public void setColumnAnnotation(boolean columnAnnotation) {
        this.columnAnnotation = columnAnnotation;
    }

}
