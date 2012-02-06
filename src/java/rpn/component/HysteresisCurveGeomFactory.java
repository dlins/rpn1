/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import java.awt.Color;
import rpnumerics.BifurcationCurve;
import rpnumerics.CoincidenceCurve;
import rpnumerics.HysteresisCurve;
import rpnumerics.HysteresisCurveCalc;
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
    @Override
    protected RpGeometry createGeomFromSource() {

        HysteresisCurve curve = (HysteresisCurve) geomSource();

        // assuming a container with HugoniotSegment elements
        int resultSize = curve.segments().size();
        BifurcationSegGeom[] bifurcationSegArray = new BifurcationSegGeom[resultSize];
        for (int i = 0; i < resultSize; i++) {
            bifurcationSegArray[i] = new BifurcationSegGeom((RealSegment) curve.segments().get(i),viewAtt_);

        }
        return new HysteresisCurveGeom(bifurcationSegArray, this);

    }

    public String toMatlab(int curveIndex) {

        StringBuffer buffer = new StringBuffer();
        CoincidenceCurve curve = (CoincidenceCurve) geomSource();
        buffer.append("%%\nclose all;clear all;\n");
        buffer.append(RpCalcBasedGeomFactory.createMatlabColorTable());
        buffer.append(curve.toMatlabData(0));

        buffer.append("%%\n% begin plot x y\n");
        buffer.append("figure; set(gca, 'Color',[0 0 0]); hold on\n");
        buffer.append(curve.createMatlabPlotLoop(0, 1, 0));

        return buffer.toString();

    }

    @Override
    public String toXML() {

        StringBuffer buffer = new StringBuffer();

        buffer.append(super.toXML());

        HysteresisCurveCalc hysteresisCurveCalc = (HysteresisCurveCalc) rpCalc();

        buffer.append(" curvefamily=\"" + hysteresisCurveCalc.getCurveFamily() +
                "\"" + " domainfamily=\"" + hysteresisCurveCalc.getDomainFamily() +
                "\"" +" characteristic=\""+hysteresisCurveCalc.getCharacteristicWhere()+
                "\""+" singular=\""+hysteresisCurveCalc.getSingular()+"\""+ ">\n");

        buffer.append(((BifurcationCurve) geomSource()).toXML());

        buffer.append("</COMMAND>\n");

        return buffer.toString();

    }
}
