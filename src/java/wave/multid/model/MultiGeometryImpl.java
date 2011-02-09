/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package wave.multid.model;

import java.awt.Color;
import wave.multid.view.*;
import wave.multid.*;
import wave.multid.map.Map;
import java.io.FileWriter;
import java.io.FileReader;

public abstract class MultiGeometryImpl implements MultiGeometry {
    //
    // Members
    //

    private AbstractPath path_;
    private ViewingAttr viewAttr_;
   
    //
    // Constructors
    //

    public MultiGeometryImpl(Space space, ViewingAttr viewAttr) {
        path_ = new AbstractPath(space);
        viewAttr_ = new ViewingAttr(viewAttr);
    }

    //
    // Accessors/Mutators
    //
    public Space getSpace() {
        return path_.getSpace();
    }

    public BoundingBox getBoundary() {
        return path_.getBoundary();
    }

    public AbstractPathIterator getPathIterator() {
        return path_.getPathIterator();
    }

    public AbstractPathIterator getPathIterator(Map map) throws DimMismatchEx {
        return path_.getPathIterator(map);
    }

    public ViewingAttr viewingAttr() {
        return viewAttr_;
    }

    //
    // Methods
    //
    public void load(FileReader cin) {
        path_.load(cin);
    }

    public void print(FileWriter cout) {
        path_.print(cout);
    }

    public void applyMap(Map map) throws DimMismatchEx {
        path_.applyMap(map);
    }

    public void append(AbstractPathIterator toAppend, boolean connect) throws DimMismatchEx {
        path_.add(toAppend, connect);
    }

    public void append(AbstractSegment toAppend, boolean connect) throws DimMismatchEx {
        path_.add(toAppend, connect);
    }

    public void closePath() {
        path_.closePath();
    }

   

    @Override
    public String toString() {
        return path_.toString();
    }

    protected AbstractPath getPath() {
        return this.path_;
    }
}
