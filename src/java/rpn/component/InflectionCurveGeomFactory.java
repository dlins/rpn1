/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.component;

import rpnumerics.BifurcationCurve;
import rpnumerics.CoincidenceCurve;
import rpnumerics.HugoniotSegment;
import rpnumerics.InflectionCurve;
import rpnumerics.InflectionCurveCalc;

public class InflectionCurveGeomFactory extends BifurcationCurveGeomFactory{

    public InflectionCurveGeomFactory(InflectionCurveCalc calc) {
        super(calc);
    }

  
    // Methods
    //
    @Override
    protected RpGeometry createGeomFromSource() {

        InflectionCurve curve = (InflectionCurve) geomSource();

        // assuming a container with HugoniotSegment elements
        int resultSize = curve.segments().size();
        BifurcationSegGeom[] bifurcationSegArray = new BifurcationSegGeom[resultSize];
        for (int i = 0; i < resultSize; i++) {
            bifurcationSegArray[i] = new BifurcationSegGeom((HugoniotSegment) curve.segments().get(i));

        }
        return new InflectionCurveGeom(bifurcationSegArray, this);

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
