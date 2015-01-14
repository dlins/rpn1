/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import java.awt.Color;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import rpnumerics.Area;
import rpnumerics.BifurcationCurve;
import rpnumerics.CoincidenceCurve;
import rpnumerics.HysteresisCurve;
import rpnumerics.HysteresisCurveCalc;
import rpnumerics.RpException;
import wave.multid.view.ViewingAttr;
import wave.util.RealSegment;

public class HysteresisCurveGeomFactory extends BifurcationCurveGeomFactory {


    private static ViewingAttr viewAtt_ = new ViewingAttr(Color.yellow);

    public HysteresisCurveGeomFactory(HysteresisCurveCalc calc) {
        super(calc);
    }

     public HysteresisCurveGeomFactory(HysteresisCurveCalc calc,HysteresisCurve curve) {
        super(calc,curve);
    }

    // Methods
    //
//    @Override
//    public RpGeometry createGeomFromSource() {
//
//        HysteresisCurve curve = (HysteresisCurve) geomSource();
//
//        int resultSize = curve.segments().size();
//        RealSegGeom[] bifurcationSegArray = new RealSegGeom[resultSize];
//        for (int i = 0; i < resultSize; i++) {
//            bifurcationSegArray[i] = new RealSegGeom((RealSegment) curve.segments().get(i),viewAtt_);
//
//        }
//        return new HysteresisCurveGeom(bifurcationSegArray, this);
//
//    }

    public String toMatlab(int curveIndex) {

        System.out.println("Entrei no toMatlab de Hysteresis");

        StringBuffer buffer = new StringBuffer();
        CoincidenceCurve curve = (CoincidenceCurve) geomSource();
        buffer.append("%%\nclose all;clear all;\n");
        //buffer.append(RpCalcBasedGeomFactory.createMatlabColorTable());
        buffer.append(curve.toMatlabData(0));

        buffer.append("%%\n% begin plot x y\n");
        buffer.append("figure; set(gca, 'Color',[0 0 0]); hold on\n");
        buffer.append(curve.createMatlabPlotLoop(0, 1, 0));

        return buffer.toString();

    }
    
    
    
    
    @Override
    void updateGeomSource (List<Area> areaListToRefine){

            try {
                System.out.println("Entrando em hyesteresis");
                HysteresisCurve newBifurcation = (HysteresisCurve) calc_.recalc(areaListToRefine);

                HysteresisCurve oldBifurcationCurve = (HysteresisCurve) geomSource_;
                oldBifurcationCurve.leftSegments().addAll(newBifurcation.leftSegments());
                oldBifurcationCurve.rightSegments().addAll(newBifurcation.rightSegments());
                geomSource_ = new HysteresisCurve(oldBifurcationCurve.leftSegments(),oldBifurcationCurve.rightSegments());
            } catch (RpException ex) {
                Logger.getLogger(BifurcationCurveGeomFactory.class.getName()).log(Level.SEVERE, null, ex);
            }
        

    }
    
//    @Override
//    public String toXML() {
//
//        StringBuffer buffer = new StringBuffer();
//
//        buffer.append(super.toXML());
//
//        HysteresisCurveCalc hysteresisCurveCalc = (HysteresisCurveCalc) rpCalc();
//
//        buffer.append(" family=\"" + hysteresisCurveCalc.getFamily() + "\""+ ">\n");
//
//        buffer.append(((BifurcationCurve) geomSource()).toXML());
//
//        buffer.append("</COMMAND>\n");
//
//        return buffer.toString();
//
//    }
}
