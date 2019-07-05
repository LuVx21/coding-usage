package org.luvx.demo2;

import org.luvx.demo1.Customer;

/**
 * @ClassName: org.luvx.demo2
 * @Description:
 * @Author: Ren, Xie
 * @Date: 2019/6/1 19:45
 */
class Merchant<T extends Customer> {
    public double actionPrice(double price, T customer) {
        return 0.0;
    }
}