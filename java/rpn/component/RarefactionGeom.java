package rpn.component;

import rpnumerics.OrbitPoint;
import rpnumerics.RarefactionOrbit;
import wave.multid.*;
import wave.multid.view.*;

public class RarefactionGeom extends WaveCurveOrbitGeom implements RpGeometry {

    private OrbitPoint[] orbitPointsArray_;


    public RarefactionGeom(CoordsArray[] vertices, WaveCurveOrbitGeomFactory factory) {
        super(vertices, factory);
        orbitPointsArray_ = ((RarefactionOrbit) factory.geomSource()).getPoints();

    }

    public RarefactionGeom(CoordsArray[] vertices, WaveCurveOrbitGeomFactory factory, RarefactionOrbit orbit) {
        super(vertices, factory);
        orbitPointsArray_ = orbit.getPoints();
    }


    @Override
    public GeomObjView createView(ViewingTransform transf) throws DimMismatchEx {
        return new RarefactionOrbitView(this, transf,viewingAttr());

    }


    public OrbitPoint[] getPointsArray() {
        return orbitPointsArray_;
    }


   
}
