package com.ruomm.javax.encryptx.guomi;

import com.ruomm.javax.corex.Base64Utils;
import com.ruomm.javax.loggingx.Log;
import com.ruomm.javax.loggingx.LogFactory;
import org.bouncycastle.asn1.*;
import org.bouncycastle.crypto.digests.SM3Digest;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECPoint;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.Enumeration;

/**
 * 国密算法的签名、验签
 */
class SM2SignVerUtils {
    private final static Log log = LogFactory.getLog(SM2SignVerUtils.class);
    /**
     * 默认USERID
     */
    private static byte[] DEF_USR_ID_BYTES = new byte[0];

    /**
     * 秘钥对生成SM3签名
     *
     * @param privatekey 私钥信息
     * @param userIdData 用户ID信息
     * @param sourceData 原文信息
     * @return SM3签名
     */
    public static byte[] ToSm3MDByPrivateKey(byte[] privatekey, byte[] userIdData, byte[] sourceData) {
        SM2SignVO sm2SignVO = new SM2SignVO();
        sm2SignVO.setSm2_type("sign");
        SM2Factory factory = SM2Factory.getInstance();
        BigInteger userD = new BigInteger(privatekey);
        if (SMConfig.isLog) {
            log.debug("ToSm3MDByPrivateKey->" + "userD:" + userD.toString(16));
        }
        sm2SignVO.setSm2_userd(userD.toString(16));

        ECPoint userKey = factory.ecc_point_g.multiply(userD);
        if (SMConfig.isLog) {
            log.debug("ToSm3MDByPrivateKey->" + "椭圆曲线点X: " + userKey.getXCoord().toBigInteger().toString(16));
            log.debug("ToSm3MDByPrivateKey->" + "椭圆曲线点Y: " + userKey.getYCoord().toBigInteger().toString(16));
        }

        SM3Digest sm3Digest = new SM3Digest();
        byte[] z = null == userIdData || userIdData.length <= 0 ?
                factory.sm2GetZ(DEF_USR_ID_BYTES, userKey) : factory.sm2GetZ(userIdData, userKey);
        if (SMConfig.isLog) {
            log.debug("ToSm3MDByPrivateKey->" + "SM3摘要Z: " + Util.getHexString(z));
            log.debug("ToSm3MDByPrivateKey->" + "被加密数据的16进制: " + Util.getHexString(sourceData));
        }
        sm2SignVO.setSm3_z(Util.getHexString(z));
        sm2SignVO.setSign_express(Util.getHexString(sourceData));

        sm3Digest.update(z, 0, z.length);
        sm3Digest.update(sourceData, 0, sourceData.length);
        byte[] md = new byte[32];
        sm3Digest.doFinal(md, 0);
        if (SMConfig.isLog) {
            log.debug("ToSm3MDByPrivateKey->" + "SM3摘要值: " + Util.getHexString(md));
        }
        return md;
    }

    /**
     * 秘钥对生成SM3签名
     *
     * @param publicKey  公钥信息
     * @param userIdData 用户ID信息
     * @param sourceData 原文信息
     * @return SM3签名
     */
    public static byte[] ToSm3MDByPublicKey(byte[] publicKey, byte[] userIdData, byte[] sourceData) {
        try {
            SM2SignVO verifyVo = new SM2SignVO();
            verifyVo.setSm2_type("verify");
            SM2Factory factory = SM2Factory.getInstance();
            ECPoint userKey = getPublicKeyECPoint(factory.ecc_curve, publicKey);

            SM3Digest sm3Digest = new SM3Digest();
            byte[] z = null == userIdData || userIdData.length <= 0 ?
                    factory.sm2GetZ(DEF_USR_ID_BYTES, userKey) : factory.sm2GetZ(userIdData, userKey);
            if (SMConfig.isLog) {
                log.debug("ToSm3MDByPublicKey->" + "SM3摘要Z: " + Util.getHexString(z));
            }
            verifyVo.setSm3_z(Util.getHexString(z));
            sm3Digest.update(z, 0, z.length);
            sm3Digest.update(sourceData, 0, sourceData.length);
            byte[] md = new byte[32];
            sm3Digest.doFinal(md, 0);
            if (SMConfig.isLog) {
                log.debug("ToSm3MDByPublicKey->" + "SM3摘要值: " + Util.getHexString(md));
            }
            return md;
        } catch (IllegalArgumentException e) {
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 秘钥对生成SM3签名
     *
     * @param privatekey  私钥信息
     * @param userIdData  用户ID信息
     * @param inputStream 输入流
     * @return SM3签名
     */
    public static byte[] ToSm3MDByPrivateKeyStream(byte[] privatekey, byte[] userIdData, InputStream inputStream) {
        try {
            SM2SignVO sm2SignVO = new SM2SignVO();
            sm2SignVO.setSm2_type("sign");
            SM2Factory factory = SM2Factory.getInstance();
            BigInteger userD = new BigInteger(privatekey);
            if (SMConfig.isLog) {
                log.debug("ToSm3MDByPrivateKeyStream->" + "userD:" + userD.toString(16));
            }
            sm2SignVO.setSm2_userd(userD.toString(16));

            ECPoint userKey = factory.ecc_point_g.multiply(userD);
            if (SMConfig.isLog) {
                log.debug("ToSm3MDByPrivateKeyStream->" + "椭圆曲线点X: " + userKey.getXCoord().toBigInteger().toString(16));
                log.debug("ToSm3MDByPrivateKeyStream->" + "椭圆曲线点Y: " + userKey.getYCoord().toBigInteger().toString(16));
            }

            SM3Digest sm3Digest = new SM3Digest();
            byte[] z = null == userIdData || userIdData.length <= 0 ?
                    factory.sm2GetZ(DEF_USR_ID_BYTES, userKey) : factory.sm2GetZ(userIdData, userKey);
            if (SMConfig.isLog) {
                log.debug("ToSm3MDByPrivateKeyStream->" + "SM3摘要Z: " + Util.getHexString(z));
                log.debug("ToSm3MDByPrivateKeyStream->" + "被加密数据是输入流。");
            }
            sm2SignVO.setSm3_z(Util.getHexString(z));
            //		sm2SignVO.setSign_express(Util.getHexString(sourceData));

            sm3Digest.update(z, 0, z.length);
            //		sm3Digest.update(sourceData,0,sourceData.length);
            // 获取输入流
            byte[] buffer = new byte[1024];
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024);
            int len = 0;
            while ((len = inputStream.read(buffer)) > 0) {
                byteArrayOutputStream.write(buffer, 0, len);
                byte[] data = byteArrayOutputStream.toByteArray();
                sm3Digest.update(data, 0, data.length);
                byteArrayOutputStream.reset();
            }
            byte[] md = new byte[32];
            sm3Digest.doFinal(md, 0);
            if (SMConfig.isLog) {
                log.debug("ToSm3MDByPrivateKeyStream->" + "SM3摘要值: " + Util.getHexString(md));
            }
            return md;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 秘钥对生成SM3签名
     *
     * @param publicKey   公钥信息
     * @param userIdData  用户ID信息
     * @param inputStream 输入流
     * @return SM3签名
     */
    public static byte[] ToSm3MDByPublicKeyStream(byte[] publicKey, byte[] userIdData, InputStream inputStream) {
        try {
            SM2SignVO verifyVo = new SM2SignVO();
            verifyVo.setSm2_type("verify");
            SM2Factory factory = SM2Factory.getInstance();
            ECPoint userKey = getPublicKeyECPoint(factory.ecc_curve, publicKey);


            SM3Digest sm3Digest = new SM3Digest();
            byte[] z = null == userIdData || userIdData.length <= 0 ?
                    factory.sm2GetZ(DEF_USR_ID_BYTES, userKey) : factory.sm2GetZ(userIdData, userKey);
            if (SMConfig.isLog) {
                log.debug("ToSm3MDByPublicKeyStream->" + "SM3摘要Z: " + Util.getHexString(z));
            }
            verifyVo.setSm3_z(Util.getHexString(z));
            sm3Digest.update(z, 0, z.length);
            //			sm3Digest.update(sourceData,0,sourceData.length);
            // 获取输入流
            byte[] buffer = new byte[1024];
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024);
            int len = 0;
            while ((len = inputStream.read(buffer)) > 0) {
                byteArrayOutputStream.write(buffer, 0, len);
                byte[] data = byteArrayOutputStream.toByteArray();
                //				log.debug("--------");
                //				log.debug(new String(data));
                //				log.debug("--------");
                sm3Digest.update(data, 0, data.length);
                byteArrayOutputStream.reset();
            }

            byte[] md = new byte[32];
            sm3Digest.doFinal(md, 0);
            if (SMConfig.isLog) {
                log.debug("ToSm3MDByPublicKeyStream->" + "SM3摘要值: " + Util.getHexString(md));
            }
            return md;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 私钥签名
     * 使用SM3进行对明文数据计算一个摘要值
     *
     * @param privatekey 私钥信息
     * @param sm3Md      SM3信息摘要
     * @return 签名后的值
     */
    public static SM2SignVO Sign2SM2WithSM3(byte[] privatekey, byte[] sm3Md) {
        try {
            SM2SignVO sm2SignVO = new SM2SignVO();
            sm2SignVO.setSm2_type("sign");
            SM2Factory factory = SM2Factory.getInstance();
            BigInteger userD = new BigInteger(privatekey);
            if (SMConfig.isLog) {
                log.debug("Sign2SM2WithSM3->" + "userD:" + userD.toString(16));
            }
            sm2SignVO.setSm2_userd(userD.toString(16));

            ECPoint userKey = factory.ecc_point_g.multiply(userD);
            if (SMConfig.isLog) {
                log.debug("Sign2SM2WithSM3->" + "椭圆曲线点X: " + userKey.getXCoord().toBigInteger().toString(16));
                log.debug("Sign2SM2WithSM3->" + "椭圆曲线点Y: " + userKey.getYCoord().toBigInteger().toString(16));
            }

            sm2SignVO.setSm3_z(null);
            sm2SignVO.setSign_express(Util.getHexString(sm3Md));
            if (SMConfig.isLog) {
                log.debug("Sign2SM2WithSM3->" + "SM3摘要值: " + Util.getHexString(sm3Md));
            }
            sm2SignVO.setSm3_digest(Util.getHexString(sm3Md));

            SM2Result sm2Result = new SM2Result();
            factory.sm2Sign(sm3Md, userD, userKey, sm2Result);
            if (SMConfig.isLog) {
                log.debug("Sign2SM2WithSM3->" + "r: " + sm2Result.r.toString(16));
                log.debug("Sign2SM2WithSM3->" + "s: " + sm2Result.s.toString(16));
            }
            sm2SignVO.setSign_r(sm2Result.r.toString(16));
            sm2SignVO.setSign_s(sm2Result.s.toString(16));

            ASN1Integer d_r = new ASN1Integer(sm2Result.r);
            ASN1Integer d_s = new ASN1Integer(sm2Result.s);
            ASN1EncodableVector v2 = new ASN1EncodableVector();
            v2.add(d_r);
            v2.add(d_s);
            DERSequence sign = new DERSequence(v2);

            String result = Util.byteToHex(sign.getEncoded());
            sm2SignVO.setSm2_sign(result);
            if (SMConfig.isLog) {
                log.debug("Sign2SM2WithSM3->" + "签名结果值(Hex): " + sm2SignVO.getSm2_signForSoft());
                log.debug("Sign2SM2WithSM3->" + "签名结果值(base64): " + Base64Utils.encodeToString(Util.hexToByte(sm2SignVO.getSm2_signForSoft())));
            }
            return sm2SignVO;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 验证签名
     *
     * @param publicKey 公钥信息
     * @param sm3Md     SM3信息摘要
     * @param signData  签名信息
     * @return 验签的对象 包含了相关参数和验签结果
     */
    public static SM2SignVO VerifySignSM2WithSM3(byte[] publicKey, byte[] sm3Md, byte[] signData) {
        return VerifySignSM2WithSM3(publicKey, sm3Md, signData, false);
    }

    /**
     * 验证签名
     *
     * @param publicKey 公钥信息
     * @param sm3Md     SM3信息摘要
     * @param signData  签名信息
     * @param hardMode  硬件签名模式
     * @return 验签的对象 包含了相关参数和验签结果
     */
    public static SM2SignVO VerifySignSM2WithSM3(byte[] publicKey, byte[] sm3Md, byte[] signData, boolean hardMode) {
        try {
            SM2SignVO verifyVo = new SM2SignVO();
            verifyVo.setSm2_type("verify");
            SM2Factory factory = SM2Factory.getInstance();
            ECPoint userKey = getPublicKeyECPoint(factory.ecc_curve, publicKey);

            if (SMConfig.isLog) {
                log.debug("VerifySignSM2WithSM3->" + "SM3摘要值: " + Util.getHexString(sm3Md));
            }
            verifyVo.setSm3_z(null);
            verifyVo.setSm3_digest(Util.getHexString(sm3Md));

            SM2Result sm2Result;
            if (hardMode) {
                int length = 32;
                byte[] x = new byte[length];
                byte[] y = new byte[length];
                System.arraycopy(signData, 0, x, 0, length);
                System.arraycopy(signData, 32, y, 0, length);
                BigInteger r = new BigInteger(1, x);
                BigInteger s = new BigInteger(1, y);
                sm2Result = new SM2Result();
                sm2Result.r = r;
                sm2Result.s = s;
            } else {
                ByteArrayInputStream bis = new ByteArrayInputStream(signData);
                ASN1InputStream dis = new ASN1InputStream(bis);
                ASN1Primitive derObj = dis.readObject();
                Enumeration<ASN1Integer> e = ((ASN1Sequence) derObj).getObjects();
                BigInteger r = ((ASN1Integer) e.nextElement()).getValue();
                BigInteger s = ((ASN1Integer) e.nextElement()).getValue();
                sm2Result = new SM2Result();
                sm2Result.r = r;
                sm2Result.s = s;
            }

            if (SMConfig.isLog) {
                log.debug("VerifySignSM2WithSM3->" + "vr: " + sm2Result.r.toString(16));
                log.debug("VerifySignSM2WithSM3->" + "vs: " + sm2Result.s.toString(16));
            }
            verifyVo.setVerify_r(sm2Result.r.toString(16));
            verifyVo.setVerify_s(sm2Result.s.toString(16));
            factory.sm2Verify(sm3Md, userKey, sm2Result.r, sm2Result.s, sm2Result);
            boolean verifyFlag = sm2Result.r.equals(sm2Result.R);
            verifyVo.setVerify(verifyFlag);
            return verifyVo;
        } catch (IllegalArgumentException e) {
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 私钥签名
     * 使用SM3进行对明文数据计算一个摘要值
     *
     * @param privatekey 私钥信息
     * @param userIdData 用户ID信息
     * @param sourceData 原文信息
     * @return 签名后的值
     */
    public static SM2SignVO Sign2SM2(byte[] privatekey, byte[] userIdData, byte[] sourceData) {
        try {
            SM2SignVO sm2SignVO = new SM2SignVO();
            sm2SignVO.setSm2_type("sign");
            SM2Factory factory = SM2Factory.getInstance();
            BigInteger userD = new BigInteger(privatekey);
            if (SMConfig.isLog) {
                log.debug("Sign2SM2->" + "userD:" + userD.toString(16));
            }
            sm2SignVO.setSm2_userd(userD.toString(16));

            ECPoint userKey = factory.ecc_point_g.multiply(userD);
            if (SMConfig.isLog) {
                log.debug("Sign2SM2->" + "椭圆曲线点X: " + userKey.getXCoord().toBigInteger().toString(16));
                log.debug("Sign2SM2->" + "椭圆曲线点Y: " + userKey.getYCoord().toBigInteger().toString(16));
            }

            SM3Digest sm3Digest = new SM3Digest();
            byte[] z = null == userIdData || userIdData.length <= 0 ?
                    factory.sm2GetZ(DEF_USR_ID_BYTES, userKey) : factory.sm2GetZ(userIdData, userKey);
            if (SMConfig.isLog) {
                log.debug("Sign2SM2->" + "SM3摘要Z: " + Util.getHexString(z));
                log.debug("Sign2SM2->" + "被加密数据的16进制: " + Util.getHexString(sourceData));
            }
            sm2SignVO.setSm3_z(Util.getHexString(z));
            sm2SignVO.setSign_express(Util.getHexString(sourceData));

            sm3Digest.update(z, 0, z.length);
            sm3Digest.update(sourceData, 0, sourceData.length);
            byte[] md = new byte[32];
            sm3Digest.doFinal(md, 0);
            if (SMConfig.isLog) {
                log.debug("Sign2SM2->" + "SM3摘要值: " + Util.getHexString(md));
            }
            sm2SignVO.setSm3_digest(Util.getHexString(md));

            SM2Result sm2Result = new SM2Result();
            factory.sm2Sign(md, userD, userKey, sm2Result);
            if (SMConfig.isLog) {
                log.debug("Sign2SM2->" + "r: " + sm2Result.r.toString(16));
                log.debug("Sign2SM2->" + "s: " + sm2Result.s.toString(16));
            }
            sm2SignVO.setSign_r(sm2Result.r.toString(16));
            sm2SignVO.setSign_s(sm2Result.s.toString(16));

            ASN1Integer d_r = new ASN1Integer(sm2Result.r);
            ASN1Integer d_s = new ASN1Integer(sm2Result.s);
            ASN1EncodableVector v2 = new ASN1EncodableVector();
            v2.add(d_r);
            v2.add(d_s);
            DERSequence sign = new DERSequence(v2);

            String result = Util.byteToHex(sign.getEncoded());
            sm2SignVO.setSm2_sign(result);
            if (SMConfig.isLog) {
                log.debug("Sign2SM2->" + "签名结果值(Hex): " + sm2SignVO.getSm2_signForSoft());
                log.debug("Sign2SM2->" + "签名结果值(base64): " + Base64Utils.encodeToString(Util.hexToByte(sm2SignVO.getSm2_signForSoft())));
            }
            return sm2SignVO;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 私钥签名
     * 使用SM3进行对明文数据计算一个摘要值
     *
     * @param privatekey  私钥信息
     * @param userIdData  用户ID信息
     * @param inputStream 原文信息输入流
     * @return 签名后的值
     */
    public static SM2SignVO Sign2SM2ByInputStream(byte[] privatekey, byte[] userIdData, InputStream inputStream) {
        try {
            SM2SignVO sm2SignVO = new SM2SignVO();
            sm2SignVO.setSm2_type("sign");
            SM2Factory factory = SM2Factory.getInstance();
            BigInteger userD = new BigInteger(privatekey);
            if (SMConfig.isLog) {
                log.debug("Sign2SM2->" + "userD:" + userD.toString(16));
            }
            sm2SignVO.setSm2_userd(userD.toString(16));

            ECPoint userKey = factory.ecc_point_g.multiply(userD);
            if (SMConfig.isLog) {
                log.debug("Sign2SM2->" + "椭圆曲线点X: " + userKey.getXCoord().toBigInteger().toString(16));
                log.debug("Sign2SM2->" + "椭圆曲线点Y: " + userKey.getYCoord().toBigInteger().toString(16));
            }

            SM3Digest sm3Digest = new SM3Digest();
            byte[] z = null == userIdData || userIdData.length <= 0 ?
                    factory.sm2GetZ(DEF_USR_ID_BYTES, userKey) : factory.sm2GetZ(userIdData, userKey);
            if (SMConfig.isLog) {
                log.debug("Sign2SM2->" + "SM3摘要Z: " + Util.getHexString(z));
                log.debug("Sign2SM2->" + "被加密数据是输入流。");
            }
            sm2SignVO.setSm3_z(Util.getHexString(z));
            //		sm2SignVO.setSign_express(Util.getHexString(sourceData));

            sm3Digest.update(z, 0, z.length);
            //		sm3Digest.update(sourceData,0,sourceData.length);
            // 获取输入流
            byte[] buffer = new byte[1024];
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024);
            int len = 0;
            while ((len = inputStream.read(buffer)) > 0) {
                byteArrayOutputStream.write(buffer, 0, len);
                byte[] data = byteArrayOutputStream.toByteArray();
                sm3Digest.update(data, 0, data.length);
                byteArrayOutputStream.reset();
            }
            byte[] md = new byte[32];
            sm3Digest.doFinal(md, 0);
            if (SMConfig.isLog) {
                log.debug("Sign2SM2->" + "SM3摘要值: " + Util.getHexString(md));
            }
            sm2SignVO.setSm3_digest(Util.getHexString(md));

            SM2Result sm2Result = new SM2Result();
            factory.sm2Sign(md, userD, userKey, sm2Result);
            if (SMConfig.isLog) {
                log.debug("Sign2SM2->" + "r: " + sm2Result.r.toString(16));
                log.debug("Sign2SM2->" + "s: " + sm2Result.s.toString(16));
            }
            sm2SignVO.setSign_r(sm2Result.r.toString(16));
            sm2SignVO.setSign_s(sm2Result.s.toString(16));

            ASN1Integer d_r = new ASN1Integer(sm2Result.r);
            ASN1Integer d_s = new ASN1Integer(sm2Result.s);
            ASN1EncodableVector v2 = new ASN1EncodableVector();
            v2.add(d_r);
            v2.add(d_s);
            DERSequence sign = new DERSequence(v2);

            String result = Util.byteToHex(sign.getEncoded());
            sm2SignVO.setSm2_sign(result);
            if (SMConfig.isLog) {
                log.debug("Sign2SM2->" + "签名结果值(Hex): " + sm2SignVO.getSm2_signForSoft());
                log.debug("Sign2SM2->" + "签名结果值(base64): " + Base64Utils.encodeToString(Util.hexToByte(sm2SignVO.getSm2_signForSoft())));
            }
            return sm2SignVO;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 验证签名
     *
     * @param publicKey  公钥信息
     * @param userIdData 用户ID信息
     * @param sourceData 原文信息
     * @param signData   签名信息
     * @return 验签的对象 包含了相关参数和验签结果
     */
    public static SM2SignVO VerifySignSM2(byte[] publicKey, byte[] userIdData, byte[] sourceData, byte[] signData) {
        return VerifySignSM2(publicKey, userIdData, sourceData, signData, false);
    }

    /**
     * 验证签名
     *
     * @param publicKey  公钥信息
     * @param userIdData 用户ID信息
     * @param sourceData 原文信息
     * @param signData   签名信息
     * @param hardMode   硬件签名模式
     * @return 验签的对象 包含了相关参数和验签结果
     */
    public static SM2SignVO VerifySignSM2(byte[] publicKey, byte[] userIdData, byte[] sourceData, byte[] signData, boolean hardMode) {
        try {
            SM2SignVO verifyVo = new SM2SignVO();
            verifyVo.setSm2_type("verify");
            SM2Factory factory = SM2Factory.getInstance();
            ECPoint userKey = getPublicKeyECPoint(factory.ecc_curve, publicKey);

            SM3Digest sm3Digest = new SM3Digest();
            byte[] z = null == userIdData || userIdData.length <= 0 ?
                    factory.sm2GetZ(DEF_USR_ID_BYTES, userKey) : factory.sm2GetZ(userIdData, userKey);
            if (SMConfig.isLog) {
                log.debug("VerifySignSM2->" + "SM3摘要Z: " + Util.getHexString(z));
            }
            verifyVo.setSm3_z(Util.getHexString(z));
            sm3Digest.update(z, 0, z.length);
            sm3Digest.update(sourceData, 0, sourceData.length);
            byte[] md = new byte[32];
            sm3Digest.doFinal(md, 0);
            if (SMConfig.isLog) {
                log.debug("VerifySignSM2->" + "SM3摘要值: " + Util.getHexString(md));
            }
            verifyVo.setSm3_digest(Util.getHexString(md));
            SM2Result sm2Result;
            if (hardMode) {
                int length = 32;
                byte[] x = new byte[length];
                byte[] y = new byte[length];
                System.arraycopy(signData, 0, x, 0, length);
                System.arraycopy(signData, 32, y, 0, length);
                BigInteger r = new BigInteger(1, x);
                BigInteger s = new BigInteger(1, y);
                sm2Result = new SM2Result();
                sm2Result.r = r;
                sm2Result.s = s;
            } else {
                ByteArrayInputStream bis = new ByteArrayInputStream(signData);
                ASN1InputStream dis = new ASN1InputStream(bis);
                ASN1Primitive derObj = dis.readObject();
                Enumeration<ASN1Integer> e = ((ASN1Sequence) derObj).getObjects();
                BigInteger r = ((ASN1Integer) e.nextElement()).getValue();
                BigInteger s = ((ASN1Integer) e.nextElement()).getValue();
                sm2Result = new SM2Result();
                sm2Result.r = r;
                sm2Result.s = s;
            }

            if (SMConfig.isLog) {
                log.debug("VerifySignSM2->" + "vr: " + sm2Result.r.toString(16));
                log.debug("VerifySignSM2->" + "vs: " + sm2Result.s.toString(16));
            }
            verifyVo.setVerify_r(sm2Result.r.toString(16));
            verifyVo.setVerify_s(sm2Result.s.toString(16));
            factory.sm2Verify(md, userKey, sm2Result.r, sm2Result.s, sm2Result);
            boolean verifyFlag = sm2Result.r.equals(sm2Result.R);
            verifyVo.setVerify(verifyFlag);
            return verifyVo;
        } catch (IllegalArgumentException e) {
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 验证签名
     *
     * @param publicKey   公钥信息
     * @param userIdData  用户ID信息
     * @param inputStream 原文信息输入流
     * @param signData    签名信息
     * @return 验签的对象 包含了相关参数和验签结果
     */
    public static SM2SignVO VerifySignSM2ByInputStream(byte[] publicKey, byte[] userIdData, InputStream inputStream, byte[] signData) {
        return VerifySignSM2ByInputStream(publicKey, userIdData, inputStream, signData, false);
    }

    /**
     * 验证签名
     *
     * @param publicKey   公钥信息
     * @param userIdData  用户ID信息
     * @param inputStream 原文信息输入流
     * @param signData    签名信息
     * @param hardMode    硬件签名模式
     * @return 验签的对象 包含了相关参数和验签结果
     */
    public static SM2SignVO VerifySignSM2ByInputStream(byte[] publicKey, byte[] userIdData, InputStream inputStream, byte[] signData, boolean hardMode) {
        try {
            SM2SignVO verifyVo = new SM2SignVO();
            verifyVo.setSm2_type("verify");
            SM2Factory factory = SM2Factory.getInstance();
            ECPoint userKey = getPublicKeyECPoint(factory.ecc_curve, publicKey);


            SM3Digest sm3Digest = new SM3Digest();
            byte[] z = null == userIdData || userIdData.length <= 0 ?
                    factory.sm2GetZ(DEF_USR_ID_BYTES, userKey) : factory.sm2GetZ(userIdData, userKey);
            if (SMConfig.isLog) {
                log.debug("VerifySignSM2->" + "SM3摘要Z: " + Util.getHexString(z));
            }
            verifyVo.setSm3_z(Util.getHexString(z));
            sm3Digest.update(z, 0, z.length);
//			sm3Digest.update(sourceData,0,sourceData.length);
            // 获取输入流
            byte[] buffer = new byte[1024];
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024);
            int len = 0;
            while ((len = inputStream.read(buffer)) > 0) {
                byteArrayOutputStream.write(buffer, 0, len);
                byte[] data = byteArrayOutputStream.toByteArray();
//				log.debug("--------");
//				log.debug(new String(data));
//				log.debug("--------");
                sm3Digest.update(data, 0, data.length);
                byteArrayOutputStream.reset();
            }

            byte[] md = new byte[32];
            sm3Digest.doFinal(md, 0);
            if (SMConfig.isLog) {
                log.debug("VerifySignSM2->" + "SM3摘要值: " + Util.getHexString(md));
            }
            verifyVo.setSm3_digest(Util.getHexString(md));
            SM2Result sm2Result;
            if (hardMode) {
                int length = 32;
                byte[] x = new byte[length];
                byte[] y = new byte[length];
                System.arraycopy(signData, 0, x, 0, length);
                System.arraycopy(signData, 32, y, 0, length);
                BigInteger r = new BigInteger(1, x);
                BigInteger s = new BigInteger(1, y);
                sm2Result = new SM2Result();
                sm2Result.r = r;
                sm2Result.s = s;
            } else {
                ByteArrayInputStream bis = new ByteArrayInputStream(signData);
                ASN1InputStream dis = new ASN1InputStream(bis);
                ASN1Primitive derObj = dis.readObject();
                Enumeration<ASN1Integer> e = ((ASN1Sequence) derObj).getObjects();
                BigInteger r = ((ASN1Integer) e.nextElement()).getValue();
                BigInteger s = ((ASN1Integer) e.nextElement()).getValue();
                sm2Result = new SM2Result();
                sm2Result.r = r;
                sm2Result.s = s;
            }
            if (SMConfig.isLog) {
                log.debug("VerifySignSM2->" + "vr: " + sm2Result.r.toString(16));
                log.debug("VerifySignSM2->" + "vs: " + sm2Result.s.toString(16));
            }
            verifyVo.setVerify_r(sm2Result.r.toString(16));
            verifyVo.setVerify_s(sm2Result.s.toString(16));
            factory.sm2Verify(md, userKey, sm2Result.r, sm2Result.s, sm2Result);
            boolean verifyFlag = sm2Result.r.equals(sm2Result.R);
            verifyVo.setVerify(verifyFlag);
            return verifyVo;
        } catch (IllegalArgumentException e) {
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static ECPoint getPublicKeyECPoint(ECCurve ecc_curve, byte[] publicKey) {
        if (publicKey.length == 68) {
            int length = 32;
            byte[] x = new byte[length];
            byte[] y = new byte[length];
            System.arraycopy(publicKey, 4, x, 0, length);
            System.arraycopy(publicKey, 36, y, 0, length);
            return ecc_curve.createPoint(new BigInteger(1, x), new BigInteger(1, y));
        } else if (publicKey.length == 64) {
            int length = 32;
            byte[] x = new byte[length];
            byte[] y = new byte[length];
            System.arraycopy(publicKey, 0, x, 0, length);
            System.arraycopy(publicKey, 32, y, 0, length);
            return ecc_curve.createPoint(new BigInteger(1, x), new BigInteger(1, y));
        } else {
            return ecc_curve.decodePoint(publicKey);
        }
    }
}
