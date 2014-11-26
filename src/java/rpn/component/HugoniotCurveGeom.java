/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import rpn.component.util.LinePlotted;
import rpnumerics.HugoniotCurve;
import rpnumerics.HugoniotSegment;
import rpnumerics.Orbit;
import rpnumerics.SegmentedCurve;
import wave.multid.view.*;
import wave.multid.*;
import wave.multid.model.MultiPoint;
import wave.util.RealVector;

public class HugoniotCurveGeom extends SegmentedCurveGeom {//implements MultiGeometry, RpGeometry {

    //
    // Constructors
    //
    
    private List<MultiPoint> transitionPointsList_;
    public HugoniotCurveGeom(HugoniotSegGeom[] segArray, HugoniotCurveGeomFactory factory) {

        super(segArray, factory);
        
        transitionPointsList_=new ArrayList();
    }

    public GeomObjView createView(ViewingTransform transf) throws DimMismatchEx {
        return new HugoniotCurveView(this, transf, viewingAttr());
    }

    @Override
    public void showSpeed(CoordsArray curvePoint, CoordsArray wcPoint, ViewingTransform transform) {

        HugoniotCurve fundamentalCurve = (HugoniotCurve) geomFactory().geomSource();
        double velocity = fundamentalCurve.velocity(new RealVector(curvePoint.getCoords()));

        List<Object> wcObject = new ArrayList<Object>();
        wcObject.add(new RealVector(curvePoint.getCoords()));
        wcObject.add(new RealVector(wcPoint.getCoords()));
        wcObject.add(String.valueOf(velocity));

        LinePlotted speedAnnotation = new LinePlotted(wcObject, transform, new ViewingAttr(Color.white));

        addAnnotation(speedAnnotation);

    }

    @Override
    public void showClassification(CoordsArray curvePoint, CoordsArray wcPoint, ViewingTransform transform) {
        HugoniotCurve fundamentalCurve = (HugoniotCurve) geomFactory().geomSource();
        int dir = ((HugoniotCurve) fundamentalCurve).getDirection();

        HugoniotSegment segment = (HugoniotSegment) (((SegmentedCurve) fundamentalCurve).segments()).get(fundamentalCurve.findClosestSegment(new RealVector(curvePoint.getCoords())));
        String typeStr = permuteString(HugoniotSegGeom.s[segment.getType()], dir);

        List<Object> wcObjectsList = new ArrayList();

        wcObjectsList.add(new RealVector(curvePoint.getCoords()));
        wcObjectsList.add(new RealVector(wcPoint.getCoords()));

        wcObjectsList.add(typeStr);

        LinePlotted speedAnnotation = new LinePlotted(wcObjectsList, transform, new ViewingAttr(Color.white));

        addAnnotation(speedAnnotation);

    }

    private String permuteString(String string, int flag) {
        String str = string;
        if (flag == Orbit.BACKWARD_DIR) {
            String str1 = str.substring(0, 2);
            String str2 = str.substring(2, 4);
            str = str2.concat(str1);
        }
        return str;
    }
    
    List<MultiPoint> getTransitionList(){
        return transitionPointsList_;
    }

    void setTransitionList(List<MultiPoint> multiPointList) {
        transitionPointsList_= multiPointList;
    }

}
