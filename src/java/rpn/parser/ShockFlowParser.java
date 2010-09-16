package rpn.parser;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import rpn.controller.ui.UIController;
import rpn.controller.ui.GEOM_SELECTION;
import rpn.usecase.HugoniotPlotAgent;
import rpn.controller.ui.UI_ACTION_SELECTED;
import rpn.usecase.XZeroPlotAgent;



public class ShockFlowParser implements ActionListener {
    public void actionPerformed(ActionEvent e) {

        if (e.getActionCommand().equals("endShockFlowData")) {

            if (!RPnDataModule.InputHandler.calcReady_) {
                UIController.instance().setState(new
                                                 UI_ACTION_SELECTED(
                        XZeroPlotAgent.
                        instance()));

                UIController.instance().userInputComplete(
                        RPnDataModule.XZERO.getCoords());
//                InputHandler.xZero_.
//                        getCoords());
//                // plots hugoniot
                UIController.instance().setState(new
                                                 UI_ACTION_SELECTED(
                        HugoniotPlotAgent.instance()));
                 UIController.instance().userInputComplete(
                        RPnDataModule.XZERO.getCoords());
//                UIController.instance().userInputComplete(
//                        RPnDataModule.InputHandler.xZero_.
//                        getCoords());

                UIController.instance().setState(new
                                                 GEOM_SELECTION());
            }
        }

    }
}

