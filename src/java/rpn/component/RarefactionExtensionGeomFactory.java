/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import rpnumerics.RarefactionExtensionCalc;
import rpnumerics.RarefactionExtensionCurve;
import wave.util.RealSegment;
import wave.util.RealVector;

public class RarefactionExtensionGeomFactory extends BifurcationCurveGeomFactory {
    //
    // Constants
    //
    //
    // Members
    //
    //
    // Constructors/Initializers
    //

    public RarefactionExtensionGeomFactory(RarefactionExtensionCalc calc) {
        super(calc);
    }
    //
    // Accessors/Mutators
    //
    //
    // Methods
    //

    @Override
    protected RpGeometry createGeomFromSource() {

        RarefactionExtensionCurve curve = (RarefactionExtensionCurve) geomSource();

       
        int resultSize = curve.segments().size();

        BifurcationSegGeom[] hugoniotArray = new BifurcationSegGeom[resultSize];
        for (int i = 0; i < resultSize; i++) {
            hugoniotArray[i] = new BifurcationSegGeom((RealSegment) curve.segments().get(i));


        }

        return new RarefactionExtensionGeom(hugoniotArray, this);

    }

    public String toXML() {
        StringBuilder str = new StringBuilder();

        RarefactionExtensionCalc calc = (RarefactionExtensionCalc) rpCalc();

        RealVector firstPoint = new RealVector(calc.getStart());

        String commandName = geomSource().getClass().getName();

        commandName = commandName.toLowerCase();

        commandName = commandName.replaceAll(".+\\.", "");

        str.append("<COMMAND name=\"" + commandName + "\"");

        if (calc.getIncrease() != OrbitGeom.BOTH_DIR) {
            String direction = "forward\"";

            if (calc.getIncrease() == OrbitGeom.BACKWARD_DIR) {
                direction = "backward\"";

            }
            str.append(" direction=\"");
            str.append(direction+" ");
        }

        StringBuilder resolution = new StringBuilder();

        for (int i = 0; i < calc.getResolution().length; i++) {
            resolution.append(calc.getResolution()[i]);
            resolution.append(" ");
        }

        str.append("resolution=\"" + resolution.toString().trim() + "\"" + " inputpoint=\"" + firstPoint.toString() + "\" curvefamily=\"" + calc.getCurveFamily() + "\" domainfamily =\""
                + calc.getDomainFamily() + "\"" + ">\n");
        str.append(((RarefactionExtensionCurve) geomSource()).toXML());
        str.append("</COMMAND>\n");
        return str.toString();


    }

    public String toMatlab(int curveIndex) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
