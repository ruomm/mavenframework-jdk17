/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年8月29日 上午9:40:51
 */
package com.ruomm.javax.corex;

import com.ruomm.javax.corex.helper.Base64Helper;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class Base64Utils {
    /**
     * Encodes Source String into Base64 String
     *
     * @param str Source String for Encode
     * @return Encoded Base64 String
     */
    public static String encodeString(String str) {
        return encodeString(str, null);
    }

    /**
     * Encodes Source String into Base64 String
     *
     * @param str         Source String for Encode
     * @param charsetName charset for Source String getByte
     * @return Encoded Base64 String
     */
    public static String encodeString(String str, String charsetName) {
        if (null == str) {
            return null;
        }
        Charset charset = CharsetUtils.parseRealCharset(charsetName, Charset.forName("UTF-8"));
        byte[] binaryData = str.getBytes(charset);
        return Base64Helper.getEncoder().encodeToString(binaryData);
    }

    /**
     * Decodes Base64 String into Destination String
     *
     * @param encodedStr Base64 String for Decode
     * @return String Destination String
     */
    public static String decodeString(String encodedStr) {
        return decodeString(encodedStr, null, false);
    }

    /**
     * Decodes Base64 String into Destination String
     *
     * @param encodedStr  Base64 String for Decode
     * @param charsetName charset for byte Array to Destination String
     * @return String Destination String
     */
    public static String decodeString(String encodedStr, String charsetName) {
        return decodeString(encodedStr, charsetName, false);
    }

    /**
     * Decodes Base64 String into Destination String
     *
     * @param encodedStr         Base64 String for Decode
     * @param charsetName        charset for byte Array to Destination String
     * @param isRemoveWhiteSpace whiteSpace is removed for Base64 String for Decode
     * @return String Destination String
     */
    public static String decodeString(String encodedStr, String charsetName, boolean isRemoveWhiteSpace) {
        if (null == encodedStr) {
            return null;
        }
        String tmpStr = null;
        if (isRemoveWhiteSpace) {
            tmpStr = encodedStr.replaceAll("\r", "").replaceAll("\n", "").replaceAll("\t", "").replaceAll(" ", "");
        } else {
            tmpStr = encodedStr;
        }
        byte[] dstBytes = Base64Helper.getDecoder().decode(tmpStr);
        Charset charset = CharsetUtils.parseRealCharset(charsetName, Charset.forName("UTF-8"));
        return new String(dstBytes, charset);
    }

    /**
     * Encodes the specified byte array into a String using the {@link Base64Helper} encoding scheme.
     *
     * <p>
     * This method first encodes all input bytes into a base64 encoded byte array and then constructs a new String by
     *
     * <p>
     * In other words, an invocation of this method has exactly the same effect as invoking
     * {@code new String(encode(src), Charset.forName("ISO-8859-1"))}.
     *
     * @param binaryData the byte array to encode
     * @return A String containing the resulting Base64 encoded characters
     */
    public static String encodeToString(byte[] binaryData) {
        if (binaryData == null) {
            return null;
        }
        return Base64Helper.getEncoder().encodeToString(binaryData);
    }

    /**
     * Encodes all bytes from the specified byte array into a newly-allocated byte array using the {@link Base64Helper}
     * encoding scheme. The returned byte array is of the length of the resulting bytes.
     *
     * @param src the byte array to encode
     * @return A newly-allocated byte array containing the resulting encoded bytes.
     */
    public byte[] encode(byte[] src) {
        if (null == src) {
            return null;
        }
        return Base64Helper.getEncoder().encode(src);
    }

    /**
     * Encodes all bytes from the specified byte array using the {@link Base64Helper} encoding scheme, writing the
     * resulting bytes to the given output byte array, starting at offset 0.
     *
     * <p>
     * It is the responsibility of the invoker of this method to make sure the output byte array {@code dst} has enough
     * space for encoding all bytes from the input byte array. No bytes will be written to the output byte array if the
     * output byte array is not big enough.
     *
     * @param src the byte array to encode
     * @param dst the output byte array
     * @return The number of bytes written to the output byte array
     * @Throws IllegalArgumentException if {@code dst} does not have enough space for encoding all input bytes.
     */
    public int encode(byte[] src, byte[] dst) {
        if (null == src) {
            return 0;
        }
        return Base64Helper.getEncoder().encode(src, dst);
    }

    /**
     * Encodes all remaining bytes from the specified byte buffer into a newly-allocated ByteBuffer using the
     * {@link Base64Helper} encoding scheme.
     * <p>
     * Upon return, the source buffer's position will be updated to its limit; its limit will not have been changed. The
     * returned output buffer's position will be zero and its limit will be the number of resulting encoded bytes.
     *
     * @param buffer the source ByteBuffer to encode
     * @return A newly-allocated byte buffer containing the encoded bytes.
     */
    public ByteBuffer encode(ByteBuffer buffer) {
        if (null == buffer) {
            return null;
        }
        return Base64Helper.getEncoder().encode(buffer);
    }

    /**
     * Decodes a Base64 encoded String into a newly-allocated byte array using the {@link Base64Helper} encoding scheme.
     *
     * <p>
     * An invocation of this method has exactly the same effect as invoking
     * {@code decode(src.getBytes(Charset.forName("ISO-8859-1")))}
     *
     * @param src the string to decode
     * @return A newly-allocated byte array containing the decoded bytes.
     * @Throws IllegalArgumentException if {@code src} is not in valid Base64 scheme
     */
    public static byte[] decode(String src) {
        if (null == src) {
            return null;
        }
        return Base64Helper.getDecoder().decode(src);
    }

    /**
     * Decodes all bytes from the input byte array using the {@link Base64Helper} encoding scheme, writing the results
     * into a newly-allocated output byte array. The returned byte array is of the length of the resulting bytes.
     *
     * @param src the byte array to decode
     * @return A newly-allocated byte array containing the decoded bytes.
     * @Throws IllegalArgumentException if {@code src} is not in valid Base64 scheme
     */
    public byte[] decode(byte[] src) {
        if (null == src) {
            return null;
        }
        return Base64Helper.getDecoder().decode(src);
    }

    /**
     * Decodes all bytes from the input byte array using the {@link Base64Helper} encoding scheme, writing the results
     * into the given output byte array, starting at offset 0.
     *
     * <p>
     * It is the responsibility of the invoker of this method to make sure the output byte array {@code dst} has enough
     * space for decoding all bytes from the input byte array. No bytes will be be written to the output byte array if
     * the output byte array is not big enough.
     *
     * <p>
     * If the input byte array is not in valid Base64 encoding scheme then some bytes may have been written to the
     * output byte array before IllegalargumentException is thrown.
     *
     * @param src the byte array to decode
     * @param dst the output byte array
     * @return The number of bytes written to the output byte array
     * @Throws IllegalArgumentException if {@code src} is not in valid Base64 scheme, or {@code dst} does not have
     * enough space for decoding all input bytes.
     */
    public int decode(byte[] src, byte[] dst) {
        if (null == src) {
            return 0;
        }
        return Base64Helper.getDecoder().decode(src, dst);
    }

    /**
     * Decodes all bytes from the input byte buffer using the {@link Base64Helper} encoding scheme, writing the results
     * into a newly-allocated ByteBuffer.
     *
     * <p>
     * Upon return, the source buffer's position will be updated to its limit; its limit will not have been changed. The
     * returned output buffer's position will be zero and its limit will be the number of resulting decoded bytes
     *
     * <p>
     * {@code IllegalArgumentException} is thrown if the input buffer is not in valid Base64 encoding scheme. The
     * position of the input buffer will not be advanced in this case.
     *
     * @param buffer the ByteBuffer to decode
     * @return A newly-allocated byte buffer containing the decoded bytes
     * @Throws IllegalArgumentException if {@code src} is not in valid Base64 scheme.
     */
    public ByteBuffer decode(ByteBuffer buffer) {
        if (null == buffer) {
            return null;
        }
        return Base64Helper.getDecoder().decode(buffer);
    }

    /**
     * remove WhiteSpace from MIME containing encoded Base64 data.
     *
     * @param src the Source String of base64 data (with WS)
     * @return String the new String
     */
//	public static String removeWhiteSpace(String src) {
//		if (null == src) {
//			return src;
//		}
//		String dst = src.replaceAll("\r", "").replaceAll("\n", "").replaceAll("\t", "").replaceAll(" ", "");
//		return dst;
//	}

//	private static boolean isWhiteSpace(char octect) {
//		return (octect == 0x20 || octect == 0xd || octect == 0xa || octect == 0x9);
//	}
//
//	/**
//	 * remove WhiteSpace from MIME containing encoded Base64 data.
//	 *
//	 * @param data the byte array of base64 data (with WS)
//	 * @return the new length
//	 */
//	private static int removeWhiteSpace(char[] data) {
//		if (data == null) {
//			return 0;
//		}
//		// count characters that's not whitespace
//		int newSize = 0;
//		int len = data.length;
//		for (int i = 0; i < len; i++) {
//			if (!isWhiteSpace(data[i])) {
//				data[newSize++] = data[i];
//			}
//		}
//		return newSize;
//	}

}
