/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpnumerics.physics;

import rpnumerics.FluxFunction;
import wave.multid.Space;
import wave.multid.Multid;
import wave.util.*;


public class Quad2 implements Physics  {

    //
    // Constants
    //
    static public final String FLUX_ID = "QuadraticR2";
    static public final String DEFAULT_SIGMA = "-.021";
    static public final String DEFAULT_XZERO = ".13 .07";
    static public final RectBoundary DEFAULT_BOUNDARY = new RectBoundary(new RealVector(
        new double[] { -.5, -.5 }), new RealVector(
        new double[] { .5, .5 }));

    //
    // Members
    //

    private FluxFunction fluxFunction_;
    private Quad2FluxParams params_;
    private AccumulationFunction accumulationFunction_;
    private Boundary boundary_=DEFAULT_BOUNDARY;

    //
    // Constructors/Inner Classes
    //


   

    public Quad2(Quad2FluxParams params){

      fluxFunction_ = (FluxFunction) new QuadNDFluxFunction(params);
//      accumulationFunction_ = new DefaultAccFunction();
      params_=params;

    }


    //
    // Accessors/Mutators
    //
    public Space domain() { return Multid.PLANE; }
    public FluxFunction fluxFunction( ){return fluxFunction_;}
    public String ID() { return FLUX_ID; }
//    public Boundary boundary() { return DEFAULT_BOUNDARY; }

    public void setBoundary (Boundary boundary) {boundary_=boundary;}

    public Boundary boundary() {return boundary_;}


    public  FluxFunction [] fluxFunctionArray(){return null;}

    public FluxFunction fluxFunction(int index){return null;}

    public AccumulationFunction accumulationFunction(){return accumulationFunction_;}





}
