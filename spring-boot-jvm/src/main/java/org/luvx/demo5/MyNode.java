package org.luvx.demo5;

/**
 * @ClassName: org.luvx.demo4
 * @Description:
 * @Author: Ren, Xie
 * @Date: 2019/6/1 20:17
 */
public class MyNode extends Node {
    /**
     * 编译器生成的桥接方法
     *
     * @param data
     */
    // public void setData(Object data) {
    //     setData((Integer) data);
    // }

    public MyNode(Integer data) {
        super(data);
    }

    public void setData(Integer data) {
        System.out.println("MyNode.setData");
        super.setData(data);
    }
}