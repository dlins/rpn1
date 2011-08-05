/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import rpnumerics.*;

public class SubInflectionCurveGeomFactory extends RpCalcBasedGeomFactory {

    public SubInflectionCurveGeomFactory(SubInflectionCurveCalc calc) {
        super(calc);

    }

    //
    // Methods
    //
    protected RpGeometry createGeomFromSource() {

        SubInflectionCurve curve = (SubInflectionCurve) geomSource();

        // assuming a container with HugoniotSegment elements
        int resultSize = curve.segments().size();
        HugoniotSegGeom[] hugoniotArray = new HugoniotSegGeom[resultSize];
        for (int i = 0; i < resultSize; i++) {
            hugoniotArray[i] = new HugoniotSegGeom((HugoniotSegment) curve.segments().get(i));
        }
        return new SubInflectionCurveGeom(hugoniotArray, this);

    }



    public String toXML() {

        StringBuffer buffer = new StringBuffer();
        BifurcationCurve curve = (BifurcationCurve) geomSource();

        buffer.append("<COMMAND name=\"subinflection\">\n");

        buffer.append(curve.toXML());

        buffer.append("</COMMAND>\n");


        return buffer.toString();

    }

    public String toMatlab(int curveIndex) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
