/**
 * @copyright www.ruomm.com
 * @author 牛牛-wanruome@163.com
 * @create 2020年9月14日 上午10:50:55
 */
package com.ruomm.javax.demox.objvalidatorx;

import com.ruomm.javax.objvalidatorx.annotation.ValidBigDecimal;

public class ValidDal {
    @ValidBigDecimal(zeroStartAllow = false, pointDigits = 0, max = "9999", min = "1", message = "借记卡费率封顶值 debitUpperLimit不合法，需要在{min}-{max}之间，必须为整数")
    private String debitUpperLimit;// 37 借记卡费率封顶值 debitUpperLimit N1..4 M 单位（分）
    @ValidBigDecimal(zeroStartAllow = false, pointDigits = 7, max = "0.02", min = "0.001", message = "借记卡费率 debitFeeRate不合法，需要在{min}-{max}之间，最大{pointDigits}位小数")
    private String debitFeeRate;// 38 借记卡费率 debitFeeRate ANS1..8 M 例：千分之五为 0.0050000，支持七位小数
    @ValidBigDecimal(zeroStartAllow = false, pointDigits = 7, max = "0.02", min = "0.001", message = "贷记卡费率 creditFeeRate不合法，需要在{min}-{max}之间，最大{pointDigits}位小数")
    private String creditFeeRate;// 39 贷记卡费率 creditFeeRate ANS1..8 M 例：千分之五为 0.0050000，支持七位小数
    @ValidBigDecimal(zeroStartAllow = false, pointDigits = 7, max = "0.02", min = "0.001", message = "境外借记卡费率overseaDebitFeeRate不合法，需要在{min}-{max}之间，最大{pointDigits}位小数")
    private String overseaDebitFeeRate;// 40 境外借记卡费率 overseaDebitFeeRate ANS1..8 O 例：千分之五为 0.0050000，支持七位小数
    @ValidBigDecimal(zeroStartAllow = false, pointDigits = 7, max = "0.02", min = "0.001", message = "境外贷记卡费率overseaCreditFeeRate不合法，需要在{min}-{max}之间，最大{pointDigits}位小数")
    private String overseaCreditFeeRate;// 41 境外贷记卡费率 overseaCreditFeeRate ANS1..8 O 例：千分之五为 0.0050000，支持七位小数
    @ValidBigDecimal(zeroStartAllow = false, pointDigits = 7, max = "0.02", min = "0.001", message = "银联二维码费率cupQrFeeRate不合法，需要在{min}-{max}之间，最大{pointDigits}位小数")
    private String cupQrFeeRate;// 42 银联二维码费率 cupQrFeeRate ANS1..8 O 例：千分之五为 0.0050000，支持七位小数
    @ValidBigDecimal(zeroStartAllow = false, pointDigits = 7, max = "0.02", min = "0.001", message = "支付宝费率alipayQrFeeRate不合法，需要在{min}-{max}之间，最大{pointDigits}位小数")
    private String alipayQrFeeRate;// 43 支付宝费率 alipayQrFeeRate ANS1..8 O 例：千分之五为 0.0050000，支持七位小数
    @ValidBigDecimal(zeroStartAllow = false, pointDigits = 7, max = "0.02", min = "0.001", message = "微信费率wechatQrFeeRate不合法，需要在{min}-{max}之间，最大{pointDigits}位小数")
    private String wechatQrFeeRate;// 44 微信费率 wechatQrFeeRate ANS1..8 O 例：千分之五为 0.0050000，支持七位小数
    @ValidBigDecimal(zeroStartAllow = false, pointDigits = 0, max = "9999", min = "1", message = "清算服务费serviceFee不合法，需要在{min}-{max}之间，必须为整数")
    private String serviceFee;// 45 清算服务费 serviceFee N1..4 O 单位（分）

    public String getDebitUpperLimit() {
        return debitUpperLimit;
    }

    public void setDebitUpperLimit(String debitUpperLimit) {
        this.debitUpperLimit = debitUpperLimit;
    }

    public String getDebitFeeRate() {
        return debitFeeRate;
    }

    public void setDebitFeeRate(String debitFeeRate) {
        this.debitFeeRate = debitFeeRate;
    }

    public String getCreditFeeRate() {
        return creditFeeRate;
    }

    public void setCreditFeeRate(String creditFeeRate) {
        this.creditFeeRate = creditFeeRate;
    }

    public String getOverseaDebitFeeRate() {
        return overseaDebitFeeRate;
    }

    public void setOverseaDebitFeeRate(String overseaDebitFeeRate) {
        this.overseaDebitFeeRate = overseaDebitFeeRate;
    }

    public String getOverseaCreditFeeRate() {
        return overseaCreditFeeRate;
    }

    public void setOverseaCreditFeeRate(String overseaCreditFeeRate) {
        this.overseaCreditFeeRate = overseaCreditFeeRate;
    }

    public String getCupQrFeeRate() {
        return cupQrFeeRate;
    }

    public void setCupQrFeeRate(String cupQrFeeRate) {
        this.cupQrFeeRate = cupQrFeeRate;
    }

    public String getAlipayQrFeeRate() {
        return alipayQrFeeRate;
    }

    public void setAlipayQrFeeRate(String alipayQrFeeRate) {
        this.alipayQrFeeRate = alipayQrFeeRate;
    }

    public String getWechatQrFeeRate() {
        return wechatQrFeeRate;
    }

    public void setWechatQrFeeRate(String wechatQrFeeRate) {
        this.wechatQrFeeRate = wechatQrFeeRate;
    }

    public String getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(String serviceFee) {
        this.serviceFee = serviceFee;
    }

    @Override
    public String toString() {
        return "ValidDal [debitUpperLimit=" + debitUpperLimit + ", debitFeeRate=" + debitFeeRate + ", creditFeeRate="
                + creditFeeRate + ", overseaDebitFeeRate=" + overseaDebitFeeRate + ", overseaCreditFeeRate="
                + overseaCreditFeeRate + ", cupQrFeeRate=" + cupQrFeeRate + ", alipayQrFeeRate=" + alipayQrFeeRate
                + ", wechatQrFeeRate=" + wechatQrFeeRate + ", serviceFee=" + serviceFee + "]";
    }

}
