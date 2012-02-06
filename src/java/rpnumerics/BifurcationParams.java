package rpnumerics;

public class BifurcationParams {

    private int[] resolution_;

    public BifurcationParams(int[] resolution) {
        resolution_ = resolution;
    }

    public BifurcationParams(){

    }

    public int[] getResolution() {
        return resolution_;
    }


    @Override
    public String toString() {

        StringBuilder resolution = new StringBuilder();
        StringBuilder result = new StringBuilder();
        
        //Adding resolution
        resolution.append("resolution=\"");
        for (int i = 0; i < resolution_.length; i++) {
            resolution.append(resolution_[i]+" ");
        }

        result.append(resolution.toString().trim());
        result.append("\" ");
        //

        return result.toString();

}


}
