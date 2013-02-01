package rpn.component;

import rpnumerics.OrbitPoint;
import rpnumerics.RarefactionCurve;
import wave.multid.*;
import wave.multid.view.*;

public class RarefactionCurveGeom extends WaveCurveOrbitGeom implements RpGeometry {

    private OrbitPoint[] orbitPointsArray_;


    public RarefactionCurveGeom(CoordsArray[] vertices, WaveCurveOrbitGeomFactory factory) {
        super(vertices, factory);
        orbitPointsArray_ = ((RarefactionCurve) factory.geomSource()).getPoints();

    }

    public RarefactionCurveGeom(CoordsArray[] vertices, WaveCurveOrbitGeomFactory factory, RarefactionCurve orbit) {
        super(vertices, factory);
        orbitPointsArray_ = orbit.getPoints();
    }


    @Override
    public GeomObjView createView(ViewingTransform transf) throws DimMismatchEx {
        return new RarefactionCurveView(this, transf,viewingAttr());

    }


    public OrbitPoint[] getPointsArray() {
        return orbitPointsArray_;
    }


   
}
