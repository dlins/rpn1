/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.component;

import rpnumerics.ManifoldOrbit;
import rpnumerics.ManifoldOrbitCalc;

public class ManifoldGeomFactory extends RpCalcBasedGeomFactory {
    //
    // Constants
    //
    //
    // Constructors/Initializers
    //
    public ManifoldGeomFactory(ManifoldOrbitCalc calc) {
        super(calc);
    }

    //
    // Methods
    //
    protected RpGeometry createGeomFromSource() {
        ManifoldOrbit orbit = (ManifoldOrbit)geomSource();
        return new ManifoldGeom(MultidAdapter.converseOrbitToCoordsArray(orbit.getOrbit()), this);
    }

    public String toXML() {
        StringBuffer str = new StringBuffer();
        String tdir = "pos";
        if (((ManifoldOrbitCalc)rpCalc()).tDirection() == OrbitGeom.BACKWARD_DIR)
            tdir = "neg";
        str.append("<MANIFOLDCALC tdirection=\"" + tdir + "\" calcready=\""+rpn.parser.RPnDataModule.RESULTS+"\">\n");
        str.append(((ManifoldOrbit)geomSource()).getFirstPoint().toXML()+"\n");
        str.append(((ManifoldOrbit)geomSource()).toXML(rpn.parser.RPnDataModule.RESULTS));
        str.append("</MANIFOLDCALC>\n");
        return str.toString();
    }
}
