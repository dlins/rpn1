/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import wave.multid.view.*;
import wave.multid.*;
import wave.multid.model.*;
import java.awt.Color;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Map;

public class SubInflectionCurveGeom implements MultiGeometry, RpGeometry {
    //
    // Constants
    //

    public static ViewingAttr VIEWING_ATTR = new ViewingAttr(Color.green);
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
    public SubInflectionCurveGeom(HugoniotSegGeom[] segArray, SubInflectionCurveGeomFactory factory) {
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

    //
    // Accessors/Mutators
    //
    public RpGeomFactory geomFactory() {
        return factory_;
    }

    public ViewingAttr viewingAttr() {
        return VIEWING_ATTR;
    }

    public Iterator getHugoniotSegIterator() {
        return hugoniotSegList_.iterator();
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

    public GeomObjView createView(ViewingTransform transf) throws DimMismatchEx {
        return new SubInflectionCurveView(this, transf, viewingAttr());
    }

    public AbstractPathIterator getPathIterator(wave.multid.map.Map map) throws DimMismatchEx {

         AbstractPath nullPath = new AbstractPath(getSpace());
        return nullPath.getPathIterator(map);

    }

    public void applyMap(wave.multid.map.Map map) throws DimMismatchEx {

    }

    public AbstractPathIterator getPathIterator() {
         AbstractPath nullPath = new AbstractPath(getSpace());
        return nullPath.getPathIterator();
    }
}
