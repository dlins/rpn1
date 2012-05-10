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
import rpn.controller.phasespace.NUMCONFIG_READY;
import rpn.controller.phasespace.NumConfigImpl;
import rpn.parser.RPnDataModule;
import rpn.usecase.ChangeSigmaAgent;
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

        // XZERO
//        XZeroGeomFactory xzeroFactory = new XZeroGeomFactory(new rpnumerics.StationaryPointCalc(RPNUMERICS.getShockProfile().getXZero()));

//        XZeroGeomFactory xzeroFactory = new XZeroGeomFactory(RPNUMERICS.createStationaryPointCalc(RPNUMERICS.getShockProfile().getXZero()));

//        ((ShockFlow)RPNUMERICS.flow()).getXZero()));
//        RPnDataModule.PHASESPACE.plot(xzeroFactory.geom());
//        // plots all other singularities

        Iterator<RpGeometry> iterator = RPnDataModule.PHASESPACE.getGeomObjIterator();




//        while (iterator.hasNext()) {
//            RpGeometry rpGeometry = iterator.next();

//            if (rpGeometry.geomFactory().geomSource() instanceof HugoniotCurve) {

        HugoniotCurve hCurve = (HugoniotCurve) ((NumConfigImpl) RPnDataModule.PHASESPACE.state()).hugoniotGeom().geomFactory().geomSource();
//                HugoniotCurve hCurve = (HugoniotCurve) rpGeometry.geomFactory().geomSource();

        RealVector closestPoint = hCurve.findClosestPoint(userInput);

        List<RealVector> eqPoints = hCurve.equilPoints(closestPoint);
        System.out.println("Sigma do profile: " + RPNUMERICS.getShockProfile().getSigma());

        eqPoints.add(hCurve.getXZero());

        for (RealVector realVector : eqPoints) {

            StationaryPointGeomFactory xzeroFactory = new StationaryPointGeomFactory(new StationaryPointCalc(new PhasePoint(realVector), hCurve.getXZero()));

            RPnDataModule.PHASESPACE.plot(xzeroFactory.geom());



//                }





//            }

        }







//        HugoniotCurve hCurve = findHugoniot();//(HugoniotCurve) ((NUMCONFIG_READY) RPnDataModule.PHASESPACE.state()).hugoniotGeom().geomFactory().geomSource();





//        System.out.println("tamanho da lista :"+eqPoints.size());
//
////        for (RealVector realVector : eqPoints) {
////
////            System.out.println(realVector);
////
////        }






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





