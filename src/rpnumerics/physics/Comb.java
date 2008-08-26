/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpnumerics.physics;

import rpnumerics.FluxFunction;
import wave.multid.Space;
import wave.util.*;

public class Comb implements Physics
{
    private CombFluxFunction fluxFunction_;

    public static final RectBoundary DEFAULT_BOUNDARY = new RectBoundary(new RealVector(new double[]{-.5,-.5}),new RealVector(new double[]{.5,.5}));

    public static final String FLUX_ID = "Combustion";

    private AccumulationFunction accumulationFunction_;
    private Boundary boundary_=DEFAULT_BOUNDARY;

    public Comb( CombFluxParams params )
    {
      fluxFunction_ = new CombFluxFunction(params);
      accumulationFunction_= new DefaultAccFunction();
    }

    //
    // Methods
    //

    // Accessors/Mutators

    public AccumulationFunction accumulationFunction(){return accumulationFunction_;}


//    public Boundary boundary() {return DEFAULT_BOUNDARY;}


    public void setBoundary (Boundary boundary) {boundary_=boundary;}

    public Boundary boundary() {return boundary_;}


    public  FluxFunction [] fluxFunctionArray(){return null;}

    public FluxFunction fluxFunction(int index){return null;}



    public Space domain() {return null;}
    public FluxFunction fluxFunction( ) { return fluxFunction_; }
    public String ID( ) {return FLUX_ID;}

}
