/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import java.awt.Color;
import rpnumerics.BifurcationCurve;
import rpnumerics.CoincidenceCurve;
import rpnumerics.InflectionCurve;
import rpnumerics.InflectionCurveCalc;
import wave.multid.view.ViewingAttr;
import wave.util.RealSegment;

public class InflectionCurveGeomFactory extends BifurcationCurveGeomFactory {

    private static ViewingAttr viewAtt_ = new ViewingAttr(Color.ORANGE);

    public InflectionCurveGeomFactory(InflectionCurveCalc calc) {
        super(calc);
    }

    public InflectionCurveGeomFactory(InflectionCurveCalc calc, InflectionCurve curve) {
        super(calc, curve);
    }

    // Methods
    //
    @Override
    protected RpGeometry createGeomFromSource() {

        InflectionCurve curve = (InflectionCurve) geomSource();

        // assuming a container with HugoniotSegment elements
        int resultSize = curve.segments().size();
        RealSegGeom[] bifurcationSegArray = new RealSegGeom[resultSize];
        for (int i = 0; i < resultSize; i++) {
            bifurcationSegArray[i] = new RealSegGeom((RealSegment) curve.segments().get(i), viewAtt_);
        }
        return new InflectionCurveGeom(bifurcationSegArray, this);

    }

    public String toMatlab(int curveIndex) {

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
    public String toXML() {

        StringBuffer buffer = new StringBuffer();

        buffer.append(super.toXML());

        InflectionCurveCalc calc = ((InflectionCurveCalc) rpCalc());

        int familyIndex = calc.getFamilyIndex();

        buffer.append("family=\"" + familyIndex + "\">\n");

        buffer.append(((BifurcationCurve) geomSource()).toXML());

        buffer.append("</COMMAND>\n");

        return buffer.toString();

    }
}
