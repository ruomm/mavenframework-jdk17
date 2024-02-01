/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年3月8日 上午10:10:38
 */
package com.ruomm.javax.corex;

import com.ruomm.javax.loggingx.Log;
import com.ruomm.javax.loggingx.LogFactory;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.security.MessageDigest;

public class DigestByJdkUtils {
    private final static Log log = LogFactory.getLog(DigestByJdkUtils.class);
    private final static String METHOD_MD2 = "MD2";
    private final static String METHOD_MD5 = "MD5";
    private final static String METHOD_SHA1 = "SHA-1";
    private final static String METHOD_SHA256 = "SHA-256";
    private final static String METHOD_SHA384 = "SHA-384";
    private final static String METHOD_SHA512 = "SHA-512";
    private final static boolean RESULT_UPPER_CASE = true;
    public final static int BUFFER_SIZE = 1024;

    public static String encodingMD2(String data) {
        return encodingByMessageDigest(data, METHOD_MD2);
    }

    public static String encodingMD5(String data) {
        return encodingByMessageDigest(data, METHOD_MD5);
    }

    public static String encodingSHA1(String data) {
        return encodingByMessageDigest(data, METHOD_SHA1);
    }

    public static String encodingSHA256(String data) {
        return encodingByMessageDigest(data, METHOD_SHA256);
    }

    public static String encodingSHA384(String data) {
        return encodingByMessageDigest(data, METHOD_SHA384);
    }

    public static String encodingSHA512(String data) {
        return encodingByMessageDigest(data, METHOD_SHA512);
    }

    public static String encodingMD2(String data, String charsetName) {
        return encodingByMessageDigest(data, METHOD_MD2, charsetName);
    }

    public static String encodingMD5(String data, String charsetName) {
        return encodingByMessageDigest(data, METHOD_MD5, charsetName);
    }

    public static String encodingSHA1(String data, String charsetName) {
        return encodingByMessageDigest(data, METHOD_SHA1, charsetName);
    }

    public static String encodingSHA256(String data, String charsetName) {
        return encodingByMessageDigest(data, METHOD_SHA256, charsetName);
    }

    public static String encodingSHA384(String data, String charsetName) {
        return encodingByMessageDigest(data, METHOD_SHA384, charsetName);
    }

    public static String encodingSHA512(String data, String charsetName) {
        return encodingByMessageDigest(data, METHOD_SHA512, charsetName);
    }

    public static String encodingDataMD2(byte[] sourceData) {
        return getMessageDigest(sourceData, METHOD_MD2);
    }

    public static String encodingDataMD5(byte[] sourceData) {
        return getMessageDigest(sourceData, METHOD_MD5);
    }

    public static String encodingDataSHA1(byte[] sourceData) {
        return getMessageDigest(sourceData, METHOD_SHA1);
    }

    public static String encodingDataSHA256(byte[] sourceData) {
        return getMessageDigest(sourceData, METHOD_SHA256);
    }

    public static String encodingDataSHA384(byte[] sourceData) {
        return getMessageDigest(sourceData, METHOD_SHA384);
    }

    public static String encodingDataSHA512(byte[] sourceData) {
        return getMessageDigest(sourceData, METHOD_SHA512);
    }

    public static String encodingInputStreamMD2(InputStream sourceInputStream) {
        return encodingInputByMessageDigest(sourceInputStream, METHOD_MD2);
    }

    public static String encodingInputStreamMD5(InputStream sourceInputStream) {
        return encodingInputByMessageDigest(sourceInputStream, METHOD_MD5);
    }

    public static String encodingInputStreamSHA1(InputStream sourceInputStream) {
        return encodingInputByMessageDigest(sourceInputStream, METHOD_SHA1);
    }

    public static String encodingInputStreamSHA256(InputStream sourceInputStream) {
        return encodingInputByMessageDigest(sourceInputStream, METHOD_SHA256);
    }

    public static String encodingInputStreamSHA384(InputStream sourceInputStream) {
        return encodingInputByMessageDigest(sourceInputStream, METHOD_SHA384);
    }

    public static String encodingInputStreamSHA512(InputStream sourceInputStream) {
        return encodingInputByMessageDigest(sourceInputStream, METHOD_SHA512);
    }

    public static String encodingByMessageDigest(String sourceString, String digestMethod) {
        return encodingByMessageDigest(sourceString, digestMethod, null);
    }

    public static String encodingByMessageDigest(String sourceString, String digestMethod, String charsetName) {
        if (null == sourceString) {
            return null;
        }
        try {
            Charset charset = CharsetUtils.parseRealCharset(charsetName, Charset.forName("UTF-8"));
            return getMessageDigest(sourceString.getBytes(charset), digestMethod);
        } catch (Exception e) {
            log.error("Error:encodingByMessageDigest", e);
            return null;
        }
    }

    public static String encodingInputByMessageDigest(InputStream sourceInputStream, String digestMethod) {
        return getMessageDigestForInputStream(sourceInputStream, null, null, digestMethod);
    }

    public static String encodingInputByMessageDigest(InputStream sourceInputStream, String saltStr,
                                                      String digestMethod) {
        return getMessageDigestForInputStream(sourceInputStream, saltStr, null, digestMethod);
    }

    public static String encodingInputByMessageDigest(InputStream sourceInputStream, String saltStr, String charsetName,
                                                      String digestMethod) {
        return getMessageDigestForInputStream(sourceInputStream, saltStr, charsetName, digestMethod);
    }

    public static String getMessageDigest(byte[] sourceData, String digestMethod) {
        return getMessageDigestByJDKMode(sourceData, digestMethod);
    }

    private static String getMessageDigestByJDKMode(byte[] sourceData, String digestMethod) {
        if (null == sourceData) {
            return null;
        }
        String resultData;
        try {
            MessageDigest localMessageDigest = MessageDigest.getInstance(digestMethod);
            localMessageDigest.update(sourceData);
            byte[] resultBytes = localMessageDigest.digest();
            String resultTemp = HexUtils.encodeToString(resultBytes);
            if (RESULT_UPPER_CASE) {
                resultData = resultTemp.toUpperCase();
            } else {
                resultData = resultTemp;
            }
        } catch (Exception e) {
            // TODO: handle exception
            log.error("Error:getMessageDigestByJDKMode", e);
            resultData = null;
        }
        return resultData;
    }


//	private static String getMessageDigestByCCMode(byte[] sourceData, String digestMethod) {
//		if (null == sourceData) {
//			return null;
//		}
//		String resultData;
//		try {
//			if (isMethodEqual(METHOD_MD2, digestMethod)) {
//				resultData = DigestUtils.md2Hex(sourceData);
//			}
//			else if (isMethodEqual(METHOD_MD5, digestMethod)) {
//				resultData = DigestUtils.md5Hex(sourceData);
//			}
//			else if (isMethodEqual(METHOD_SHA1, digestMethod)) {
//				resultData = DigestUtils.sha1Hex(sourceData);
//			}
//			else if (isMethodEqual(METHOD_SHA256, digestMethod)) {
//				resultData = DigestUtils.sha256Hex(sourceData);
//			}
//			else if (isMethodEqual(METHOD_SHA384, digestMethod)) {
//				resultData = DigestUtils.sha384Hex(sourceData);
//			}
//			else if (isMethodEqual(METHOD_SHA512, digestMethod)) {
//				resultData = DigestUtils.sha512Hex(sourceData);
//			}
//			else {
//				return null;
//			}
//			if (RESULT_UPPER_CASE) {
//				resultData = resultData.toUpperCase();
//			}
//			else {
//				resultData = resultData.toLowerCase();
//			}
//		}
//		catch (Exception e) {
//			// TODO Auto-generated catch block
//			log.error("Error:getMessageDigestByCCMode",e);
//			resultData = null;
//		}
//		return resultData;
//	}

//	private static String getMessageDigestForInputStream(InputStream inputStream, String digestMethod) {
//		return getMessageDigestForInputStream(inputStream, null, ENCODING, digestMethod);
//	}
//
//	public static String getMessageDigestForInputStream(InputStream inputStream, String saltStr, String digestMethod) {
//		return getMessageDigestForInputStream(inputStream, saltStr, ENCODING, digestMethod);
//	}

    public static String getMessageDigestForInputStream(InputStream inputStream, String saltStr, String charsetName,
                                                        String digestMethod) {
        return getMessageDigestForInputStreamByJDKMode(inputStream, saltStr, charsetName, digestMethod);
    }

    public static String getMessageDigestForInputStreamByJDKMode(InputStream inputStream, String saltStr,
                                                                 String charsetName, String digestMethod) {
        String resultData;
        try {
            if (null == inputStream) {
                return null;
            }

            byte[] saltData;
            if (null != saltStr && saltStr.length() > 0) {
                Charset charset = CharsetUtils.parseRealCharset(charsetName, Charset.forName("UTF-8"));
                saltData = saltStr.getBytes(charset);
            } else {
                saltData = null;
            }

            MessageDigest localMessageDigest = MessageDigest.getInstance(digestMethod);
            byte[] buffer = new byte[BUFFER_SIZE];
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(BUFFER_SIZE);
            int len = 0;
            while ((len = inputStream.read(buffer)) > 0) {
                byteArrayOutputStream.write(buffer, 0, len);
                byte[] data = byteArrayOutputStream.toByteArray();
//				log.debug("--------");
//				log.debug(new String(data));
//				log.debug("--------");
                localMessageDigest.update(data);
                byteArrayOutputStream.reset();
            }
            if (null != saltData && saltData.length > 0) {
                localMessageDigest.update(saltData);
            }
            byte[] resultBytes = localMessageDigest.digest();
            String resultTemp = HexUtils.encodeToString(resultBytes);
            if (RESULT_UPPER_CASE) {
                resultData = resultTemp.toUpperCase();
            } else {
                resultData = resultTemp;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            log.error("Error:getMessageDigestForInputStreamByJDKMode", e);
            resultData = null;
        } finally {
            try {
                if (null != inputStream) {
                    inputStream.close();
                }
            } catch (Exception e) {
                // TODO: handle exception
                log.error("Error:getMessageDigestForInputStreamByJDKMode", e);
            }
        }
        return resultData;
    }


    private static boolean isMethodEqual(String methodSystem, String methodUser) {
        if (null == methodUser || null == methodSystem || methodUser.length() <= 0 || methodSystem.length() <= 0) {
            return false;
        }
        String tempSystem = methodSystem.replace("-", "").toUpperCase();
        String tempUser = methodUser.replace("-", "").toUpperCase();
        boolean flag = tempSystem.equals(tempUser);
        return flag;
    }

}
