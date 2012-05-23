/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.controller.ui;

import java.util.Iterator;
import java.util.List;
import rpn.component.RpGeometry;
import rpn.component.StationaryPointGeomFactory;
import rpn.component.XZeroGeomFactory;
import rpn.controller.phasespace.NUMCONFIG_READY;
import rpn.controller.phasespace.NumConfigImpl;
import rpn.parser.RPnDataModule;
import rpn.usecase.ChangeSigmaAgent;
import rpn.usecase.PoincareSectionPlotAgent;
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
    }

    @Override
    public void userInputComplete(rpn.controller.ui.UIController ui, RealVector userInput) {

        super.userInputComplete(ui, userInput);
        
        System.out.println("User input complete de Sigma Config");
        
        HugoniotCurve hCurve = (HugoniotCurve) ((NumConfigImpl) RPnDataModule.PHASESPACE.state()).hugoniotGeom().geomFactory().geomSource();



        //----------------------------------------------------------------------
        XZeroGeomFactory xzeroRef = new XZeroGeomFactory(new StationaryPointCalc(hCurve.getXZero(), userInput));
        RPnDataModule.PHASESPACE.plot(xzeroRef.geom());
        //----------------------------------------------------------------------


        //----------------------------------------------------------------------
        RealVector closestPoint = hCurve.findClosestPoint(userInput);

        List<RealVector> eqPoints = hCurve.equilPoints(closestPoint);
        System.out.println("Sigma do profile: " + RPNUMERICS.getShockProfile().getSigma());

        //eqPoints.add(hCurve.getXZero());

        for (RealVector realVector : eqPoints) {

            StationaryPointGeomFactory xzeroFactory = new StationaryPointGeomFactory(new StationaryPointCalc(new PhasePoint(realVector), hCurve.getXZero()));

            RPnDataModule.PHASESPACE.plot(xzeroFactory.geom());

        }
        //----------------------------------------------------------------------


        ui.setState(new GEOM_SELECTION());

        return;
    }

    private HugoniotCurve findHugoniot() {

        Iterator<RpGeometry> iterator = RPnDataModule.PHASESPACE.getGeomObjIterator();


        while (iterator.hasNext()) {
            RpGeometry rpGeometry = iterator.next();

            if (rpGeometry.geomFactory().geomSource() instanceof HugoniotCurve) {


                return (HugoniotCurve) rpGeometry.geomFactory().geomSource();
            }

        }

        return null;


    }
}





