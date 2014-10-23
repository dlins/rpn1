package rpnumerics;

import rpn.plugininterface.PluginProfile;
import rpn.plugininterface.PluginTableModel;
import wave.util.RealVector;

public class RarefactionProfile extends ShockRarefactionProfile {

//    private int family_;
    private String methodName_ = "default";
    private static RarefactionProfile instance_;
    public static String RAREFACTIONFLOW_NAME = "RarefactionFlow";

    private RarefactionProfile() {

        super(new PhasePoint(new RealVector(2)));
//        family_ = 0;


    }

    public static RarefactionProfile instance() {

        if (instance_ == null) {
            instance_ = new RarefactionProfile();
            return instance_;
        }
        return instance_;
    }

    public int getFamily() {
//        return family_;
        PluginProfile profile = PluginTableModel.getPluginConfig(RAREFACTIONFLOW_NAME);
        String strValue = profile.getParamValue("family");
        return (new Integer (strValue)).intValue();
        
        
    }

    public void setFamily(int family) {
//        family_ = aFamily_;
        PluginProfile profile = PluginTableModel.getPluginConfig(RAREFACTIONFLOW_NAME);
        profile.setPluginParm("family", new Integer(family).toString());
    }
    
     @Override
    public void setXZero(PhasePoint xZero) {
        PluginProfile profile = PluginTableModel.getPluginConfig(RAREFACTIONFLOW_NAME);
        profile.setPluginParm("xzero", xZero.toString());
    }

    @Override
    public PhasePoint getXZero() {

        PluginProfile profile = PluginTableModel.getPluginConfig(RAREFACTIONFLOW_NAME);
        String data = profile.getParamValue("xzero");
        return new PhasePoint(new RealVector(data));
    }

    public String geRarefactiontMethodName() {
        return methodName_;
    }

    public void setRarefactionMethodName(String methodName_) {
        this.methodName_ = methodName_;
    }
}
