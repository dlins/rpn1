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
import rpn.component.ManifoldGeom;
import rpn.component.RpGeometry;
import rpn.component.StationaryPointGeom;
import rpn.component.StationaryPointGeomFactory;
import rpn.component.XZeroGeom;
import rpn.component.XZeroGeomFactory;
import rpn.controller.phasespace.InvariantsReadyImpl;
import rpn.controller.phasespace.NUMCONFIG;
import rpn.controller.phasespace.NumConfigReadyImpl;
import rpn.controller.phasespace.ProfileSetupReadyImpl;
import rpn.controller.ui.UIController;
import rpn.parser.RPnDataModule;
import rpnumerics.HugoniotCurve;
import rpnumerics.HugoniotCurveCalcND;
import rpnumerics.HugoniotParams;
import rpnumerics.PhasePoint;
import rpnumerics.RPNUMERICS;
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

        //--------------------- Remove os pontos estacionarios
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
        //--------------------------------------------------------

        //--- Atualiza o ponto estacionario associado ao XZero
        XZeroGeomFactory xzeroRef = new XZeroGeomFactory(new StationaryPointCalc(new PhasePoint(lastPointAdded), lastPointAdded));

        rpnumerics.RPNUMERICS.getShockProfile().setXZero(new PhasePoint(lastPointAdded));

        HugoniotCurveGeom hGeom = ((NUMCONFIG) RPnDataModule.PHASESPACE.state()).hugoniotGeom();
        HugoniotCurve hCurve = (HugoniotCurve) hGeom.geomFactory().geomSource();
        double sigma = rpnumerics.RPNUMERICS.getShockProfile().getSigma();


        //--- Produz lista de pontos de equilibrio

        HugoniotCurveCalcND hCalc = (HugoniotCurveCalcND) (((HugoniotCurveGeomFactory) hGeom.geomFactory()).rpCalc());

        PhasePoint oldXZero = ((HugoniotParams) hCalc.getParams()).getXZero();

        ((HugoniotParams) hCalc.getParams()).setXZero(lastPointAdded);


        applyChange(new PropertyChangeEvent(this, DESC_TEXT, oldXZero, lastPointAdded));

        //*** Define a nova curva
        hGeom = ((NumConfigReadyImpl) RPnDataModule.PHASESPACE.state()).hugoniotGeom();
        hCurve = (HugoniotCurve) hGeom.geomFactory().geomSource();

        //*** Nova curva chama o método novo
        List<RealVector> eqPoints = hCurve.equilPoints(sigma);	//***

        RPNUMERICS.updateUplus(eqPoints);

        //------------------------- Recalcula os pontos estacionarios
        for (RealVector realVector : eqPoints) {    // *** o join daqui é para as setas dos pontos estacionarios
            StationaryPointGeomFactory statPointFactory = new StationaryPointGeomFactory(new StationaryPointCalc(new PhasePoint(realVector), hCurve.getXZero()));

            RPnDataModule.PHASESPACE.join(statPointFactory.geom());

        }

        RPnDataModule.PHASESPACE.join(xzeroRef.geom());
        //----------------------

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

            StationaryPointCalc statCalc = new StationaryPointCalc(new PhasePoint(RPNUMERICS.getShockProfile().getUplus()), hCurve.getXZero());
            StationaryPointGeomFactory statFactory = new StationaryPointGeomFactory(statCalc);
            StationaryPointGeom statUPlus = (StationaryPointGeom) statFactory.geom();
            RPnDataModule.PHASESPACE.state().plot(RPnDataModule.PHASESPACE, statUPlus);
            RPnDataModule.PHASESPACE.state().plot(RPnDataModule.PHASESPACE, xzeroRef.geom());



        } else {

            boolean manifold = ((NumConfigReadyImpl) RPnDataModule.PHASESPACE.state()).isPlotManifold();
            NumConfigReadyImpl state = new NumConfigReadyImpl(hGeom, (XZeroGeom) xzeroRef.geom(), manifold);
            RPnDataModule.PHASESPACE.changeState(state);

            if (state.isPlotManifold()) {   //*** os plots daqui sao para manifolds
                RPnDataModule.PHASESPACE.changeState(new InvariantsReadyImpl(hGeom, (XZeroGeom) xzeroRef.geom()));

                for (RealVector realVector : eqPoints) {

                    StationaryPointGeomFactory xzeroFactory = new StationaryPointGeomFactory(new StationaryPointCalc(new PhasePoint(realVector), new PhasePoint(lastPointAdded)));

                    RPnDataModule.PHASESPACE.plot(xzeroFactory.geom());

                }

                RPnDataModule.PHASESPACE.plot((XZeroGeom) xzeroRef.geom());


            }

        }



    }



    static public ChangeXZeroAgent instance() {
        if (instance_ == null) {
            instance_ = new ChangeXZeroAgent();
        }
        return instance_;
    }
}
