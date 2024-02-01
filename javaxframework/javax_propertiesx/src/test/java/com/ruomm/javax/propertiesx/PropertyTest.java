package com.ruomm.javax.propertiesx;

import com.ruomm.javax.loggingx.LoggerConfig;

/**
 * @author 牛牛-wanruome@163.com
 * @version 1.0
 * @copyright www.ruomm.com
 * @create 2020/12/17 9:26
 */
public class PropertyTest {
    public final static String ENCODING = "GBK";
    public final static int LINE_SIZE = 7;

    public static void main(String[] args) {
        String dataValue = "<BR/>\"来源：综合@我们的太空 网友评论 央视新闻\"\n      \"" +
                "\n" +
                "  <BR/>流程\r\n编辑：\nu004\n" +
                "\n" +
                "<br> 【来源：北晚新视觉网】 \n" +
                "\n" +
                "\"声明：转载此文是出于传递更多信息之目的。若有来源标注错误或侵犯了您的合法权益，请作者持权属证明与本网联系，我们将及时更正、删除，谢谢。 邮箱地址：newmedia@xxcb.cn\n" +
                "\n" +
                "举报/反馈  " + "\n" + "\n";

        putAndVerify("data", dataValue);
        readAndVerify("data", dataValue);
    }

    public static void putAndVerify(String key, String dataValue) {
        LoggerConfig.configDebugLevel(LoggerConfig.DebugLevel.INFO);
        PropertyReaderCharset propertyReaderCharset = new PropertyReaderCharset("D:\\temp\\bdssconfig\\seqConfig.txt", ENCODING);
        propertyReaderCharset.setLineSize(LINE_SIZE);
        propertyReaderCharset.setAppendToCnMaoHao(true);
        propertyReaderCharset.loadProps(true);
        propertyReaderCharset.putProperty(key, dataValue);
        String dataStroe = propertyReaderCharset.getProperty(key);
        propertyReaderCharset.setAppendToCnMaoHao(true);
        propertyReaderCharset.storeProperty(null);
        System.out.println(dataStroe);
        System.out.println("输入校验：" + dataStroe.equals(dataValue));
    }

    public static void readAndVerify(String key, String dataValue) {
        LoggerConfig.configDebugLevel(LoggerConfig.DebugLevel.INFO);
        PropertyReaderCharset propertyReaderCharset = new PropertyReaderCharset("D:\\temp\\bdssconfig\\seqConfig.txt", ENCODING);
        propertyReaderCharset.setLineSize(LINE_SIZE);
        propertyReaderCharset.setAppendToMaoHao(true);
        propertyReaderCharset.loadProps(true);
        String dataStroe = propertyReaderCharset.getProperty(key);
        System.out.println(dataStroe);
        System.out.println("获取校验：" + dataStroe.equals(dataValue));
    }
}
