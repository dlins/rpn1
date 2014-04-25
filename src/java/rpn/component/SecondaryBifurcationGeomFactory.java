/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import java.awt.Color;
import java.util.List;
import rpnumerics.*;
import wave.multid.CoordsArray;
import wave.multid.model.MultiPoint;
import wave.multid.view.ViewingAttr;
import wave.util.RealSegment;
import wave.util.RealVector;

public class SecondaryBifurcationGeomFactory extends BifurcationCurveGeomFactory {

    public SecondaryBifurcationGeomFactory(SecondaryBifurcationCurveCalc calc, SecondaryBifurcationCurve curve) {
        super(calc, curve);

    }

//    @Override
//    public RpGeometry createGeomFromSource() {
//
//        SecondaryBifurcationCurve curve = (SecondaryBifurcationCurve) geomSource();
//
//        RealSegGeom[] bifurcationArray = new RealSegGeom[curve.segments().size()];
//
//        RealSegGeom[] bifurcationArrayRight = new RealSegGeom[curve.rightSegments().size()];
//        RealSegGeom[] bifurcationArrayLeft = new RealSegGeom[curve.leftSegments().size()];
//
//
//
//        int i = 0;
//        for (Object realSegment : curve.rightSegments()) {
//            bifurcationArrayRight[i] = new RealSegGeom((RealSegment) realSegment, rightViewingAttr());
//            bifurcationArray[i] = bifurcationArrayRight[i];
//
//            i++;
//        }
//
//
//        int j = 0;
//        for (Object realSegment : curve.leftSegments()) {
//            bifurcationArrayLeft[j] = new RealSegGeom((RealSegment) realSegment, leftViewingAttr());
//            bifurcationArray[i + j] = bifurcationArrayLeft[j];
//            j++;
//        }
//      
//
//
//        RealVector umbilicPointVector = curve.getUmbilicPoint();
//        MultiPoint umbilicPoint=null;
//        if (umbilicPointVector != null) {
//
//         CoordsArray   coordsArray = new CoordsArray(umbilicPointVector);
//            umbilicPoint = new MultiPoint(coordsArray, new ViewingAttr(Color.blue));
//        }
//
//
//
//        return new SecondaryBifurcationCurveGeom(bifurcationArray, umbilicPoint, this);
//
//    }

    @Override
    void updateGeomSource(List<Area> areaListToRefine) {

        try {
            BifurcationCurve newBifurcation = (BifurcationCurve) calc_.recalc(areaListToRefine);
            BifurcationCurve oldBifurcationCurve = (BifurcationCurve) geomSource_;
            oldBifurcationCurve.leftSegments().addAll(newBifurcation.leftSegments());
            oldBifurcationCurve.rightSegments().addAll(newBifurcation.rightSegments());

            geomSource_ = new SecondaryBifurcationCurve(oldBifurcationCurve.leftSegments(), oldBifurcationCurve.rightSegments());


        } catch (RpException ex) {
            ex.printStackTrace();
        }


    }

    public SecondaryBifurcationGeomFactory(SecondaryBifurcationCurveCalc calc) {
        super(calc);
    }

    @Override
    protected ViewingAttr leftViewingAttr() {

        RpCalculation calc = rpCalc();
        if (calc instanceof StoneExplicitSecondaryBifurcationCurveCalc) {
            return new ViewingAttr(Color.white);
        }



        return new ViewingAttr(Color.yellow);
    }

    @Override
    protected ViewingAttr rightViewingAttr() {
        RpCalculation calc = rpCalc();
        if (calc instanceof StoneExplicitSecondaryBifurcationCurveCalc) {
            return new ViewingAttr(Color.white);
        }

        return new ViewingAttr(Color.magenta);
    }
}
