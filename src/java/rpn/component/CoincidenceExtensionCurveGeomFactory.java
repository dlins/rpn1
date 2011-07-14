/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rpn.component;

import java.awt.Color;
import rpnumerics.CoincidenceCurve;
import rpnumerics.CoincidenceExtensionCurve;
import rpnumerics.HugoniotCurve;
import rpnumerics.HugoniotSegment;
import rpnumerics.RPNUMERICS;
import rpnumerics.RpCalculation;
import rpnumerics.ShockProfile;
import wave.multid.view.ViewingAttr;

public class CoincidenceExtensionCurveGeomFactory extends RpCalcBasedGeomFactory{

    public CoincidenceExtensionCurveGeomFactory(RpCalculation calc) {
        super(calc);
    }

  
    // Methods
    //
    protected RpGeometry createGeomFromSource() {

        CoincidenceExtensionCurve curve = (CoincidenceExtensionCurve) geomSource();

        // assuming a container with HugoniotSegment elements
        int resultSize = curve.segments().size();
        ViewingAttr viewingAttr = new ViewingAttr(Color.MAGENTA);
        HugoniotSegGeom[] hugoniotArray = new HugoniotSegGeom[resultSize];
        for (int i = 0; i < resultSize; i++) {
            hugoniotArray[i] = new HugoniotSegGeom((HugoniotSegment) curve.segments().get(i),viewingAttr);

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
        buffer.append(curve.createMatlabPlotLoop(0,1,0));


//        buffer.append("\n%%\n% begin plot x z\n");
//        buffer.append("figure; set(gca, 'Color',[0 0 0]); hold on\n");
//        buffer.append(curve.toMatlabPlot(0, 2));
//
//        buffer.append("\n%%\n% begin plot y z\n");
//        buffer.append("figure; set(gca, 'Color',[0 0 0]); hold on\n");
//        buffer.append(curve.toMatlabPlot(1, 2));

        return buffer.toString();

    }

    public String toXML() {

        StringBuffer buffer = new StringBuffer();

        buffer.append("<HUGONIOTCALC xzero=\"" + ((HugoniotCurve) geomSource()).getXZero() + "\"" + " methodname=\"" + ShockProfile.instance().getHugoniotMethodName() + "\"" + " flowname=\"" + RPNUMERICS.getShockProfile().getFlowName() + "\"" + ">\n");

        buffer.append(((HugoniotCurve) geomSource()).toXML(rpn.parser.RPnDataModule.RESULTS));

        buffer.append("</HUGONIOTCALC>\n");

        return buffer.toString();

    }


















}
