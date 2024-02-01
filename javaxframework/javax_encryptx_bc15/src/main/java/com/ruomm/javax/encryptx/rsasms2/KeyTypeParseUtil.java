package com.ruomm.javax.encryptx.rsasms2;

import com.ruomm.javax.corex.StringUtils;

public class KeyTypeParseUtil {
    public static enum KeyType {
        RSA_512("RSA-512", 512),
        RSA_768("RSA-768", 768),
        RSA_1024("RSA-1024", 1024),
        RSA_2048("RSA-2048", 2048),
        RSA_3072("RSA-3072", 3072),
        RSA_4096("RSA-4096", 4096),
        SM2_GUOMI("SM2-GUOMI", 256);
        // 成员变量
        private String keyName;
        private int keySize;

        // 构造方法
        private KeyType(String keyName, int keySize) {
            this.keyName = keyName;
            this.keySize = keySize;
        }

        public String getKeyName() {
            return keyName;
        }

        public int getKeySize() {
            return keySize;
        }

        @Override
        public String toString() {
            return "KeyType{" +
                    "keyName='" + keyName + '\'' +
                    ", keySize=" + keySize +
                    '}';
        }
    }

    ;

    /**
     * 智能判断公钥类型
     *
     * @param keyStr       秘钥信息
     * @param ignoreHeader 是否忽略开始结尾标记判断
     * @return
     */
    public static KeyType parseKeyTypeByPublic(String keyStr, boolean ignoreHeader) {
        //RSA512    128   MFwwDQYJKo  CAwEAAQ==
        //RSA768    168 MHwwDQYJKo  IDAQAB
        //RSA1024   216   MIGfMA0GCS  IDAQAB
        //RSA2048   392   MIIBIjANBg  IDAQAB
        //RSA3072   564   MIIBojANBg  AgMBAAE=
        //RSA4096   736   MIICIjANBg  CAwEAAQ==


        if (StringUtils.isEmpty(keyStr)) {
            return null;
        } else if (keyStr.length() >= 86 && keyStr.length() <= 90 && (ignoreHeader || keyStr.startsWith("B"))
                && (ignoreHeader || keyStr.endsWith("="))) {
            return KeyType.SM2_GUOMI;
        } else if (keyStr.length() >= 124 && keyStr.length() <= 132 && (ignoreHeader || keyStr.startsWith("MFwwDQ"))
                && (ignoreHeader || keyStr.endsWith("EAAQ=="))) {
            return KeyType.RSA_512;
        } else if (keyStr.length() >= 164 && keyStr.length() <= 172 && (ignoreHeader || keyStr.startsWith("MHwwDQ"))
                && (ignoreHeader || keyStr.endsWith("IDAQAB"))) {
            return KeyType.RSA_768;
        } else if (keyStr.length() >= 208 && keyStr.length() <= 224 && (ignoreHeader || keyStr.startsWith("MIGfMA"))
                && (ignoreHeader || keyStr.endsWith("IDAQAB"))) {
            return KeyType.RSA_1024;
        } else if (keyStr.length() >= 384 && keyStr.length() <= 400 && (ignoreHeader || keyStr.startsWith("MIIBIj"))
                && (ignoreHeader || keyStr.endsWith("IDAQAB"))) {
            return KeyType.RSA_2048;
        } else if (keyStr.length() >= 556 && keyStr.length() <= 572 && (ignoreHeader || keyStr.startsWith("MIIBoj"))
                && (ignoreHeader || keyStr.endsWith("MBAAE="))) {
            return KeyType.RSA_3072;
        } else if (keyStr.length() >= 724 && keyStr.length() <= 748 && (ignoreHeader || keyStr.startsWith("MIICIj"))
                && (ignoreHeader || keyStr.endsWith("EAAQ=="))) {
            return KeyType.RSA_4096;
        } else {
            return null;
        }
    }

    /**
     * 智能判断私钥类型
     *
     * @param keyStr       秘钥信息
     * @param ignoreHeader 是否忽略开始结尾标记判断
     * @return
     */
    public static KeyType parseKeyTypeByPrivate(String keyStr, boolean ignoreHeader) {
        //RSA512    460-464   MIIB
        //RSA768    652-656  MIIB
        //RSA1024   844-848   MIIC
        //RSA2048   1620-1628   MIIE
        //RSA3072   2392-2396 MIIG MIIH
        //RSA4096   3164-3168   MIIJ
        if (StringUtils.isEmpty(keyStr)) {
            return null;
        } else if (keyStr.length() >= 42 && keyStr.length() <= 46) {
            return KeyType.SM2_GUOMI;
        } else if (keyStr.length() >= 452 && keyStr.length() <= 472 && (ignoreHeader || keyStr.startsWith("MIIB"))) {
            return KeyType.RSA_512;
        } else if (keyStr.length() >= 644 && keyStr.length() <= 664 && (ignoreHeader || keyStr.startsWith("MIIB"))) {
            return KeyType.RSA_768;
        } else if (keyStr.length() >= 832 && keyStr.length() <= 860 && (ignoreHeader || keyStr.startsWith("MIIC"))) {
            return KeyType.RSA_1024;
        } else if (keyStr.length() >= 1604 && keyStr.length() <= 1644 && (ignoreHeader || keyStr.startsWith("MIIE"))) {
            return KeyType.RSA_2048;
        } else if (keyStr.length() >= 2376 && keyStr.length() <= 2416 && (ignoreHeader || keyStr.startsWith("MIIG") || keyStr.startsWith("MIIH"))) {
            return KeyType.RSA_3072;
        } else if (keyStr.length() >= 3144 && keyStr.length() <= 3188 && (ignoreHeader || keyStr.startsWith("MIIJ"))) {
            return KeyType.RSA_4096;
        } else {
            return null;
        }
    }

    public static KeyType parseKeyTypeByName(String keyTypeVal) {

        if (StringUtils.isEmpty(keyTypeVal)) {
            return null;
        }
        String keyTypeStr = keyTypeVal.toUpperCase();
        if (keyTypeStr.startsWith("RSA") && keyTypeStr.contains("512")) {
            return KeyType.RSA_512;
        } else if (keyTypeStr.startsWith("RSA") && keyTypeStr.contains("768")) {
            return KeyType.RSA_768;
        } else if (keyTypeStr.startsWith("RSA") && keyTypeStr.contains("1024")) {
            return KeyType.RSA_1024;
        } else if (keyTypeStr.startsWith("RSA") && keyTypeStr.contains("2048")) {
            return KeyType.RSA_2048;
        } else if (keyTypeStr.startsWith("RSA") && keyTypeStr.contains("3072")) {
            return KeyType.RSA_3072;
        } else if (keyTypeStr.startsWith("RSA") && keyTypeStr.contains("4096")) {
            return KeyType.RSA_4096;
        } else if (keyTypeStr.startsWith("SM2")) {
            return KeyType.SM2_GUOMI;
        } else {
            return null;
        }
    }

    public static boolean isRSA(KeyType keyType) {
        String keyName = null == keyType ? null : keyType.getKeyName();
        if (StringUtils.isEmpty(keyName)) {
            return false;
        } else if (keyName.startsWith("RSA")) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isSM2(KeyType keyType) {
        String keyName = null == keyType ? null : keyType.getKeyName();
        if (StringUtils.isEmpty(keyName)) {
            return false;
        } else if (keyName.startsWith("SM2")) {
            return true;
        } else {
            return false;
        }
    }

}
