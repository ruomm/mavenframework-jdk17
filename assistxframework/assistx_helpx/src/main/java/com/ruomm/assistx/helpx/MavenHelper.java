/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年9月11日 下午2:12:09
 */
package com.ruomm.assistx.helpx;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class MavenHelper {
    public static String printPomHelper(String artifactId) {
        return printHelper(artifactId, "res/pom.xml", "com.ruomm");
    }

    public static String printHelper(String artifactId) {
        return printHelper(artifactId, "res/readme.txt", "com.ruomm");
    }

    public static String printHelper(String artifactId, String subRes) {
        return printHelper(artifactId, subRes, "com.ruomm");
    }

    public static String printHelper(String artifactId, String subRes, String packageName) {
        String resPath = null;
        if (null == subRes || subRes.length() <= 0) {
            resPath = "res/readme.txt";
        } else {
            resPath = subRes.replaceAll("\\\\", "/");
            if (!subRes.contains("/")) {
                resPath = "res/" + resPath;
            }
            if (resPath.startsWith("/")) {
                resPath = resPath.substring(1);
            }
        }
        String realPack = null == packageName || packageName.length() <= 0 ? "com.ruomm" : packageName;
        String resUrl = getResPathByArtifactId(realPack, artifactId, resPath);
        if (null == resUrl || resUrl.length() <= 0) {
            return null;
        }
        String resultStr = null;
        InputStream stream = null;
        try {
            URL url = MavenHelper.class.getResource(resUrl);
            stream = url.openStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line);
                sb.append("\r\n");
            }
            resultStr = sb.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
            resultStr = null;
        } finally {
            try {
                if (null != stream) {
                    stream.close();
                }
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
        return resultStr;
    }

    private static String getResPathByArtifactId(String packageName, String artifactId, String subRes) {
        if (null == packageName || packageName.length() <= 0) {
            return null;
        }
        if (null == artifactId || artifactId.length() <= 0) {
            return null;
        }
        if (null == subRes || subRes.length() <= 0) {
            return null;
        }
        String packPath = packageName.replaceAll("\\\\", "/").replaceAll("\\.", "/");
        if (!packPath.startsWith("/")) {
            packPath = "/" + packPath;
        }
        if (!packPath.endsWith("/")) {
            packPath = packPath + "/";
        }
        String artifactPath = artifactId.replaceAll("_", "/").replaceAll("-", "/").replaceAll("\\.", "/");
        if (artifactPath.startsWith("/")) {
            artifactPath = artifactPath.substring(1);
        }
        if (artifactPath.endsWith("/")) {
            int subIndex = artifactPath.length() - 1;
            artifactPath = artifactPath.substring(0, subIndex);
        }
        String resPath = subRes.replaceAll("\\\\", "/");
        if (!resPath.startsWith("/")) {
            resPath = "/" + resPath;
        }
        if (resPath.endsWith("/")) {
            int subIndex = resPath.length() - 1;
            resPath = resPath.substring(0, subIndex);
        }
        String path = packPath + artifactPath + resPath;
        return path;

    }
}
