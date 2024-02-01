package com.ruomm.javax.encryptx.guomi;

import com.ruomm.javax.corex.Base64Utils;
import com.ruomm.javax.corex.CharsetUtils;
import com.ruomm.javax.loggingx.Log;
import com.ruomm.javax.loggingx.LogFactory;

import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SM4Digest {
    private final static Log log = LogFactory.getLog(SM4Digest.class);

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public boolean isHexString() {
        return hexString;
    }

    public void setHexString(boolean hexString) {
        this.hexString = hexString;
    }

    private String secretKey = "";

    public String getIv() {
        return iv;
    }

    public void setIv(String iv) {
        this.iv = iv;
    }

    private String iv = "";

    private boolean hexString = false;

    public SM4Digest() {
    }

    public String encryptData_ECB(String plainText, String charsetName) {
        try {
            SM4_Context ctx = new SM4_Context();
            ctx.isPadding = true;
            ctx.mode = SM4.SM4_ENCRYPT;

            byte[] keyBytes;
            if (hexString) {
                keyBytes = Util.hexStringToBytes(secretKey);
            } else {
                keyBytes = secretKey.getBytes(Charset.forName("ISO-8859-1"));
            }

            SM4 sm4 = new SM4();
            sm4.sm4_setkey_enc(ctx, keyBytes);
            Charset charset = CharsetUtils.parseRealCharset(charsetName, Charset.forName("UTF-8"));
            byte[] encrypted = sm4.sm4_crypt_ecb(ctx, plainText.getBytes(charset));
            String cipherText = Base64Utils.encodeToString(encrypted);
            if (cipherText != null && cipherText.trim().length() > 0) {
                Pattern p = Pattern.compile("\\s*|\t|\r|\n");
                Matcher m = p.matcher(cipherText);
                cipherText = m.replaceAll("");
            }
            return cipherText;
        } catch (Exception e) {
            log.error("Error:encryptData_ECB", e);
            return null;
        }
    }

    public String decryptData_ECB(String cipherText, String charsetName) {
        try {
            SM4_Context ctx = new SM4_Context();
            ctx.isPadding = true;
            ctx.mode = SM4.SM4_DECRYPT;

            byte[] keyBytes;
            if (hexString) {
                keyBytes = Util.hexStringToBytes(secretKey);
            } else {
                keyBytes = secretKey.getBytes(Charset.forName("ISO-8859-1"));
            }

            SM4 sm4 = new SM4();
            sm4.sm4_setkey_dec(ctx, keyBytes);
            Charset charset = CharsetUtils.parseRealCharset(charsetName, Charset.forName("UTF-8"));
            byte[] decrypted = sm4.sm4_crypt_ecb(ctx, Base64Utils.decode(cipherText));
            return new String(decrypted, charset);
        } catch (Exception e) {
            log.error("Error:decryptData_ECB", e);
            return null;
        }
    }

    public String encryptData_CBC(String plainText, String charsetName) {
        try {
            SM4_Context ctx = new SM4_Context();
            ctx.isPadding = true;
            ctx.mode = SM4.SM4_ENCRYPT;

            byte[] keyBytes;
            byte[] ivBytes;
            if (hexString) {
                keyBytes = Util.hexStringToBytes(secretKey);
                ivBytes = Util.hexStringToBytes(iv);
            } else {
                keyBytes = secretKey.getBytes(Charset.forName("ISO-8859-1"));
                ivBytes = iv.getBytes(Charset.forName("ISO-8859-1"));
            }

            SM4 sm4 = new SM4();
            sm4.sm4_setkey_enc(ctx, keyBytes);
            Charset charset = CharsetUtils.parseRealCharset(charsetName, Charset.forName("UTF-8"));
            byte[] encrypted = sm4.sm4_crypt_cbc(ctx, ivBytes, plainText.getBytes(charset));
            String cipherText = Base64Utils.encodeToString(encrypted);
            if (cipherText != null && cipherText.trim().length() > 0) {
                Pattern p = Pattern.compile("\\s*|\t|\r|\n");
                Matcher m = p.matcher(cipherText);
                cipherText = m.replaceAll("");
            }
            return cipherText;
        } catch (Exception e) {
            log.error("Error:encryptData_CBC", e);
            return null;
        }
    }

    public String decryptData_CBC(String cipherText, String charsetName) {
        try {
            SM4_Context ctx = new SM4_Context();
            ctx.isPadding = true;
            ctx.mode = SM4.SM4_DECRYPT;

            byte[] keyBytes;
            byte[] ivBytes;
            if (hexString) {
                keyBytes = Util.hexStringToBytes(secretKey);
                ivBytes = Util.hexStringToBytes(iv);
            } else {
                keyBytes = secretKey.getBytes(Charset.forName("ISO-8859-1"));
                ivBytes = iv.getBytes(Charset.forName("ISO-8859-1"));
            }

            SM4 sm4 = new SM4();
            sm4.sm4_setkey_dec(ctx, keyBytes);
            byte[] decrypted = sm4.sm4_crypt_cbc(ctx, ivBytes, Base64Utils.decode(cipherText));
            Charset charset = CharsetUtils.parseRealCharset(charsetName, Charset.forName("UTF-8"));
            return new String(decrypted, charset);
        } catch (Exception e) {
            log.error("Error:decryptData_CBC", e);
            return null;
        }
    }

}
