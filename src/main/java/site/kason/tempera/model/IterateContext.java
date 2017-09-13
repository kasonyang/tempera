package site.kason.tempera.model;

import java.util.Iterator;

/**
 *
 * @author Kason Yang
 */
public class IterateContext<T> {

    private int index = -1;

    private final Iterator<T> iterator;

    public IterateContext(Iterator<T> iterator) {
        this.iterator = iterator;
    }

    public boolean isFirst() {
        return index == 0;
    }

    public boolean isLast() {
        return !iterator.hasNext();
    }

    public int getIndex() {
        return index;
    }

    public boolean hasNext() {
        return iterator.hasNext();
    }

    public T next() {
        T val = iterator.next();
        index++;
        return val;
    }

}
