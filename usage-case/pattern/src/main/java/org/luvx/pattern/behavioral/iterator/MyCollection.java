package org.luvx.pattern.behavioral.iterator;

/**
 * @author: Ren, Xie
 */
public interface MyCollection<E> {
    boolean add(E e);

    E remove(int i);

    MyIterator<E> iterator();
}
