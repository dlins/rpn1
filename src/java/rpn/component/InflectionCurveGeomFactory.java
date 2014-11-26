/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import rpnumerics.Area;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import rpnumerics.InflectionCurve;
import rpnumerics.InflectionCurveCalc;
import rpnumerics.RpException;

public class InflectionCurveGeomFactory extends BifurcationCurveGeomFactory {

    
    public InflectionCurveGeomFactory(InflectionCurveCalc calc) {
        super(calc);
    }

    public InflectionCurveGeomFactory(InflectionCurveCalc calc, InflectionCurve curve) {
        super(calc, curve);
    }

    //
    // Methods
    //

    
    @Override
    void updateGeomSource (List<Area> areaListToRefine){

            try {
                System.out.println("Entrando em inflection");
                InflectionCurve newBifurcation = (InflectionCurve) calc_.recalc(areaListToRefine);

                InflectionCurve oldBifurcationCurve = (InflectionCurve) geomSource_;
                oldBifurcationCurve.leftSegments().addAll(newBifurcation.leftSegments());

                geomSource_ = new InflectionCurve(oldBifurcationCurve.leftSegments());

            } catch (RpException ex) {
                Logger.getLogger(BifurcationCurveGeomFactory.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
    

}
