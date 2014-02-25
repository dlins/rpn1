/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import java.awt.Color;
import rpnumerics.Area;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import rpnumerics.InflectionCurve;
import rpnumerics.InflectionCurveCalc;
import rpnumerics.RpException;
import wave.multid.view.ViewingAttr;
import wave.util.RealSegment;

public class InflectionCurveGeomFactory extends BifurcationCurveGeomFactory {

    private static ViewingAttr viewAtt_ = new ViewingAttr(Color.GREEN);



    public InflectionCurveGeomFactory(InflectionCurveCalc calc) {
        super(calc);
    }

    public InflectionCurveGeomFactory(InflectionCurveCalc calc, InflectionCurve curve) {
        super(calc, curve);
    }

    //
    // Methods
    //
//    @Override
//    public RpGeometry createGeomFromSource() {
//
//        InflectionCurve curve = (InflectionCurve) geomSource();
//
//        // assuming a container with HugoniotSegment elements
//        int resultSize = curve.segments().size();
//
//        RealSegGeom[] bifurcationSegArray = new RealSegGeom[resultSize];
//        for (int i = 0; i < resultSize; i++) {
//            bifurcationSegArray[i] = new RealSegGeom((RealSegment) curve.segments().get(i), viewAtt_);
//        }
//
//        return new InflectionCurveGeom(bifurcationSegArray, this);
//
//    }
    
    @Override
    void updateGeomSource (List<Area> areaListToRefine){

            try {
                System.out.println("Entrando em inflection");
                InflectionCurve newBifurcation = (InflectionCurve) calc_.recalc(areaListToRefine);

                InflectionCurve oldBifurcationCurve = (InflectionCurve) geomSource_;
                oldBifurcationCurve.leftSegments().addAll(newBifurcation.leftSegments());

                geomSource_ = new InflectionCurve(oldBifurcationCurve.leftSegments());

            } catch (RpException ex) {
                Logger.getLogger(BifurcationCurveGeomFactory.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
    

}
