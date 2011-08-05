/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import rpnumerics.*;

public class SubInflectionExtensionCurveGeomFactory extends BifurcationCurveGeomFactory {

    public SubInflectionExtensionCurveGeomFactory(SubInflectionExtensionCurveCalc calc) {
        super(calc);
    }

    //
    // Methods
    //
    @Override
    protected RpGeometry createGeomFromSource() {

        SubInflectionExtensionCurve curve = (SubInflectionExtensionCurve) geomSource();

        BifurcationSegGeom[] bifurcationSegArray = null;

        int resultSize = curve.segments().size();

        bifurcationSegArray = new BifurcationSegGeom[resultSize];
        for (int i = 0; i < resultSize; i++) {
            bifurcationSegArray[i] = new BifurcationSegGeom((HugoniotSegment) curve.segments().get(i));

        }

        return new SubInflectionExtensionCurveGeom(bifurcationSegArray, this);

    }

    public String toXML() {

        StringBuffer buffer = new StringBuffer();

        BifurcationCurve curve = (BifurcationCurve) geomSource();

        buffer.append("<COMMAND name=\"subinflectionextension\">\n");

        buffer.append(curve.toXML());

        buffer.append("</COMMAND>\n");

        return buffer.toString();


    }
}
