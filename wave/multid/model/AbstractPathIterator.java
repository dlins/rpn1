/*
* Instituto de Matematica Pura e Aplicada - IMPA
* Departamento de Dinamica dos Fluidos
*
*/

package wave.multid.model;

import wave.multid.CoordsArray;
import java.util.ListIterator;

public class AbstractPathIterator {
    //
    // Members
    //
    private ListIterator iterator_;
    private AbstractSegment current_;

    //
    // Constructors
    //
    AbstractPathIterator(ListIterator iterator) {
        iterator_ = iterator;
    }

    //
    // Methods
    //
    // this is a read only Iterator
    public AbstractSegmentAtt currentSegment(CoordsArray[] coords) {
        if (current_ == null)
            current_ = (AbstractSegment)iterator_.next();
        // this will naturally throw an array out of bound exception
        for (int i = 0; i < AbstractSegment.MAX_DEF_POINTS; i++)
            coords[i] = new CoordsArray(current_.getDefinitionPoints() [i]);
        return current_.getAttributes();
    }

    public void next() {
        current_ = (AbstractSegment)iterator_.next();
    }

    public boolean isDone() {
        return !iterator_.hasNext();
    }

    public void remove() {
        if (current_ != null)
            iterator_.remove();
    }
}
