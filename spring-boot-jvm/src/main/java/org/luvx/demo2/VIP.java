package org.luvx.demo2;

import org.luvx.demo1.Customer;

/**
 * @ClassName: org.luvx.demo2
 * @Description:
 * @Author: Ren, Xie
 * @Date: 2019/6/1 19:47
 */
public class VIP implements Customer {
    @Override
    public boolean isVIP() {
        return true;
    }
}
