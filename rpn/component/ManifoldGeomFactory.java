/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.component;

import rpn.usecase.ChangeSigmaAgent;
import rpn.controller.RpController;
import rpn.controller.RpCalcController;
import rpn.controller.ProfileController;
import rpnumerics.*;
import wave.multid.model.MultiGeometryImpl;
import wave.multid.model.MultiPolyLine;
import wave.multid.view.*;
import wave.multid.CoordsArray;
import wave.multid.DimMismatchEx;
import java.awt.Color;

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
