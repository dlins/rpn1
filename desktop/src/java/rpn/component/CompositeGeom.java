package rpn.component;

import rpnumerics.CompositeCurve;
import rpnumerics.OrbitPoint;
import wave.multid.*;
import wave.multid.view.*;

public class CompositeGeom extends WaveCurveOrbitGeom implements RpGeometry {

    private OrbitPoint[] orbitPointsArray_;

    public CompositeGeom(CoordsArray[] coordsArray, WaveCurveOrbitGeomFactory factory) {
        super(coordsArray, factory);
        orbitPointsArray_ = ((CompositeCurve) factory.geomSource()).getPoints();
    }

    public CompositeGeom(CoordsArray[] vertices, WaveCurveOrbitGeomFactory factory, CompositeCurve orbit) {
        super(vertices, factory);
        orbitPointsArray_ = orbit.getPoints();
    }

    @Override
    public GeomObjView createView(ViewingTransform transf) throws DimMismatchEx {
        return new CompositeOrbitView(this, transf,  viewingAttr());

    }

    public OrbitPoint[] getPointsArray() {
        return orbitPointsArray_;
    }
}
