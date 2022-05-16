package org.luvx.demo2;



/**
 * @ClassName: org.luvx.demo2
 * @Description:
 * @Author: Ren, Xie
 * @Date: 2019/6/1 19:45
 */
class VIPOnlyMerchant extends Merchant<VIP> {
    @Override
    public double actionPrice(double price, VIP customer) {
        return 0.0;
    }
}