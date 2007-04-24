/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpnumerics.physics;

import wave.multid.Space;
import wave.multid.Multid;
import wave.util.Boundary;
import wave.util.RectBoundary;
import wave.util.RealVector;

public class Quad3 implements Physics
{


    //
    // Constants
    //
    static public final String FLUX_ID = "Quadratic R3";
    static public final RectBoundary DEFAULT_BOUNDARY = new RectBoundary(new RealVector(
        new double[] { -.5, -.5, -.5 }), new RealVector(
        new double[] { .5, .5, .5 }));

    //
    // Members
    //
    private FluxFunction fluxFunction_;

private AccumulationFunction accumulationFunction_;
    private Boundary boundary_=DEFAULT_BOUNDARY;

    //
    // Constructors/Inner Classes
    //
    public Quad3(Quad3FluxParams params) {

	fluxFunction_ = new QuadNDFluxFunction(params);
        accumulationFunction_= new DefaultAccFunction();
    }

    //
    // Accessors/Mutators
    //
    public Space domain() { return Multid.SPACE; }
    public FluxFunction fluxFunction( ){return fluxFunction_;}
    public String ID() { return FLUX_ID; }
//    public Boundary boundary() { return DEFAULT_BOUNDARY; }

    public void setBoundary (Boundary boundary) {boundary_=boundary;}

 public Boundary boundary() {return boundary_;}

 public  FluxFunction [] fluxFunctionArray(){return null;}

    public FluxFunction fluxFunction(int index){return null;}


    public AccumulationFunction accumulationFunction(){return accumulationFunction_;}



}
