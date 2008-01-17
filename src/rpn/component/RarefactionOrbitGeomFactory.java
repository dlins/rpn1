/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.component;

import rpnumerics.Orbit;
import rpnumerics.OrbitCalc;
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

        RarefactionOrbit orbit = (RarefactionOrbit)geomSource();
        
//        Object orbit = geomSource();
//        
//        if (orbit instanceof Orbit ){
//            Orbit orbit2 =(Orbit)geomSource();
//            System.out.println("Eh orbita");
//            return new RarefactionGeom(MultidAdapter.converseOrbitToCoordsArray(orbit2), this);
//            
//        } else
//            System.out.println("Nao eh orbita");
        
        return new RarefactionGeom(MultidAdapter.converseOrbitPointsToCoordsArray(orbit.getPoints()), this);
//        return null;
    }
    
    public String toXML() {
        StringBuffer str = new StringBuffer();
        String tdir = "pos";
        if (((OrbitCalc)rpCalc()).tDirection() == OrbitGeom.BACKWARD_DIR)
            tdir = "neg";
        str.append("<ORBITCALC tdirection=\"" + tdir + "\" calcready=\""+rpn.parser.RPnDataModule.RESULTS+"\">\n");
        if (!rpn.parser.RPnDataModule.RESULTS)
            str.append(((Orbit)geomSource()).getPoints() [0].toXML());
        str.append(((Orbit)geomSource()).toXML(rpn.parser.RPnDataModule.RESULTS));
        str.append("</ORBITCALC>\n");
        return str.toString();
    }
}
