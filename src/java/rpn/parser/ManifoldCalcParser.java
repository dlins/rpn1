package rpn.parser;

import rpn.component.ManifoldGeom;
import rpnumerics.ManifoldOrbit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import rpn.controller.ui.UIController;
import rpn.controller.ui.UI_ACTION_SELECTED;
import rpn.usecase.ForwardManifoldPlotAgent;
import rpn.usecase.BackwardManifoldPlotAgent;
import rpn.controller.ui.GEOM_SELECTION;
import rpn.controller.phasespace.ProfileSetupReadyImpl;


public class ManifoldCalcParser implements ActionListener{



    public void actionPerformed(ActionEvent e){

        if (e.getActionCommand().equals("startManifoldCalc")){


        }

        if (e.getActionCommand().equals("endManifoldCalc")){
            if (!RPnDataModule.InputHandler.calcReady_){
                if (OrbitParser.dir == 1) {
                    UIController.instance().setState(new UI_ACTION_SELECTED(
                            ForwardManifoldPlotAgent.instance()));
                }
                else {
                    UIController.instance().setState(
                            new UI_ACTION_SELECTED(BackwardManifoldPlotAgent.instance()));
                }

                UIController.instance().userInputComplete(RPnDataModule.InputHandler.tempVector_);
                UIController.instance().setState(new GEOM_SELECTION());

            }

            else {

                if (ManifoldParser.manifoldNumber==2){

                    RPnDataModule.PHASESPACE.changeState(new ProfileSetupReadyImpl(HugoniotParser.tempHugoniot,
                            RPnDataModule.InputHandler.xZeroGeom_,
                            PoincareParser.tempPoincareSection,
                            ManifoldParser.manifoldGeomA,
                            ManifoldParser.manifoldGeomB, false));

                }
            }


            UIController.instance().setState(new GEOM_SELECTION());
        }
    }


}
