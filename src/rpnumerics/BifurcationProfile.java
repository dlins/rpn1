package rpnumerics;

import wave.util.Boundary;

public class BifurcationProfile {

    private static int plusFamily_;
    private static int minusFamily_;
    private static BifurcationProfile instance_;

    private Boundary boundary_;

    public Boundary getBoundary() {
        return boundary_;
    }

    public void setBoundary(Boundary boundary) {
        this.boundary_ = boundary;
    }


    private BifurcationProfile() {
        minusFamily_ = 0;
        plusFamily_ = 0;
    }

    public static BifurcationProfile instance() {

        if (instance_ == null) {
            instance_ = new BifurcationProfile();
            return instance_;
        }

        return instance_;
    }

    public  int getPlusFamily() {
        return plusFamily_;
    }

    public  void setPlusFamily(int aPlusFamily_) {
        plusFamily_ = aPlusFamily_;
    }

    public int getMinusFamily() {
        return minusFamily_;
    }

    public  void setMinusFamily(int aMinusFamily_) {
        minusFamily_ = aMinusFamily_;
    }
}
