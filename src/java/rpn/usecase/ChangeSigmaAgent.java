/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.usecase;

import rpn.controller.phasespace.NUMCONFIG;
import rpn.parser.RPnDataModule;
import rpnumerics.RPNUMERICS;
import rpnumerics.PhasePoint;
import rpnumerics.HugoniotCurve;
import wave.util.RealVector;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import rpn.component.HugoniotCurveGeom;
import rpn.component.ManifoldGeom;
import rpn.component.RpGeometry;
import rpn.component.StationaryPointGeom;
import rpn.component.StationaryPointGeomFactory;
import rpn.component.XZeroGeom;
import rpn.component.XZeroGeomFactory;
import rpn.controller.phasespace.InvariantsReadyImpl;
import rpn.controller.phasespace.NumConfigReadyImpl;
import rpn.controller.phasespace.ProfileSetupReadyImpl;
import rpn.controller.ui.*;
import rpnumerics.StationaryPoint;
import rpnumerics.StationaryPointCalc;

public class ChangeSigmaAgent extends RpModelConfigChangeAgent {
    //
    // Constants
    //

    static public final String DESC_TEXT = "Change Hugoniot Speed";
    //
    // Members
    //
    private static ChangeSigmaAgent instance_ = null;

    //
    // Constructors
    //
    protected ChangeSigmaAgent() {
        super(DESC_TEXT);
    }

    public void execute() {
        System.out.println("Execute de ChangeSigmaAgent");

        Double oldValue = new Double(RPNUMERICS.getShockProfile().getSigma());
        RealVector[] userInputList = UIController.instance().userInputList();
        RealVector lastPointAdded = userInputList[userInputList.length - 1];
        double newSigma;

        HugoniotCurveGeom hGeom = ((NUMCONFIG) RPnDataModule.PHASESPACE.state()).hugoniotGeom();
        HugoniotCurve hCurve = (HugoniotCurve) hGeom.geomFactory().geomSource();

        newSigma = hCurve.findSigma(new PhasePoint(lastPointAdded));
        RPNUMERICS.getShockProfile().setSigma(newSigma);

        Double newValue = new Double(RPNUMERICS.getShockProfile().getSigma());

        RealVector closestPoint = hCurve.findClosestPoint(lastPointAdded);
        RPNUMERICS.getShockProfile().setUplus(new PhasePoint(closestPoint));

        //--------------------- Remove os pontos estacionarios
        Iterator it = RPnDataModule.PHASESPACE.getGeomObjIterator();
        List<RpGeometry> list = new ArrayList<RpGeometry>();

        while (it.hasNext()) {
            RpGeometry geometry = (RpGeometry) it.next();

            if (((geometry instanceof StationaryPointGeom)) && !(geometry instanceof XZeroGeom)) {
                list.add(geometry);

            }

        }

        for (RpGeometry rpgeometry : list) {
            RPnDataModule.PHASESPACE.remove(rpgeometry);
        }

        //--- Atualiza o ponto estacionario associado ao XZero
        XZeroGeomFactory xzeroRef = new XZeroGeomFactory(new StationaryPointCalc(hCurve.getXZero(), hCurve.getXZero()));

        //--- Produz lista de pontos de equilibrio
        List<RealVector> eqPoints = hCurve.equilPoints(closestPoint);

        //--- o join daqui é para as setas dos pontos estacionarios
        for (RealVector realVector : eqPoints) {
            StationaryPointGeomFactory xzeroFactory = new StationaryPointGeomFactory(new StationaryPointCalc(new PhasePoint(realVector), hCurve.getXZero()));

            RPnDataModule.PHASESPACE.join(xzeroFactory.geom());

        }

        System.out.println("RPnDataModule.PHASESPACE.state() :::::::::::::::::: " + RPnDataModule.PHASESPACE.state());


        if (RPnDataModule.PHASESPACE.state() instanceof ProfileSetupReadyImpl) {

            Iterator iter = RPnDataModule.PHASESPACE.getGeomObjIterator();
            List<RpGeometry> listManifolds = new ArrayList<RpGeometry>();

            while (iter.hasNext()) {
                RpGeometry geometry = (RpGeometry) iter.next();

                if (geometry instanceof ManifoldGeom) {
                    listManifolds.add(geometry);
                }

            }

            for (RpGeometry rpgeometry : listManifolds) {
                RPnDataModule.PHASESPACE.remove(rpgeometry);
            }

            // ------------------

            System.out.println("ENtramos no ProfileSetup...............");

            RPnDataModule.PHASESPACE.state().plot(RPnDataModule.PHASESPACE, xzeroRef.geom());

            StationaryPointCalc statCalc = new StationaryPointCalc(new PhasePoint(closestPoint), hCurve.getXZero());
            StationaryPointGeomFactory statFactory = new StationaryPointGeomFactory(statCalc);
            StationaryPointGeom statGeom = (StationaryPointGeom) statFactory.geom();
            RPnDataModule.PHASESPACE.state().plot(RPnDataModule.PHASESPACE, statGeom);
        } else {
            boolean manifold = ((NumConfigReadyImpl) RPnDataModule.PHASESPACE.state()).isPlotManifold();
            NumConfigReadyImpl state = new NumConfigReadyImpl(hGeom, (XZeroGeom) xzeroRef.geom(), manifold);

            RPnDataModule.PHASESPACE.changeState(state);


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

        }


        applyChange(new PropertyChangeEvent(this, DESC_TEXT, oldValue, newValue));

    }

    public void unexecute() {
        Double oldValue = (Double) log().getNewValue();
        System.out.println("OLD SIGMA = " + oldValue);
        Double newValue = (Double) log().getOldValue();
        RPNUMERICS.getShockProfile().setSigma(newValue);
        System.out.println("NEW SIGMA = " + newValue);
        applyChange(new PropertyChangeEvent(this, DESC_TEXT, oldValue, newValue));
    }

    static public ChangeSigmaAgent instance() {
        if (instance_ == null) {
            instance_ = new ChangeSigmaAgent();
        }
        return instance_;
    }
}
