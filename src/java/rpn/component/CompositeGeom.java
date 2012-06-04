package rpn.component;

import wave.multid.*;
import wave.multid.view.*;

public class CompositeGeom extends WaveCurveOrbitGeom implements RpGeometry {


    private int beginOfComp_;
    private int endOfComp_;


    public CompositeGeom(CoordsArray[] coordsArray, WaveCurveOrbitGeomFactory factory) {
        super(coordsArray, factory);
        System.out.println("coordsArray.length : " +coordsArray.length);
        beginOfComp_ = 0;
        endOfComp_   = coordsArray.length;
       
    }



    public CompositeGeom(CoordsArray[] coordsArray, WaveCurveOrbitGeomFactory factory, int beginOfComp, int endOfComp) {
        super(coordsArray, factory);
        beginOfComp_ = beginOfComp;
        endOfComp_   = endOfComp;

    }



    @Override
    public GeomObjView createView(ViewingTransform transf) throws DimMismatchEx {
        return new CompositeOrbitView(this, transf,  viewingAttr());

    }


    //--------------------------------
    public int getBegin() {
        return beginOfComp_;
    }

    public int getEnd() {
        return endOfComp_;
    }
    //--------------------------------


}
