package rpnumerics;

import wave.util.RealVector;

public class ShockProfile extends ShockRarefactionProfile {

    private static double sigma_=0d;
    private static String hugoniotMethodName_;
    private static boolean hugoniotSpecific_;
    private static PhasePoint uminus_;
    private static ShockProfile instance_ = null;
    private static RealVector fx0_=null;

    public  RealVector getFx0() {
        return fx0_;
    }

    public  void setFx0(RealVector aFx0_) {
        fx0_ = aFx0_;
    }

    private ShockProfile() {
        super(new PhasePoint(new RealVector(2)));
        sigma_ = 0d;

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
        return sigma_;
    }

    public void setSigma(double sigma_) {
        ShockProfile.sigma_ = sigma_;
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
