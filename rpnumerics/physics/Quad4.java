/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpnumerics.physics;

import wave.multid.Space;
import wave.util.Boundary;
import wave.util.RectBoundary;
import wave.util.RealVector;

public class Quad4 implements Physics {

    //
    // Constants
    //
    static public final String FLUX_ID = "Quadratic R4";
    static public final RectBoundary DEFAULT_BOUNDARY = new RectBoundary(new
            RealVector(
                    new double[] { -.4, -.4, -.4, -.4}), new RealVector(
                            new double[] {.4, .4, .4, .4}));


    //
    // Members
    //
    private FluxFunction fluxFunction_;
    private AccumulationFunction accumulationFunction_;
    private Boundary boundary_ = DEFAULT_BOUNDARY;

    //
    // Constructors/Inner Classes
    //

     public Quad4(String libName,Quad4FluxParams params) {
         System.loadLibrary(libName);
        fluxFunction_ = new QuadNDFluxFunction(params);
//      accumulationFunction_= new DefaultAccFunction();

    }


    public Quad4(Quad4FluxParams params) {

        fluxFunction_ = new QuadNDFluxFunction(params);
//      accumulationFunction_= new DefaultAccFunction();

    }

    //
    // Accessors/Mutators
    //
    public Space domain() {
        return new Space("R4", 4);
    }

    public FluxFunction fluxFunction() {
        return fluxFunction_;
    }

    public  FluxFunction [] fluxFunctionArray(){return null;}

    public FluxFunction fluxFunction(int index){return null;}


    public String ID() {
        return FLUX_ID;
    }


    public void setBoundary(Boundary boundary) {
        boundary_ = boundary;
    }

    public Boundary boundary() {
        return boundary_;
    }


    public AccumulationFunction accumulationFunction() {
        return accumulationFunction_;
    }

}
