/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.controller;

import rpn.component.RpGeomFactory;
import rpn.command.*;
import java.beans.PropertyChangeEvent;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import rpn.component.DiagramGeom;
import rpn.component.RpDiagramFactory;
import rpn.component.RpGeometry;
import rpn.component.WaveCurveGeomFactory;
import rpn.controller.phasespace.riemannprofile.RiemannProfileReady;
import rpn.controller.phasespace.riemannprofile.RiemannProfileState;
import rpn.parser.RPnDataModule;
import rpnumerics.RpException;
import rpnumerics.WaveCurve;

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



        if (RiemannProfileCommand.instance().getState() instanceof RiemannProfileReady) {
            RiemannProfileReady state = (RiemannProfileReady) RiemannProfileCommand.instance().getState();

            state.updateRiemannProfile();

        }
        
        if(RPnDataModule.SPEEDGRAPHICSPHASESPACE.getLastGeometry()!=null){
            
            RPnDataModule.SPEEDGRAPHICSPHASESPACE.clear();
            WaveCurve waveCurve = (WaveCurve)geomFactory_.geomSource();
            
            RpDiagramFactory factory = new RpDiagramFactory(waveCurve);
            try {
                DiagramGeom diagramGeom = (DiagramGeom) factory.createDiagramFromSource();
                RPnDataModule.SPEEDGRAPHICSPHASESPACE.join(diagramGeom);
                WaveCurveSpeedPlotCommand.instance().updateDiagramView(diagramGeom);
            } catch (RpException ex) {
                Logger.getLogger(RiemannProfileController.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            

           

            
        }
        
        
        

    }
}
