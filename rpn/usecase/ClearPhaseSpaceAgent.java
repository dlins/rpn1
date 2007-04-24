/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.usecase;

import rpn.component.RpGeomFactory;
import rpn.component.RpCalcBasedGeomFactory;
import rpn.component.PoincareSectionGeomFactory;
import rpn.controller.ui.UndoActionController;
import rpn.RPnUIFrame;
import wave.ode.Rk4BPProfile;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import rpn.controller.ui.*;
import rpn.controller.PhaseSpacePanel2DController;
import rpn.message.RPnActionMediator;


public class ClearPhaseSpaceAgent extends javax.swing.AbstractAction {
    //
    // Constants
    //
    static public final String DESC_TEXT = "Clears the Phase Space";
    //
    // Members
    //
    static private ClearPhaseSpaceAgent instance_ = null;

    //
    // Constructors
    //
    protected ClearPhaseSpaceAgent() { super(DESC_TEXT, null); }


    public void clear (){

      // rpn.RPnUIFrame.instance().setTitle(" completing ...  " + DESC_TEXT);
        UIController.instance().setWaitCursor();
        UIController.instance().panelsBufferClear();
        rpn.parser.RPnDataModule.PHASESPACE.clear();
        // ClearScene is not undoable
        UndoActionController.instance().setEnabled(false);
        System.gc();
        //rpn.RPnUIFrame.instance().setTitle("");
        UIController.instance().resetCursor();


    }


    public void actionPerformed(ActionEvent event) {

      if (UIController.instance().getNetStatusHandler().isOnline() && UIController.instance().getNetStatusHandler().isMaster())
          RPnActionMediator.instance().setState(DESC_TEXT);
      clear();

    }

    static public ClearPhaseSpaceAgent instance() {
        if (instance_ == null)
            instance_ = new ClearPhaseSpaceAgent();
        return instance_;
    }
}
