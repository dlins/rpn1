/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rpn.component;

import java.awt.Color;
import rpnumerics.BifurcationCurve;
import rpnumerics.CoincidenceCurve;
import rpnumerics.HugoniotSegment;
import rpnumerics.RpCalculation;
import wave.multid.view.ViewingAttr;

public class CoincidenceCurveGeomFactory extends RpCalcBasedGeomFactory{

    public CoincidenceCurveGeomFactory(RpCalculation calc) {
        super(calc);
    }

  
    // Methods
    //
    protected RpGeometry createGeomFromSource() {

        CoincidenceCurve curve = (CoincidenceCurve) geomSource();

        // assuming a container with HugoniotSegment elements
        int resultSize = curve.segments().size();
        ViewingAttr viewAtt = new ViewingAttr(CoincidenceCurveGeom.COLOR);
        HugoniotSegGeom[] hugoniotArray = new HugoniotSegGeom[resultSize];
        for (int i = 0; i < resultSize; i++) {
            hugoniotArray[i] = new HugoniotSegGeom((HugoniotSegment) curve.segments().get(i),viewAtt);

        }
        return new CoincidenceCurveGeom(hugoniotArray, this);

    }


  

    public String toMatlab(int curveIndex) {

        StringBuffer buffer = new StringBuffer();
        CoincidenceCurve curve = (CoincidenceCurve) geomSource();
        buffer.append("%%\nclose all;clear all;\n");
        buffer.append(RpCalcBasedGeomFactory.createMatlabColorTable());
        buffer.append(curve.toMatlabData(0));

        buffer.append("%%\n% begin plot x y\n");
        buffer.append("figure; set(gca, 'Color',[0 0 0]); hold on\n");
        buffer.append(curve.createMatlabPlotLoop(0,1,0));

        return buffer.toString();

    }

    public String toXML() {

        StringBuffer buffer = new StringBuffer();

        buffer.append("<COMMAND name=\"coincidence\">\n");

        buffer.append(((BifurcationCurve)geomSource()).toXML());

        buffer.append("</COMMAND>\n");

        return buffer.toString();

    }


















}
