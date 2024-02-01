/**
 * @copyright wanruome-2018
 * @author 牛牛-wanruome@163.com
 * @create 2018年6月14日 上午10:57:58
 */
package com.ruomm.javax.encryptx.test.utils;

import java.util.Random;
import java.util.UUID;

public class TokenHelper {
    public final static String TOKEN_LETTER_UPPER_CASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public final static String TOKEN_LETTER_LOWER_CASE = "abcdefghijklmnopqrstuvwxyz";
    public final static String TOKEN_NUMBER = "0123456789";
    public final static String TOKEN_SYMBOL = "/_";
    public final static String TOKEN_HEX_STRING = "0123456789abcdef";
    public final static String TOKEN_SYMBOL_TWO = ",./<>?;:'\"[]{}`~!@#$%^&*()-_=+";
    private final static int TOKEN_DEFAULT_SIZE = 16;
    public final static String TOKEN_ZANGHUAYIN = "葬花吟\n" +
            "【作者】曹雪芹 【朝代】清\n" +
            "花谢花飞花满天，红消香断有谁怜？\n" +
            "游丝软系飘春榭，落絮轻沾扑绣帘。\n" +
            "闺中女儿惜春暮，愁绪满怀无释处。\n" +
            "手把花锄出绣帘，忍踏落花来复去。\n" +
            "柳丝榆荚自芳菲，不管桃飘与李飞；\n" +
            "桃李明年能再发，明年闺中知有谁？\n" +
            "三月香巢已垒成，梁间燕子太无情！\n" +
            "明年花发虽可啄，却不道人去梁空巢也倾。\n" +
            "一年三百六十日，风刀霜剑严相逼；\n" +
            "明媚鲜妍能几时，一朝漂泊难寻觅。\n" +
            "花开易见落难寻，阶前愁杀葬花人，\n" +
            "独倚花锄泪暗洒，洒上空枝见血痕。\n" +
            "杜鹃无语正黄昏，荷锄归去掩重门；\n" +
            "青灯照壁人初睡，冷雨敲窗被未温。\n" +
            "怪奴底事倍伤神？半为怜春半恼春。\n" +
            "怜春忽至恼忽去，至又无言去未闻。\n" +
            "昨宵庭外悲歌发，知是花魂与鸟魂？\n" +
            "花魂鸟魂总难留，鸟自无言花自羞；\n" +
            "愿侬此日生双翼，随花飞到天尽头。\n" +
            "天尽头，何处有香丘？\n" +
            "未若锦囊收艳骨，一抔净土掩风流。\n" +
            "质本洁来还洁去，强于污淖陷渠沟。\n" +
            "尔今死去侬收葬，未卜侬身何日丧？\n" +
            "侬今葬花人笑痴，他年葬侬知是谁？\n" +
            "试看春残花渐落，便是红颜老死时；\n" +
            "一朝春尽红颜老，花落人亡两不知！";

    private String tokenStr = null;
    private int defaultTokenSize = TOKEN_DEFAULT_SIZE;

    public static TokenHelper getInstanceHexTokenHelper() {
        return new TokenHelper(TOKEN_HEX_STRING, 32);
    }

    ;

    public TokenHelper(String tokenStr) {
        this(tokenStr, 0);
    }

    public TokenHelper(String tokenStr, int defaultTokenSize) {
        super();
        if (null == tokenStr || tokenStr.length() <= 0) {
            this.tokenStr = TOKEN_LETTER_UPPER_CASE + TOKEN_LETTER_LOWER_CASE + TOKEN_NUMBER + TOKEN_SYMBOL;
        } else {
            this.tokenStr = tokenStr;
        }
        if (defaultTokenSize <= 0) {
            int tmp = TOKEN_DEFAULT_SIZE;
            if (tmp <= 0) {
                this.defaultTokenSize = 16;
            } else {
                this.defaultTokenSize = tmp;
            }
        } else {
            this.defaultTokenSize = defaultTokenSize;
        }
    }

    public String generateToken() {
        return generateToken(defaultTokenSize, false);
    }

    public String generateToken(int length) {
        return generateToken(length, false);
    }

    public String generateToken(int length, boolean isFirstNoZero) {
        int tokenStrSize = null == this.tokenStr ? 0 : this.tokenStr.length();
        if (tokenStrSize <= 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            if (i == 0 && isFirstNoZero) {
                String tmpTokenStr = tokenStr.replace("0", "");
                if (tmpTokenStr.length() > 0) {
                    sb.append(tmpTokenStr.charAt(random.nextInt(tmpTokenStr.length())));
                } else {
                    sb.append(tokenStr.charAt(random.nextInt(tokenStrSize)));
                }

            } else {
                sb.append(tokenStr.charAt(random.nextInt(tokenStrSize)));
            }
        }
        return sb.toString();
    }

    /**
     * 封装JDK自带的UUID, 通过Random数字生成, 中间无-分割.
     */
    public static String uuid() {
        return UUID.randomUUID().toString().trim().replaceAll("-", "");
    }

}
