/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.controller.ui;

import java.util.List;
import rpn.component.StationaryPointGeomFactory;
import rpn.component.XZeroGeomFactory;
import rpn.controller.phasespace.NumConfigImpl;
import rpn.parser.RPnDataModule;
import rpn.usecase.ChangeSigmaAgent;
import rpn.usecase.ChangeXZeroAgent;
import rpnumerics.HugoniotCurve;
import rpnumerics.PhasePoint;
import rpnumerics.RPNUMERICS;
import rpnumerics.StationaryPointCalc;
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
        ChangeXZeroAgent.instance().setEnabled(true);
    }

    @Override
    public void userInputComplete(rpn.controller.ui.UIController ui, RealVector userInput) {

        // *** ATENCAO: XZERO NAO EH COLOCADO NA LISTA DE EQPOINTS !!! iSSO DEVE SER LEMBRADO

        HugoniotCurve hCurve = (HugoniotCurve) ((NumConfigImpl) RPnDataModule.PHASESPACE.state()).hugoniotGeom().geomFactory().geomSource();


        double newSigma = hCurve.findSigma(new PhasePoint(userInput));
        RPNUMERICS.getShockProfile().setSigma(newSigma);


        XZeroGeomFactory xzeroRef = new XZeroGeomFactory(new StationaryPointCalc(hCurve.getXZero(), hCurve.getXZero()));
        RPnDataModule.PHASESPACE.plot(xzeroRef.geom());

        RealVector closestPoint = hCurve.findClosestPoint(userInput);

        List<RealVector> eqPoints = hCurve.equilPoints(closestPoint);

        for (RealVector realVector : eqPoints) {

            StationaryPointGeomFactory xzeroFactory = new StationaryPointGeomFactory(new StationaryPointCalc(new PhasePoint(realVector), hCurve.getXZero()));

            RPnDataModule.PHASESPACE.plot(xzeroFactory.geom());

        }

        ui.setState(new GEOM_SELECTION());

        return;
    }
}





