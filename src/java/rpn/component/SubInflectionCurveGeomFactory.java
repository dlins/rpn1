/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import rpnumerics.*;

public class SubInflectionCurveGeomFactory extends BifurcationCurveGeomFactory {

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
        BifurcationSegGeom[] hugoniotArray = new BifurcationSegGeom[resultSize];
        for (int i = 0; i < resultSize; i++) {
            hugoniotArray[i] = new BifurcationSegGeom((HugoniotSegment) curve.segments().get(i));
        }
        return new SubInflectionCurveGeom(hugoniotArray, this);

    }



    public String toXML() {

        StringBuffer buffer = new StringBuffer();

        buffer.append(super.toXML());
        BifurcationCurve curve = (BifurcationCurve) geomSource();

        buffer.append(">\n");

        buffer.append(curve.toXML());

        buffer.append("</COMMAND>\n");


        return buffer.toString();

    }

    public String toMatlab(int curveIndex) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
