/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpnumerics.physics;


import rpnumerics.FluxFunction;
import wave.multid.Multid;
import wave.multid.Space;
import wave.util.IsoTriang2DBoundary;
import wave.util.Boundary;
import wave.util.RealVector;

public class TriPhase
    implements Physics {
  //
  // Constants
  //
  static public final String FLUX_ID = "Triphase";
  static public final IsoTriang2DBoundary DEFAULT_BOUNDARY = new
      IsoTriang2DBoundary(new RealVector(
      new double[] {.0, .0}), new RealVector(
      new double[] {0., 1.}), new RealVector(
      new double[] {1., 0.}));

  //
  // Members
  //

  //	private TriPhaseFluxFunction fluxFunction_;

  private FluxFunction fluxFunction_;

  private FluxFunction [] fluxFunctionArray_;

  private AccumulationFunction accumulationFunction_;
  private Boundary boundary_=DEFAULT_BOUNDARY;

  //
  // Constructors/Inner Classes
  //

//  public TriPhase(String libName,FluxParams params) {
//
//      System.loadLibrary(libName);
//
//      fluxFunctionArray_= new FluxFunction[1];
//
//      fluxFunction_ = new NativeFluxFunctionFacade(params);
//
//      fluxFunctionArray_[0]=fluxFunction_;
//
//
//      //    accumulationFunction_= new DefaultAccFunction();
//
//  }

  public TriPhase(TriPhaseFluxParams params, PermParams permParams,
                  CapilParams capilParams, ViscosityParams viscParams) {
      fluxFunctionArray_= new FluxFunction[1];
      fluxFunction_ = new TriPhaseFluxFunction(params, permParams, capilParams,
                                             viscParams);

    fluxFunctionArray_[0]=fluxFunction_;


  }

  // protected Physics createNativePhysicsInstance(NativeFluxFunctionFacade facade,FluxParams fluxParams){

// 	return new TriPhase(fluxParams,facade);


//     }

//     public static Physics createNativeTriphase(NativeFluxFunction facade , FluxParams fluxParams){


// 	return createNativePhysicsInstance(facade,fluxParams);

//     }





  //
  // Accessors/Mutators
  //
  public String ID() {
    return FLUX_ID;
  }

//  public Boundary boundary() {
//    return DEFAULT_BOUNDARY;
//  }
//


  public void setBoundary (Boundary boundary) {boundary_=boundary;}

  public Boundary boundary() {return boundary_;}

  public  FluxFunction [] fluxFunctionArray(){return fluxFunctionArray_; }

  public FluxFunction fluxFunction(int index){return fluxFunctionArray_[index];}

  public Space domain() {
    return Multid.PLANE;
  }

  public FluxFunction fluxFunction() {
    return fluxFunction_;
  }

  public AccumulationFunction accumulationFunction() {
    return accumulationFunction_;
  }

}
