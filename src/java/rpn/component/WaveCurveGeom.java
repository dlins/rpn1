package rpn.component;

import java.util.List;
import wave.multid.*;
import wave.multid.view.*;

public class WaveCurveGeom extends OrbitGeom  {


    private List<WaveCurveOrbitGeom> orbitsList_;

    public WaveCurveGeom(CoordsArray[] vertices, List<WaveCurveOrbitGeom> orbitsList,WaveCurveGeomFactory factory) {
        super(vertices, factory);

        orbitsList_ = orbitsList;

    }

    public List<WaveCurveOrbitGeom> getOrbitsGeomList() {
        return orbitsList_;
    }




    @Override
    public GeomObjView createView(ViewingTransform transf) throws DimMismatchEx {
        return new WaveCurveView(this, transf,viewingAttr());

    }


   
}
