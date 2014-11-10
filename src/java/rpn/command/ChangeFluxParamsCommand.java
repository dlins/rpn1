/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.command;

import wave.util.RealVector;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import rpn.configuration.RPnConfig;
import rpn.RPnFluxParamsDialog;
import rpn.RPnFluxParamsSubject;
import rpn.RPnFluxParamsObserver;
import rpn.component.HugoniotCurveGeom;
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
import rpn.parser.RPnDataModule;
import rpnumerics.HugoniotCurve;
import rpnumerics.PhasePoint;
import rpnumerics.StationaryPointCalc;

public class ChangeFluxParamsCommand extends RpModelConfigChangeCommand {
    //
    // Constants
    //

    static public final String DESC_TEXT = "Change Flux Parameters";
    //
    // Members
    //
    static private ChangeFluxParamsCommand instance_ = null;

    //
    // Constructors
    //
    protected ChangeFluxParamsCommand() {
        super(DESC_TEXT);
    }

    public void unexecute() {

        RealVector newValue = (RealVector) log().getOldValue();
        RealVector oldValue = (RealVector) log().getNewValue();
        applyChange(new PropertyChangeEvent(this, DESC_TEXT, oldValue, newValue));
    }

    public void execute() {

        RPnFluxParamsSubject[] fluxParamsSubject = RPnConfig.getFluxParamsSubject();
        RPnFluxParamsObserver fluxParamsObserver = RPnConfig.getFluxParamsObserver();

//        RPnFluxParamsDialog dialog = new RPnFluxParamsDialog(fluxParamsSubject, fluxParamsObserver);
        
        RPnFluxParamsDialog dialog = new RPnFluxParamsDialog();
        dialog.setVisible(true);


    }
    
    
    @Override
    public void applyChange(PropertyChangeEvent change) {

        logCommand( new RpCommand(change));
        super.applyChange(change);

    }

    @Override
    public void actionPerformed(ActionEvent event) {
        System.out.println("Pronto pra chamar execute()");
        execute();
    }

    public void updatePhaseDiagram() {

//        System.out.println("RPnDataModule.PHASESPACE.state() ::::: " + RPnDataModule.PHASESPACE.state());

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


        //--- Depende de teste do estado

        if ((RPnDataModule.PHASESPACE.state() instanceof NumConfigReadyImpl)) {
            HugoniotCurveGeom hGeom = ((NUMCONFIG) RPnDataModule.PHASESPACE.state()).hugoniotGeom();
            HugoniotCurve hCurve = (HugoniotCurve) hGeom.geomFactory().geomSource();
            double sigma = rpnumerics.RPNUMERICS.getViscousProfileData().getSigma();


            //*** Nova curva chama o método novo
            List<RealVector> eqPoints = hCurve.equilPoints(sigma);	//***

            rpnumerics.RPNUMERICS.updateUplus(eqPoints);

            System.out.println("Valor do XZERO no updateDiagram do ChangeFlux ::::: " + hCurve.getXZero());
            System.out.println("Valor de sigma: " + sigma);



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

                //--- Atualiza o ponto estacionario associado ao XZero
                XZeroGeomFactory xzeroRef = new XZeroGeomFactory(new StationaryPointCalc(hCurve.getXZero(), hCurve.getXZero()));

                NumConfigReadyImpl state = (NumConfigReadyImpl) RPnDataModule.PHASESPACE.state();

                RPnDataModule.PHASESPACE.state().plot(RPnDataModule.PHASESPACE, xzeroRef.geom());

                StationaryPointCalc statCalc = new StationaryPointCalc(new PhasePoint(rpnumerics.RPNUMERICS.getViscousProfileData().getUplus()), hCurve.getXZero());
                StationaryPointGeomFactory statFactory = new StationaryPointGeomFactory(statCalc);
                StationaryPointGeom statGeom = (StationaryPointGeom) statFactory.geom();
                RPnDataModule.PHASESPACE.state().plot(RPnDataModule.PHASESPACE, statGeom);
            } else {
                boolean manifold = ((NumConfigReadyImpl) RPnDataModule.PHASESPACE.state()).isPlotManifold();
                XZeroGeom zeroGeom = ((NumConfigReadyImpl) RPnDataModule.PHASESPACE.state()).xzeroGeom();
                NumConfigReadyImpl state = new NumConfigReadyImpl(hGeom, zeroGeom, manifold);
                RPnDataModule.PHASESPACE.changeState(state);

                if (state.isPlotManifold()) {   //*** os plots daqui sao para manifolds
                    RPnDataModule.PHASESPACE.changeState(new InvariantsReadyImpl(hGeom, zeroGeom));

                    for (RealVector realVector : eqPoints) {

                        StationaryPointGeomFactory xzeroFactory = new StationaryPointGeomFactory(new StationaryPointCalc(new PhasePoint(realVector), hCurve.getXZero()));

                        RPnDataModule.PHASESPACE.plot(xzeroFactory.geom());

                    }

                    RPnDataModule.PHASESPACE.plot((XZeroGeom) zeroGeom);


                }
            }




            //------------------------- Recalcula os pontos estacionarios
            for (RealVector realVector : eqPoints) {    // *** o join daqui é para as setas dos pontos estacionarios
                StationaryPointGeomFactory statPointFactory = new StationaryPointGeomFactory(new StationaryPointCalc(new PhasePoint(realVector), hCurve.getXZero()));

                RPnDataModule.PHASESPACE.join(statPointFactory.geom());

            }



        }



    }

    static public ChangeFluxParamsCommand instance() {
        if (instance_ == null) {
            instance_ = new ChangeFluxParamsCommand();
        }
        return instance_;
    }
}
