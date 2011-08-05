/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import rpnumerics.RarefactionOrbit;
import rpnumerics.RarefactionOrbitCalc;
import wave.util.RealVector;

public class RarefactionOrbitGeomFactory extends RpCalcBasedGeomFactory {
    //
    // Constants
    //
    //
    // Members
    //
    //
    // Constructors/Initializers
    //

    public RarefactionOrbitGeomFactory(RarefactionOrbitCalc calc) {
        super(calc);
    }

    //
    // Accessors/Mutators
    //
    //
    // Methods
    //
    protected RpGeometry createGeomFromSource() {

        RarefactionOrbit orbit = (RarefactionOrbit) geomSource();

        return new RarefactionGeom(MultidAdapter.converseOrbitPointsToCoordsArray(orbit.getPoints()), this);

    }

    public String toXML() {
        StringBuffer str = new StringBuffer();
        RarefactionOrbit orbit = (RarefactionOrbit) geomSource();
        RealVector firstPoint = (RealVector)orbit.firstPoint().getCoords();
        RealVector coords = new RealVector(firstPoint.getSize());
        for (int i = 0; i < coords.getSize(); i++) {
            coords.setElement(i, firstPoint.getElement(i));
        }


        String direction = "forward\"";
        str.append("<COMMAND name=\"rarefaction");
        System.out.println("Direcao: "+((RarefactionOrbitCalc) rpCalc()).tDirection());

        if (((RarefactionOrbitCalc) rpCalc()).tDirection() == OrbitGeom.BACKWARD_DIR) {
            direction = "backward\"";

        }

        if (((RarefactionOrbitCalc) rpCalc()).tDirection() == OrbitGeom.BOTH_DIR) {
            direction = "both\"";
        }

        str.append(direction);
        str.append(" inputpoint=\""+coords.toString()+"\">\n");
        str.append(((RarefactionOrbit) geomSource()).toXML(false));//TODO Save with calculations
        str.append("</COMMAND>\n");
        return str.toString();
    }

    public String toMatlab(int curveIndex) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
