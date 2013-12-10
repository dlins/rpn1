/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.command;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Iterator;
import rpn.RPnPhaseSpaceAbstraction;
import rpn.RPnPhaseSpaceManager;
import rpn.RPnPhaseSpacePanel;
import rpn.component.RpGeometry;
import rpn.controller.ui.*;
import rpn.message.RPnNetworkStatus;

public class ClearPhaseSpaceCommand extends javax.swing.AbstractAction {
    //
    // Constants
    //

    static public final String DESC_TEXT = "Clears the Phase Space";
    //
    // Members
    //
    static private ClearPhaseSpaceCommand instance_ = null;

    //
    // Constructors
    //
    protected ClearPhaseSpaceCommand() {
        super(DESC_TEXT, null);
    }

    public void clear() {

        // rpn.RPnUIFrame.instance().setTitle(" completing ...  " + DESC_TEXT);
        UIController.instance().setWaitCursor();
        UIController.instance().panelsBufferClear();
//        rpn.parser.RPnDataModule.PHASESPACE.clear();

        


        Iterator<RPnPhaseSpaceAbstraction> phaseSpaceIterator = rpn.parser.RPnDataModule.phaseSpaceIterator();

        while (phaseSpaceIterator.hasNext()) {
            
            RPnPhaseSpaceAbstraction phaseSpace = phaseSpaceIterator.next();

            Iterator<RpGeometry> geometryIterator = phaseSpace.getGeomObjIterator();

            ArrayList<RpGeometry> toRemove = new ArrayList<RpGeometry>();

            while (geometryIterator.hasNext()) {
                toRemove.add(geometryIterator.next());
            }

            for (RpGeometry rpGeometry : toRemove) {
                RPnPhaseSpaceManager.instance().remove(phaseSpace, rpGeometry);
            }



        }


        Iterator<RPnPhaseSpacePanel> phaseSpacePanelIterator = UIController.instance().getInstalledPanelsIterator();
        while (phaseSpacePanelIterator.hasNext()) {
            RPnPhaseSpacePanel panel = phaseSpacePanelIterator.next();
            panel.clearAllStrings();
            panel.clearAreaSelection();
            panel.repaint();
            
        }

               
        

        // ClearScene is not undoable
        UndoActionController.instance().setEnabled(false);
        System.gc();
        //rpn.RPnUIFrame.instance().setTitle("");
        UIController.instance().resetCursor();
        
        if (RPnNetworkStatus.instance().isOnline() && RPnNetworkStatus.instance().isMaster()) {
            
            
            RpCommand command = new RpCommand("clear");
            
            RPnNetworkStatus.instance().sendCommand(command.toXML());
        }

    }

    public void actionPerformed(ActionEvent event) {

        

        clear();

    }

    static public ClearPhaseSpaceCommand instance() {
        if (instance_ == null) {
            instance_ = new ClearPhaseSpaceCommand();
        }
        return instance_;
    }
}
