/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import java.awt.Color;
import rpn.controller.OrbitController;
import rpn.controller.RpController;
import rpnumerics.*;
import wave.multid.view.ViewingAttr;
import wave.util.RealVector;

public class RiemannProfileGeomFactory extends RpCalcBasedGeomFactory {
    //
    // Constants
    //
    //
    // Members
    //
    //
    // Constructors/Initializers
    //

    public RiemannProfileGeomFactory(RiemannProfileCalc calc) {
        super(calc);
    }

    //
    // Accessors/Mutators
    //
    //
    // Methods
    //
    protected ViewingAttr selectViewingAttr() {

        return new ViewingAttr(Color.white);
    }

    public RpGeometry createGeomFromSource() {
        RiemannProfile riemannProfile = (RiemannProfile) geomSource();
        

        return new RiemannProfileGeom(MultidAdapter.converseRiemannProfileToCoordsArray(riemannProfile.getPoints()), this);
    }

    public String toXML() {

        StringBuffer str = new StringBuffer();
        RealVector firstPoint = new RealVector(((OrbitCalc) rpCalc()).getStart());

        String commandName = geomSource().getClass().getName();

        commandName = commandName.toLowerCase();

        commandName = commandName.replaceAll(".+\\.", "");

        str.append("<COMMAND name=\"" + commandName + "\"");

        if (((OrbitCalc) rpCalc()).getDirection() != Orbit.BOTH_DIR) {
            String direction = "forward\"";

            if (((OrbitCalc) rpCalc()).getDirection() == Orbit.BACKWARD_DIR) {
                direction = "backward\"";

            }
            str.append(" direction=\"");
            str.append(direction);
        }

//        str.append(" inputpoint=\"" + firstPoint.toString() + "\" family=\"" + ((Orbit) geomSource()).getFamilyIndex() + "\" " + ">\n");
        str.append(((Orbit) geomSource()).toXML());
        str.append("</COMMAND>\n");
        return str.toString();



    }

    public String toMatlab(int curveIndex) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
