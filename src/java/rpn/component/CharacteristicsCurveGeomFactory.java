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
import wave.multid.view.ViewingAttr;

public class CharacteristicsCurveGeomFactory extends RpCalcBasedGeomFactory {

    private static Color COLOR = new Color(20, 43, 140);

    public CharacteristicsCurveGeomFactory(RpCalculation calc, RpSolution solution) {
        super(calc, solution);
    }

    public RpGeometry getFamilyGeometry(int index) {

        List<PhasePoint[]> phasePointList = ((CharacteristicsCurve) geomSource()).getFamilyPoints(index);
        CharacteristicCurveGeom familyGeom = new CharacteristicCurveGeom(phasePointList, this, selectViewingAttr(index));

        return familyGeom;

    }

    public ViewingAttr selectViewingAttr(int familyIndex) {
        ViewingAttr viewingAttr = new ViewingAttr(COLOR);

        if (familyIndex == 0) {
            viewingAttr.setColor(Color.blue);
        }
        if (familyIndex == 1) {
            viewingAttr.setColor(Color.red);
        }
        return viewingAttr;
    }

    @Override
    public RpGeometry createGeomFromSource() {

        return null;

    }
}
