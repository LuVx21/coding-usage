package org.luvx.demo4;

/**
 * @ClassName: org.luvx.demo4
 * @Description:
 * @Author: Ren, Xie
 * @Date: 2019/6/1 20:16
 */
public class Node<T> {
    public T data;

    public Node(T data) { this.data = data; }

    public void setData(T data) {
        System.out.println("Node.setData");
        this.data = data;
    }
}