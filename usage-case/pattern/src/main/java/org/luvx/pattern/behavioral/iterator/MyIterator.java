package org.luvx.pattern.behavioral.iterator;

/**
 * @author: Ren, Xie
 */
public interface MyIterator<E> {

    boolean hasNext();

    E next();

    void remove();
}
