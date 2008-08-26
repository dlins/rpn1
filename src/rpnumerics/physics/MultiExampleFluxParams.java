package rpnumerics.physics;

import wave.util.RealVector;

public class MultiExampleFluxParams extends FluxParams {


    public MultiExampleFluxParams(RealVector params) {

        super("MultiExampleParams",params,0);

    }



      public MultiExampleFluxParams(RealVector params,int index) {

        super("MultiExampleParams",params,index);

    }

    /**
     * defaultParams
     *
     * @return FluxParams
     * @todo Implement this rpnumerics.physics.FluxParams method
     *
     *
     *
     */





    public FluxParams defaultParams() {
        return null;
    }
}
