/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.controller.ui;

import rpnumerics.RPNUMERICS;

import rpnumerics.ShockFlow;
import rpn.controller.phasespace.*;
import rpn.parser.RPnDataModule;
import rpn.usecase.ChangeSigmaAgent;
import rpn.component.XZeroGeomFactory;
import rpn.component.StationaryPointGeomFactory;
import wave.util.RealVector;
import rpnumerics.HugoniotCurve;
import rpnumerics.PhasePoint;
import java.util.List;

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

    public void userInputComplete(rpn.controller.ui.UIController ui, RealVector userInput) {

        super.userInputComplete(ui, userInput);

        // XZERO
//        XZeroGeomFactory xzeroFactory = new XZeroGeomFactory(new rpnumerics.StationaryPointCalc(RPNUMERICS.getShockProfile().getXZero()));

        XZeroGeomFactory xzeroFactory = new XZeroGeomFactory(RPNUMERICS.createStationaryPointCalc(RPNUMERICS.getShockProfile().getXZero()));

//        ((ShockFlow)RPNUMERICS.flow()).getXZero()));
        RPnDataModule.PHASESPACE.plot(xzeroFactory.geom());
        // plots all other singularities

        HugoniotCurve hCurve = (HugoniotCurve) ((NUMCONFIG_READY) RPnDataModule.PHASESPACE.state()).hugoniotGeom().geomFactory().geomSource();

//            List pList = hCurve.findPoints(((ShockFlow) RPNUMERICS.flow()).getSigma());

        List pList = hCurve.findPoints(RPNUMERICS.getShockProfile().getSigma());
        for (int i = 0; i < pList.size(); i++) {
            RealVector sigmaPoint = (RealVector) pList.get(i);
//                StationaryPointGeomFactory factory = new StationaryPointGeomFactory(new rpnumerics.StationaryPointCalc(new PhasePoint(sigmaPoint)));
            StationaryPointGeomFactory factory = new StationaryPointGeomFactory(RPNUMERICS.createStationaryPointCalc(new PhasePoint(sigmaPoint)));

            RPnDataModule.PHASESPACE.plot(factory.geom());

        }


        ui.setState(new GEOM_SELECTION());
    }
    }


