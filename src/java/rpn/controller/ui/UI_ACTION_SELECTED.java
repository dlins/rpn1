/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.controller.ui;

import rpn.command.RpCommand;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import wave.util.RealVector;
import rpn.command.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import rpn.RPnPhaseSpacePanel;

public class UI_ACTION_SELECTED implements UserInputHandler {
    //
    // Members
    //

    private RpModelActionCommand actionSelected_;
    private List userInputList_;

    public UI_ACTION_SELECTED(RpModelActionCommand action) {

        actionSelected_ = action;
        userInputList_ = new ArrayList();

        if (action instanceof RpModelPlotCommand) {
            Iterator<RPnPhaseSpacePanel> iterator = UIController.instance().getInstalledPanelsIterator();
            while (iterator.hasNext()) {
                RPnPhaseSpacePanel panel = iterator.next();
              
                
                MouseMotionListener[] mouseMotionArray = (MouseMotionListener[]) panel.getListeners(MouseMotionListener.class);
                MouseListener[] mouseListenerArray = (MouseListener[]) panel.getListeners(MouseListener.class);

                for (MouseListener mouseListener : mouseListenerArray) {

                    if (mouseListener instanceof RPn2DMouseController) {
                        panel.removeMouseListener(mouseListener);

                    }
                }

                for (MouseMotionListener mouseMotionListener : mouseMotionArray) {

                    if (mouseMotionListener instanceof RPn2DMouseController) {
                        panel.removeMouseMotionListener(mouseMotionListener);
                    }

                }

            }

        }



    }

    public RealVector[] userInputList(rpn.controller.ui.UIController ui) {

        return UIController.inputConvertion(userInputList_);
    }

    public void userInputComplete(rpn.controller.ui.UIController ui,
            RealVector userInput) {

        userInputList_.add(new RealVector(userInput));

        if (actionSelected_ instanceof PoincareSectionPlotCommand) {
            if (isPoincareInputReady()) {
                ArrayList<String> tempInputList = new ArrayList<String>();
                for (RealVector inputElement : userInputList(ui)) {
                    tempInputList.add(inputElement.toString());
                }

                UIController.instance().logCommand(new RpCommand(this, tempInputList));

                //************************ acrescentei para testar (Leandro)
                UIController.instance().setWaitCursor();
                actionSelected_.execute();
                UIController.instance().resetCursor();
                userInputList_.clear();
                ui.panelsBufferClear();
                rpn.parser.RPnDataModule.PHASESPACE.unselectAll();
                //************************************************

            }
        } else if (UIController.instance().getState() instanceof AREASELECTION_CONFIG) {
            UIController.instance().logCommand(new RpCommand(this, userInput));
            UIController.instance().setWaitCursor();
            actionSelected_.execute();
            UIController.instance().resetCursor();
            userInputList_.clear();
            ui.panelsBufferClear();
            rpn.parser.RPnDataModule.PHASESPACE.unselectAll();
        } else {
            UIController.instance().logCommand(new RpCommand(this, userInput));
            UIController.instance().setWaitCursor();
            actionSelected_.execute();
            UIController.instance().resetCursor();
            userInputList_.clear();
            ui.panelsBufferClear();
            rpn.parser.RPnDataModule.PHASESPACE.unselectAll();
        }


    }

    public void userInputComplete(UIController ui) {

        UIController.instance().logCommand(new RpCommand(this));
        UIController.instance().setWaitCursor();
        actionSelected_.execute();
        UIController.instance().resetCursor();
        userInputList_.clear();
        ui.panelsBufferClear();
        rpn.parser.RPnDataModule.PHASESPACE.unselectAll();

    }

    protected boolean isPoincareInputReady() {

        if (userInputList_.size() == rpnumerics.RPNUMERICS.domainDim()) {
            System.out.println("Return true in isPoincareInputReady() !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
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

    public RpModelActionCommand getAction() {
        return actionSelected_;
    }

    public int actionDimension() {


        return rpnumerics.RPNUMERICS.domainDim();
    }
}
