/*
* Instituto de Matematica Pura e Aplicada - IMPA
* Departamento de Dinamica dos Fluidos
*
*/

package wave.multid.model;

import wave.multid.DimMismatchEx;
import wave.multid.view.*;
import wave.multid.*;
import wave.multid.map.Map;
import java.io.FileWriter;
import java.io.FileReader;
import java.awt.Color;

public class MultiPoint extends MultiGeometryImpl {
    //
    // Constants
    //
    static public final double SHIFT_VALUE = .2;

    //
    // Members
    //
    //
    // Constructors
    //
    public MultiPoint(CoordsArray coords, ViewingAttr attr) {
        super(coords.getSpace(), attr);
        // by definition a point will be represented
        // as a one segment with both extremeties having
        // the same coords. (in order to use attributes)
        AbstractSegment[] segList = new AbstractSegment[2];
        CoordsArray[] moveSegCoords = new CoordsArray[AbstractSegment.MAX_DEF_POINTS];
        moveSegCoords[0] = new CoordsArray(coords);
        moveSegCoords[1] = new CoordsArray(coords.getSpace());
        try {
            segList[0] = new AbstractSegment(moveSegCoords, new AbstractSegmentAtt(AbstractSegment.SEG_MOVETO));
            append(segList[0], false);
        } catch (WrongNumberOfDefPointsEx wex) {
            wex.printStackTrace();
        } catch (DimMismatchEx dex) { dex.printStackTrace(); }
        CoordsArray[] lineSegCoords = new CoordsArray[AbstractSegment.MAX_DEF_POINTS];
        lineSegCoords[0] = new CoordsArray(coords);
        lineSegCoords[1] = new CoordsArray(coords);
        try {
            segList[1] = new AbstractSegment(lineSegCoords, new AbstractSegmentAtt(AbstractSegment.SEG_LINETO));
            append(segList[1], false);
        } catch (WrongNumberOfDefPointsEx wex) {
            wex.printStackTrace();
        } catch (DimMismatchEx dex) { dex.printStackTrace(); }
    }

    //
    // Accessors/Mutators
    //
    public CoordsArray getLocation() {
        AbstractPathIterator iterator = getPathIterator();
        CoordsArray[] segCoords = new CoordsArray[AbstractSegment.MAX_DEF_POINTS];
        // first one is fine
        iterator.next();
        iterator.currentSegment(segCoords);
        return segCoords[0];
    }

    //
    // Methods
    //
    public GeomObjView createView(ViewingTransform transf) throws DimMismatchEx {
        return new PointMark(this, transf, viewingAttr());
    }
}
