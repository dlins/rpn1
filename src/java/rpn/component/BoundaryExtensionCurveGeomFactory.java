/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import java.awt.Color;
import rpnumerics.*;
import wave.multid.view.ViewingAttr;
import wave.util.RealSegment;

public class BoundaryExtensionCurveGeomFactory extends BifurcationCurveGeomFactory {

    private static ViewingAttr viewAtt_ = new ViewingAttr(Color.white);

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

        BifurcationSegGeom[] leftBifurcationSegArray = new BifurcationSegGeom[resultSize];
        for (int i = 0; i < resultSize; i++) {
            leftBifurcationSegArray[i] = new BifurcationSegGeom((RealSegment) curve.segments().get(i), viewAtt_);
        }

        return new BoundaryExtensionCurveGeom(leftBifurcationSegArray, this);

    }

    @Override
    public String toXML() {


        StringBuffer buffer = new StringBuffer();

        buffer.append(super.toXML());

        BoundaryExtensionCurveCalc boundaryExtensionCurveCalc = (BoundaryExtensionCurveCalc) rpCalc();

        buffer.append("curvefamily=\"" + boundaryExtensionCurveCalc.getCurveFamily()
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
