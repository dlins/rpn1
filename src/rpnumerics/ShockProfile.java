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

public class ShockProfile extends ShockRarefactionProfile {

    private static String hugoniotMethodName_;
    private static boolean hugoniotSpecific_;
    private static PhasePoint uminus_;
    private static ShockProfile instance_ = null;
    private static RealVector fx0_ = null;
    public static String SHOCKFLOW_NAME = "ShockFlow";

    public RealVector getFx0() {
        return fx0_;
    }

    public void setFx0(RealVector aFx0_) {
        fx0_ = aFx0_;
    }

    private ShockProfile() {
        super(new PhasePoint(new RealVector(2)));


        hugoniotMethodName_ = "continuation";
        hugoniotSpecific_ = false;


    }

    public static ShockProfile instance() {

        if (instance_ == null) {

            instance_ = new ShockProfile();
            return instance_;
        }

        return instance_;
    }

    public PhasePoint getUminus() {
        return uminus_;
    }

    public void setUminus(PhasePoint aUminus_) {
        uminus_ = aUminus_;
    }

    public double getSigma() {
        PluginProfile profile = PluginTableModel.getPluginConfig(SHOCKFLOW_NAME);
        String strValue = profile.getParamValue("sigma");
        return (new Double(strValue)).doubleValue();

    }

    public void setSigma(double sigma_) {

        PluginProfile profile = PluginTableModel.getPluginConfig(SHOCKFLOW_NAME);
        profile.setPluginParm("sigma", new Double(sigma_).toString());
    }

    @Override
    public void setXZero(PhasePoint xZero) {
        PluginProfile profile = PluginTableModel.getPluginConfig(SHOCKFLOW_NAME);
        profile.setPluginParm("xzero", xZero.toString());
    }

    @Override
    public PhasePoint getXZero() {

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
