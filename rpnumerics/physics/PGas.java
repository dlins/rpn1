/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpnumerics.physics;

import wave.multid.*;
import wave.util.*;

public class PGas implements Physics
{
    //
    // Constants
    //
    static public final String FLUX_ID = "P Gas";
    static public final String DEFAULT_SIGMA = ".1";
    static public final String DEFAULT_XZERO = ".65 .0";
    static public final RectBoundary DEFAULT_BOUNDARY = new RectBoundary(new RealVector(
        new double[] { -.4, -.2 }), new RealVector(
        new double[] { 1.5, .2 }));

    //
    // Members
    //
    private PGasFluxFunction fluxFunction_;

    private FluxFunction [] fluxFunctionArray_;
    private AccumulationFunction accumulationFunction_;
    private Boundary boundary_=DEFAULT_BOUNDARY;

   //
   // Constructors
   //
   public PGas( PGasFluxParams pgasparams )
	{

            fluxFunctionArray_ = new FluxFunction[1];
            fluxFunction_ = new PGasFluxFunction(pgasparams);

            fluxFunctionArray_[0]=fluxFunction_;

            accumulationFunction_= new DefaultAccFunction();
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

   public  FluxFunction [] fluxFunctionArray(){return fluxFunctionArray_;}

    public FluxFunction fluxFunction(int index){return fluxFunctionArray_[index];}

    public AccumulationFunction accumulationFunction(){return accumulationFunction_;}

    //
    // Methods
    //


}
