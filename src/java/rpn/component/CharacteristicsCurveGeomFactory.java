/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import java.awt.Color;
import java.util.List;
import rpnumerics.CharacteristicsCurve;
import rpnumerics.PhasePoint;
import rpnumerics.RpCalculation;
import rpnumerics.RpSolution;

public class CharacteristicsCurveGeomFactory extends RpCalcBasedGeomFactory {

    public CharacteristicsCurveGeomFactory(RpCalculation calc, RpSolution solution) {
        super(calc, solution);
    }

    public RpGeometry getFamilyGeometry(int index) {

        List<PhasePoint[]> phasePointList = ((CharacteristicsCurve) geomSource()).getFamilyPoints(index);
        CharacteristicCurveGeom familyGeom =  new CharacteristicCurveGeom(phasePointList, this);
        
        if (index ==0)
            familyGeom.viewingAttr().setColor(Color.blue);
        if (index==1)
            familyGeom.viewingAttr().setColor(Color.red);
                return familyGeom;


    }

    @Override
    public RpGeometry createGeomFromSource() {
        
        return getFamilyGeometry(0);

    }

    public String toXML() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String toMatlab(int curveIndex) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
