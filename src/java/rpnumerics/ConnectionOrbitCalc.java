/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import java.util.List;
import rpn.component.HugoniotCurveGeom;
import rpn.controller.phasespace.NUMCONFIG;
import rpn.parser.RPnDataModule;
import rpnumerics.viscousprofile.ViscousProfileData;
import wave.util.RealMatrix2;
import wave.util.RealVector;


public class ConnectionOrbitCalc implements RpCalculation {

    static double APPROXIMATION_CHANGE_RELATIVE_MAXIMUM = 0.5;
    //
    // Members
    //
    private ManifoldOrbit manifoldOrbitA_;
    private ManifoldOrbit manifoldOrbitB_;
    private int flag_;
    private int iterationNumber_;
    private double sigmaAccuracy_;
    private String methodCalcName_;
    private ShockFlow flow_;
    private HugoniotCurve hCurve_;

    private double sA = ViscousProfileData.instance().getPreviousSigma();
    private double sB = ViscousProfileData.instance().getSigma();
    private double dotA = ViscousProfileData.instance().getPreviousDot();
    private double dotB = ViscousProfileData.instance().getDot();

    //
    // Constructor
    //

    public ConnectionOrbitCalc(HugoniotCurve hCurve) {
        hCurve_ = hCurve;
    }

    //
    // Accessors/Mutators
    //
    public ManifoldOrbit getManifoldOrbitA() {
        return manifoldOrbitA_;
    }

    public ManifoldOrbit getManifoldOrbitB() {
        return manifoldOrbitB_;
    }

    public int getIterationNumber() {
        return iterationNumber_;
    }

    public double getSigmaAccuracy() {
        return sigmaAccuracy_;
    }

    //
    // Methods
    //


   



    protected Orbit createConnectingOrbit() {
        return null;
    }


    //*** Usado dentro da bisseção em sigma
    private void updateDeltaM(RealVector pUref, RealVector pUPlus) {
        RealVector poincareLimits = new RealVector(2);
        poincareLimits.sub(ViscousProfileData.instance().getPoincare().getPoints()[0], ViscousProfileData.instance().getPoincare().getPoints()[1]);
        RealVector connectionLimits = new RealVector(2);

        connectionLimits.sub(pUref, pUPlus);

        ViscousProfileData.instance().setDot(poincareLimits.dot(connectionLimits));
    }


    public RpSolution calc() throws RpException {

        // ---
        int directionXZero = 0;
        int directionUPlus = 0;

        PhasePoint[] firstPointXZero = null;
        PhasePoint[] firstPointUPlus = null;
        // ---

        int i = 0;
        int nmax = 10;
        double sigmaM = 0.;

        Orbit orbitXZero;
        Orbit orbitUPlus;

        StationaryPoint xZero;
        StationaryPoint uPlus;

        double sigmaA = ViscousProfileData.instance().getPreviousSigma();
        double sigmaB = ViscousProfileData.instance().getSigma();

        System.out.println("Valores de sigmaA e sigmaB : " +sigmaA  +" e " +sigmaB);

        long t = System.currentTimeMillis();

        do {
            sigmaM = 0.5 * (sigmaA + sigmaB);
            
            ViscousProfileData.instance().setSigma(sigmaM);

            HugoniotCurveGeom hGeom = ((NUMCONFIG) RPnDataModule.PHASESPACE.state()).hugoniotGeom();
            HugoniotCurve hCurve = (HugoniotCurve) hGeom.geomFactory().geomSource();
            List<RealVector> eqPoints = hCurve.equilPoints(sigmaM);
            //System.out.println("Uref da HUGONIOT : " +hCurve.getXZero().getCoords());

            // ---
            if (hCurve.getDirection()==Orbit.FORWARD_DIR) {
                directionXZero = Orbit.FORWARD_DIR;
                directionUPlus = Orbit.BACKWARD_DIR;
            }
            if (hCurve.getDirection()==Orbit.BACKWARD_DIR) {
                directionXZero = Orbit.BACKWARD_DIR;
                directionUPlus = Orbit.FORWARD_DIR;
            }
            // ---

            RPNUMERICS.updateUplus(eqPoints);

            StationaryPointCalc xZeroCalc = new StationaryPointCalc(ViscousProfileData.instance().getXZero(), ViscousProfileData.instance().getXZero());
            StationaryPointCalc uPlusCalc = new StationaryPointCalc(ViscousProfileData.instance().getUplus(), ViscousProfileData.instance().getXZero());
            xZero = (StationaryPoint) xZeroCalc.calc();
            uPlus = (StationaryPoint) uPlusCalc.calc();

            // ---
            if (hCurve.getDirection()==Orbit.FORWARD_DIR) {
                firstPointXZero = xZero.orbitDirectionFWD();
                firstPointUPlus = uPlus.orbitDirectionBWD();
            }
            if (hCurve.getDirection()==Orbit.BACKWARD_DIR) {
                firstPointXZero = xZero.orbitDirectionBWD();
                firstPointUPlus = uPlus.orbitDirectionFWD();
            }
            // ---

            // ---
            ManifoldOrbitCalc manifoldXZeroCalc0 = new ManifoldOrbitCalc(xZero, firstPointXZero[0], ViscousProfileData.instance().getPoincare(), directionXZero);
            ManifoldOrbitCalc manifoldXZeroCalc1 = new ManifoldOrbitCalc(xZero, firstPointXZero[1], ViscousProfileData.instance().getPoincare(), directionXZero);
            // ---

            ManifoldOrbit manifoldXZero0 = (ManifoldOrbit) manifoldXZeroCalc0.calc();
            ManifoldOrbit manifoldXZero1 = (ManifoldOrbit) manifoldXZeroCalc1.calc();


            if (manifoldXZero0.getOrbit().isInterPoincare()) {
                orbitXZero = manifoldXZero0.getOrbit();
            } else {
                orbitXZero = manifoldXZero1.getOrbit();
            }
            

            // ---
            ManifoldOrbitCalc manifoldUPlusCalc0 = new ManifoldOrbitCalc(uPlus, firstPointUPlus[0], ViscousProfileData.instance().getPoincare(), directionUPlus);
            ManifoldOrbitCalc manifoldUPlusCalc1 = new ManifoldOrbitCalc(uPlus, firstPointUPlus[1], ViscousProfileData.instance().getPoincare(), directionUPlus);
            // ---

            ManifoldOrbit manifoldUPlus0 = (ManifoldOrbit) manifoldUPlusCalc0.calc();
            ManifoldOrbit manifoldUPlus1 = (ManifoldOrbit) manifoldUPlusCalc1.calc();


            if (manifoldUPlus0.getOrbit().isInterPoincare()) {
                orbitUPlus = manifoldUPlus0.getOrbit();
            } else {
                orbitUPlus = manifoldUPlus1.getOrbit();
            }
            

            RealVector p1 = orbitXZero.lastPoint();
            RealVector p2 = orbitUPlus.lastPoint();


            updateDeltaM(p1, p2);


            if (ViscousProfileData.instance().getPreviousDot() * ViscousProfileData.instance().getDot() < 0.) {
                System.out.println("f(A)*f(M) < 0");
                sigmaB = sigmaM;
                //ViscousProfileData.instance().setPreviousSigma(sigmaA);
                
            } else {
                System.out.println("f(A)*f(M) > 0");
                sigmaA = sigmaM;
                //ViscousProfileData.instance().setPreviousSigma(sigmaB);
            }

            //System.out.println("Passo " +i +" Valores dos Uplus depois do teste da bissecao: ");
            //System.out.println(ViscousProfileData.instance().getPreviousUPlus() + "  e  " + ViscousProfileData.instance().getUplus());
            //System.out.println("Passo " +i +" Intervalo de sigma depois do teste da bissecao : " +sigmaA +" , " +sigmaB);

            i++;

        } while (i < nmax);

        System.out.println("Tempo em 10 passos da bissecao :::::::::::::::::::: " +(System.currentTimeMillis()-t));

        System.out.println("SAIU DA BISSECAO COM SIGMA = " +ViscousProfileData.instance().getSigma());


        // --- Substituir este trecho pelo concat, talvez...
        OrbitPoint[] pointsArray = new OrbitPoint[orbitXZero.getPoints().length+orbitUPlus.getPoints().length];
        for (int j = 0; j < orbitXZero.getPoints().length; j++) {
            pointsArray[j] = orbitXZero.getPoints()[j];

        }

        int k = 0;
        for (int j = orbitUPlus.getPoints().length-1; j >=0; j--) {
            pointsArray[k + orbitXZero.getPoints().length] =orbitUPlus.getPoints()[j];
            k++;
        }
        // --------------------------------------------------

        Orbit result = new Orbit(pointsArray, Orbit.BOTH_DIR);
        
        return new ConnectionOrbit(xZero, uPlus, result);
        
    }

    // updating initial point for the manifold orbit
    protected RealVector newPointShift(RealVector x_Stat, RealVector x_t0, RealMatrix2 X_t0, RealVector xi) {
        int m = x_t0.getSize();
        RealVector ddx0 = new RealVector(m);
        ddx0.mul(X_t0, xi);
//        RealVector f0 = new RealVector(RPNUMERICS.flow().flux(x_t0));

//        WaveState input = new WaveState(new PhasePoint(x_t0));
//        JetMatrix output = new JetMatrix (m);
//        
//        flow_.jet(input, output, 0);

        RealVector f0 = flow_.flux(x_t0);



//        RealVector f0 = flow_.flux((x_t0));

        RealVector scaleF = new RealVector(m);
        scaleF.scale(f0.dot(ddx0) / f0.dot(f0), f0);
        ddx0.sub(scaleF);
        ddx0.add(x_t0);
        ddx0.sub(x_Stat);
        return ddx0;
    }

    public String getCalcMethodName() {
        return methodCalcName_;

    }

     public RpSolution recalc() throws RpException {
        ViscousProfileData.instance().setSigma(sB);
        ViscousProfileData.instance().setPreviousSigma(sA);
        ViscousProfileData.instance().setDot(dotB);
        ViscousProfileData.instance().setPreviousDot(dotA);

        System.out.println("Entrei no recalc() de ConnectionOrbitCalc ************************");
        return calc();
    }

    public RpSolution recalc(Area area) throws RpException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

   
}
