/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.command;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.JButton;
import rpn.RPnPhaseSpacePanel;
import rpn.component.*;
import rpn.controller.phasespace.riemannprofile.RiemannProfileWaitState;
import rpn.controller.ui.UIController;
import rpnumerics.*;
import wave.util.RealVector;

public class RiemannResetCommand extends RpModelPlotCommand  {
    //
    // Constants
    //

    static public final String DESC_TEXT = "Riemann Profile Reset";
    //
    // Members
    //
    static private RiemannResetCommand instance_ = null;
    private WaveCurve waveCurveForward_;
    private WaveCurve waveCurveBackward_;
    private List<RpGeometry> selectedCurves;

    //
    // Constructors/Initializers
    //
    protected RiemannResetCommand() {
        super(DESC_TEXT, rpn.configuration.RPnConfig.HUGONIOT, new JButton());
    }

  

    public RpGeometry createRpGeometry(RealVector[] input) {
        return null;
    }

    static public RiemannResetCommand instance() {
        if (instance_ == null) {
            instance_ = new RiemannResetCommand();
        }
        return instance_;
    }
    
    
    @Override
    public void actionPerformed(ActionEvent event){
        
        execute();
        
    }


    @Override
    public void execute() {
        
        RiemannProfileCommand.instance().changeState(new RiemannProfileWaitState());
        RiemannProfileCommand.instance().setEnabled(false);
        Iterator geomObjIterator = UIController.instance().getActivePhaseSpace().getGeomObjIterator();
        
        ArrayList<WaveCurveGeom> waveCurvesToRemove = new ArrayList<WaveCurveGeom>();
        
        while (geomObjIterator.hasNext()) {
            RpGeometry geometry = (RpGeometry) geomObjIterator.next();
            
            if( geometry instanceof WaveCurveGeom){
                waveCurvesToRemove.add((WaveCurveGeom)geometry);
            }
        }
        
        
        for (WaveCurveGeom waveCurveGeom : waveCurvesToRemove) {
            UIController.instance().getActivePhaseSpace().remove(waveCurveGeom);            
        }
        
        Iterator<RPnPhaseSpacePanel> installedPanelsIterator = UIController.instance().getInstalledPanelsIterator();
        
        while (installedPanelsIterator.hasNext()) {
            RPnPhaseSpacePanel rPnPhaseSpacePanel = installedPanelsIterator.next();
            
            rPnPhaseSpacePanel.clearAreaSelection();
            rPnPhaseSpacePanel.clearPointSelection();
            rPnPhaseSpacePanel.getCastedUI().pointMarkBuffer().clear();
            rPnPhaseSpacePanel.repaint();
            
        }
        
        
        UIController.instance().getSelectedGeometriesList().clear();
        UIController.instance().globalInputTable().reset();
        
        UIController.instance().getActivePhaseSpace().updateCurveSelection();
        
        
        
        
        
       
    }

   
}
