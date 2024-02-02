/**
 * @copyright www.ruomm.com
 * @author 牛牛-wanruome@163.com
 * @create 2020年6月23日 下午3:36:24
 */
package com.ruomm.javax.encryptx;

import com.ruomm.javax.corex.Base64Utils;
import com.ruomm.javax.loggingx.Log;
import com.ruomm.javax.loggingx.LogFactory;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.*;
import java.security.cert.Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class KeyLoadUtil {
    static {
        BCProviderLoadUtil.loadProvider();
    }

    private final static Log log = LogFactory.getLog(KeyLoadUtil.class);
    public final static String PUBLIC_KEY = "publicKey";
    public final static String PRIVATE_KEY = "privateKey";
    /**
     * 加密算法RSA
     */
    public final static String RSA_ALGORITHM = "RSA";

    public final static String BC_PROVIDER = "BC";
    /**
     * 读取秘钥证书
     */

    /**
     * 将签名私钥证书文件读取为证书密钥对
     *
     * @param keyfilePath   证书文件路径
     * @param storePwd      存储密码
     * @param keypwd        密钥对密码
     * @param keyAliasName  密钥对名称，keyAliasName为空keyAliasIndex为负数则遍历证书取得密钥对名称
     * @param keyAliasIndex 密钥对序号，keyAliasName为空keyAliasIndex为负数则遍历证书取得密钥对名称
     * @param type          存储类型
     * @param provider      证书提供者工厂名，JAVA证书留空，BouncyCastleProvider证书填写BC
     * @return 证书密钥对
     */
    public static KeyPair loadKeyPair(String keyfilePath, String storePwd, String keypwd, String keyAliasName, int keyAliasIndex, String type, String provider) {
        FileInputStream fis = null;
        KeyPair keyPair = null;
        try {

            KeyStore ks = null;
            if (null == provider || provider.length() <= 0) {
                ks = KeyStore.getInstance(type);
            } else {
                ks = KeyStore.getInstance(type, provider);
            }
            fis = new FileInputStream(keyfilePath);
            char[] nPassword = null;
            nPassword = null == storePwd || storePwd.length() <= 0 ? null : storePwd.toCharArray();
            if (null != ks) {
                ks.load(fis, nPassword);
            }
            // 私钥
            // 判断keyAlias是否存在
            Enumeration<String> aliases = ks.aliases();
            String keyAlias = null;
            int tmpIndex = 0;
            if (aliases.hasMoreElements()) {
                String tmpKeyAlias = aliases.nextElement();
                log.debug("pfx's alias----->" + tmpKeyAlias);
                if ((null == keyAliasName || keyAliasName.length() <= 0) & keyAliasIndex < 0) {
                    if (null == keyAlias || keyAlias.length() <= 0) {
                        keyAlias = tmpKeyAlias;
                    }
                } else if (null == keyAliasName || keyAliasName.length() <= 0) {
                    if (keyAliasIndex == tmpIndex) {
                        if (null == keyAlias || keyAlias.length() <= 0) {
                            keyAlias = tmpKeyAlias;
                        }
                    }
                } else {
                    if (keyAliasName.equalsIgnoreCase(tmpKeyAlias)) {
                        if (null == keyAlias || keyAlias.length() <= 0) {
                            keyAlias = tmpKeyAlias;
                        }
                    }
                }
                tmpIndex++;
            }
            if (null != keyAlias && keyAlias.length() > 0) {
                char[] kPassword = null;
                kPassword = null == keypwd || keypwd.length() <= 0 ? null : keypwd.toCharArray();
                PrivateKey privateKey = (PrivateKey) ks.getKey(keyAlias, kPassword);
                // 公钥
                Certificate certificate = ks.getCertificate(keyAlias);
                PublicKey publicKey = certificate.getPublicKey();
                if (null != publicKey && null != privateKey) {
                    keyPair = new KeyPair(publicKey, privateKey);
                }
            }
        } catch (Exception e) {
            log.error("Error:loadKeyPairFromPfx", e);
        } finally {
            try {
                if (null != fis) {
                    fis.close();
                }
            } catch (Exception e2) {
                // TODO: handle exception
            }
        }
        return keyPair;
    }

    /**
     * 将签名私钥证书文件读取为证书密钥对Map存储
     *
     * @param keyfilePath   证书文件路径
     * @param storePwd      存储密码
     * @param keypwd        密钥对密码
     * @param keyAliasName  密钥对名称，keyAliasName为空keyAliasIndex为负数则遍历证书取得密钥对名称
     * @param keyAliasIndex 密钥对序号，keyAliasName为空keyAliasIndex为负数则遍历证书取得密钥对名称
     * @param type          存储类型
     * @param provider      证书提供者工厂名，JAVA证书留空，BouncyCastleProvider证书填写BC
     * @return 证书密钥对Map存储
     */
    public static Map<String, String> loadKeyMap(String keyfilePath, String storePwd, String keypwd, String keyAliasName, int keyAliasIndex, String type, String provider) {
        FileInputStream fis = null;
        Map<String, String> keyMap = null;
        try {

            KeyStore ks = null;
            if (null == provider || provider.length() <= 0) {
                ks = KeyStore.getInstance(type);
            } else {
                ks = KeyStore.getInstance(type, provider);
            }
            fis = new FileInputStream(keyfilePath);
            char[] nPassword = null;
            nPassword = null == storePwd || storePwd.length() <= 0 ? null : storePwd.toCharArray();
            if (null != ks) {
                ks.load(fis, nPassword);
            }
            // 私钥
            // 判断keyAlias是否存在
            Enumeration<String> aliases = ks.aliases();
            String keyAlias = null;
            int tmpIndex = 0;
            if (aliases.hasMoreElements()) {
                String tmpKeyAlias = aliases.nextElement();
                log.debug("pfx's alias----->" + tmpKeyAlias);
                if ((null == keyAliasName || keyAliasName.length() <= 0) & keyAliasIndex < 0) {
                    if (null == keyAlias || keyAlias.length() <= 0) {
                        keyAlias = tmpKeyAlias;
                    }
                } else if (null == keyAliasName || keyAliasName.length() <= 0) {
                    if (keyAliasIndex == tmpIndex) {
                        if (null == keyAlias || keyAlias.length() <= 0) {
                            keyAlias = tmpKeyAlias;
                        }
                    }
                } else {
                    if (keyAliasName.equalsIgnoreCase(tmpKeyAlias)) {
                        if (null == keyAlias || keyAlias.length() <= 0) {
                            keyAlias = tmpKeyAlias;
                        }
                    }
                }
                tmpIndex++;
            }
            if (null != keyAlias && keyAlias.length() > 0) {
                char[] kPassword = null;
                kPassword = null == keypwd || keypwd.length() <= 0 ? null : keypwd.toCharArray();
                PrivateKey privateKey = (PrivateKey) ks.getKey(keyAlias, kPassword);
                String privateKeyStr = Base64Utils.encodeToString(privateKey.getEncoded());
                // 公钥
                Certificate certificate = ks.getCertificate(keyAlias);
                PublicKey publicKey = certificate.getPublicKey();
                String publicKeyStr = Base64Utils.encodeToString(publicKey.getEncoded());
                if ((null != publicKeyStr && publicKeyStr.length() > 0)
                        || (null != privateKeyStr && privateKeyStr.length() > 0)) {
                    keyMap = new HashMap<String, String>();
                    if (null != publicKeyStr && publicKeyStr.length() > 0) {
                        keyMap.put(PUBLIC_KEY, publicKeyStr);
                    }
                    if (null != privateKeyStr && privateKeyStr.length() > 0) {
                        keyMap.put(PRIVATE_KEY, privateKeyStr);
                    }
                }
            }
        } catch (Exception e) {
            log.error("Error:loadKeyPairFromPfx", e);
        } finally {
            try {
                if (null != fis) {
                    fis.close();
                }
            } catch (Exception e2) {
                // TODO: handle exception
            }

        }
        return keyMap;
    }

    public static KeyPair loadKeyPairFromP12(String keyfilePath, String password) {
        return loadKeyPair(keyfilePath, password, password, null, -1, "PKCS12", null);
    }

    public static KeyPair loadKeyPairFromP12(String keyfilePath, String storePwd, String keypwd) {
        return loadKeyPair(keyfilePath, storePwd, keypwd, null, -1, "PKCS12", null);
    }

    public static KeyPair loadKeyPairFromP12(String keyfilePath, String storePwd, String keypwd, String keyAliasName) {
        return loadKeyPair(keyfilePath, storePwd, keypwd, keyAliasName, -1, "PKCS12", null);
    }

    /**
     * 将签名私钥证书文件(*.p12)读取为证书密钥对
     *
     * @param keyfilePath   证书文件路径
     * @param storePwd      存储密码
     * @param keypwd        密钥对密码
     * @param keyAliasName  密钥对名称，keyAliasName为空keyAliasIndex为负数则遍历证书取得密钥对名称
     * @param keyAliasIndex 密钥对序号，keyAliasName为空keyAliasIndex为负数则遍历证书取得密钥对名称
     * @return 证书密钥对
     * @paramDefault type 存储类型
     * @paramDefault provider 证书提供者工厂名，JAVA证书留空，BouncyCastleProvider证书填写BC
     */
    public static KeyPair loadKeyPairFromP12(String keyfilePath, String storePwd, String keypwd, String keyAliasName, int keyAliasIndex) {
        return loadKeyPair(keyfilePath, storePwd, keypwd, keyAliasName, keyAliasIndex, "PKCS12", null);
    }

    public static Map<String, String> loadKeyMapFromP12(String keyfilePath, String password) {
        return loadKeyMap(keyfilePath, password, password, null, -1, "PKCS12", null);
    }

    public static Map<String, String> loadKeyMapFromP12(String keyfilePath, String storePwd, String keypwd) {
        return loadKeyMap(keyfilePath, storePwd, keypwd, null, -1, "PKCS12", null);
    }

    public static Map<String, String> loadKeyMapFromP12(String keyfilePath, String storePwd, String keypwd, String keyAliasName) {
        return loadKeyMap(keyfilePath, storePwd, keypwd, keyAliasName, -1, "PKCS12", null);
    }

    /**
     * 将签名私钥证书文件(*.p12)读取为证书密钥对Map存储
     *
     * @param keyfilePath   证书文件路径
     * @param storePwd      存储密码
     * @param keypwd        密钥对密码
     * @param keyAliasName  密钥对名称，keyAliasName为空keyAliasIndex为负数则遍历证书取得密钥对名称
     * @param keyAliasIndex 密钥对序号，keyAliasName为空keyAliasIndex为负数则遍历证书取得密钥对名称
     * @return 证书密钥对Map存储
     * @paramDefault type 存储类型
     * @paramDefault provider 证书提供者工厂名，JAVA证书留空，BouncyCastleProvider证书填写BC
     */
    public static Map<String, String> loadKeyMapFromP12(String keyfilePath, String storePwd, String keypwd, String keyAliasName, int keyAliasIndex) {
        return loadKeyMap(keyfilePath, storePwd, keypwd, keyAliasName, keyAliasIndex, "PKCS12", null);
    }

    public static KeyPair loadKeyPairFromPfx(String keyfilePath, String password) {
        return loadKeyPair(keyfilePath, password, password, null, -1, "PKCS12", "BC");
    }

    public static KeyPair loadKeyPairFromPfx(String keyfilePath, String storePwd, String keypwd) {
        return loadKeyPair(keyfilePath, storePwd, keypwd, null, -1, "PKCS12", "BC");
    }

    public static KeyPair loadKeyPairFromPfx(String keyfilePath, String storePwd, String keypwd, String keyAliasName) {
        return loadKeyPair(keyfilePath, storePwd, keypwd, keyAliasName, -1, "PKCS12", "BC");
    }

    /**
     * 将签名私钥证书文件(*.pfx)读取为证书密钥对
     *
     * @param keyfilePath   证书文件路径
     * @param storePwd      存储密码
     * @param keypwd        密钥对密码
     * @param keyAliasName  密钥对名称，keyAliasName为空keyAliasIndex为负数则遍历证书取得密钥对名称
     * @param keyAliasIndex 密钥对序号，keyAliasName为空keyAliasIndex为负数则遍历证书取得密钥对名称
     * @return 证书密钥对
     * @paramDefault type 存储类型
     * @paramDefault provider 证书提供者工厂名，JAVA证书留空，BouncyCastleProvider证书填写BC
     */
    public static KeyPair loadKeyPairFromPfx(String keyfilePath, String storePwd, String keypwd, String keyAliasName, int keyAliasIndex) {
        return loadKeyPair(keyfilePath, storePwd, keypwd, keyAliasName, keyAliasIndex, "PKCS12", "BC");
    }

    public static Map<String, String> loadKeyMapFromPfx(String keyfilePath, String password) {
        return loadKeyMap(keyfilePath, password, password, null, -1, "PKCS12", "BC");
    }

    public static Map<String, String> loadKeyMapFromPfx(String keyfilePath, String storePwd, String keypwd) {
        return loadKeyMap(keyfilePath, storePwd, keypwd, null, -1, "PKCS12", "BC");
    }

    public static Map<String, String> loadKeyMapFromPfx(String keyfilePath, String storePwd, String keypwd, String keyAliasName) {
        return loadKeyMap(keyfilePath, storePwd, keypwd, keyAliasName, -1, "PKCS12", "BC");
    }

    /**
     * 将签名私钥证书文件(*.pfx)读取为证书密钥对Map存储
     *
     * @param keyfilePath   证书文件路径
     * @param storePwd      存储密码
     * @param keypwd        密钥对密码
     * @param keyAliasName  密钥对名称，keyAliasName为空keyAliasIndex为负数则遍历证书取得密钥对名称
     * @param keyAliasIndex 密钥对序号，keyAliasName为空keyAliasIndex为负数则遍历证书取得密钥对名称
     * @return 证书密钥对Map存储
     * @paramDefault type 存储类型
     * @paramDefault provider 证书提供者工厂名，JAVA证书留空，BouncyCastleProvider证书填写BC
     */
    public static Map<String, String> loadKeyMapFromPfx(String keyfilePath, String storePwd, String keypwd, String keyAliasName, int keyAliasIndex) {
        return loadKeyMap(keyfilePath, storePwd, keypwd, keyAliasName, keyAliasIndex, "PKCS12", "BC");
    }


    public static KeyPair loadKeyPairFromJks(String keyfilePath, String password) {
        return loadKeyPair(keyfilePath, password, password, null, -1, "JKS", null);
    }

    public static KeyPair loadKeyPairFromJks(String keyfilePath, String storePwd, String keypwd) {
        return loadKeyPair(keyfilePath, storePwd, keypwd, null, -1, "JKS", null);
    }

    public static KeyPair loadKeyPairFromJks(String keyfilePath, String storePwd, String keypwd, String keyAliasName) {
        return loadKeyPair(keyfilePath, storePwd, keypwd, keyAliasName, -1, "JKS", null);
    }

    /**
     * 将签名私钥证书文件(*.jks)读取为证书密钥对
     *
     * @param keyfilePath   证书文件路径
     * @param storePwd      存储密码
     * @param keypwd        密钥对密码
     * @param keyAliasName  密钥对名称，keyAliasName为空keyAliasIndex为负数则遍历证书取得密钥对名称
     * @param keyAliasIndex 密钥对序号，keyAliasName为空keyAliasIndex为负数则遍历证书取得密钥对名称
     * @return 证书密钥对
     * @paramDefault type 存储类型
     * @paramDefault provider 证书提供者工厂名，JAVA证书留空，BouncyCastleProvider证书填写BC
     */
    public static KeyPair loadKeyPairFromJks(String keyfilePath, String storePwd, String keypwd, String keyAliasName, int keyAliasIndex) {
        return loadKeyPair(keyfilePath, storePwd, keypwd, keyAliasName, keyAliasIndex, "JKS", null);
    }

    public static Map<String, String> loadKeyMapFromJsk(String keyfilePath, String password) {
        return loadKeyMap(keyfilePath, password, password, null, -1, "JKS", null);
    }

    public static Map<String, String> loadKeyMapFromJsk(String keyfilePath, String storePwd, String keypwd) {
        return loadKeyMap(keyfilePath, storePwd, keypwd, null, -1, "JKS", null);
    }

    public static Map<String, String> loadKeyMapFromJsk(String keyfilePath, String storePwd, String keypwd, String keyAliasName) {
        return loadKeyMap(keyfilePath, storePwd, keypwd, keyAliasName, -1, "JKS", null);
    }

    /**
     * 将签名私钥证书文件(*.jks)读取为证书密钥对Map存储
     *
     * @param keyfilePath   证书文件路径
     * @param storePwd      存储密码
     * @param keypwd        密钥对密码
     * @param keyAliasName  密钥对名称，keyAliasName为空keyAliasIndex为负数则遍历证书取得密钥对名称
     * @param keyAliasIndex 密钥对序号，keyAliasName为空keyAliasIndex为负数则遍历证书取得密钥对名称
     * @return 证书密钥对Map存储
     * @paramDefault type 存储类型
     * @paramDefault provider 证书提供者工厂名，JAVA证书留空，BouncyCastleProvider证书填写BC
     */
    public static Map<String, String> loadKeyMapFromJsk(String keyfilePath, String storePwd, String keypwd, String keyAliasName, int keyAliasIndex) {
        return loadKeyMap(keyfilePath, storePwd, keypwd, keyAliasName, keyAliasIndex, "JKS", null);
    }

    public static KeyPair loadKeyPairByJksP12PfxFile(String keyfilePath, String password) {
        return loadKeyPair(keyfilePath, password, password, null, -1, parseKeyTypeByFile(keyfilePath), parseKeyProviderByFile(keyfilePath));
    }

    public static KeyPair loadKeyPairByJksP12PfxFile(String keyfilePath, String storePwd, String keypwd) {
        return loadKeyPair(keyfilePath, storePwd, keypwd, null, -1, parseKeyTypeByFile(keyfilePath), parseKeyProviderByFile(keyfilePath));
    }

    public static KeyPair loadKeyPairByJksP12PfxFile(String keyfilePath, String storePwd, String keypwd, String keyAliasName) {
        return loadKeyPair(keyfilePath, storePwd, keypwd, keyAliasName, -1, parseKeyTypeByFile(keyfilePath), parseKeyProviderByFile(keyfilePath));
    }

    /**
     * 将签名私钥证书文件(*.jks,*.p12,*.pfx)读取为证书密钥对
     *
     * @param keyfilePath   证书文件路径
     * @param storePwd      存储密码
     * @param keypwd        密钥对密码
     * @param keyAliasName  密钥对名称，keyAliasName为空keyAliasIndex为负数则遍历证书取得密钥对名称
     * @param keyAliasIndex 密钥对序号，keyAliasName为空keyAliasIndex为负数则遍历证书取得密钥对名称
     * @return 证书密钥对
     * @paramDefault type 存储类型
     * @paramDefault provider 证书提供者工厂名，JAVA证书留空，BouncyCastleProvider证书填写BC
     */
    public static KeyPair loadKeyPairByJksP12PfxFile(String keyfilePath, String storePwd, String keypwd, String keyAliasName, int keyAliasIndex) {
        return loadKeyPair(keyfilePath, storePwd, keypwd, keyAliasName, keyAliasIndex, parseKeyTypeByFile(keyfilePath), parseKeyProviderByFile(keyfilePath));
    }

    public static Map<String, String> loadKeyMapByJksP12PfxFile(String keyfilePath, String password) {
        return loadKeyMap(keyfilePath, password, password, null, -1, parseKeyTypeByFile(keyfilePath), parseKeyProviderByFile(keyfilePath));
    }

    public static Map<String, String> loadKeyMapByJksP12PfxFile(String keyfilePath, String storePwd, String keypwd) {
        return loadKeyMap(keyfilePath, storePwd, keypwd, null, -1, parseKeyTypeByFile(keyfilePath), parseKeyProviderByFile(keyfilePath));
    }

    public static Map<String, String> loadKeyMapByJksP12PfxFile(String keyfilePath, String storePwd, String keypwd, String keyAliasName) {
        return loadKeyMap(keyfilePath, storePwd, keypwd, keyAliasName, -1, parseKeyTypeByFile(keyfilePath), parseKeyProviderByFile(keyfilePath));
    }

    /**
     * 将签名私钥证书文件(*.jks,*.p12,*.pfx)读取为证书密钥对Map存储
     *
     * @param keyfilePath   证书文件路径
     * @param storePwd      存储密码
     * @param keypwd        密钥对密码
     * @param keyAliasName  密钥对名称，keyAliasName为空keyAliasIndex为负数则遍历证书取得密钥对名称
     * @param keyAliasIndex 密钥对序号，keyAliasName为空keyAliasIndex为负数则遍历证书取得密钥对名称
     * @return 证书密钥对Map存储
     * @paramDefault type 存储类型
     * @paramDefault provider 证书提供者工厂名，JAVA证书留空，BouncyCastleProvider证书填写BC
     */
    public static Map<String, String> loadKeyMapByJksP12PfxFile(String keyfilePath, String storePwd, String keypwd, String keyAliasName, int keyAliasIndex) {
        return loadKeyMap(keyfilePath, storePwd, keypwd, keyAliasName, keyAliasIndex, parseKeyTypeByFile(keyfilePath), parseKeyProviderByFile(keyfilePath));
    }


    private static String parseKeyTypeByFile(String keyfilePath) {
        if (null == keyfilePath || keyfilePath.length() <= 0) {
            return "JKS";
        }
        String lowerPath = keyfilePath.toLowerCase();
        if (lowerPath.endsWith(".pfx")) {
            return "PKCS12";
        } else if (lowerPath.endsWith(".p12")) {
            return "PKCS12";
        } else if (lowerPath.endsWith(".jks")) {
            return "JKS";
        } else {
            return "JKS";
        }
    }

    private static String parseKeyProviderByFile(String keyfilePath) {
        if (null == keyfilePath || keyfilePath.length() <= 0) {
            return null;
        }
        String lowerPath = keyfilePath.toLowerCase();
        if (lowerPath.endsWith(".pfx")) {
            return "BC";
        } else if (lowerPath.endsWith(".p12")) {
            return null;
        } else if (lowerPath.endsWith(".jks")) {
            return null;
        } else {
            return null;
        }
    }

    /**
     * 从字符串中加载公钥
     *
     * @param publicKeyStr 公钥数据字符串
     * @param algorithm    加密方法
     * @param provider     提供者
     * @return 公钥
     * @Throws Exception 加载公钥时产生的异常
     */
    public static PublicKey loadPublicKey(String publicKeyStr, String algorithm, String provider) {
        try {
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64Utils.decode(publicKeyStr));
            KeyFactory keyFactory = parseKeyFactory(algorithm, provider);
            PublicKey publicK = keyFactory.generatePublic(keySpec);
            return publicK;
        } catch (Exception e) {
            log.error("Error:loadPublicKey", e);
            return null;
        }
    }

    /**
     * 从字符串中加载私钥<br>
     * 加载时使用的是PKCS8EncodedKeySpec（PKCS#8编码的Key指令）。
     *
     * @param privateKeyStr 私钥数据字符串
     * @param algorithm     加密方法
     * @param provider      提供者
     * @return 私钥
     * @Throws Exception 加载私钥时产生的异常
     */
    public static PrivateKey loadPrivateKey(String privateKeyStr, String algorithm, String provider) {
        try {
            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(Base64Utils.decode(privateKeyStr));
            KeyFactory keyFactory = parseKeyFactory(algorithm, provider);
            PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
            return privateK;
        } catch (Exception e) {
            log.error("Error:loadPrivateKey", e);
            return null;
        }
    }

    /**
     * 从文件中输入流中加载公钥
     *
     * @param in        公钥输入流
     * @param algorithm 加密方法
     * @param provider  提供者
     * @return 公钥
     * @Throws Exception 加载公钥时产生的异常
     */
    public static PublicKey loadPublicKey(InputStream in, String algorithm, String provider) {
        return loadPublicKey(in, algorithm, provider, null);
    }

    public static PublicKey loadPublicKey(InputStream in, String algorithm, String provider, String charsetName) {
        try {
            return loadPublicKey(UtilsApp.readKey(in, charsetName), algorithm, provider);
        } catch (Exception e) {
            log.error("Error:loadPublicKey", e);
            return null;
        }
    }

    /**
     * 从文件中加载私钥
     *
     * @param in        私钥文件
     * @param algorithm 加密方法
     * @param provider  提供者
     * @return 私钥
     * @Throws Exception 加载私钥时产生的异常
     */
    public static PrivateKey loadPrivateKey(InputStream in, String algorithm, String provider) {
        return loadPrivateKey(in, algorithm, provider, null);
    }

    public static PrivateKey loadPrivateKey(InputStream in, String algorithm, String provider, String charsetName) {
        try {
            return loadPrivateKey(UtilsApp.readKey(in, charsetName), algorithm, provider);
        } catch (Exception e) {
            log.error("Error:loadPrivateKey", e);
            return null;
        }
    }

    /**
     * 解析KeyFactory
     *
     * @param algorithm 加密方法
     * @param provider  提供者
     * @return KeyFactory
     * @Throws Exception 解析KeyFactory时产生的异常
     */
    private static KeyFactory parseKeyFactory(String algorithm, String provider) throws NoSuchAlgorithmException, NoSuchProviderException {
        KeyFactory keyFactory = null;
        String realAlgorithm = null == algorithm || algorithm.length() <= 0 ? RSA_ALGORITHM : algorithm;
        if (null == provider || provider.length() <= 0) {
            keyFactory = KeyFactory.getInstance(realAlgorithm);
        } else {
            keyFactory = KeyFactory.getInstance(realAlgorithm, provider);
        }
        return keyFactory;
    }
}
