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

public class DoubleContactGeomFactory extends BifurcationCurveGeomFactory {

    public DoubleContactGeomFactory(DoubleContactCurveCalc calc, DoubleContactCurve curve) {
        super(calc, curve);
        
    }

    public DoubleContactGeomFactory(DoubleContactCurveCalc calc) {
        super(calc);
    }

    @Override
    protected ViewingAttr leftViewingAttr(){
        return new ViewingAttr(Color.yellow);
    }


    @Override
    protected ViewingAttr rightViewingAttr() {
        return new ViewingAttr(Color.magenta);
    }
    
    
    @Override
    void updateGeomSource (List<Area> areaListToRefine){

            try {
                BifurcationCurve newBifurcation = (BifurcationCurve) calc_.recalc(areaListToRefine);
                BifurcationCurve oldBifurcationCurve = (BifurcationCurve) geomSource_;
                oldBifurcationCurve.leftSegments().addAll(newBifurcation.leftSegments());
                oldBifurcationCurve.rightSegments().addAll(newBifurcation.rightSegments());

                geomSource_ = new DoubleContactCurve(oldBifurcationCurve.leftSegments(), oldBifurcationCurve.rightSegments());


            } catch (RpException ex) {
                ex.printStackTrace();
            }
            

    }

}
