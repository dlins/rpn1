package rpnumerics;

import wave.util.Boundary;

public class BifurcationParams {

    private int familyIndex_;
    private static int DEFAULT_FAMILY_INDEX = 0;
    private Boundary boundary_;

    

    public BifurcationParams(Boundary boundary) {
        this.boundary_ = boundary;
    }

    public BifurcationParams(int familyIndex) {
        this.familyIndex_ = familyIndex;
    }

    public BifurcationParams() {
        this.familyIndex_ = DEFAULT_FAMILY_INDEX;
    }

    public int getFamilyIndex() {
        return familyIndex_;
    }

    public void setFamilyIndex(int familyIndex) {
        this.familyIndex_ = familyIndex;
    }

    public Boundary getBoundary() {
        return boundary_;
    }
}
