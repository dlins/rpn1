package rpn.component;

import java.awt.Color;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Iterator;

import wave.multid.*;
import wave.multid.map.Map;
import wave.multid.model.AbstractPath;
import wave.multid.model.AbstractPathIterator;
import wave.multid.model.BoundingBox;
import wave.multid.model.MultiGeometry;
import wave.multid.view.*;

public class BifurcationCurveGeom implements MultiGeometry, RpGeometry {

    private RpGeomFactory factory_;
    public static ViewingAttr VIEWING_ATTR = new ViewingAttr(Color.white);
    private ArrayList segList_;
    private Space space_;
    private BoundingBox boundary_;

    public BifurcationCurveGeom(BifurcationSegGeom[] segArray, BifurcationCurveGeomFactory factory) {

        segList_ = new ArrayList();
        for (int i = 0; i < segArray.length; i++) {
            segList_.add(segArray[i]);
        }
        factory_ = factory;
        space_ = rpnumerics.RPNUMERICS.domain();
        try {
            boundary_ = new BoundingBox(new CoordsArray(space_), new CoordsArray(space_));
        } catch (DimMismatchEx dex) {
            dex.printStackTrace();
        }

    }

    public GeomObjView createView(ViewingTransform transf) throws DimMismatchEx {
        return new BifurcationCurveView(this, transf, viewingAttr());
    }

    public RpGeomFactory geomFactory() {
        return factory_;
    }

    public AbstractPathIterator getPathIterator() {
        AbstractPath nullPath = new AbstractPath(getSpace());
        return nullPath.getPathIterator();
    }

    public AbstractPathIterator getPathIterator(Map map) throws DimMismatchEx {
        AbstractPath nullPath = new AbstractPath(getSpace());
        return nullPath.getPathIterator(map);
    }

    public ViewingAttr viewingAttr() {
        return VIEWING_ATTR;
    }

    public Iterator getBifurcationSegmentsIterator() {
        return segList_.iterator();
    }

    public BoundingBox getBoundary() {
        return boundary_;
    }

    public Space getSpace() {
        return space_;
    }

    //
    // Methods
    //
    public void applyMap(Map map) {
    }

    public void print(FileWriter cout) {
    }

    public void load(FileReader cin) {
    }
}
