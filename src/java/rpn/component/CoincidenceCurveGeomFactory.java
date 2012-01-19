/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.component;

import rpnumerics.BifurcationCurve;
import rpnumerics.BifurcationCurveCalc;
import rpnumerics.CoincidenceCurve;
import rpnumerics.HugoniotSegment;

public class CoincidenceCurveGeomFactory extends BifurcationCurveGeomFactory{

    public CoincidenceCurveGeomFactory(BifurcationCurveCalc calc) {
        super(calc);
    }

  
    // Methods
    //
    protected RpGeometry createGeomFromSource() {

        CoincidenceCurve curve = (CoincidenceCurve) geomSource();

        // assuming a container with HugoniotSegment elements
        int resultSize = curve.segments().size();
        BifurcationSegGeom[] hugoniotArray = new BifurcationSegGeom[resultSize];
        for (int i = 0; i < resultSize; i++) {
            hugoniotArray[i] = new BifurcationSegGeom((HugoniotSegment) curve.segments().get(i));

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
