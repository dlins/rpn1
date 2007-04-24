

/**
 * <p>Description: Implements a n-dimensional function </p>
 * <p>Company: Impa</p>
 * @author F�bio Dias Bleasby Rodrigues
 */
package rpnumerics.methods.contour.functionsobjects;


import rpnumerics.HugoniotParams;
import rpnumerics.physics.FluxFunction;
import wave.util.PointNDimension;
import wave.util.RealVector;
import wave.util.VectorFunction;

public abstract class CubeFunction implements VectorFunction {

  private int componentsNumber;
  private FluxFunction fluxFunction; 
  private HugoniotParams params;

  //
  // Methods
  //
  //


 public CubeFunction(FluxFunction fluxFunction,HugoniotParams params) {
   
   this.componentsNumber = rpnumerics.RPNUMERICS.domainDim();
   
   this.fluxFunction = fluxFunction; 
   this.params = params;


  }


//  public CubeFunction(int componentsNumber) {
//
//    this.componentsNumber = componentsNumber;
//  }


  public int getFunctionDimension() {
    return this.componentsNumber;
  }

  /**
   * @param p N-dimensional point
   * @return The value of the function at p
   */
  public double function(RealVector p) {
    return 0.0;
  }


  public double function(PointNDimension point) throws CanNotPerformCalculations {
    return 0.0;
  }
}
