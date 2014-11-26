/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import rpn.command.CurveRemoveCommand;
import rpn.command.RpCommand;
import rpn.component.RpGeomFactory;
import rpn.component.RpGeometry;
import rpn.configuration.CommandConfiguration;
import rpn.configuration.Configuration;
import rpn.controller.ui.UndoActionController;
import rpnumerics.RPnCurve;
import wave.multid.model.MultiGeometry;

public class RPnPhaseSpaceManager {
    
    private HashMap<RPnPhaseSpaceAbstraction, ArrayList<RPnPhaseSpaceAbstraction>> phaseSpaceMap_;
    private static RPnPhaseSpaceManager instance_;
    
    private RPnPhaseSpaceManager() {
        phaseSpaceMap_ = new HashMap<RPnPhaseSpaceAbstraction, ArrayList<RPnPhaseSpaceAbstraction>>();
    }
    
    public void register(RPnPhaseSpaceAbstraction phaseSpace, ArrayList<RPnPhaseSpaceAbstraction> pointedPhaseSpaces) {
        
        phaseSpaceMap_.put(phaseSpace, pointedPhaseSpaces);
        
    }
    
    public void unregister(RPnPhaseSpaceAbstraction phaseSpace) {
        phaseSpaceMap_.remove(phaseSpace);
    }
    
    private void logRemoveCommand(RPnPhaseSpaceAbstraction phaseSpace, MultiGeometry bifurcationGeom) {
        RpGeometry geometry = (RpGeometry) bifurcationGeom;
        
        RpGeomFactory factory = (RpGeomFactory) geometry.geomFactory();
        
        RPnCurve curve = (RPnCurve) factory.geomSource();
        
        Configuration eraseConfiguration = new CommandConfiguration("erase");
        
        eraseConfiguration.setParamValue("curveid", String.valueOf(curve.getId()));
        eraseConfiguration.setParamValue("phasespace", phaseSpace.getName());
        
        System.out.println(eraseConfiguration.toXML());
        
        PropertyChangeEvent event = new PropertyChangeEvent(CurveRemoveCommand.instance(), null, null, eraseConfiguration);
        UndoActionController.instance().addAction(new RpCommand(event));

    }
    
    public void remove(RPnPhaseSpaceAbstraction phaseSpace, MultiGeometry bifurcationGeom) {
        logRemoveCommand(phaseSpace, bifurcationGeom);
        
        
        phaseSpace.remove(bifurcationGeom);
        
        if (phaseSpaceMap_.containsKey(phaseSpace)) {
            ArrayList<RPnPhaseSpaceAbstraction> pointedPhaseSpaceAbstractions = phaseSpaceMap_.get(phaseSpace);
            for (RPnPhaseSpaceAbstraction pointedPhaseSpace : pointedPhaseSpaceAbstractions) {
                
                Iterator iteratorList = pointedPhaseSpace.curvesListIterator();
                
                while (iteratorList.hasNext()) {
                    RPnCurvesList list = (RPnCurvesList) iteratorList.next();
                    list.removeGeometrySide(bifurcationGeom);
                    
                }
            }
        }
        
        
    }
    
    public static RPnPhaseSpaceManager instance() {
        
        if (instance_ == null) {
            instance_ = new RPnPhaseSpaceManager();
        }
        return instance_;
        
        
    }
}
