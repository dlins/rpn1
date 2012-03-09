/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import rpn.controller.RarefactionExtensionController;
import rpn.controller.RpController;
import rpnumerics.Orbit;
import rpnumerics.RarefactionExtensionCalc;
import rpnumerics.RarefactionExtensionCurve;
import wave.util.RealSegment;
import wave.util.RealVector;

public class RarefactionExtensionGeomFactory extends RpCalcBasedGeomFactory {
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

        RealSegGeom[] hugoniotArray = new RealSegGeom[resultSize];
        for (int i = 0; i < resultSize; i++) {
            hugoniotArray[i] = new RealSegGeom((RealSegment) curve.segments().get(i));


        }

        return new RarefactionExtensionGeom(hugoniotArray, this);

    }


    @Override
     protected RpController createUI() {
        return new RarefactionExtensionController();
    }

    @Override
    public String toXML() {
        StringBuilder str = new StringBuilder();

        RarefactionExtensionCalc calc = (RarefactionExtensionCalc) rpCalc();

        RealVector firstPoint = new RealVector(calc.getStart());

        String commandName = geomSource().getClass().getName();

        commandName = commandName.toLowerCase();

        commandName = commandName.replaceAll(".+\\.", "");

        str.append("<COMMAND name=\"" + commandName + "\"");

        if (calc.getIncrease() != Orbit.BOTH_DIR) {
            String direction = "forward\"";

            if (calc.getIncrease() == Orbit.BACKWARD_DIR) {
                direction = "backward\"";

            }
            str.append(" direction=\"");
            str.append(direction+" ");
        }

     

       
        str.append(calc.getParams().toString() + "\"" + " inputpoint=\"" + firstPoint.toString() + "\" curvefamily=\"" + calc.getCurveFamily() + "\" domainfamily =\""
                + calc.getDomainFamily() + "\" " + "characteristic=\""+ calc.getCharacteristic()+"\""+ ">\n");
        str.append(((RarefactionExtensionCurve) geomSource()).toXML());
        str.append("</COMMAND>\n");
        return str.toString();


    }

    public String toMatlab(int curveIndex) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
