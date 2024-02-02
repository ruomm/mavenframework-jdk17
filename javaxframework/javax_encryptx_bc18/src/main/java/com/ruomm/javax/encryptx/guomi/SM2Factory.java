package com.ruomm.javax.encryptx.guomi;

import com.ruomm.javax.loggingx.Log;
import com.ruomm.javax.loggingx.LogFactory;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.digests.SM3Digest;
import org.bouncycastle.crypto.generators.ECKeyPairGenerator;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECKeyGenerationParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.math.ec.custom.gm.SM2P256V1Curve;

import java.math.BigInteger;
import java.security.SecureRandom;


public class SM2Factory {
    private final static Log log = LogFactory.getLog(SM2Factory.class);
    //国密参数
    public static String[] ecc_param = {
            "FFFFFFFEFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF00000000FFFFFFFFFFFFFFFF",
            "FFFFFFFEFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF00000000FFFFFFFFFFFFFFFC",
            "28E9FA9E9D9F5E344D5A9E4BCF6509A7F39789F515AB8F92DDBCBD414D940E93",
            "FFFFFFFEFFFFFFFFFFFFFFFFFFFFFFFF7203DF6B21C6052B53BBF40939D54123",
            "32C4AE2C1F1981195F9904466A39C9948FE30BBFF2660BE1715A4589334C74C7",
            "BC3736A2F4F6779C59BDCEE36B692153D0A9877CC62A474002DF32E52139F0A0"
    };

    public static SM2 Instance() {
        return new SM2();
    }

    public final BigInteger ecc_p;
    public final BigInteger ecc_a;
    public final BigInteger ecc_b;
    public final BigInteger ecc_n;
    public final BigInteger ecc_h;
    public final BigInteger ecc_gx;
    public final BigInteger ecc_gy;
    public final ECCurve ecc_curve;
    public final ECPoint ecc_point_g;
    public final ECDomainParameters ecc_bc_spec;
    public final ECKeyPairGenerator ecc_key_pair_generator;

    /**
     * 初始化方法
     *
     * @return
     */
    public static SM2Factory getInstance() {
        return new SM2Factory();
    }

    public SM2Factory() {

        SM2P256V1Curve ECCurve  = new SM2P256V1Curve();
        this.ecc_p = ECCurve.getQ();
        this.ecc_a = ECCurve.getA().toBigInteger();
        this.ecc_b = ECCurve.getB().toBigInteger();
        this.ecc_n = ECCurve.getOrder();
        this.ecc_h = ECCurve.getCofactor();
        this.ecc_gx = new BigInteger(ecc_param[4], 16);
        this.ecc_gy = new BigInteger(ecc_param[5], 16);

        this.ecc_point_g = ECCurve.createPoint(this.ecc_gx, this.ecc_gy);
        this.ecc_bc_spec = new ECDomainParameters(ECCurve, this.ecc_point_g, this.ecc_n, this.ecc_h);
        this.ecc_curve = ECCurve;
        ECKeyGenerationParameters ecc_ecgenparam;
        ecc_ecgenparam = new ECKeyGenerationParameters(this.ecc_bc_spec, new SecureRandom());

        this.ecc_key_pair_generator = new ECKeyPairGenerator();
        this.ecc_key_pair_generator.init(ecc_ecgenparam);
    }

    /**
     * 根据私钥、曲线参数计算Z
     *
     * @param userId
     * @param userKey
     * @return
     */
    public byte[] sm2GetZ(byte[] userId, ECPoint userKey) {
        SM3Digest sm3 = new SM3Digest();

        int len = userId.length * 8;
        sm3.update((byte) (len >> 8 & 0xFF));
        sm3.update((byte) (len & 0xFF));
        sm3.update(userId, 0, userId.length);

        byte[] p = Util.byteConvert32Bytes(this.ecc_a);
        sm3.update(p, 0, p.length);

        p = Util.byteConvert32Bytes(this.ecc_b);
        sm3.update(p, 0, p.length);

        p = Util.byteConvert32Bytes(this.ecc_gx);
        sm3.update(p, 0, p.length);

        p = Util.byteConvert32Bytes(this.ecc_gy);
        sm3.update(p, 0, p.length);

        p = Util.byteConvert32Bytes(userKey.normalize().getXCoord().toBigInteger());
        sm3.update(p, 0, p.length);

        p = Util.byteConvert32Bytes(userKey.normalize().getYCoord().toBigInteger());
        sm3.update(p, 0, p.length);

        byte[] md = new byte[sm3.getDigestSize()];
        sm3.doFinal(md, 0);
        return md;
    }

    /**
     * 签名相关值计算
     *
     * @param md
     * @param userD
     * @param userKey
     * @param sm2Result
     */
    public void sm2Sign(byte[] md, BigInteger userD, ECPoint userKey, SM2Result sm2Result) {
        BigInteger e = new BigInteger(1, md);
        BigInteger k = null;
        ECPoint kp = null;
        BigInteger r = null;
        BigInteger s = null;
        do {
            do {
                // 正式环境
                AsymmetricCipherKeyPair keypair = ecc_key_pair_generator.generateKeyPair();
                ECPrivateKeyParameters ecpriv = (ECPrivateKeyParameters) keypair.getPrivate();
                ECPublicKeyParameters ecpub = (ECPublicKeyParameters) keypair.getPublic();
                k = ecpriv.getD();
                kp = ecpub.getQ();
                if (SMConfig.isLog) {
//					log.debug("sm2Sign_>"+"BigInteger:" + k + "\nECPoint:" + kp);
                    log.debug("sm2Sign->" + "BigInteger:" + k + "，ECPoint:" + kp);
                    log.debug("sm2Sign->" + "计算曲线点X1: " + kp.getXCoord().toBigInteger().toString(16));
                    log.debug("sm2Sign->" + "计算曲线点Y1: " + kp.getYCoord().toBigInteger().toString(16));
                }
                r = e.add(kp.getXCoord().toBigInteger());
                r = r.mod(this.ecc_n);
            } while (r.equals(BigInteger.ZERO) || r.add(k).equals(this.ecc_n) || r.toString(16).length() != 64);

            // (1 + dA)~-1
            BigInteger da_1 = userD.add(BigInteger.ONE);
            da_1 = da_1.modInverse(this.ecc_n);
            // s
            s = r.multiply(userD);
            s = k.subtract(s).mod(this.ecc_n);
            s = da_1.multiply(s).mod(this.ecc_n);
        } while (s.equals(BigInteger.ZERO) || (s.toString(16).length() != 64));

        sm2Result.r = r;
        sm2Result.s = s;
    }

    /**
     * 验签
     *
     * @param md        sm3摘要
     * @param userKey   根据公钥decode一个ecpoint对象
     * @param r         没有特殊含义
     * @param s         没有特殊含义
     * @param sm2Result 接收参数的对象
     */
    public void sm2Verify(byte md[], ECPoint userKey, BigInteger r,
                          BigInteger s, SM2Result sm2Result) {
        sm2Result.R = null;
        BigInteger e = new BigInteger(1, md);
        BigInteger t = r.add(s).mod(this.ecc_n);
        if (t.equals(BigInteger.ZERO)) {
            return;
        } else {
            ECPoint x1y1 = ecc_point_g.multiply(sm2Result.s);
            if (SMConfig.isLog) {
                log.debug("sm2Verify->" + "计算曲线点X0: " + x1y1.normalize().getXCoord().toBigInteger().toString(16));
                log.debug("sm2Verify->" + "计算曲线点Y0: " + x1y1.normalize().getYCoord().toBigInteger().toString(16));
            }
            x1y1 = x1y1.add(userKey.multiply(t));
            if (SMConfig.isLog) {
                log.debug("sm2Verify->" + "计算曲线点X1: " + x1y1.normalize().getXCoord().toBigInteger().toString(16));
                log.debug("sm2Verify->" + "计算曲线点Y1: " + x1y1.normalize().getYCoord().toBigInteger().toString(16));
            }
            sm2Result.R = e.add(x1y1.normalize().getXCoord().toBigInteger()).mod(this.ecc_n);
            if (SMConfig.isLog) {
                log.debug("sm2Verify->" + "R: " + sm2Result.R.toString(16));
            }
            return;
        }
    }

}
