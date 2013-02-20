/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import java.awt.Color;
import java.util.List;
import rpnumerics.*;
import wave.multid.view.ViewingAttr;

public class SecondaryBifurcationGeomFactory extends BifurcationCurveGeomFactory {

    public SecondaryBifurcationGeomFactory(SecondaryBifurcationCurveCalc calc, SecondaryBifurcationCurve curve) {
        super(calc, curve);

    }

    @Override
    void updateGeomSource(List<Area> areaListToRefine) {

        try {
            BifurcationCurve newBifurcation = (BifurcationCurve) calc_.recalc(areaListToRefine);
            BifurcationCurve oldBifurcationCurve = (BifurcationCurve) geomSource_;
            oldBifurcationCurve.leftSegments().addAll(newBifurcation.leftSegments());
            oldBifurcationCurve.rightSegments().addAll(newBifurcation.rightSegments());

            geomSource_ = new SecondaryBifurcationCurve(oldBifurcationCurve.leftSegments(), oldBifurcationCurve.rightSegments());


        } catch (RpException ex) {
            ex.printStackTrace();
        }


    }

    public SecondaryBifurcationGeomFactory(SecondaryBifurcationCurveCalc calc) {
        super(calc);
    }

    @Override
    protected ViewingAttr leftViewingAttr() {
        return new ViewingAttr(Color.yellow);
    }

    @Override
    protected ViewingAttr rightViewingAttr() {
        return new ViewingAttr(Color.magenta);
    }
}
