/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年7月30日 下午4:12:29
 */
package com.ruomm.demotest.jsonx;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NameUtils {
    //	public final static String CONTENT_DEMO="花谢花飞花满天，红消香断有谁怜？" +
//			"游丝软系飘春榭1，落絮轻沾扑绣帘。" +
//			"闺中女儿惜春暮，愁绪满怀无释处。" +
//			"手把花锄出绣帘，忍踏落花来复去。" +
//			"柳丝榆荚自芳菲，不管桃飘与李飞；" +
//			"桃李明年能再发，明年闺中知有谁？" +
//			"三月香巢已垒成，梁间燕子太无情！" +
//			"明年花发虽可啄，却不道人去梁空巢也倾。" +
//			"一年三百六十日，风刀霜剑严相逼；" +
//			"明媚鲜妍能几时，一朝漂泊难寻觅。" +
//			"花开易见落难寻，阶前愁杀葬花人，" +
//			"独倚花锄泪暗洒，洒上空枝见血痕。" +
//			"杜鹃无语正黄昏，荷锄归去掩重门；" +
//			"青灯照壁人初睡，冷雨敲窗被未温。" +
//			"怪奴底事倍伤神？半为怜春半恼春。" +
//			"怜春忽至恼忽去，至又无言去未闻。" +
//			"昨宵庭外悲歌发，知是花魂与鸟魂？" +
//			"花魂鸟魂总难留，鸟自无言花自羞；" +
//			"愿侬此日生双翼，随花飞到天尽头。" +
//			"天尽头，何处有香丘2？" +
//			"未若锦囊收艳骨，一抔净土掩风流3。" +
//			"质本洁来还洁去，强于污淖陷渠沟。" +
//			"尔今死去侬收葬，未卜侬身何日丧？" +
//			"侬今葬花人笑痴，他年葬侬知是谁？" +
//			"试看春残花渐落，便是红颜老死时；" +
//			"一朝春尽红颜老，花落人亡两不知！"+
//			"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789/_";
    private final static int RATE_TWO = 30;
    private final static int RATE_THREE = 60;
    //	private final static String NAME_FIRST = "李|王|张|刘|陈|杨|赵|黄|周|吴|徐|孙|胡|朱|高|林|何|郭|马|罗|梁|宋|郑|谢|韩|唐|冯|于|董|萧|程|曹|袁|邓|许|傅|沈|曾|彭|吕|苏|卢|蒋|蔡|贾|丁|魏|薛|叶|阎|余|潘|杜|戴|夏|钟|汪|田|任|姜|范|方|石|姚|谭|廖|邹|熊|金|陆|郝|孔|白|崔|康|毛|邱|秦|江|史|顾|侯|邵|孟|龙|万|段|漕|钱|汤|尹|黎|易|常|武|乔|贺|赖|龚|文|";
    private final static String NAME_FIRST = "李|王|张|刘|陈|杨|赵|黄|周|吴|徐|孙|胡|朱|高|林|何|郭|马|罗|梁|宋|郑|谢|韩|唐|冯|于|董|萧|程|曹|袁|邓|许|傅|沈|曾|彭|吕|苏|卢|蒋|蔡|贾|丁|魏|薛|叶|阎|余|潘|杜|戴|夏|钟|汪|田|任|姜|范|方|石|姚|谭|廖|邹|熊|金|陆|郝|孔|白|崔|康|毛|邱|秦|江|史|顾|侯|邵|孟|龙|万|段|漕|钱|汤|尹|黎|易|常|武|乔|贺|赖|龚|文|"
            + "欧阳|上官|东方|独孤|令狐|诸葛|夏侯|司马|太史|司徒|南宫|澹台|慕容|公孙|长孙|皇甫|百里|闻人|宇文|西门|";
    private final static String NAME_DEFAULT = "张三";
    //	private final static String NAME_HEADER = "李王张刘陈杨赵黄周吴徐孙胡朱高林何郭马罗梁宋郑谢韩唐冯于董萧程曹袁邓许傅沈曾彭吕苏卢蒋蔡贾丁魏薛叶阎余潘杜戴夏钟汪田任姜范方石姚谭廖邹熊金陆郝孔白崔康毛邱秦江史顾侯邵孟龙万段漕钱汤尹黎易常武乔贺赖龚文";
    private final static String NAME_CONTENT = "谦亨奇固之轮翰朗伯宏先柏镇淇淳一洁铭皑言若鸣朋斌梁栋维启克伦翔旭鹏泽晨辰士以建家致树炎德行时泰盛雄琛钧冠策腾楠榕风航弘瑛玲憧萍雪珍滢筠柔竹霭凝晓欢霄枫芸菲寒伊亚宜可姬舒影荔枝丽秀娟英华慧巧美静淑惠珠莹雪琳晗瑶允元源渊和函妤宜云琪勤珍贞莉兰凤洁琳素云莲真环雪荣爱妹霞亮香月媛艳瑞凡佳嘉叶璧璐娅琦晶妍茹清吉克茜秋珊莎锦黛青倩婷姣婉娴瑾颖露瑶怡婵雁蓓纨仪荷丹蓉眉君琴蕊薇菁梦岚苑婕馨瑗琰韵融园艺咏卿聪澜纯毓悦昭冰爽琬茗羽希宁欣飘育涵琴晴丽美瑶梦茜倩希夕月悦乐彤影珍依沫玉灵瑶嫣倩妍萱漩娅媛怡佩淇雨娜莹娟文芳莉雅芝文晨宇怡全子凡悦思奕依浩泓钊钧铎";
    private final static String CONTENT_DEMO = "花谢花飞花满天红消香断有谁怜" + "游丝软系飘春榭1落絮轻沾扑绣帘" + "闺中女儿惜春暮愁绪满怀无释处"
            + "手把花锄出绣帘忍踏落花来复去" + "柳丝榆荚自芳菲不管桃飘与李飞" + "桃李明年能再发明年闺中知有谁" + "三月香巢已垒成梁间燕子太无情" + "明年花发虽可啄却不道人去梁空巢也倾"
            + "一年三百六十日风刀霜剑严相逼" + "明媚鲜妍能几时一朝漂泊难寻觅" + "花开易见落难寻阶前愁杀葬花人" + "独倚花锄泪暗洒洒上空枝见血痕" + "杜鹃无语正黄昏荷锄归去掩重门"
            + "青灯照壁人初睡冷雨敲窗被未温" + "怪奴底事倍伤神半为怜春半恼春" + "怜春忽至恼忽去至又无言去未闻" + "昨宵庭外悲歌发知是花魂与鸟魂" + "花魂鸟魂总难留鸟自无言花自羞"
            + "愿侬此日生双翼随花飞到天尽头" + "天尽头何处有香丘" + "未若锦囊收艳骨一抔净土掩风流" + "质本洁来还洁去强于污淖陷渠沟" + "尔今死去侬收葬未卜侬身何日丧" + "侬今葬花人笑痴他年葬侬知是谁"
            + "试看春残花渐落便是红颜老死时" + "一朝春尽红颜老花落人亡两不知" + "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789/_";

    public static String generateName(boolean isRandom) {
        if (!isRandom) {
            return NAME_DEFAULT;
        }
        return generateName(RATE_TWO, RATE_THREE);
    }

    public static String generateName() {
        return generateName(RATE_TWO, RATE_THREE);
    }

    public static String generateNameBySize(int nameSize) {

        Random random = new Random();
        List<String> listNameFirst = new ArrayList<String>();
        String[] arrayFirstName = NAME_FIRST.split("\\|");
        for (String tmp : arrayFirstName) {
            if (null == tmp || tmp.length() <= 0) {
                continue;
            }
            if (nameSize > 3) {
                if (tmp.length() > 1) {
                    listNameFirst.add(tmp);
                }
            } else if (nameSize == 3) {
                if (tmp.length() < nameSize) {
                    listNameFirst.add(tmp);
                }
            } else {
                if (tmp.length() == 1) {
                    listNameFirst.add(tmp);
                }
            }
        }
        if (listNameFirst.size() <= 0) {
            for (String tmp : arrayFirstName) {
                if (null != tmp && tmp.length() == 1) {
                    listNameFirst.add(tmp);
                }
            }
        }
        StringBuilder sb = new StringBuilder();
        int nameFirstSize = listNameFirst.size();
        sb.append(listNameFirst.get(random.nextInt(nameFirstSize)));
        int xSize = sb.length();
        for (int i = xSize; i < nameSize; i++) {
            sb.append(NAME_CONTENT.charAt(random.nextInt(NAME_CONTENT.length())));
        }
        return sb.toString();
    }

    public static String generateName(int rateTwo, int rateThree) {
        int r2 = rateTwo;
        int r3 = rateThree;
        if (r2 < 0 || r2 > 100) {
            r2 = RATE_TWO;
        }
        if (r3 < 0 || r3 > 100) {
            r3 = RATE_THREE;
        }
        int iRandom = new Random().nextInt(100);
        if (iRandom < r2) {
            return generateNameBySize(2);
        } else if (iRandom < r2 + r3) {
            return generateNameBySize(3);
        } else {
            return generateNameBySize(4);
        }
    }

    public static String generateContent(int length) {
        if (length < 0) {
            return null;
        } else if (length == 0) {
            return "";
        }
        int size = CONTENT_DEMO.length();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(CONTENT_DEMO.charAt(new Random().nextInt(size)));
        }
        return sb.toString();
    }

}
