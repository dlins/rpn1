package rpnumerics;

public class BifurcationParams {

    private int familyIndex_;
    private static int DEFAULT_FAMILY_INDEX = 0;

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
}
