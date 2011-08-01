/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import rpnumerics.Orbit;
import rpnumerics.RarefactionOrbit;
import rpnumerics.RarefactionOrbitCalc;

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
        String direction = "forward\"/>\n";
        str.append("<COMMAND name=\"rarefaction");
        System.out.println("Direcao: "+((RarefactionOrbitCalc) rpCalc()).tDirection());

        if (((RarefactionOrbitCalc) rpCalc()).tDirection() == OrbitGeom.BACKWARD_DIR) {
            direction = "backward\"/>\n";

        }
        str.append(direction);
        str.append(((RarefactionOrbit) geomSource()).toXML(true));
        str.append("</COMMAND>\n");
        return str.toString();
    }

    public String toMatlab(int curveIndex) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
