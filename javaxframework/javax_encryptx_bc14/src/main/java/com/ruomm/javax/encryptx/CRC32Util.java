/**
 * @copyright www.ruomm.com
 * @author 牛牛-wanruome@163.com
 * @create 2020年11月20日 上午9:08:15
 */
package com.ruomm.javax.encryptx;

import com.ruomm.javax.corex.CharsetUtils;
import com.ruomm.javax.loggingx.Log;
import com.ruomm.javax.loggingx.LogFactory;

import java.io.*;
import java.nio.charset.Charset;
import java.util.zip.CRC32;

public class CRC32Util {
    private final static Log log = LogFactory.getLog(CRC32Util.class);
    private final static int BUFFER_SIZE = 1024;

    private static String crc32ParseLongToHex(Long resultVal) {
        long valLong = null == resultVal ? 0 : resultVal;
        int valInt = (int) valLong;
        String data = Integer.toHexString(valInt);
        int dataSize = null == data ? 0 : data.length();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 8 - dataSize; i++) {
            sb.append("0");
        }
        if (dataSize > 0) {
            sb.append(data);
        }
        return sb.toString();
    }

    private static Long crc32EncodingStringToLong(String sourceString, String charsetName) {
        if (null == sourceString) {
            return null;
        }
        Long resultVal = null;
        try {
            Charset charset = CharsetUtils.parseRealCharset(charsetName, Charset.forName("UTF-8"));
            CRC32 crc32 = new CRC32();
            crc32.update(sourceString.getBytes(charset));
            resultVal = crc32.getValue();
        } catch (Exception e) {
            log.error("Error:crc32EncodingStringToLong", e);
            resultVal = null;
        }
        return resultVal;
    }

    private static Long crc32EncodingInputStreamToLong(InputStream in, String saltStr, String charsetName) {
        Long resultVal = null;
        BufferedInputStream inputStream = null;
        boolean isBuffer = false;
        try {
            if (null == in) {
                return null;
            }
            if (inputStream instanceof BufferedInputStream) {
                inputStream = (BufferedInputStream) in;
                isBuffer = true;
            } else {
                inputStream = new BufferedInputStream(in);
                isBuffer = false;
            }

            byte[] saltData;
            if (null != saltStr && saltStr.length() > 0) {
                Charset charset = CharsetUtils.parseRealCharset(charsetName, Charset.forName("UTF-8"));
                saltData = saltStr.getBytes(charset);
            } else {
                saltData = null;
            }
            CRC32 crc32 = new CRC32();
            byte[] buffer = new byte[BUFFER_SIZE];
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(BUFFER_SIZE);
            int len = 0;
            while ((len = inputStream.read(buffer)) > 0) {
                byteArrayOutputStream.write(buffer, 0, len);
                byte[] data = byteArrayOutputStream.toByteArray();
//				log.debug("--------");
//				log.debug(new String(data));
//				log.debug("--------");
                crc32.update(data);
                byteArrayOutputStream.reset();
            }
            byteArrayOutputStream.close();
            if (null != saltData && saltData.length > 0) {
                crc32.update(saltData);
            }
            resultVal = crc32.getValue();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            log.error("Error:crc32EncodingInputStreamToLong", e);
            resultVal = null;
        } finally {
            try {
                if (null != inputStream) {
                    inputStream.close();
                }
            } catch (Exception e) {
                // TODO: handle exception
                log.error("Error:crc32EncodingInputStreamToLong", e);
            }
            if (!isBuffer) {
                try {
                    if (null != in) {
                        in.close();
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                    log.error("Error:crc32EncodingInputStreamToLong", e);
                }
            }
        }
        return resultVal;
    }

    public static long encodingStringToLong(String sourceString, String charsetName) {
        Long resultVal = crc32EncodingStringToLong(sourceString, charsetName);
        return null == resultVal ? 0 : resultVal;
    }

    public static long encodingStringToLong(String sourceString) {
        Long resultVal = crc32EncodingStringToLong(sourceString, null);
        return null == resultVal ? 0 : resultVal;
    }

    public static long encodingInputStreamToLong(InputStream inputStream) {
        Long resultVal = crc32EncodingInputStreamToLong(inputStream, null, null);
        return null == resultVal ? 0 : resultVal;
    }

    public static long encodingFileToLong(String fileName) {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(fileName);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            log.error("Error:encodingFileToLong", e);
        }
        Long resultVal = crc32EncodingInputStreamToLong(inputStream, null, null);
        return null == resultVal ? 0 : resultVal;
    }

    public static long encodingFileToLong(File file) {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            log.error("Error:encodingFileToLong", e);
        }
        Long resultVal = crc32EncodingInputStreamToLong(inputStream, null, null);
        return null == resultVal ? 0 : resultVal;
    }

    public static String encodingString(String sourceString, String charsetName) {
        Long resultVal = crc32EncodingStringToLong(sourceString, charsetName);
        return crc32ParseLongToHex(resultVal);
    }

    public static String encodingString(String sourceString) {
        Long resultVal = crc32EncodingStringToLong(sourceString, null);
        return crc32ParseLongToHex(resultVal);
    }

    public static String encodingInputStream(InputStream inputStream) {
        Long resultVal = crc32EncodingInputStreamToLong(inputStream, null, null);
        return crc32ParseLongToHex(resultVal);
    }

    public static String encodingFile(String fileName) {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(fileName);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            log.error("Error:encodingFile", e);
        }
        Long resultVal = crc32EncodingInputStreamToLong(inputStream, null, null);
        return crc32ParseLongToHex(resultVal);
    }

    public static String encodingFile(File file) {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            log.error("Error:encodingFile", e);
        }
        Long resultVal = crc32EncodingInputStreamToLong(inputStream, null, null);
        return crc32ParseLongToHex(resultVal);
    }

}
