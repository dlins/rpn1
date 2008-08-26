package rpnumerics;

import wave.util.RealVector;

public class RarefactionProfile extends ShockRarefactionProfile{


    private int family_;
    private String methodName_ = "default";
    private static RarefactionProfile instance_;

    private RarefactionProfile() {

        super(new PhasePoint(new RealVector(2)));
        family_ = 0;


    }

    public static RarefactionProfile instance() {

        if (instance_ == null) {
            instance_ = new RarefactionProfile();
            return instance_;
        }
        return instance_;
    }

   
  
    public int getFamily() {
        return family_;
    }

    public void setFamily(int aFamily_) {
        family_ = aFamily_;
    }

    
    public String geRarefactiontMethodName() {
        return methodName_;
    }

    public void setRarefactionMethodName(String methodName_) {
        this.methodName_ = methodName_;
    }
    
   
}
