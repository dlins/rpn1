/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import rpnumerics.*;

public class ExtensionCurveGeomFactory extends BifurcationCurveGeomFactory {

    public ExtensionCurveGeomFactory(ExtensionCurveCalc calc) {
        super(calc);
    }

    //
    // Methods
    //
    @Override
    protected RpGeometry createGeomFromSource() {

        ExtensionCurve curve = (ExtensionCurve) geomSource();

        int resultSize = curve.segments().size();
//        System.out.println("Tamanho da extensao da fronteira: "+resultSize);

        BifurcationSegGeom[] leftBifurcationSegArray = new BifurcationSegGeom[resultSize];
        for (int i = 0; i < resultSize; i++) {
            leftBifurcationSegArray[i] = new BifurcationSegGeom((HugoniotSegment) curve.segments().get(i));
        }

        return new ExtensionCurveGeom(leftBifurcationSegArray, this);

    }
 

    public String toXML() {


        StringBuffer buffer = new StringBuffer();

        buffer.append("<COMMAND name=\"boundaryextension\"/>\n");

        buffer.append(((BifurcationCurve)geomSource()).toXML());

        buffer.append("</COMMAND>\n");

        return buffer.toString();


    }
}
