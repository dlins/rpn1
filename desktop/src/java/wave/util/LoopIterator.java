/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package wave.util;

public class LoopIterator implements java.util.Iterator {
    private LoopConstruct[] loopArray_;

    public LoopIterator(LoopConstruct[] loops) {
        loopArray_ = loops;
    }

    public Object next() {
        int size = loopArray_.length;
        int[] next = new int[size];
        for (int i = 0; i < size; i++)
            next[i] = loopArray_[i].current();
        return next;
    }

    // not implemented
    public void remove() { }

    // TODO
    public boolean hasNext() {
        return true;
    }
}
