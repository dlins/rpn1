/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.component;

import wave.multid.view.*;
import wave.multid.*;
import wave.multid.model.*;
import wave.multid.map.Map;
import java.awt.Color;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.io.FileReader;
import java.io.FileWriter;

public class BuckleyLeverettinInflectionGeom implements MultiGeometry, RpGeometry {
    //
    // Constants
    //
    public static ViewingAttr VIEWING_ATTR = new ViewingAttr(Color.yellow);
    //
    // Members
    //
    private Space space_;
    private RpGeomFactory factory_;
    private List hugoniotSegList_;
    private BoundingBox boundary_;

    //
    // Constructors
    //
    public BuckleyLeverettinInflectionGeom(HugoniotSegGeom[] segArray, BuckleyLeverettinCurveGeomFactory factory) {
        hugoniotSegList_ = new ArrayList();
        for (int i = 0; i < segArray.length; i++)
            hugoniotSegList_.add(segArray[i]);
        factory_ = factory;
        space_ = rpnumerics.RPNUMERICS.domain();
        try {
            boundary_ = new BoundingBox(new CoordsArray(space_), new CoordsArray(space_));
        } catch (DimMismatchEx dex) { dex.printStackTrace(); }
   }


//    public HugoniotCurveGeom(HugoniotCurve  hugoniotCurve, HugoniotCurveGeomFactory factory) {
//      hugoniotSegList_=hugoniotCurve.segments();
//      factory_ = factory;
//      space_ = rpnumerics.RPNUMERICS.domain();
//      try {
//        boundary_ = new BoundingBox(new CoordsArray(space_), new CoordsArray(space_));
//      } catch (DimMismatchEx dex) { dex.printStackTrace(); }
//    }

    //
    // Accessors/Mutators
    //
    public RpGeomFactory geomFactory() { return factory_; }

    public AbstractPathIterator getPathIterator() {
        AbstractPath nullPath = new AbstractPath(getSpace());
        return nullPath.getPathIterator();
    }

    public AbstractPathIterator getPathIterator(Map map) throws DimMismatchEx {
        AbstractPath nullPath = new AbstractPath(getSpace());
        return nullPath.getPathIterator(map);
    }

    public ViewingAttr viewingAttr() { return VIEWING_ATTR; }

    public Iterator getHugoniotSegIterator() { return hugoniotSegList_.iterator(); }

    public BoundingBox getBoundary() { return boundary_; }

    public Space getSpace() { return space_; }

    //
    // Methods
    //
    public void applyMap(Map map) {
    }

    public void print(FileWriter cout) { }

    public void load(FileReader cin) { }

    public GeomObjView createView(ViewingTransform transf) throws DimMismatchEx {
        return new BuckleyLeverettinCurveView(this, transf, viewingAttr());
    }
}
