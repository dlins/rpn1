/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.controller.phasespace;

import rpn.component.*;
import rpn.RPnPhaseSpaceAbstraction;
import rpn.controller.ui.UIController;
import rpn.parser.RPnDataModule;
import rpn.command.FindProfileCommand;
import rpn.command.OrbitPlotCommand;
import rpnumerics.HugoniotCurve;
import rpnumerics.ManifoldOrbit;
import rpnumerics.ManifoldOrbitCalc;
import rpnumerics.Orbit;
import rpnumerics.PhasePoint;
import rpnumerics.RPNUMERICS;
import rpnumerics.StationaryPoint;
import rpnumerics.viscousprofile.ViscousProfileData;
import wave.util.RealVector;
import wave.util.SimplexPoincareSection;

public class ProfileSetupReadyImpl extends PoincareReadyImpl
        implements PROFILE_SETUP_READY {

    //
    // Members
    //
    private ManifoldGeom fwdManifoldGeom_;
    private ManifoldGeom bwdManifoldGeom_;

    //
    // Constructors
    //
    public ProfileSetupReadyImpl(HugoniotCurveGeom hugoniotGeom, XZeroGeom xzeroGeom, PoincareSectionGeom poincareGeom) {
        super(hugoniotGeom, xzeroGeom, poincareGeom, false);
        fwdManifoldGeom_ = null;
        bwdManifoldGeom_ = null;
        // ENABLED
        FindProfileCommand.instance().setEnabled(true);
        OrbitPlotCommand.instance().setEnabled(false);
    }

    public ProfileSetupReadyImpl(HugoniotCurveGeom hugoniotGeom, XZeroGeom xzeroGeom, PoincareSectionGeom poincareGeom,
            ManifoldGeom fwdManifoldGeom, ManifoldGeom bwdManifoldGeom, boolean manifold) {
        super(hugoniotGeom, xzeroGeom, poincareGeom, manifold);
        fwdManifoldGeom_ = fwdManifoldGeom;
        bwdManifoldGeom_ = bwdManifoldGeom;
        // ENABLED
        FindProfileCommand.instance().setEnabled(true);
        OrbitPlotCommand.instance().setEnabled(false);
    }

    //
    // Accessors/Mutators
    //
    public ManifoldGeom fwdManifoldGeom() {
        return fwdManifoldGeom_;
    }

    public ManifoldGeom bwdManifoldGeom() {
        return bwdManifoldGeom_;
    }

    //
    // Methods
    //
    @Override
    public void plot(RPnPhaseSpaceAbstraction phaseSpace, RpGeometry geom) {

        ManifoldGeom[] manifoldGeom = null;

        // ---
        HugoniotCurveGeom hGeom = ((NUMCONFIG) RPnDataModule.PHASESPACE.state()).hugoniotGeom();
        HugoniotCurve hCurve = (HugoniotCurve) hGeom.geomFactory().geomSource();
        // ---

        System.out.println("Esta no plot de ProfileSetupReadyImpl *************************************");

        StationaryPoint statPoint = null;
        int direction = 0;

        if (geom instanceof XZeroGeom) {

            statPoint = (StationaryPoint) geom.geomFactory().geomSource();

            // ---
            if (hCurve.getDirection()==Orbit.FORWARD_DIR) {
                direction = Orbit.FORWARD_DIR;
                manifoldGeom = buildManifold(statPoint, direction);
                fwdManifoldGeom_ = manifoldGeom[0];
            }
            if (hCurve.getDirection()==Orbit.BACKWARD_DIR) {
                direction = Orbit.BACKWARD_DIR;
                manifoldGeom = buildManifold(statPoint, direction);
                bwdManifoldGeom_ = manifoldGeom[0];
            }
            // ---

        }
        if (geom instanceof StationaryPointGeom && !(geom instanceof XZeroGeom)) {
            statPoint = (StationaryPoint) geom.geomFactory().geomSource();

            // ---
            if (hCurve.getDirection()==Orbit.FORWARD_DIR) {
                direction = Orbit.BACKWARD_DIR;
                manifoldGeom = buildManifold(statPoint, direction);
                bwdManifoldGeom_ = manifoldGeom[0];
            }
            if (hCurve.getDirection()==Orbit.BACKWARD_DIR) {
                direction = Orbit.FORWARD_DIR;
                manifoldGeom = buildManifold(statPoint, direction);
                fwdManifoldGeom_ = manifoldGeom[0];
            }
            // ---


            testeDotPoincare();
        }


        RPnDataModule.PHASESPACE.join(manifoldGeom[0]);
        RPnDataModule.PHASESPACE.join(manifoldGeom[1]);

        UIController.instance().panelsUpdate();



    }

    private void testeDotPoincare() {

        ProfileSetupReadyImpl state = (ProfileSetupReadyImpl) RPnDataModule.PHASESPACE.state();

        ManifoldGeom fwdGeom = state.fwdManifoldGeom();
        ManifoldGeom bwdGeom = state.bwdManifoldGeom();

        ManifoldOrbit fwdManifold = (ManifoldOrbit) fwdGeom.geomFactory().geomSource();
        ManifoldOrbit bwdManifold = (ManifoldOrbit) bwdGeom.geomFactory().geomSource();

        Orbit fwdOrbit = fwdManifold.getOrbit();
        Orbit bwdOrbit = bwdManifold.getOrbit();

        RealVector p1 = fwdOrbit.lastPoint();
        RealVector p2 = bwdOrbit.lastPoint();

        if (fwdOrbit.isInterPoincare() && bwdOrbit.isInterPoincare()) {

            RPNUMERICS.getViscousProfileData().updateDelta(p1, p2);


            System.out.println("Valores dos Dots no ProfileSetup: ");
            System.out.println(ViscousProfileData.instance().getPreviousDot() + "  e  " +ViscousProfileData.instance().getDot());


            if (RPNUMERICS.getViscousProfileData().changedDotSignal()) {
                //System.out.println("Intervalo de sigma ::::::::::::::: " + RPNUMERICS.getViscousProfileData().getPreviousSigma() + " , " + RPNUMERICS.getViscousProfileData().getSigma());
                //System.out.println("Intervalo de Uplus ::::::::::::::: " + RPNUMERICS.getViscousProfileData().getPreviousUPlus() + " , " + RPNUMERICS.getViscousProfileData().getUplus());
                //System.out.println("Intervalo de XZero ::::::::::::::: " + RPNUMERICS.getViscousProfileData().getPreviousXZero() + " , " + RPNUMERICS.getViscousProfileData().getXZero());
                //int tam = RPNUMERICS.getViscousProfileData().getPreviousPhysicsParams().length;
                //String[] previous = RPNUMERICS.getViscousProfileData().getPreviousPhysicsParams();

                //System.out.println("Vetor de previous parametros :::::::::::::: ");
                //for (int i = 0; i < previous.length; i++) System.out.print(previous[i] + " ");

                //System.out.println("Vetor de atuais parametros :::::::::::::: " +RPNUMERICS.getFluxParams().getParams());
            }
        }




        //System.out.println("Sigma sem trocar::::::::::::::: " + RPNUMERICS.getViscousProfileData().getPreviousSigma());

        //System.out.println("Uplus sem trocar::::::::::::::: " + RPNUMERICS.getViscousProfileData().getPreviousUPlus());







    }

    private ManifoldGeom[] buildManifold(StationaryPoint statPoint, int direction) {

        SimplexPoincareSection poincareSection = (SimplexPoincareSection) (poincareGeom().geomFactory().geomSource());

        PhasePoint[] firstPoint = null;
        if (direction == Orbit.FORWARD_DIR) {
            firstPoint = statPoint.orbitDirectionFWD();
        }
        if (direction == Orbit.BACKWARD_DIR) {
            firstPoint = statPoint.orbitDirectionBWD();
        }


        ManifoldGeomFactory factoryRef0 = new ManifoldGeomFactory(new ManifoldOrbitCalc(statPoint, firstPoint[0], poincareSection, direction));
        ManifoldGeomFactory factoryRef1 = new ManifoldGeomFactory(new ManifoldOrbitCalc(statPoint, firstPoint[1], poincareSection, direction));

        ManifoldGeom geom0 = (ManifoldGeom) factoryRef0.geom();
        ManifoldGeom geom1 = (ManifoldGeom) factoryRef1.geom();

        Orbit orbit0 = ((ManifoldOrbit) geom0.geomFactory().geomSource()).getOrbit();

        ManifoldGeom[] manifoldGeom = new ManifoldGeom[2];

        if (orbit0.isInterPoincare()) {
            manifoldGeom[0] = geom0;
            manifoldGeom[1] = geom1;
        } else {
            manifoldGeom[0] = geom1;
            manifoldGeom[1] = geom0;
        }


        return manifoldGeom;
    }

    public void delete(RPnPhaseSpaceAbstraction phaseSpace, RpGeometry geom) {
        super.delete(phaseSpace, geom);
        if (geom == fwdManifoldGeom()) {
            phaseSpace.changeState(new bwdProfileReadyImpl(hugoniotGeom(), xzeroGeom(), poincareGeom(), bwdManifoldGeom(), isPlotManifold()));
        }
        if (geom == bwdManifoldGeom()) {
            phaseSpace.changeState(new bwdProfileReadyImpl(hugoniotGeom(), xzeroGeom(), poincareGeom(), fwdManifoldGeom(), isPlotManifold()));
        }
    }
}
