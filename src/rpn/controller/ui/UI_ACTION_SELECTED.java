/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.controller.ui;

import wave.util.RealVector;
import rpn.usecase.*;
import java.util.ArrayList;
import java.util.List;

public class UI_ACTION_SELECTED implements UserInputHandler {
    //
    // Members
    //

    private RpModelActionAgent actionSelected_;
    private List userInputList_;

    public UI_ACTION_SELECTED(RpModelActionAgent action) {
        actionSelected_ = action;
        userInputList_ = new ArrayList();
    }

    public RealVector[] userInputList(rpn.controller.ui.UIController ui) {
        return UIController.inputConvertion(userInputList_);
    }

    public void userInputComplete(rpn.controller.ui.UIController ui,
            RealVector userInput) {
        userInputList_.add(new RealVector(userInput));

        UIController.instance().addCommand(userInput);

        if (!(actionSelected_ instanceof ChangeDirectionAgent)) {



            if (actionSelected_ instanceof PoincareSectionPlotAgent || actionSelected_ instanceof AreaSelectionAgent) {

                if (actionSelected_ instanceof AreaSelectionAgent) {
                    if (isDiagonalSelection()){
                          //		        rpn.RPnUIFrame.instance().setTitle(" completing ...  " +
                        //		actionSelected_.getValue(javax.swing.Action.SHORT_DESCRIPTION).toString());
                        UIController.instance().setWaitCursor();
                        actionSelected_.execute();
                        //rpn.RPnUIFrame.instance().setTitle("");
                        UIController.instance().resetCursor();
                        userInputList_.clear();
                        ui.panelsBufferClear();
                        rpn.parser.RPnDataModule.PHASESPACE.unselectAll();
                    }

                }

                if (actionSelected_ instanceof PoincareSectionPlotAgent) {

                    if (isPoincareInputReady()) {
                        //		        rpn.RPnUIFrame.instance().setTitle(" completing ...  " +
                        //		actionSelected_.getValue(javax.swing.Action.SHORT_DESCRIPTION).toString());
                        UIController.instance().setWaitCursor();
                        actionSelected_.execute();
                        //rpn.RPnUIFrame.instance().setTitle("");
                        UIController.instance().resetCursor();
                        userInputList_.clear();
                        ui.panelsBufferClear();
                        rpn.parser.RPnDataModule.PHASESPACE.unselectAll();
                    }



                }



            } else {

                //rpn.RPnUIFrame.instance().setTitle(" completing ...  " +
                //	    actionSelected_.getValue(javax.swing.Action.SHORT_DESCRIPTION).toString());
                UIController.instance().setWaitCursor();
                actionSelected_.execute();

                //      rpn.RPnUIFrame.instance().setTitle("");
                UIController.instance().resetCursor();
                userInputList_.clear();
                ui.panelsBufferClear();
                rpn.parser.RPnDataModule.PHASESPACE.unselectAll();
            }
        }
    }

    protected boolean isPoincareInputReady() {
        if (userInputList_.size() == rpnumerics.RPNUMERICS.domainDim()) {
            return true;
        }
        return false;
    }

    protected boolean isDiagonalSelection() {
        if (userInputList_.size() == 2) {
            return true;
        }
        return false;
    }

    public RpModelActionAgent getAction() {
        return actionSelected_;
    }
}
