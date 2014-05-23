/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.controller.ui;

import java.beans.PropertyChangeEvent;
import java.util.List;
import rpn.component.StationaryPointGeomFactory;
import rpn.component.XZeroGeomFactory;
import rpn.controller.phasespace.NumConfigImpl;
import rpn.parser.RPnDataModule;
import rpn.command.ChangeSigmaCommand;
import rpn.command.RpCommand;
import rpn.configuration.CommandConfiguration;
import rpn.configuration.Configuration;
import rpnumerics.HugoniotCurve;
import rpnumerics.PhasePoint;
import rpnumerics.RPNUMERICS;
import rpnumerics.StationaryPoint;
import rpnumerics.StationaryPointCalc;
import rpnumerics.viscousprofile.ViscousProfileData;
import wave.util.RealVector;

public class SigmaConfig extends UI_ACTION_SELECTED {

    //
    // Members
    //

    //
    // Constructors
    //

    public SigmaConfig() {
        super(ChangeSigmaCommand.instance());


    }

    @Override
    public void userInputComplete(rpn.controller.ui.UIController ui, RealVector userInput) {

        // *** ATENCAO: XZERO NAO EH COLOCADO NA LISTA DE EQPOINTS !!! iSSO DEVE SER LEMBRADO

        HugoniotCurve hCurve = (HugoniotCurve) ((NumConfigImpl) RPnDataModule.PHASESPACE.state()).hugoniotGeom().geomFactory().geomSource();


        //double newSigma = hCurve.findSigma(new PhasePoint(userInput));
        double newSigma = hCurve.velocity(userInput);
        RPNUMERICS.getViscousProfileData().setSigma(newSigma);
        
        logSigmaCommand(newSigma);

        XZeroGeomFactory xzeroRef = new XZeroGeomFactory(new StationaryPointCalc(hCurve.getXZero(), hCurve.getXZero()));
        RPnDataModule.PHASESPACE.plot(xzeroRef.geom());

        RealVector closestPoint = hCurve.findClosestPoint(userInput);

        // -----
        RPNUMERICS.getViscousProfileData().setUplus(new PhasePoint(closestPoint));

        List<RealVector> eqPoints = hCurve.equilPoints(closestPoint);




        for (RealVector realVector : eqPoints) {

            StationaryPointGeomFactory xzeroFactory = new StationaryPointGeomFactory(new StationaryPointCalc(new PhasePoint(realVector), hCurve.getXZero()));

            RPnDataModule.PHASESPACE.plot(xzeroFactory.geom());

        }
        eqPoints.add(((StationaryPoint) xzeroRef.geomSource()).getPoint());

        ViscousProfileData.instance().updateStationaryPointsList(eqPoints);

        ui.setState(new GEOM_SELECTION());

        return;
    }

    public void logSigmaCommand(double sigma) {

        Configuration newConfiguration = new CommandConfiguration(toString());

        newConfiguration.setParamValue("sigma", String.valueOf(sigma));

        PropertyChangeEvent commandEvent = new PropertyChangeEvent(this, null, null, newConfiguration);

        UndoActionController.instance().addAction(new RpCommand(commandEvent));



    }
    
    
    public String toString (){
        
        return "sigmaconfig";
    }
    
}
