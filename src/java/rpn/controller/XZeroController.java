/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.controller;

import rpn.component.RpGeomFactory;
import rpn.component.XZeroGeomFactory;
import rpn.command.*;
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
    @Override
    protected void register() {
        ChangeFluxParamsCommand.instance().addPropertyChangeListener(this);
        ChangeDirectionCommand.instance().addPropertyChangeListener(this);
        ChangeSigmaCommand.instance().addPropertyChangeListener(this);
        ChangeXZeroCommand.instance().addPropertyChangeListener(this);

    }

    @Override
    protected void unregister() {
        ChangeFluxParamsCommand.instance().removePropertyChangeListener(this);
        ChangeDirectionCommand.instance().removePropertyChangeListener(this);
        ChangeSigmaCommand.instance().removePropertyChangeListener(this);
        ChangeXZeroCommand.instance().removePropertyChangeListener(this);

    }

    @Override
    public void install(RpGeomFactory geom) {
        super.install(geom);
        geomFactory_ = (XZeroGeomFactory) geom;
    }

    @Override
    public void uninstall(RpGeomFactory geom) {
        super.uninstall(geom);
        geomFactory_ = null;
    }


    @Override
    public void propertyChange(PropertyChangeEvent change) {

        // this is to avoid void notifications of enabled/disbled
        if (change.getPropertyName().compareTo("enabled") != 0) {

            System.out.println("Atualizando xzero");

                // UPDATES THE CALC INIT POINT
                ((StationaryPointCalc) geomFactory_.rpCalc()).setInitPoint(rpnumerics.RPNUMERICS.getViscousProfileData().getXZero());

                ((StationaryPointCalc) geomFactory_.rpCalc()).setReferencePoint_(rpnumerics.RPNUMERICS.getViscousProfileData().getXZero());
            

            
            // UPDATES EIGENS
            super.propertyChange(change);
        }
    }


}
