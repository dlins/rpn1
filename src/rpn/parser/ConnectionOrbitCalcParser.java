package rpn.parser;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import rpnumerics.ConnectionOrbit;
import rpnumerics.ConnectionOrbitCalc;
import rpn.component.ProfileGeom;
import rpn.component.ProfileGeomFactory;
import rpn.component.MultidAdapter;
import rpn.controller.phasespace.ProfileReadyImpl;
import rpn.controller.phasespace.ProfileSetupReadyImpl;
import rpn.usecase.FindProfileAgent;
import rpnumerics.RPNUMERICS;




public class ConnectionOrbitCalcParser implements ActionListener {



    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("endConnectionOrbitCalc")) {
            try {

                if (RPnDataModule.InputHandler.calcReady_) {
                    RPnDataModule.PHASESPACE.changeState(new
                            ProfileSetupReadyImpl(HugoniotParser.tempHugoniot,
                                                  RPnDataModule.InputHandler.
                                                  xZeroGeom_,
                                                  PoincareParser.tempPoincareSection,
                                                  ManifoldParser.manifoldGeomA,
                                                  ManifoldParser.manifoldGeomB));
                    ConnectionOrbitCalc connectionCalc = RPNUMERICS.createConnectionOrbitCalc(ManifoldParser.manifoldOrbitA, ManifoldParser.manifoldOrbitB);//new
//                            ConnectionOrbitCalc(
//                                    ManifoldParser.manifoldOrbitA,
//                                    ManifoldParser.manifoldOrbitB);
                    ProfileGeomFactory factory = new ProfileGeomFactory(
                            connectionCalc);
                    ConnectionOrbit cOrbit = new ConnectionOrbit(
                            StationaryPointParser.uMinus,
                            StationaryPointParser.uPlus,
                            RPnDataModule.ORBIT);
                    ProfileGeom profile = new ProfileGeom(MultidAdapter.
                            converseOrbitToCoordsArray(cOrbit.
                            orbit()), factory);
                    RPnDataModule.PHASESPACE.join(profile);
                    RPnDataModule.PHASESPACE.changeState(new
                            ProfileReadyImpl(
                                    HugoniotParser.tempHugoniot,
                                    RPnDataModule.InputHandler.
                                    xZeroGeom_,
                                   PoincareParser.tempPoincareSection,
                                    ManifoldParser.manifoldGeomA,
                                    ManifoldParser.manifoldGeomB, profile));

                } else {
                    FindProfileAgent.instance().actionPerformed(null);
                }
            } catch (Exception ex) {
                ex.printStackTrace();

            }
        }
    }
}

