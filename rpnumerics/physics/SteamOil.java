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

public class SteamOil implements Physics
{

    //
    // Constants
    //
    static public final String FLUX_ID = "SteamOil";
    static public final RectBoundary DEFAULT_BOUNDARY = new RectBoundary(new RealVector(new double[] { 0, 0, 290 }), new RealVector(new double[] { 1, 1, 370 }));
    //
    // Members
    //
    private FluxFunction fluxFunction_;

    private FluxFunction [] fluxFunctionArray_;

    private AccumulationFunction accumulationFunction_;

    private Boundary boundary_=DEFAULT_BOUNDARY;

    //
    // Constructors/Inner Classes
    //

     public SteamOil(String libName,SteamOilFluxParams params) {

         System.loadLibrary(libName);

         fluxFunctionArray_= new FluxFunction[1];
	fluxFunction_ = new NativeFluxFunctionFacade(params);
        fluxFunctionArray_[0]=fluxFunction_;

//        accumulationFunction_= new DefaultAccFunction();

    }

    public SteamOil(SteamFluxParams params) {

	fluxFunction_ = new SteamFluxFunction(params);
    }

    //
    // Accessors/Mutators
    //
    public Space domain() { return new Space("R3",3); }
    public FluxFunction fluxFunction( ){return fluxFunction_;}
    public String ID() { return FLUX_ID; }
//    public Boundary boundary() { return DEFAULT_BOUNDARY; }

    public void setBoundary (Boundary boundary) {boundary_=boundary;}

    public Boundary boundary() {return boundary_;}

    public  FluxFunction [] fluxFunctionArray(){return fluxFunctionArray_;}

    public FluxFunction fluxFunction(int index){return fluxFunctionArray_[index];}

    public AccumulationFunction accumulationFunction(){return accumulationFunction_;}

}
