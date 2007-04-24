/* Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpnumerics.methods.contour.functionsobjects;

import rpnumerics.physics.FluxFunction;
import wave.util.PointNDimension;
import wave.util.RealMatrix2;
import wave.util.RealVector;
import rpnumerics.HugoniotParams;
import rpnumerics.RPNUMERICS;
import rpnumerics.NativeVectorFunctionFacade;
import rpnumerics.PhasePoint;

public class NativeHugoniotMDFunction extends MDVectorFunction {

  private  NativeVectorFunctionFacade fac_;
  static  private PhasePoint Uminus_;

  public NativeHugoniotMDFunction(FluxFunction fluxFunction,
                            HugoniotParams params,NativeVectorFunctionFacade facade) {

    super(rpnumerics.RPNUMERICS.domainDim(),
          rpnumerics.RPNUMERICS.domainDim() - 1, fluxFunction, params);

    fac_=facade;
    Uminus_=params.getPhasePoint();


    //		super(2, 1, fluxFunction, params);
    // TODO Auto-generated constructor stub
  }

  public RealVector value(PointNDimension point) {

    RealVector result =null;

    try {

        result=fac_.value(point.toRealVector(),Uminus_);

    }
    catch (Exception e) {

      System.out.println("Erro em value (PointNDimension)");

      e.printStackTrace();

    }

    return result;

  }



  public RealMatrix2 deriv(PointNDimension point) {

    RealMatrix2 result = null;

    try {

        result= fac_.deriv(point.toRealVector(),Uminus_);

      }
      catch (Exception e) {

          System.out.println("Erro em derive de HugoniotMDFunction");

        }

        return result;

      }

    }
