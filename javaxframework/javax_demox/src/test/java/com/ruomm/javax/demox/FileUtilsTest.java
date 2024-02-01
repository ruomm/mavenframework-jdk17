package com.ruomm.javax.demox;

import com.ruomm.javax.corex.FileUtils;
import com.ruomm.javax.corex.StringUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

public class FileUtilsTest {
    public final static String data = "花谢花飞花满天红消香断有谁怜" + "游丝软系飘春榭1落絮轻沾扑绣帘" + "闺中女儿惜春暮愁绪满怀无释处"
            + "手把花锄出绣帘忍踏落花来复去" + "柳丝榆荚自芳菲不管桃飘与李飞" + "桃李明年能再发明年闺中知有谁" + "三月香巢已垒成梁间燕子太无情" + "明年花发虽可啄却不道人去梁空巢也倾"
            + "一年三百六十日风刀霜剑严相逼" + "明媚鲜妍能几时一朝漂泊难寻觅" + "花开易见落难寻阶前愁杀葬花人" + "独倚花锄泪暗洒洒上空枝见血痕" + "杜鹃无语正黄昏荷锄归去掩重门"
            + "青灯照壁人初睡冷雨敲窗被未温" + "怪奴底事倍伤神半为怜春半恼春" + "怜春忽至恼忽去至又无言去未闻" + "昨宵庭外悲歌发知是花魂与鸟魂" + "花魂鸟魂总难留鸟自无言花自羞"
            + "愿侬此日生双翼随花飞到天尽头" + "天尽头何处有香丘" + "未若锦囊收艳骨一抔净土掩风流" + "质本洁来还洁去强于污淖陷渠沟" + "尔今死去侬收葬未卜侬身何日丧" + "侬今葬花人笑痴他年葬侬知是谁"
            + "试看春残花渐落便是红颜老死时" + "一朝春尽红颜老花落人亡两不知";

    public static void main(String[] args) throws Exception {
//        readFileTest();
//        readByteTest();
//        readFileTest();
//        readFileByLineTest();
//        readFileToListTest();
//        readFileToListByTagTest();
//        writeFileInputStreamTest();
//        writeFileListTest();
//        createDirTest();
//            delAllFileTest();
//        getSizeBytesTest();
        parseHongLoumen();


    }

    public static void parseHongLoumen() {
        String outPath = "D:\\temp\\txt\\hongloumeng_result.txt";
        FileUtils.delAllFiles(outPath);
        List<String> listData = new ArrayList<>();
        for (int i = 0; i < data.length(); i++) {
            String val = data.substring(i, i + 1);
            if (listData.contains(val)) {
                continue;
            }
            if (StringUtils.isEnStr(val)) {
                continue;
            }
            listData.add(val);

            FileUtils.writeFile(outPath, val, true);
        }
        System.out.println(data.length());
        System.out.println(listData.size());
    }

    public static void getSizeBytesTest() {
        String filePath = "D:\\temp\\temp\\assistx_curltoolsx";
        long sizeResult = FileUtils.getSizeBytes(new File("D:\\temp\\temp\\debug.keystore2"));
        System.out.println(sizeResult);
        System.out.println(File.separator);
    }

    public static void delAllFileTest() {
        String filePath = "D:\\temp\\temp\\assistx_curltoolsx";
        boolean delResult = FileUtils.delFolder(new File(filePath));
        System.out.println(delResult);
    }

    public static void createDirTest() {
        String filePath = "D:\\temp/readmeList2\\大师傅/fdsa.txt";
        File file = FileUtils.createFile(filePath);
        System.out.println(file.getPath());
    }

    public static void writeFileListTest() {
        List<String> listContent = readFileToListTest();
        String filePath = "D:\\temp\\readmeList.txt";
        FileUtils.writeFile(new File(filePath), listContent, "\r\n", true, "GBK");
//        String data=readFileTest();
//        FileUtils.(filePath,data,true);
    }

    public static void writeFileInputStreamTest() throws Exception {
        String filePath = "D:\\temp\\readmeInput.txt";
        String filePathIn = "D:\\temp\\readmeUTF-8.txt";
//        String data=readFileTest();
        FileUtils.writeFile(filePath, new BufferedInputStream(new FileInputStream(filePathIn)), false);

    }

    public static void writeFileTest() {
        String filePath = "D:\\temp\\readmeWrite.txt";
        String data = readFileTest();
//        FileUtils.(filePath,data,true);
    }


    public static void readFileToListByTagTest() {
        String filePath = "D:\\temp\\readmeByTag.txt";
        List<String> dataList = FileUtils.readFileToListByTag(filePath, "UTF-8", "你好", "大家好", true, true);

        System.out.println(dataList);
        System.out.println(dataList.get(0).substring(1));
//        FileUtils.readFile(filePath);
    }

    public static List<String> readFileToListTest() {
        String filePath = "D:\\temp\\readmeUTF-8.txt";
        List<String> dataList = FileUtils.readFileToList(filePath);

        System.out.println(dataList);
//        System.out.println(dataList.get(0).substring(1));
        return dataList;
//        FileUtils.readFile(filePath);
    }

    public static void readFileByLineTest() {
        String filePath = "D:\\temp\\readmeUTF-8.txt";
        String data = FileUtils.readFileByLine(new File(filePath), "UTF-8", "\r\n", false);

        System.out.println(data);
//        FileUtils.readFile(filePath);
    }

    public static void readByteTest() {
        String filePath = "D:\\temp\\readmeUTF-8.txt";
        byte[] data = FileUtils.readFileToByte(filePath);

        System.out.println(data.length);
        try {
            System.out.println(new String(data, "UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }

//        FileUtils.readFile(filePath);
    }

    public static String readFileTest() {
        String filePath = "D:\\temp\\readmeUTF-8.txt";
        String data = FileUtils.readFile(filePath, "UTF-8", false);

        System.out.println(data.substring(1) + "你好");
        return data;
//        FileUtils.readFile(filePath);
    }

}
