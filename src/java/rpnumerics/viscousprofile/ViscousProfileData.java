/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics.viscousprofile;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import rpn.plugininterface.PluginProfile;
import rpn.plugininterface.PluginTableModel;
import rpnumerics.PhasePoint;
import rpnumerics.RPNUMERICS;
import rpnumerics.RpException;
import rpnumerics.ShockRarefactionProfile;
import wave.util.RealVector;
import wave.util.SimplexPoincareSection;
import rpnumerics.Configuration;
import rpnumerics.StationaryPoint;
import rpnumerics.StationaryPointCalc;

public class ViscousProfileData extends ShockRarefactionProfile {

    private static String hugoniotMethodName_ = "Contour";//Default method
    private static boolean hugoniotSpecific_ = false;
    private static ViscousProfileData instance_ = null;
    private static RealVector fx0_ = null;
    private PhasePoint Uplus_ = null;
    private PhasePoint previousUPlus_;
    private SimplexPoincareSection poincare_;
    private double dot_;
    private double previousSigma;
    private double sigma_;
    private PhasePoint previousXZero_;
    private double previousDot;
    private String[] previousParams;
    private PhasePoint xZero_;
    public static final String SHOCKFLOW_NAME = "ShockFlow";
    public static final String[] HUGONIOT_METHOD_NAMES = {"Continuation", "Contour"}; //TODO Put these names into a better place

    public RealVector getFx0() {
        return fx0_;
    }

    public void setFx0(RealVector aFx0_) {
        fx0_ = aFx0_;
    }

    private ViscousProfileData() {
        super(new PhasePoint(new RealVector(2)));
        previousDot = 0.;
    }

    public static ViscousProfileData instance() {

        if (instance_ == null) {

            instance_ = new ViscousProfileData();
            return instance_;
        }

        return instance_;
    }

    public PhasePoint getUminus() {
//        return uminus_;
        PluginProfile profile = PluginTableModel.getPluginConfig(SHOCKFLOW_NAME);
        String data = profile.getParamValue("xzero");
        return new PhasePoint(new RealVector(data));
    }

    public SimplexPoincareSection getPoincare() {
        return poincare_;
    }

    public void setPoincare(SimplexPoincareSection poincare) {
        poincare_ = poincare;
    }

    public PhasePoint getUplus() {
        return Uplus_;
    }

    public PhasePoint getPreviousUPlus() {
        return previousUPlus_;
    }

    public void setUplus(PhasePoint Uplus) {
        previousUPlus_ = Uplus_;
        Uplus_ = Uplus;
    }

    public void setUplusM(PhasePoint Uplus) {
        Uplus_ = Uplus;
        System.out.println("Setou UplusM em :::::::::::: " + Uplus.getCoords());
    }

    public void setPreviousUplus(PhasePoint Uplus) {
        previousUPlus_ = Uplus;
    }

    public boolean changedDotSignal() {
        return ((previousDot * dot_) < 0.);
    }

    //*** Usado fora da bisseção em sigma
    public void updateDelta(RealVector pUref, RealVector pUPlus) {
        RealVector poincareLimits = new RealVector(2);
        poincareLimits.sub(poincare_.getPoints()[0], poincare_.getPoints()[1]);
        RealVector connectionLimits = new RealVector(2);
        connectionLimits.sub(pUref, pUPlus);

        previousDot = getDot();
        dot_ = poincareLimits.dot(connectionLimits);

        System.out.println("Sinal do produto interno :::::::: " + Math.signum(dot_));
    }

    public double getPreviousDot() {
        return previousDot;
    }

    public double getDot() {
        return dot_;
    }

    public void setDot(double dot) {
        dot_ = dot;
    }

    public double getPreviousSigma() {
        return previousSigma;
    }

    public PhasePoint getPreviousXZero() {
        return previousXZero_;
    }

    public double getSigma() {
        return sigma_;
    }

    public void setPreviousSigma(double sigma) {
        previousSigma = sigma;
    }

    public void setSigma(double sigma) {
        previousSigma = getSigma();
        sigma_ = sigma;
        previousXZero_ = getXZero();

        Configuration physics = RPNUMERICS.getConfiguration(RPNUMERICS.physicsID());
        Configuration previousConfig = physics.getConfiguration("fluxfunction");

        previousParams = new String[previousConfig.getParamsSize()];

        for (int i = 0; i < previousConfig.getParamsSize(); i++) {
            previousParams[i] = previousConfig.getParam(i);
        }

    }

    public void setSigmaM(double sigma) {
        sigma_ = sigma;
    }

    public void setPreviousXZero(PhasePoint xZero) {
        previousXZero_ = xZero;
    }

    @Override
    public void setXZero(PhasePoint xZero) {
        previousXZero_ = getXZero();
        xZero_ = xZero;

        Configuration physics = RPNUMERICS.getConfiguration(RPNUMERICS.physicsID());
        Configuration previousConfig = physics.getConfiguration("fluxfunction");

        previousParams = new String[previousConfig.getParamsSize()];

        for (int i = 0; i < previousConfig.getParamsSize(); i++) {
            previousParams[i] = previousConfig.getParam(i);
        }

        previousSigma = sigma_;

    }

    @Override
    public PhasePoint getXZero() {

        return xZero_;

    }

    public List<StationaryPoint> updateStationaryList(List<RealVector> list) {

        List<StationaryPoint> statList = new ArrayList<StationaryPoint>();

        for (RealVector stat : list) {
            StationaryPointCalc statCalc = new StationaryPointCalc(new PhasePoint(stat), xZero_);
            try {
                statList.add((StationaryPoint) statCalc.calc());
            } catch (RpException ex) {
                Logger.getLogger(ViscousProfileData.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        updateStationaryList(statList);

        return statList;
    }

    public void setPreviousPhysicsParams(String[] params) {
        previousParams = params;
    }

    public String[] getPreviousPhysicsParams() {
        return previousParams;
    }

    public String getHugoniotMethodName() {
        return hugoniotMethodName_;
    }

    public void setHugoniotMethodName(String hugoniotMethodName_) {
        ViscousProfileData.hugoniotMethodName_ = hugoniotMethodName_;
    }

    public boolean isHugoniotSpecific() {
        return hugoniotSpecific_;
    }

    public void setHugoniotSpecific(boolean hugoniotSpecific_) {
        ViscousProfileData.hugoniotSpecific_ = hugoniotSpecific_;
    }

    private native void updateStationaryList(List<StationaryPoint> statList);
}
