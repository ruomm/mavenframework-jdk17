/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年9月19日 上午10:04:09
 */
package com.ruomm.javax.propertiesx;

class PropertyItem {

    private ItemMode mode;
    private String key;
    private String value;
    private String commit;
    private String lineStr;

    public ItemMode getMode() {
        return mode;
    }

    public void setMode(ItemMode mode) {
        this.mode = mode;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCommit() {
        return commit;
    }

    public void setCommit(String commit) {
        this.commit = commit;
    }

    public String getLineStr() {
        return lineStr;
    }

    public void setLineStr(String lineStr) {
        this.lineStr = lineStr;
    }

    @Override
    public String toString() {
//		return "PropertyItem{" +
//				"mode=" + mode +
//				", key='" + key + '\'' +
//				", value='" + value + '\'' +
//				", commit='" + commit + '\'' +
//				", lineStr='" + lineStr + '\'' +
//				'}';
        StringBuilder sb = new StringBuilder();
        sb.append("PropertyItem{");
        sb.append("mode=" + mode);
        if (null != key) {
            sb.append(", key='" + key + '\'');
        }
        if (null != value) {
            sb.append(", value='" + value + '\'');
        }
        if (null != commit) {
            sb.append(", commit='" + commit + '\'');
        }
        if (null != lineStr) {
            sb.append(", lineStr='" + lineStr + '\'');
        }
        sb.append('}');
        return sb.toString();

    }
}
