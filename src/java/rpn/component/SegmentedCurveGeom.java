/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import java.awt.Color;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import wave.multid.CoordsArray;
import wave.multid.DimMismatchEx;
import wave.multid.Space;
import wave.multid.model.AbstractPath;
import wave.multid.model.AbstractPathIterator;
import wave.multid.model.BoundingBox;
import wave.multid.model.MultiGeometry;
import wave.multid.view.ViewingAttr;

public abstract class SegmentedCurveGeom implements MultiGeometry, RpGeometry {

    protected Space space_;
    protected  BoundingBox boundary_;
    private List<HugoniotSegGeom> hugoniotSegList_;
    private RpGeomFactory factory_;
    public  ViewingAttr VIEWING_ATTR = new ViewingAttr(Color.white);

    public SegmentedCurveGeom(HugoniotSegGeom[] segArray, RpGeomFactory factory) {

        hugoniotSegList_ = new ArrayList();
        for (int i = 0; i < segArray.length; i++) {
            hugoniotSegList_.add(segArray[i]);
        }
        factory_ = factory;
        space_ = rpnumerics.RPNUMERICS.domain();
        try {
            boundary_ = new BoundingBox(new CoordsArray(space_), new CoordsArray(space_));
        } catch (DimMismatchEx dex) {
            dex.printStackTrace();
        }


    }

    public AbstractPathIterator getPathIterator() {
        AbstractPath nullPath = new AbstractPath(getSpace());
        return nullPath.getPathIterator();
    }

    public Space getSpace() {
        return space_;
    }

//    public void applyMap(Map map) throws DimMismatchEx {
//    }
    public void print(FileWriter cout) {
    }

    public void load(FileReader cin) {
    }

    public AbstractPathIterator getPathIterator(wave.multid.map.Map map) throws DimMismatchEx {
        AbstractPath nullPath = new AbstractPath(getSpace());
        return nullPath.getPathIterator(map);
    }

    public BoundingBox getBoundary() {
        return boundary_;
    }

    public ViewingAttr viewingAttr() {
        return VIEWING_ATTR;
    }

    public Iterator getHugoniotSegIterator() {
        return hugoniotSegList_.iterator();
    }

    public void lowLight() {

        for (HugoniotSegGeom object : hugoniotSegList_) {
            object.lowLight();
        }

    }

    public void highLight() {
        for (HugoniotSegGeom object : hugoniotSegList_) {
            object.highLight();
        }

    }

    public RpGeomFactory geomFactory() {
        return factory_;
    }

    public void applyMap(wave.multid.map.Map map) throws DimMismatchEx {
    }
}
