package com.ruomm.javax.encryptx.rsasms2;

import com.ruomm.javax.corex.StringUtils;
import com.ruomm.javax.corex.helper.EncoderHelper;
import com.ruomm.javax.encryptx.RSAHelper;
import com.ruomm.javax.encryptx.guomi.SM2Helper;
import com.ruomm.javax.encryptx.guomi.SM2SignHelper;

public class CommonEncryptHelper {
    private final static String DEFAULT_SM2_USER_ID = "www.ruomm.com";
    private final static int KEY_TYPE_RSA = 0;
    private final static int KEY_TYPE_SM2 = 1;

    private String publicKeyStr;    // 公钥信息
    private int publicKeyType; // 公钥类型，为空则依据公钥信息智能判断
    private String privateKeyStr;    // 私钥信息
    private int privateKeyType;// 私钥类型，为空则依据私钥信息智能判断
    private String sm2UserId = DEFAULT_SM2_USER_ID;   //SM2签名时候的USER名称
    private boolean rsaWithSign = false;//RSA加解密时候是否使用加签后加密解密后验签模式

    private RSAHelper rsaHelper;
    private SM2Helper sm2Helper;

    private SM2SignHelper sm2SignHelper;

    /**
     * 无参数构造函数，使用此方法构造需要使用config*函数参数来配置相关秘钥信息
     */
    public CommonEncryptHelper() {
        super();
    }

    /**
     * 配置公钥信息和公钥类型
     *
     * @param publicKeyStr  公钥信息
     * @param publicKeyType 公钥类型
     */
    public CommonEncryptHelper configPublicKey(String publicKeyStr, int publicKeyType) {
        this.publicKeyStr = publicKeyStr;
        this.publicKeyType = publicKeyType;
        return this;
    }

    /**
     * 配置私钥信息和私钥类型
     *
     * @param privateKeyStr  私钥信息
     * @param privateKeyType 公钥类型
     */
    public CommonEncryptHelper configPrivateKey(String privateKeyStr, int privateKeyType) {
        this.privateKeyStr = privateKeyStr;
        this.privateKeyType = privateKeyType;
        return this;
    }

    /**
     * 配置RSA加密解密工具类
     */
    public CommonEncryptHelper configRsaHelper(RSAHelper rsaHelper) {
        this.rsaHelper = rsaHelper;
        return this;
    }

    /**
     * 配置SM2加密解密工具类
     *
     * @param sm2Helper     sm2加密工具
     * @param sm2SignHelper sm2签名工具
     */
    public CommonEncryptHelper configSM2Helper(SM2Helper sm2Helper, SM2SignHelper sm2SignHelper) {
        this.sm2Helper = sm2Helper;
        this.sm2SignHelper = sm2SignHelper;
        return this;
    }

    /**
     * 配置SM2加密解密工具类
     *
     * @param isC1C3C2 sm2是否C1C3C2编码
     * @param keyMode  秘钥编码方式
     * @param dataMode 数据编码方式
     */
    public CommonEncryptHelper configSM2Helper(boolean isC1C3C2, EncoderHelper.EncoderMode keyMode, EncoderHelper.EncoderMode dataMode) {
        this.sm2Helper = sm2Helper;
        this.sm2SignHelper = sm2SignHelper;
        this.sm2Helper = new SM2Helper(isC1C3C2, keyMode, dataMode);
        this.sm2SignHelper = new SM2SignHelper(keyMode, dataMode);
        return this;
    }

    /**
     * 配置SM2签名时候的USER名称
     *
     * @param sm2UserId SM2签名时候的USER名称,为空则取默认值www.ruomm.com
     */
    public CommonEncryptHelper configSm2UserId(String sm2UserId) {
        this.sm2UserId = StringUtils.isEmpty(sm2UserId) ? DEFAULT_SM2_USER_ID : sm2UserId;
        return this;
    }

    /**
     * 配置RSA加解密时候是否使用加签后加密解密后验签模式
     *
     * @param rsaWithSign RSA加解密时候是否使用加签后加密解密后验签模式
     */
    public CommonEncryptHelper configRsaWithSign(boolean rsaWithSign) {
        this.rsaWithSign = rsaWithSign;
        return this;
    }

    /**
     * 使用私钥加密，RSA使用私钥加密，SM2使用私钥签名公钥加密
     *
     * @param data 待加密的数据
     * @return
     */
    public String encryptString(String data) {
        if (KEY_TYPE_RSA == this.privateKeyType) {
            if (this.rsaWithSign) {
                String singData = rsaHelper.getSignData(data, rsaHelper.loadPrivateKey(privateKeyStr));
                if (StringUtils.isEmpty(singData)) {
                    return null;
                }
                String dataDec = data + "|" + singData;
                String dataEnc = null;
                if (KEY_TYPE_RSA == this.publicKeyType) {
                    dataEnc = rsaHelper.encryptDataBigByPublicKey(dataDec, publicKeyStr);
                } else if (KEY_TYPE_SM2 == this.publicKeyType) {
                    dataEnc = sm2Helper.encryptDataByPublicKey(dataDec, publicKeyStr);
                } else {
                    dataEnc = null;
                }
                return dataEnc;
            } else {
                return rsaHelper.encryptDataBigByPrivateKey(data, privateKeyStr);
            }
        } else if (KEY_TYPE_SM2 == this.privateKeyType) {
            String singData = sm2SignHelper.signToStringSoft(this.sm2UserId, data, privateKeyStr);
            if (StringUtils.isEmpty(singData)) {
                return null;
            }
            String dataDec = data + "|" + singData;
            String dataEnc = null;
            if (KEY_TYPE_RSA == this.publicKeyType) {
                dataEnc = rsaHelper.encryptDataBigByPublicKey(dataDec, publicKeyStr);
            } else if (KEY_TYPE_SM2 == this.publicKeyType) {
                dataEnc = sm2Helper.encryptDataByPublicKey(dataDec, publicKeyStr);
            } else {
                dataEnc = null;
            }
            return dataEnc;
        } else {
            return null;
        }
    }

    /**
     * 使用公钥解密，RSA使用公钥解密，SM2使用私钥解密公钥验签
     *
     * @param data 待解密的数据
     * @return
     */

    public String decryptString(String data) {
        if (KEY_TYPE_RSA == this.publicKeyType) {
            if (this.rsaWithSign) {
                String dataDec = null;
                if (KEY_TYPE_RSA == this.privateKeyType) {
                    dataDec = rsaHelper.decryptDataBigByPrivateKey(data, privateKeyStr);
                } else if (KEY_TYPE_SM2 == this.privateKeyType) {
                    dataDec = sm2Helper.decryptDataByPrivateKey(data, privateKeyStr);
                } else {
                    dataDec = null;
                }
                if (StringUtils.isEmpty(dataDec)) {
                    return dataDec;
                }
                int dataSpitIndex = dataDec.lastIndexOf("|");
                if (dataSpitIndex <= 0 || dataSpitIndex >= data.length() - 1) {
                    return null;
                }
                String dataClear = dataDec.substring(0, dataSpitIndex);
                String dataSign = dataDec.substring(dataSpitIndex + 1);
                boolean verifyResult = rsaHelper.verifySignData(dataClear, dataSign, rsaHelper.loadPublicKey(publicKeyStr));
                if (verifyResult) {
                    return dataClear;
                } else {
                    return null;
                }
            } else {
                return rsaHelper.decryptDataBigByPublicKey(data, publicKeyStr);
            }
        } else if (KEY_TYPE_SM2 == this.publicKeyType) {
            String dataDec = null;
            if (KEY_TYPE_RSA == this.privateKeyType) {
                dataDec = rsaHelper.decryptDataBigByPrivateKey(data, privateKeyStr);
            } else if (KEY_TYPE_SM2 == this.privateKeyType) {
                dataDec = sm2Helper.decryptDataByPrivateKey(data, privateKeyStr);
            } else {
                dataDec = null;
            }
            if (StringUtils.isEmpty(dataDec)) {
                return dataDec;
            }
            int dataSpitIndex = dataDec.lastIndexOf("|");
            if (dataSpitIndex <= 0 || dataSpitIndex >= data.length() - 1) {
                return null;
            }
            String dataClear = dataDec.substring(0, dataSpitIndex);
            String dataSign = dataDec.substring(dataSpitIndex + 1);
            boolean verifyResult = sm2SignHelper.verifyByStringSoft(this.sm2UserId, dataClear, dataSign, publicKeyStr);
            if (verifyResult) {
                return dataClear;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * 使用私钥签名
     *
     * @param data 待签名的数据
     * @return
     */
    public String signString(String data) {
        if (KEY_TYPE_RSA == this.privateKeyType) {
            return rsaHelper.getSignData(data, rsaHelper.loadPrivateKey(privateKeyStr));
        } else if (KEY_TYPE_SM2 == this.privateKeyType) {
            return sm2SignHelper.signToStringSoft(this.sm2UserId, data, privateKeyStr);
        } else {
            return null;
        }
    }

    /**
     * 使用公钥验签
     *
     * @param data     待验签的数据
     * @param dataSign 签名值
     * @return
     */
    public boolean verifySignString(String data, String dataSign) {
        if (KEY_TYPE_RSA == this.publicKeyType) {
            return rsaHelper.verifySignData(data, dataSign, rsaHelper.loadPublicKey(publicKeyStr));
        } else if (KEY_TYPE_SM2 == this.publicKeyType) {
            return sm2SignHelper.verifyByStringSoft(this.sm2UserId, data, dataSign, publicKeyStr);
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "CommonEncryptHelper{" +
                "publicKeyStr='" + publicKeyStr + '\'' +
                ", publicKeyType=" + publicKeyType +
                ", privateKeyStr='" + privateKeyStr + '\'' +
                ", privateKeyType=" + privateKeyType +
                ", sm2UserId='" + sm2UserId + '\'' +
                ", rsaWithSign=" + rsaWithSign +
                ", rsaHelper=" + rsaHelper +
                ", sm2Helper=" + sm2Helper +
                ", sm2SignHelper=" + sm2SignHelper +
                '}';
    }
}
