/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.controller;

import java.beans.PropertyChangeEvent;
import java.util.Iterator;
import rpn.RPnPhaseSpaceFrame;
import rpn.RPnUIFrame;
import rpn.component.LevelCurveGeomFactory;
import rpn.component.OrbitGeomFactory;
import rpn.component.OrbitGeomView;
import rpn.component.RpGeomFactory;
import rpn.parser.RPnDataModule;
import rpn.usecase.ChangeFluxParamsAgent;
import rpn.usecase.ChangeOrbitLevel;
import rpn.usecase.DragPlotAgent;
import rpnumerics.OrbitCalc;
import rpnumerics.PointLevelCalc;
import rpnumerics.RpCalculation;
import wave.multid.view.GeomObjView;
import wave.util.RealVector;

public class LevelCurveController extends RpCalcController {
    //
    // Members
    //

    LevelCurveGeomFactory factory_;

    @Override
    public void install(RpGeomFactory geom) {
        factory_ = (LevelCurveGeomFactory) geom;
        super.install(geom);

    }

    @Override
    public void uninstall(RpGeomFactory geom) {
        super.uninstall(geom);

    }

    @Override
    protected void register() {
        DragPlotAgent.instance().addPropertyChangeListener(this);
        ChangeFluxParamsAgent.instance().addPropertyChangeListener(this);



    }

    @Override
    protected void unregister() {
        DragPlotAgent.instance().removePropertyChangeListener(this);
        ChangeFluxParamsAgent.instance().removePropertyChangeListener(this);
        factory_ = null;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {


        RpCalculation calc = factory_.rpCalc();
        if (evt.getSource() instanceof DragPlotAgent && calc instanceof PointLevelCalc) {
            ((PointLevelCalc) factory_.rpCalc()).setStartPoint((RealVector) evt.getNewValue());
            factory_.updateGeom();
            return;
        }

        super.propertyChange(evt);


    }
}
