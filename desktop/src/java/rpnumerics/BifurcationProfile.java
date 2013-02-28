package rpnumerics;

import java.util.ArrayList;
import wave.util.Boundary;

public class BifurcationProfile {

    private static int plusFamily_;
    private static int minusFamily_;
    private static BifurcationProfile instance_;
    private ArrayList<Area> selectedAreas_;
    private Boundary boundary_;

    private BifurcationProfile() {
        minusFamily_ = 0;
        plusFamily_ = 0;
        selectedAreas_ = new ArrayList<Area>();
    }

    public static BifurcationProfile instance() {

        if (instance_ == null) {
            instance_ = new BifurcationProfile();
            return instance_;
        }

        return instance_;
    }

    public int getPlusFamily() {
        return plusFamily_;
    }

    public void setPlusFamily(int aPlusFamily_) {
        plusFamily_ = aPlusFamily_;
    }

    public int getMinusFamily() {
        return minusFamily_;
    }

    public void setMinusFamily(int aMinusFamily_) {
        minusFamily_ = aMinusFamily_;
    }

    public Boundary getBoundary() {
        return boundary_;
    }

    public void setBoundary(Boundary boundary) {
        this.boundary_ = boundary;
    }

    public ArrayList<Area> getSelectedAreas() {
        return selectedAreas_;
    }

    public void addArea(Area area) {

        selectedAreas_.add(area);
        //DEBUG
        for (Area element : selectedAreas_) {
            System.out.println(element.toString());
        }
    }
}
