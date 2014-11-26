package rpn.component;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import rpn.component.util.LinePlotted;
import rpnumerics.FundamentalCurve;
import rpnumerics.IntegralCurve;
import rpnumerics.OrbitPoint;
import rpnumerics.RPnCurve;
import rpnumerics.WaveCurveBranch;
import wave.multid.*;
import wave.multid.view.*;
import wave.util.RealVector;

public class IntegralGeom extends OrbitGeom implements RpGeometry {

    public IntegralGeom(CoordsArray[] vertices, IntegralOrbitGeomFactory factory) {
        super(vertices, factory);

    }

    @Override
    public GeomObjView createView(ViewingTransform transf) throws DimMismatchEx {
        return new IntegralOrbitView(this, transf, viewingAttr());

    }

    @Override
    public void showSpeed(CoordsArray curvePoint, CoordsArray wcPoint, ViewingTransform transform) {

        FundamentalCurve fundamentalCurve = (FundamentalCurve) geomFactory().geomSource();

        RPnCurve rpnCurve = (RPnCurve) fundamentalCurve;

        int segmentIndex = rpnCurve.findClosestSegment(new RealVector(curvePoint.getCoords()));

        OrbitPoint point = (OrbitPoint) ((WaveCurveBranch) fundamentalCurve).getBranchPoints().get(segmentIndex);

        OrbitPoint[] orbitPoints = fundamentalCurve.getPoints();

        for (OrbitPoint orbitPoint : orbitPoints) {

            RealVector coords = point.getCoords();
            if (orbitPoint.getCoords().equals(coords)) {

                List<Object> wcObject = new ArrayList<Object>();
                wcObject.add(new RealVector(curvePoint.getCoords()));
                wcObject.add(new RealVector(wcPoint.getCoords()));
                wcObject.add(String.valueOf(orbitPoint.getSpeed()));

                LinePlotted speedAnnotation = new LinePlotted(wcObject, transform, new ViewingAttr(Color.white));

                addAnnotation(speedAnnotation);

            }

        }

    }

}
