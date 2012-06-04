package rpn.component;

import wave.multid.*;
import wave.multid.view.*;

public class RarefactionGeom extends WaveCurveOrbitGeom implements RpGeometry {

    //no RarefactionGeom : criar um get dos indices (inicio e fim da curva). Usar este intervalo para limitar o source
    //os indices entram no CTR como args


    private int beginOfRaref_;
    private int endOfRaref_;


    public RarefactionGeom(CoordsArray[] vertices, WaveCurveOrbitGeomFactory factory) {     //args indIni e indFin
        super(vertices, factory);
        beginOfRaref_ = 0;
        endOfRaref_   = vertices.length;

    }



    public RarefactionGeom(CoordsArray[] vertices, WaveCurveOrbitGeomFactory factory, int beginOfRaref, int endOfRaref) {     //args indIni e indFin
        super(vertices, factory);
        beginOfRaref_ = beginOfRaref;
        endOfRaref_   = endOfRaref;

    }


    @Override
    public GeomObjView createView(ViewingTransform transf) throws DimMismatchEx {
        return new RarefactionOrbitView(this, transf,viewingAttr());

    }


    //--------------------------------
    public int getBegin() {
        return beginOfRaref_;
    }

    public int getEnd() {
        return endOfRaref_;
    }
    //--------------------------------


   
}
