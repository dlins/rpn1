/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */// Orbit on the manifols
//
// stationaryPoint  the stationary point
// firstPoint       point for an orbit to start
// timDirection +1  for unstable manifold, -1 for stable manifold
// orbit            orbit on the manifold starting with firstPoint
package rpnumerics;

import wave.util.RealVector;
import wave.util.RealMatrix2;
import wave.multid.view.ViewingAttr;
import java.awt.Color;
import rpn.component.MultidAdapter;
import wave.util.JetMatrix;

public class ManifoldOrbit extends RPnCurve implements RpSolution {
    //
    // Members
    //
    private StationaryPoint stationaryPoint_;
    // TODO Alexei, please comment this (meaning of firstPoint or pass through point ?)
    private PhasePoint firstPoint_;
    private Orbit orbit_;
    private int timeDirection_;
    private int finishType_;

    //
    // Constructor
    //
    public ManifoldOrbit(StationaryPoint stationaryPoint, PhasePoint firstPoint, Orbit orbit, int timeDirection) {
        super(MultidAdapter.converseOrbitPointsToCoordsArray(orbit.getPoints()), new ViewingAttr(Color.ORANGE));
        stationaryPoint_ = new StationaryPoint(stationaryPoint);
        orbit_ = orbit;
        firstPoint_ = firstPoint;
        timeDirection_ = timeDirection;
        finishType_ = orbit_.getIntegrationFlag();
    }

    //
    // Accessors/Mutators
    //
    public void setStationaryPoint(StationaryPoint p) {
        stationaryPoint_ = new StationaryPoint(p);
    }

    public PhasePoint getFirstPoint() {
        return firstPoint_;
    }

    public void setTimeDirection(int tDir) {
        timeDirection_ = tDir;
    }

    public StationaryPoint getStationaryPoint() {
        return stationaryPoint_;
    }

    public Orbit getOrbit() {
        return orbit_;
    }

    public int getTimeDirection() {
        return timeDirection_;
    }

    public int getFinishType() {
        return finishType_;
    }

    //
    // Methods
    //
    public ManifoldSensitivity sensitivity() {

        ShockFlow flow = RPNUMERICS.createShockFlow();

        // getting local information at the stationary point
        StationaryPoint stationaryPoint = getStationaryPoint();
        int timeDirection = getTimeDirection();
        int m = stationaryPoint.getSchurFormP().getNumRow();
        RealMatrix2 schurVec = new RealMatrix2(m, m);
        int k;
        if (timeDirection < 0) {
            schurVec.set(stationaryPoint.getSchurVecN());
            k = stationaryPoint.getDimN();
        } else {
            schurVec.set(stationaryPoint.getSchurVecP());
            k = stationaryPoint.getDimP();
        }
        RealMatrix2 Uu = new RealMatrix2(m, k);
        schurVec.copySubMatrix(0, 0, m, k, 0, 0, Uu);
        int pointsNumber = getOrbit().getPoints().length;
        // stationary point
        RealVector x_Stationary = new RealVector(stationaryPoint.getPoint().getCoords());
        RealVector Xp_Stationary = new RealVector(m);


//            RealMatrix2 F = flow.fluxDeriv(x_Stationary);//new RealMatrix2(RPNUMERICS.flow().fluxDeriv(x_Stationary));
        JetMatrix output = new JetMatrix(flow.getXZero().getSize());
        RealMatrix2 F = new RealMatrix2(output.n_comps(), output.n_comps());

//        flow.jet(x_Stationary, output, 1);
//        for (int i = 0; i < output.n_comps(); i++) {
//            for (int j = 0; j < output.n_comps(); j++) {
//                F.setElement(i, j, output.getElement(i, j));
//            }
//        }

        F = flow.fluxDeriv(x_Stationary);

        F.invert();
        F.negate();
        RealVector G = flow.fluxDerivSigma(x_Stationary);//new RealVector(((ConservationShockFlow)RPNUMERICS.flow()).fluxDerivSigma(x_Stationary));
        Xp_Stationary.mul(F, G);
        // first point
        RealVector x0 = new RealVector(getOrbit().getPoints()[0].getCoords());
        RealMatrix2 XZero = new RealMatrix2(Uu);
        // last point
        RealVector x1 = new RealVector(getOrbit().lastPoint().getCoords());
        RealMatrix2 X1 = new RealMatrix2(m, k);
        RealVector Xp1 = new RealVector(m);
        // setting initial conditions
        RealMatrix2 Qx = new RealMatrix2(Uu);
        RealMatrix2 Rx = new RealMatrix2(k, k);
        RealVector sens = new RealVector(RealMatrix2.Matrices2Vector(Qx, Rx, Xp_Stationary));
        RealVector x = new RealVector(m);
        double h;
        for (int i = 0; i < pointsNumber - 1; i++) {
            x.set(getOrbit().getPoints()[i].getCoords());
            h = getOrbit().getPoints()[i + 1].getTime() - getOrbit().getPoints()[i].getTime();
            sens.set(rk4(x, sens, k, h));
        }
        // extracting the final information
        RealMatrix2.Vector2Matrices(sens, Qx, Rx, Xp1);
        X1.mul(Qx, Rx);
        return new ManifoldSensitivity(x_Stationary, Xp_Stationary, x0, XZero, x1, X1, Xp1);
    }

    // returns the vector consisting of
    // columns of the matrices Qx, Rx and
    // vecotr Xp
//    protected RealVector Matrices2Vector(RealMatrix2 Qx, RealMatrix2 Rx, RealVector Xp) {
//        int m = Qx.getNumRow();
//        int k = Qx.getNumCol();
//        RealVector result = new RealVector(m * k + k * (k + 1) / 2 + m);
//        int a = 0;
//        int i, j;
//        for (i = 0; i < m; i++)
//            for (j = 0; j < k; j++) {
//                result.setElement(a, Qx.getElement(i, j));
//                a = a + 1;
//            }
//        for (i = 0; i < k; i++)
//            for (j = i; j < k; j++) {
//                result.setElement(a, Rx.getElement(i, j));
//                a = a + 1;
//            }
//        for (i = 0; i < m; i++) {
//            result.setElement(a, Xp.getElement(i));
//            a = a + 1;
//        }
//        return result;
//    }

    // having a vector source, returns
    // the matrices Qx, Rx and vector Xp
    // as the inverse operation to Matrices2Vector function
//    protected void Vector2Matrices(RealVector source, RealMatrix2 Qx, RealMatrix2 Rx, RealVector Xp) {
//        int k = Qx.getNumCol();
//        int m = Qx.getNumRow();
//        int a = 0;
//        int i, j;
//        for (i = 0; i < m; i++)
//            for (j = 0; j < k; j++) {
//                Qx.setElement(i, j, source.getElement(a));
//                a = a + 1;
//            }
//        Rx.setZero();
//        for (i = 0; i < k; i++)
//            for (j = i; j < k; j++) {
//                Rx.setElement(i, j, source.getElement(a));
//                a = a + 1;
//            }
//        for (i = 0; i < m; i++) {
//            Xp.setElement(i, source.getElement(a));
//            a = a + 1;
//        }
//    }

    // provides the vector form sens' = result
    // for the right-hand side of equations
    // Qx' Rx + Qx Rx' = F Qx Rx
    // QxT' Qx + QxT Qx' = 0
    // Xp' = F Xp + G
    //
    // sens is a vector containing columns of
    // the matrices Qx, Rx and vector Xp
    // as returned by Matrices2Vector function
    protected RealVector sensitivityFunction(RealVector x, RealVector sens, int k) {

        ShockFlow flow = RPNUMERICS.createShockFlow();

        int m = x.getSize();
        RealMatrix2 Qx = new RealMatrix2(m, k);
        RealMatrix2 Rx = new RealMatrix2(k, k);
        RealVector Xp = new RealVector(m);
        RealMatrix2 dQx = new RealMatrix2(m, k);
        RealMatrix2 dRx = new RealMatrix2(k, k);
        RealVector dXp = new RealVector(m);
        RealMatrix2.Vector2Matrices(sens, Qx, Rx, Xp);
//        RealMatrix2 F = new RealMatrix2(RPNUMERICS.flow().fluxDeriv(x));
//        RealMatrix2 F = flow.fluxDeriv(x);//new RealMatrix2(RPNUMERICS.flow().fluxDeriv(x));


        JetMatrix output = new JetMatrix(flow.getXZero().getSize());
        RealMatrix2 F = new RealMatrix2(output.n_comps(), output.n_comps());

//        flow.jet(x, output, 1);
//        for (int i = 0; i < output.n_comps(); i++) {
//            for (int j = 0; j < output.n_comps(); j++) {
//                F.setElement(i, j, output.getElement(i, j));
//            }
//        }
        F = flow.fluxDeriv(x);



        RealVector G = flow.fluxDerivSigma(x);//new RealVector(((ConservationShockFlow)RPNUMERICS.flow()).fluxDerivSigma(x));
        RealMatrix2.QRFunction(Qx, Rx, F, dQx, dRx);
        dXp.mul(F, Xp);
        dXp.add(G);
        RealVector result = new RealVector(RealMatrix2.Matrices2Vector(dQx, dRx, dXp));
        return result;
    }

    // provides the vector form z' = result
    // for the right-hand side of equations
    // Q' R + Q R' = F Q R
    // QT' Q + QT Q' = 0
//    protected void QRFunction(RealMatrix2 Q, RealMatrix2 R, RealMatrix2 F, RealMatrix2 dQ, RealMatrix2 dR) {
//        RealMatrix2 Qcopy = new RealMatrix2(Q);
//        RealMatrix2 Rcopy = new RealMatrix2(R);
//        RealMatrix2 Fcopy = new RealMatrix2(F);
//        int k = Q.getNumCol();
//        int m = Q.getNumRow();
//        int dim = m * k + k * (k + 1) / 2;
//        double tmp;
//        RealMatrix2 C = new RealMatrix2(dim, dim);
//        C.setZero();
//        RealVector x = new RealVector(dim);
//        // Q'R+QR' = FQR
//        RealMatrix2 RcopyT = new RealMatrix2(Rcopy);
//        RcopyT.transpose();
//        for (int i = 0; i < m; i++)
//            for (int j = 0; j < k; j++)
//                for (int n = 0; n <= j; n++) {
//                    //Q'R
//                    C.setElement(i * k + j, i * k + n, Rcopy.getElement(n, j));
//                    C.setElement(i * k + j, k * m + j * (j + 1) / 2 + n, Qcopy.getElement(i, n));
//                }
//        RealMatrix2 FQR = new RealMatrix2(m, k);
//        FQR.mul(Fcopy, Qcopy);
//        FQR.mul(Rcopy);
//        //FQR
//        for (int i = 0; i < m; i++)
//            for (int j = 0; j < k; j++)
//                x.setElement(i * k + j, FQR.getElement(i, j));
//        // Q'T Q+QT Q' = 0
//        for (int i = 0; i < k; i++)
//            for (int j = 0; j <= i; j++)
//                for (int n = 0; n < m; n++) {
//                    //Q'T Q
//                    C.setElement(k * m + i * (i + 1) / 2 + j, n * k + i,
//                        C.getElement(k * m + i * (i + 1) / 2 + j, n * k + i) + Qcopy.getElement(n, j));
//                    //QT Q'
//                    C.setElement(k * m + i * (i + 1) / 2 + j, n * k + j,
//                        C.getElement(k * m + i * (i + 1) / 2 + j, n * k + j) + Qcopy.getElement(n, i));
//                }
//        // solve C result = x
//        RealVector result = new RealVector(wave.util.MathUtil.linearSolver(C, x));
//        for (int i = 0; i < m; i++)
//            for (int j = 0; j < k; j++)
//                dQ.setElement(i, j, result.getElement(i * k + j));
//        dR.setZero();
//        for (int i = 0; i < k; i++)
//            for (int j = i; j < k; j++) {
//                dR.setElement(i, j, result.getElement(k * m + j * (j + 1) / 2 + i));
//            }
//    }
    protected RealVector rk4(RealVector x0, RealVector sens0, int k, double h) {
        // Runge-Kutta step 4 th order
        ShockFlow flow = (ShockFlow) RPNUMERICS.createShockFlow();


//        RealVector x = new RealVector(x0);

//        WaveState input = new WaveState(new PhasePoint(x0));


//        JetMatrix output = new JetMatrix(x0.getSize());



        RealVector sens = new RealVector(sens0);
        double halfH = 0.5 * h;
//        RealVector halfK1x = flow.flux(x);//new RealVector(RPNUMERICS.flow().flux(x));

//        flow.jet(input, output, 0);



        RealVector halfK1x = flow.flux(x0);

//        RealVector halfK1sens = new RealVector(sensitivityFunction(x, sens, k));
        RealVector halfK1sens = new RealVector(sensitivityFunction(x0, sens, k));
        halfK1x.scale(halfH);
        halfK1sens.scale(halfH);

//        x.add(x0, halfK1x);

        x0.add(x0, halfK1x);


        sens.add(sens0, halfK1sens);

//        flow.jet(input, output, 0);


//        RealVector halfK2x = flow.flux(x);//new RealVector(RPNUMERICS.flow().flux(x));

        RealVector halfK2x = flow.flux(x0);


//        RealVector halfK2sens = new RealVector(sensitivityFunction(x, sens, k));

        RealVector halfK2sens = new RealVector(sensitivityFunction(x0, sens, k));
        halfK2x.scale(halfH);
        halfK2sens.scale(halfH);

//        x.add(x0, halfK2x);
        x0.add(x0, halfK2x);
        
        


        sens.add(sens0, halfK2sens);
//        flow.jet(input, output, 0);
        
        
        

        RealVector k3x = flow.flux(x0);

//        RealVector k3x = flow.flux(x);//new RealVector(RPNUMERICS.flow().flux(x));

//        RealVector k3sens = new RealVector(sensitivityFunction(x, sens, k));
        RealVector k3sens = new RealVector(sensitivityFunction(x0, sens, k));
        k3x.scale(h);
        k3sens.scale(h);


        x0.add(x0, k3x);
//        x.add(x0, k3x);
        sens.add(sens0, k3sens);
//        flow.jet(input, output, 0);
        
//        RealVector k4x = flow.flux(x);//new RealVector(RPNUMERICS.flow().flux(x));
        RealVector k4x = flow.flux(x0);
//        RealVector k4sens = new RealVector(sensitivityFunction(x, sens, k));

        RealVector k4sens = new RealVector(sensitivityFunction(x0, sens, k));
        k4x.scale(h);
        k4sens.scale(h);
        RealVector result = new RealVector(sens0);
        halfK1sens.scale(1.0 / 3.0);
        result.add(halfK1sens);
        halfK2sens.scale(2.0 / 3.0);
        result.add(halfK2sens);
        k3sens.scale(1.0 / 3.0);
        result.add(k3sens);
        k4sens.scale(1.0 / 6.0);
        result.add(k4sens);
        return result;
    }

    public int findClosestSegment(RealVector point, double alfa) {
        return 0;
    }

    public String toXML() {

        StringBuffer buffer = new StringBuffer();

        buffer.append("<MANIFOLD timedirection=\"" + getTimeDirection() + "\">\n");
        buffer.append(getStationaryPoint().toXML());
//      buffer.append(getFirstPoint().toXML());
        buffer.append(getOrbit().toXML());
        buffer.append("</MANIFOLD>");

        return buffer.toString();


    }

    public String toXML(boolean calcReady) {

        StringBuffer buffer = new StringBuffer();
        if (calcReady) {

            buffer.append("<MANIFOLD timedirection=\"" + getTimeDirection() + "\">\n");
            buffer.append(getStationaryPoint().toXML());
//      buffer.append(getFirstPoint().toXML());
            buffer.append(getOrbit().toXML());
            buffer.append("</MANIFOLD>");

        }
        return buffer.toString();


    }
}
