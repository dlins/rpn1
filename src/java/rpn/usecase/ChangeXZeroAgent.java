/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.usecase;

import wave.util.RealVector;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import rpn.component.HugoniotCurveGeom;
import rpn.component.HugoniotCurveGeomFactory;
import rpn.component.RpGeometry;
import rpn.component.StationaryPointGeom;
import rpn.component.StationaryPointGeomFactory;
import rpn.component.XZeroGeom;
import rpn.component.XZeroGeomFactory;
import rpn.controller.phasespace.InvariantsReadyImpl;
import rpn.controller.phasespace.NUMCONFIG;
import rpn.controller.phasespace.NumConfigReadyImpl;
import rpn.controller.ui.UIController;
import rpn.parser.RPnDataModule;
import rpnumerics.HugoniotCurve;
import rpnumerics.HugoniotCurveCalcND;
import rpnumerics.HugoniotParams;
import rpnumerics.PhasePoint;
import rpnumerics.StationaryPoint;
import rpnumerics.StationaryPointCalc;

public class ChangeXZeroAgent extends RpModelConfigChangeAgent {
    //
    // Constants
    //

    static public final String DESC_TEXT = "Change X-Zero...";
    //
    // Members
    //
    static private ChangeXZeroAgent instance_ = null;

    //
    // Constructors
    //
    protected ChangeXZeroAgent() {
        super(DESC_TEXT);
    }

    public void unexecute() {

        RealVector newValue = (RealVector) log().getOldValue();
        RealVector oldValue = (RealVector) log().getNewValue();
        applyChange(new PropertyChangeEvent(this, DESC_TEXT, oldValue, newValue));
    }

    public void execute() {

        RealVector[] userInputList = UIController.instance().userInputList();
        RealVector lastPointAdded = userInputList[userInputList.length - 1];

        Iterator it = RPnDataModule.PHASESPACE.getGeomObjIterator();
        List<RpGeometry> list = new ArrayList<RpGeometry>();

        while (it.hasNext()) {
            RpGeometry geometry = (RpGeometry) it.next();
            if (((geometry instanceof StationaryPointGeom)) || (geometry instanceof XZeroGeom)) {

                list.add(geometry);

            }

        }

        for (RpGeometry rpgeometry : list) {
            RPnDataModule.PHASESPACE.remove(rpgeometry);
        }


        System.out.println("Valor de sigma: " + rpnumerics.RPNUMERICS.getShockProfile().getSigma());

        XZeroGeomFactory xzeroRef = new XZeroGeomFactory(new StationaryPointCalc(new PhasePoint(lastPointAdded), lastPointAdded));



        
        HugoniotCurveGeom hGeom = ((NUMCONFIG) RPnDataModule.PHASESPACE.state()).hugoniotGeom();
        HugoniotCurve hCurve = (HugoniotCurve) hGeom.geomFactory().geomSource();
        double sigma = rpnumerics.RPNUMERICS.getShockProfile().getSigma();


        boolean manifold = ((NumConfigReadyImpl) RPnDataModule.PHASESPACE.state()).isPlotManifold();
        NumConfigReadyImpl state = new NumConfigReadyImpl(hGeom, (XZeroGeom) xzeroRef.geom(), manifold);
        RPnDataModule.PHASESPACE.changeState(state);


        RPnDataModule.PHASESPACE.plot(xzeroRef.geom());//



        HugoniotCurveCalcND hCalc = (HugoniotCurveCalcND) (((HugoniotCurveGeomFactory) hGeom.geomFactory()).rpCalc());

        PhasePoint oldXZero = ((HugoniotParams) hCalc.getParams()).getXZero();

        ((HugoniotParams) hCalc.getParams()).setXZero(lastPointAdded);


        //------------------------- Recalcula os pontos estacionarios
        RealVector closestPoint = hCurve.findClosestPoint(lastPointAdded);
        List<RealVector> eqPoints = hCurve.equilPoints(closestPoint);


        if (state.isPlotManifold()) {   //*** os plots daqui sao para manifolds
            RPnDataModule.PHASESPACE.changeState(new InvariantsReadyImpl(hGeom, (XZeroGeom) xzeroRef.geom()));

            for (RealVector realVector : eqPoints) {
                StationaryPointGeomFactory xzeroFactory = new StationaryPointGeomFactory(new StationaryPointCalc(new PhasePoint(realVector), hCurve.getXZero()));

                StationaryPoint testePoint = (StationaryPoint) xzeroFactory.geomSource();

                System.out.println(testePoint.getElement(0) + " " + testePoint.getElement(1) + " Sela: " + testePoint.isSaddle());


                RPnDataModule.PHASESPACE.plot(xzeroFactory.geom());

            }
            RPnDataModule.PHASESPACE.plot((XZeroGeom) xzeroRef.geom());

        }


        for (RealVector realVector : eqPoints) {    // *** o join daqui é para as setas dos pontos estacionarios
            StationaryPointGeomFactory xzeroFactory = new StationaryPointGeomFactory(new StationaryPointCalc(new PhasePoint(realVector), hCurve.getXZero()));

            RPnDataModule.PHASESPACE.join(xzeroFactory.geom());

        }


        applyChange(new PropertyChangeEvent(this, DESC_TEXT, oldXZero, lastPointAdded));

    }

    static public ChangeXZeroAgent instance() {
        if (instance_ == null) {
            instance_ = new ChangeXZeroAgent();
        }
        return instance_;
    }
}
