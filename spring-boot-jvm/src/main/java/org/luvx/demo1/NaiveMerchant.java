package org.luvx.demo1;

/**
 * @ClassName: org.luvx.demo1
 * @Description:
 * @Author: Ren, Xie
 * @Date: 2019/6/1 19:44
 */
class NaiveMerchant extends Merchant {
    @Override
    public Double actionPrice(double price, Customer customer) {
        return 0.0;
    }
}