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

        if (actionSelected_ instanceof PoincareSectionPlotAgent) {
            if (isPoincareInputReady()) {
                ArrayList<RealVector> tempInputList = new ArrayList<RealVector>();
                for (RealVector inputElement : userInputList(ui)) {
                    tempInputList.add(inputElement);
                }

                UIController.instance().addCommand(new Command(this, tempInputList));
            }
        }
        else if (UIController.instance().getState() instanceof AREASELECTION_CONFIG) {
            UIController.instance().addCommand(new Command(this, userInput));
            UIController.instance().setWaitCursor();
            actionSelected_.execute();
            UIController.instance().resetCursor();
            userInputList_.clear();
            ui.panelsBufferClear();
            rpn.parser.RPnDataModule.PHASESPACE.unselectAll();
        }
        else {
            UIController.instance().addCommand(new Command(this, userInput));
            UIController.instance().setWaitCursor();
            actionSelected_.execute();
            UIController.instance().resetCursor();
            userInputList_.clear();
            ui.panelsBufferClear();
            rpn.parser.RPnDataModule.PHASESPACE.unselectAll();
        }



    }

    public void userInputComplete(UIController ui) {
    

        UIController.instance().addCommand(new Command(this));
        UIController.instance().setWaitCursor();
        actionSelected_.execute();
        UIController.instance().resetCursor();
        userInputList_.clear();
        ui.panelsBufferClear();
        rpn.parser.RPnDataModule.PHASESPACE.unselectAll();

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
        //System.out.println("UI_ACTION_SELECTED : getAction()");
        return actionSelected_;
    }

    

    public int actionDimension() {
        

        return rpnumerics.RPNUMERICS.domainDim();
    }
}
