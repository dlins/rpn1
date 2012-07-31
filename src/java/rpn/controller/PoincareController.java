/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.controller;

import rpn.component.PoincareSectionGeomFactory;
import rpn.component.RpGeomFactory;
import rpn.controller.phasespace.*;
import rpn.usecase.*;
import rpn.parser.RPnDataModule;
import rpnumerics.ConnectionOrbit;
import wave.util.SimplexPoincareSection;
import wave.util.RealVector;
import java.beans.PropertyChangeEvent;

public class PoincareController implements RpController {

    private PoincareSectionGeomFactory geomFactory_;

    public PoincareController() {
        ChangeFluxParamsAgent.instance().addPropertyChangeListener(this);
    }

    public void propertyChange(PropertyChangeEvent change) {
        if (RPnDataModule.PHASESPACE.state() instanceof PROFILE_READY) {
            PROFILE_READY profileHolder = (PROFILE_READY) RPnDataModule.PHASESPACE.state();
            ConnectionOrbit connOrbit = (ConnectionOrbit) profileHolder.connectionGeom().geomFactory().geomSource();
            RealVector center = connOrbit.orbitCenter();
//            RealVector normal = rpnumerics.RPNUMERICS.flow().flux(center);
//            ShockFlow flow = (ShockFlow) RPNUMERICS.createShockFlow();
            
//            RealVector normal = flow.flux(center);
//            RealVector normal = flow.flux(center);//rpnumerics.RPNUMERICS.flow().flux(center);

//            ((SimplexPoincareSection) geomFactory_.geomSource()).shift(center, normal);

            geomFactory_.updateGeom();

            // updates the numerics layers
            ((wave.ode.Rk4BPProfile) rpnumerics.RPNUMERICS.odeSolver().getProfile()).setPoincareSection((SimplexPoincareSection) geomFactory_.geomSource());
        }
    }

    public void install(RpGeomFactory geom) {
        geomFactory_ = (PoincareSectionGeomFactory) geom;
        ((wave.ode.Rk4BPProfile) rpnumerics.RPNUMERICS.odeSolver().getProfile()).setPoincareSection((SimplexPoincareSection) geomFactory_.geomSource());  //ISSO NAO ATRAPALHAVA O PLOT
    }

    public void uninstall(RpGeomFactory geom) {
        if (geomFactory_ == geom) {
            geomFactory_ = null;
            ((wave.ode.Rk4BPProfile) rpnumerics.RPNUMERICS.odeSolver().getProfile()).setPoincareSectionFlag(false);
            ChangeFluxParamsAgent.instance().removePropertyChangeListener(this);
        }
    }
}
