/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rpn.component;

import rpnumerics.BifurcationCurve;
import rpnumerics.BifurcationCurveCalc;
import rpnumerics.CoincidenceCurve;
import rpnumerics.CoincidenceExtensionCurve;
import rpnumerics.HugoniotSegment;

public class CoincidenceExtensionCurveGeomFactory extends BifurcationCurveGeomFactory {

    public CoincidenceExtensionCurveGeomFactory(BifurcationCurveCalc calc) {
        super(calc);
    }

    // Methods
    //
    @Override
    protected RpGeometry createGeomFromSource() {

        CoincidenceExtensionCurve curve = (CoincidenceExtensionCurve) geomSource();

        // assuming a container with HugoniotSegment elements
        int resultSize = curve.segments().size();
        //ViewingAttr viewingAttr = new ViewingAttr(Color.MAGENTA);
        BifurcationSegGeom[] hugoniotArray = new BifurcationSegGeom[resultSize];
        for (int i = 0; i < resultSize; i++) {
            hugoniotArray[i] = new BifurcationSegGeom((HugoniotSegment) curve.segments().get(i));

        }
        return new CoincidenceExtensionCurveGeom(hugoniotArray, this);

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

    public String toXML() {
        StringBuffer buffer = new StringBuffer();

        BifurcationCurve curve = (BifurcationCurve) geomSource();

        buffer.append("<COMMAND name=\"coincidenceextension\">\n");

        buffer.append(curve.toXML());

        buffer.append("</COMMAND>\n");

        return buffer.toString();

    }
}
