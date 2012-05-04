/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.controller.ui;


import rpn.usecase.ChangeSigmaAgent;
import wave.util.RealVector;

public class SIGMA_CONFIG extends UI_ACTION_SELECTED {

    //
    // Members
    //
    //
    // Constructors
    //
    public SIGMA_CONFIG() {
        super(ChangeSigmaAgent.instance());
    }

    @Override
    public void userInputComplete(rpn.controller.ui.UIController ui, RealVector userInput) {

        super.userInputComplete(ui, userInput);


        System.out.println("User input complete de Sigma Config");

        // XZERO
//        XZeroGeomFactory xzeroFactory = new XZeroGeomFactory(new rpnumerics.StationaryPointCalc(RPNUMERICS.getShockProfile().getXZero()));

//        XZeroGeomFactory xzeroFactory = new XZeroGeomFactory(RPNUMERICS.createStationaryPointCalc(RPNUMERICS.getShockProfile().getXZero()));

//        ((ShockFlow)RPNUMERICS.flow()).getXZero()));
//        RPnDataModule.PHASESPACE.plot(xzeroFactory.geom());
//        // plots all other singularities
//
//        HugoniotCurve hCurve = (HugoniotCurve) ((NUMCONFIG_READY) RPnDataModule.PHASESPACE.state()).hugoniotGeom().geomFactory().geomSource();
//
////            List pList = hCurve.findPoints(((ShockFlow) RPNUMERICS.flow()).getSigma());
//
//        List pList = hCurve.findPoints(RPNUMERICS.getShockProfile().getSigma());
//        for (int i = 0; i < pList.size(); i++) {
//            RealVector sigmaPoint = (RealVector) pList.get(i);
////                StationaryPointGeomFactory factory = new StationaryPointGeomFactory(new rpnumerics.StationaryPointCalc(new PhasePoint(sigmaPoint)));
//            StationaryPointGeomFactory factory = new StationaryPointGeomFactory(RPNUMERICS.createStationaryPointCalc(new PhasePoint(sigmaPoint)));
//
//            RPnDataModule.PHASESPACE.plot(factory.geom());
//
//        }


        ui.setState(new GEOM_SELECTION());
    }
}


