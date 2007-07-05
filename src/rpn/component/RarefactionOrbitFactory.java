package rpn.component;

import rpnumerics.Orbit;
import rpnumerics.OrbitCalc;


public class RarefactionOrbitFactory extends OrbitGeomFactory {
    public RarefactionOrbitFactory(OrbitCalc calc) {
        super(calc);
    }

    /**
     * createGeomFromSource
     *
     * @return RpGeometry
     * @todo Implement this rpn.component.RpCalcBasedGeomFactory method
     */
    protected RpGeometry createGeomFromSource() {

        Orbit orbit = (Orbit)geomSource();

        return new RarefactionGeom(MultidAdapter.converseOrbitToCoordsArray(orbit),this);
    }

    /**
     *
     * @return String
     * @todo Implement this rpn.component.RpGeomFactory method
     */
    public String toXML() {
        return "";
    }
}
