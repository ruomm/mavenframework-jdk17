package com.ruomm.assistx.dbgenerate;

/**
 * Hello world!
 */
public class GenerateApp {
    public static void main(String[] args) {
        // 加载配置文件生成DB相关类和Mapper示例代码
//         generateDbResorceDemo(2);
//        GeneratorUtil.generateDbResorce("/Users/qx/workspace/java-projects/mavenframework-jdk17/assistxframework/assistx_dbgenerate/src/main/java/resource/demo_ruomm-springcloud.xml");
        GeneratorUtil.generateDbResorce("demo_ruomm-springcloud.xml");
    }

    // 加载配置文件生成DB相关类和Mapper示例代码
    public static void generateDbResorceDemo(int mode) {
        //加载内部resource文件夹里面的配置
        if (mode == 0) {
            GeneratorUtil.generateDbResorce("demo_config");
        }
        if (mode == 1) {
            GeneratorUtil.generateDbResorce("demo_config.xml");
        }
        //加载外部文件夹里面的配置
        if (mode == 2) {
            GeneratorUtil.generateDbResorce("D:\\temp\\dbgenerate\\demo_ruommwebapp.xml");
        }
        //加载相对路径，Idea和Eclipse通用
        if (mode == 3) {
            GeneratorUtil.generateDbResorce("userDir:src\\main\\java\\resource\\demo_config.xml");
        }
        //加载相对路径，Idea可用
        if (mode == 4) {
            GeneratorUtil.generateDbResorce("assistx_dbgenerate\\src\\main\\java\\resource\\demo_config.xml");
        }
        //加载相对路径，Eclipse可用
        if (mode == 5) {
            GeneratorUtil.generateDbResorce("src\\main\\java\\resource\\demo_config.xml");
        }
    }

}
