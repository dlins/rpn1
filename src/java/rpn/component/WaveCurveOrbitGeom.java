package rpn.component;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import rpn.component.util.LinePlotted;
import rpnumerics.OrbitPoint;
import rpnumerics.WaveCurveBranch;
import wave.multid.*;
import wave.multid.view.*;
import wave.util.RealVector;

public class WaveCurveOrbitGeom extends OrbitGeom implements WaveCurveBranchGeom {

    public WaveCurveOrbitGeom(CoordsArray[] vertices, WaveCurveOrbitGeomFactory factory) {
        super(vertices, factory);
    }

    @Override
    public GeomObjView createView(ViewingTransform transf) throws DimMismatchEx {
        return new WaveCurveOrbitGeomView(this, transf, viewingAttr());

    }

    public List<WaveCurveBranchGeom> getOrbitGeom() {
        ArrayList<WaveCurveBranchGeom> list = new ArrayList<WaveCurveBranchGeom>();
        list.add(this);
        return list;
    }

    @Override
    public void showSpeed(CoordsArray curvePoint, CoordsArray wcPoint, ViewingTransform transform) {

        WaveCurveBranch fundamentalCurve = (WaveCurveBranch) geomFactory().geomSource();
        
        double speed = fundamentalCurve.getSpeed(new OrbitPoint(curvePoint.getCoords(), 0d));

        List<Object> wcObject = new ArrayList<Object>();
        wcObject.add(new RealVector(curvePoint.getCoords()));
        wcObject.add(new RealVector(wcPoint.getCoords()));
        wcObject.add(String.valueOf(speed));

        LinePlotted speedAnnotation = new LinePlotted(wcObject, transform, new ViewingAttr(Color.white));

        addAnnotation(speedAnnotation);

    }
    
    


}
