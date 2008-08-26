package rpnumerics.physics;

import rpnumerics.FluxFunction;
import wave.multid.*;
import wave.util.*;

public class MultiPhysicsExample implements Physics {


    static public final String FLUX_ID = "MultiExampleR2";
    static public final String DEFAULT_SIGMA = "-.021";
    static public final String DEFAULT_XZERO = ".13 .07";
    static public final RectBoundary DEFAULT_BOUNDARY = new RectBoundary(new
            RealVector(
                    new double[] { -.5, -.5}), new RealVector(
                            new double[] {.5, .5}));

    //
    // Members
    //

    private FluxFunction fluxFunction_;
    private FluxFunction[] fluxFunctionArray_;
    private AccumulationFunction accumulationFunction_;
    private Boundary boundary_ = DEFAULT_BOUNDARY;

    private static MultiExampleFluxParams [] DEFAULT_PARAMS =
            new MultiExampleFluxParams[] {new MultiExampleFluxParams (new RealVector(2),0),new MultiExampleFluxParams (new RealVector(2),1)};


    public MultiPhysicsExample(String libName) {

        this(libName,DEFAULT_PARAMS);

    }

    public MultiPhysicsExample(String libName,MultiExampleFluxParams [] params) {

        System.loadLibrary(libName);

        fluxFunctionArray_ = new FluxFunction[params.length];

        for (int i = 0; i < params.length; i++) {

//            fluxFunctionArray_[i] = new NativeFluxFunctionFacadeTeste(params[i]);
//            fluxFunctionArray_[i] = new NativeFluxFunctionFacade(params[i]);

        }

        fluxFunction_ = fluxFunctionArray_[0];
    }


    /**
     * ID
     *
     * @return String
     * @todo Implement this rpnumerics.physics.Physics method
     */
    public String ID() {
        return "";
    }

    /**
     * accumulationFunction
     *
     * @return AccumulationFunction
     * @todo Implement this rpnumerics.physics.Physics method
     */
    public AccumulationFunction accumulationFunction() {
        return null;
    }

    /**
     * boundary
     *
     * @return Boundary
     * @todo Implement this rpnumerics.physics.Physics method
     */
    public Boundary boundary() {
        return boundary_;

    }

    /**
     * domain
     *
     * @return Space
     * @todo Implement this rpnumerics.physics.Physics method
     */
    public Space domain() {
        return Multid.PLANE;

    }

    /**
     * fluxFunction
     *
     * @return FluxFunction
     * @todo Implement this rpnumerics.physics.Physics method
     */
    public FluxFunction fluxFunction() {

        return fluxFunction_;
    }

    public FluxFunction fluxFunction(int index) {

        return fluxFunctionArray_[index];

    }


    public FluxFunction[] fluxFunctionArray() {

        return fluxFunctionArray_;

    }

    public void setBoundary(Boundary boundary) {
        boundary_ = boundary;
    }
}
