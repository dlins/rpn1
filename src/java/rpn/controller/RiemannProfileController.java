/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.controller;

import rpn.component.RpGeomFactory;
import rpn.command.*;
import java.beans.PropertyChangeEvent;
import rpn.component.WaveCurveGeomFactory;
import rpn.controller.phasespace.riemannprofile.RiemannProfileReady;
import rpn.controller.phasespace.riemannprofile.RiemannProfileState;

public class RiemannProfileController extends RpCalcController {
    //
    // Members
    //

    private WaveCurveGeomFactory geomFactory_;
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
        DragPlotCommand.instance().addPropertyChangeListener(this);
        ChangeFluxParamsCommand.instance().addPropertyChangeListener(this);
        ReferencePointSelectionCommand.instance().addPropertyChangeListener(this);
    }

    @Override
    protected void unregister() {
        DragPlotCommand.instance().removePropertyChangeListener(this);
        ChangeFluxParamsCommand.instance().removePropertyChangeListener(this);
        ReferencePointSelectionCommand.instance().removePropertyChangeListener(this);
    }

    @Override
    public void install(RpGeomFactory geom) {
        super.install(geom);
        geomFactory_ = (WaveCurveGeomFactory) geom;
    }

    @Override
    public void uninstall(RpGeomFactory geom) {
        super.uninstall(geom);
        geomFactory_ = null;
    }

    @Override
    public void propertyChange(PropertyChangeEvent change) {
        
        System.out.println("Chamando property change do perfil de Riemann");

        
        RiemannProfileReady state = (RiemannProfileReady)RiemannProfileCommand.instance().getState();
        
        state.updateRiemannProfile();

        
        


    }
}
