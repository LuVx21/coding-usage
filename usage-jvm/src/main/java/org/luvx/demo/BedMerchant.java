package org.luvx.demo;

import java.util.Random;

/**
 * @ClassName: com.xinmei.tdata.datablood.task.delete
 * @Description:
 * @Author: Ren, Xie
 * @Date: 2019/6/1 17:46
 */
class BedMerchant extends Merchant {
    @Override
    public double afterPreferential(double price, Customer customer) {
        if (customer.isVIP()) {
            return price * bedPrice();
        } else {
            return super.afterPreferential(price, customer);
        }
    }

    public static double bedPrice() {
        return new Random()
                .nextDouble()
                + 0.8d;
    }
}