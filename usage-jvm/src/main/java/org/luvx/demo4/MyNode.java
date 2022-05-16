package org.luvx.demo4;

/**
 * @ClassName: org.luvx.demo4
 * @Description:
 * @Author: Ren, Xie
 * @Date: 2019/6/1 20:17
 */
public class MyNode extends Node<Integer> {
    public MyNode(Integer data) {
        super(data);
    }

    @Override
    public void setData(Integer data) {
        System.out.println("MyNode.setData");
        super.setData(data);
    }
}