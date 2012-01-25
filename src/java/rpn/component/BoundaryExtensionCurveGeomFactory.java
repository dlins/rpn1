/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import rpnumerics.*;

public class BoundaryExtensionCurveGeomFactory extends BifurcationCurveGeomFactory {

    public BoundaryExtensionCurveGeomFactory(BoundaryExtensionCurveCalc calc) {
        super(calc);
    }

    public BoundaryExtensionCurveGeomFactory(BoundaryExtensionCurveCalc calc, BoundaryExtensionCurve curve) {
        super(calc, curve);
    }

    //
    // Methods
    //
    @Override
    protected RpGeometry createGeomFromSource() {

        BoundaryExtensionCurve curve = (BoundaryExtensionCurve) geomSource();

        int resultSize = curve.segments().size();
//        System.out.println("Tamanho da extensao da fronteira: "+resultSize);

        BifurcationSegGeom[] leftBifurcationSegArray = new BifurcationSegGeom[resultSize];
        for (int i = 0; i < resultSize; i++) {
            leftBifurcationSegArray[i] = new BifurcationSegGeom((HugoniotSegment) curve.segments().get(i));
        }

        return new BoundaryExtensionCurveGeom(leftBifurcationSegArray, this);

    }

    @Override
    public String toXML() {


        StringBuffer buffer = new StringBuffer();

        BoundaryExtensionCurveCalc boundaryExtensionCurveCalc = (BoundaryExtensionCurveCalc) rpCalc();

        buffer.append("<COMMAND name=\"boundary extension\" curvefamily=\"" + boundaryExtensionCurveCalc.getCurveFamily()
                + "\"" + " domainfamily=\"" + boundaryExtensionCurveCalc.getDomainFamily()
                + "\"" + " characteristic=\"" + boundaryExtensionCurveCalc.getCharacteristicWhere()
                + "\"" + " edge=\"" + boundaryExtensionCurveCalc.getEdge()
                + "\"" + " edgeresolution=\"" + boundaryExtensionCurveCalc.getEdgeResolution() + "\""
                + ">\n");

        buffer.append(((BifurcationCurve) geomSource()).toXML());

        buffer.append("</COMMAND>\n");

        return buffer.toString();


    }
}
