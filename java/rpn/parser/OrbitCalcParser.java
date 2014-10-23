package rpn.parser;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import rpn.controller.ui.UIController;
import rpn.controller.ui.GEOM_SELECTION;

import rpn.command.ForwardOrbitPlotCommand;
import rpn.command.BackwardOrbitPlotCommand;
import rpn.controller.ui.UI_ACTION_SELECTED;


public class OrbitCalcParser implements ActionListener {

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("endOrbitCalc")) {

            if (!RPnDataModule.InputHandler.calcReady_) {
                if (OrbitParser.dir == 1) {
                    UIController.instance().setState(new UI_ACTION_SELECTED(
                            ForwardOrbitPlotCommand.instance()));
                } else {
                    UIController.instance().setState(new UI_ACTION_SELECTED(
                            BackwardOrbitPlotCommand.instance()));
                }
                UIController.instance().userInputComplete(RPnDataModule.
                        InputHandler.tempVector_);
                UIController.instance().setState(new GEOM_SELECTION());
            }

        }

    }

}

