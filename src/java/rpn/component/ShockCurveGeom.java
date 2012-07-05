/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.component;

import rpnumerics.OrbitPoint;
import rpnumerics.ShockCurve;
import wave.multid.CoordsArray;
import wave.multid.view.GeomObjView;
import wave.multid.view.ViewingTransform;
import wave.multid.DimMismatchEx;

public class ShockCurveGeom extends WaveCurveOrbitGeom implements RpGeometry {

    private OrbitPoint[] orbitPointsArray_;
   
  

    //
    // Constructors
    //
    public ShockCurveGeom(CoordsArray[] source, WaveCurveOrbitGeomFactory factory) {
        super(source, factory);
        orbitPointsArray_ = ((ShockCurve) factory.geomSource()).getPoints();
    }


    public ShockCurveGeom(CoordsArray[] vertices, WaveCurveOrbitGeomFactory factory, ShockCurve orbit) {
        super(vertices, factory);
        orbitPointsArray_ = orbit.getPoints();
    }

    //
    // Accessors/Mutators
    //

    @Override
    public GeomObjView createView(ViewingTransform transf) throws DimMismatchEx {

        return new ShockCurveGeomView(this, transf,viewingAttr());

    }


    public OrbitPoint[] getPointsArray() {
        return orbitPointsArray_;
    }

    
}
