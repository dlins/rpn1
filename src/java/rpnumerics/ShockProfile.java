/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import rpn.plugininterface.PluginProfile;
import rpn.plugininterface.PluginTableModel;
import wave.util.RealVector;
import wave.util.SimplexPoincareSection;

public class ShockProfile extends ShockRarefactionProfile {

    private static String hugoniotMethodName_="Contour";//Default method 
    private static boolean hugoniotSpecific_=false;
    private static ShockProfile instance_ = null;
    private static RealVector fx0_ = null;
    private PhasePoint Uplus_ = null;
    private PhasePoint previousUPlus_;
    private SimplexPoincareSection poincare_;
    private double dot_;
    private double previousSigma;


    public static final String SHOCKFLOW_NAME = "ShockFlow";
    public static final String [] HUGONIOT_METHOD_NAMES={"Continuation","Contour"}; //TODO Put these names into a better place

    public RealVector getFx0() {
        return fx0_;
    }

    public void setFx0(RealVector aFx0_) {
        fx0_ = aFx0_;
    }

    private ShockProfile() {
        super(new PhasePoint(new RealVector(2)));
    }

    public static ShockProfile instance() {

        if (instance_ == null) {

            instance_ = new ShockProfile();
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


    public void setPoincare(SimplexPoincareSection poincare){
        poincare_=poincare;
    }

    public PhasePoint getUplus() {
        return Uplus_;
    }

    public PhasePoint getPreviousUPlus(){
        return previousUPlus_;
    }

    public void setUplus(PhasePoint Uplus) {

        previousUPlus_=Uplus_;
        Uplus_ = Uplus;

    }


    public void updateDelta(RealVector pUref, RealVector pUPlus){

        RealVector poincareLimits = new RealVector(2);


        poincareLimits.sub(poincare_.getPoints()[0], poincare_.getPoints()[1]);

        RealVector connectionLimits = new RealVector(2);


        connectionLimits.sub(pUref, pUPlus);

        dot_= Math.signum(poincareLimits.dot(connectionLimits));

        System.out.println("Sinal do produto interno :::::::: " +Math.signum(dot_));


    }

    public double getDot() {
        return dot_;
    }


    public double getPreviousSigma() {
        return previousSigma;
    }


    public double getSigma() {
        PluginProfile profile = PluginTableModel.getPluginConfig(SHOCKFLOW_NAME);
        String strValue = profile.getParamValue("sigma");
        return (new Double(strValue)).doubleValue();

    }

    public void setSigma(double sigma_) {

        previousSigma = getSigma();

        PluginProfile profile = PluginTableModel.getPluginConfig(SHOCKFLOW_NAME);
        profile.setPluginParm("sigma", new Double(sigma_).toString());
    }

    @Override
    public void setXZero(PhasePoint xZero) {
        PluginProfile profile = PluginTableModel.getPluginConfig(SHOCKFLOW_NAME);
        profile.setPluginParm("xzero", xZero.toString());
//        uminus_ = xZero;
    }

    @Override
    public PhasePoint getXZero() {

//        return uminus_;
        PluginProfile profile = PluginTableModel.getPluginConfig(SHOCKFLOW_NAME);
        String data = profile.getParamValue("xzero");
        return new PhasePoint(new RealVector(data));
    }

    public String getHugoniotMethodName() {
        return hugoniotMethodName_;
    }

    public void setHugoniotMethodName(String hugoniotMethodName_) {
        ShockProfile.hugoniotMethodName_ = hugoniotMethodName_;
    }

    public boolean isHugoniotSpecific() {
        return hugoniotSpecific_;
    }

    public void setHugoniotSpecific(boolean hugoniotSpecific_) {
        ShockProfile.hugoniotSpecific_ = hugoniotSpecific_;
    }
}
