/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.controller;

import rpn.component.RpGeomFactory;
import rpn.component.XZeroGeomFactory;
import rpn.usecase.*;
import rpnumerics.StationaryPointCalc;
import rpnumerics.PhasePoint;
import java.beans.PropertyChangeEvent;
import rpnumerics.ShockFlow;

public class XZeroController
        extends RpCalcController {
    //
    // Members
    //
    private XZeroGeomFactory geomFactory_;

    //
    // Constructors
    //
    //
    // Accessors/Mutators
    //
    //
    // Methods
    //
    protected void register() {
        ChangeFluxParamsAgent.instance().addPropertyChangeListener(this);
        ChangeDirectionAgent.instance().addPropertyChangeListener(this);
    }

    protected void unregister() {
        ChangeFluxParamsAgent.instance().removePropertyChangeListener(this);
        ChangeDirectionAgent.instance().removePropertyChangeListener(this);
    }

    public void install(RpGeomFactory geom) {
        super.install(geom);
        geomFactory_ = (XZeroGeomFactory) geom;
    }

    public void uninstall(RpGeomFactory geom) {
        super.uninstall(geom);
        geomFactory_ = null;
    }

    @Override
    public void propertyChange(PropertyChangeEvent change) {

        // this is to avoid void notifications of enabled/disbled
        if (change.getPropertyName().compareTo("enabled") != 0) {

            if (change.getSource() instanceof ChangeDirectionAgent) {

                // UPDATES THE CALC INIT POINT
                ((StationaryPointCalc) geomFactory_.rpCalc()).setInitPoint((PhasePoint) change.getNewValue());
            }

            /*
             * WE ARE ASSUMING THAT THIS CONTROLLER IS THE FIRST
             * TO BE NOTIFIED !!! THIS WILL FORCE THE XZERO TERMS UPDATE TOO !
             * IT HSA TO GET FROM STATIONARY POINT CALC
             */

            if (((StationaryPointCalc) geomFactory_.rpCalc()).getFlow() instanceof ShockFlow) {
                ((StationaryPointCalc) geomFactory_.rpCalc()).getFlow().setXZero(((StationaryPointCalc) geomFactory_.rpCalc()).getInitPoint());

            }
//            if (RPNUMERICS.flow() instanceof ConservationShockFlow) {


//            ((ConservationShockFlow) RPNUMERICS.flow()).setXZero(((StationaryPointCalc) geomFactory_.rpCalc()).getInitPoint());
        


        // UPDATES EIGENS
        super.propertyChange(change);
    }
    }

}
