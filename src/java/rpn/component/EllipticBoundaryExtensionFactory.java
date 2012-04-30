/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import rpnumerics.*;
import rpnumerics.EllipticBoundary;
import wave.util.RealSegment;

public class EllipticBoundaryExtensionFactory extends BifurcationCurveGeomFactory {

    public EllipticBoundaryExtensionFactory(EllipticBoundaryExtensionCalc calc) {
        super(calc);


    }
    //
    // Methods
    //

    protected RpGeometry createGeomFromSource() {

        EllipticBoundaryExtension curve = (EllipticBoundaryExtension) geomSource();


        // assuming a container with HugoniotSegment elements
        int resultSize = curve.segments().size();

        RealSegGeom[] hugoniotArray = new RealSegGeom[resultSize];
        for (int i = 0; i < resultSize; i++) {
            hugoniotArray[i] = new RealSegGeom((RealSegment) curve.segments().get(i));
        }
        return new EllipticBoundaryExtensionGeom(hugoniotArray, this);

    }

    public String toMatlab(int curveIndex) {
        return null;
    }

    public String toXML() {

        StringBuilder buffer = new StringBuilder();

        EllipticBoundaryExtensionCalc calc = (EllipticBoundaryExtensionCalc) rpCalc();

        String commandName = geomSource().getClass().getName();
        commandName = commandName.toLowerCase();
        commandName = commandName.replaceAll(".+\\.", "");

        buffer.append("<COMMAND name=\"" + commandName + "\"" + calc.getParams().toString() + ">\n");

        buffer.append(((EllipticBoundary) geomSource()).toXML());

        buffer.append("</COMMAND>\n");

        return buffer.toString();


    }
}
