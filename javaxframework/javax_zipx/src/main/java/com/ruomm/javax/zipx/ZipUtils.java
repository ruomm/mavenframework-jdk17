/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年9月18日 下午5:12:19
 */
package com.ruomm.javax.zipx;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import java.io.File;

public class ZipUtils {
    public static void unZip(String sourcePath, String dest, String passwd) throws ZipException {
        ZipFile zfile = new ZipFile(sourcePath);
        if (!zfile.isValidZipFile()) {
            throw new ZipException("Error:ZipUtils.unZip->压缩文件不合法，可能已经损坏！");
        }
        File file = new File(dest);
        if (file.isDirectory() && !file.exists()) {
            file.mkdirs();
        }
        if (zfile.isEncrypted()) {
            zfile.setPassword(passwd.toCharArray());
        }
        zfile.extractAll(dest);
    }

//	/**
//	 * 使用给定密码压缩指定文件或文件夹到指定位置.
//	 * <p>
//	 * dest可传最终压缩文件存放的绝对路径,也可以传存放目录,也可以传null或者"".<br />
//	 * 如果传null或者""则将压缩文件存放在当前目录,即跟源文件同目录,压缩文件名取源文件名,以.zip为后缀;<br />
//	 * 如果以路径分隔符(File.separator)结尾,则视为目录,压缩文件名取源文件名,以.zip为后缀,否则视为文件名.
//	 *
//	 * @param src         要压缩的文件或文件夹路径
//	 * @param dest        压缩文件存放路径
//	 * @param isCreateDir 是否在压缩文件里创建目录,仅在压缩文件为目录时有效.<br />
//	 *                    如果为false,将直接压缩目录下文件到压缩文件.
//	 * @param passwd      压缩使用的密码
//	 * @return 最终的压缩文件存放的绝对路径,如果为null则说明压缩失败.
//	 */
//	public static String zip(String src, String dest, boolean isCreateDir, String passwd) {
//		File srcFile = new File(src);
//		ZipParameters parameters = new ZipParameters();
//		parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE); // 压缩方式
//		parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL); // 压缩级别
//		if (!StringUtils.isEmpty(passwd)) {
//			parameters.setEncryptFiles(true);
//			parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_STANDARD); // 加密方式
//			parameters.setFileNameInZip(fileNameInZip);
//		}
//		try {
//			ZipFile zipFile = new ZipFile(dest);
//			zipFile.setP
//			if (srcFile.isDirectory()) {
//				// 如果不创建目录的话,将直接把给定目录下的文件压缩到压缩文件,即没有目录结构
//				if (!isCreateDir) {
//					File[] subFiles = srcFile.listFiles();
//					ArrayList<File> temp = new ArrayList<File>();
//					Collections.addAll(temp, subFiles);
//					zipFile.addFiles(temp, parameters);
//					return dest;
//				}
//				zipFile.addFolder(srcFile, parameters);
//			}
//			else {
//				zipFile.addFile(fileToAdd, parameters);
//				zipFile.addFile(srcFile, parameters);
//			}
//			return dest;
//		}
//		catch (ZipException e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
}
