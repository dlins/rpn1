/*
*
* Instituto de Matematica Pura e Aplicada - IMPA
* Departamento de Dinamica dos Fluidos
*
*/

package rpnumerics.factories;

import rpnumerics.HugoniotParams;
import rpnumerics.RPNumericsProfile;
import rpnumerics.methods.*;
import rpnumerics.*;
import rpnumerics.physics.*;
import wave.util.*;

import rpnumerics.methods.contour.functionsobjects.*;
import rpnumerics.methods.contour.samplefunctions.*;


public class HugoniotFactory {

  public static HugoniotCurveCalc hugoniotCurveCalcCreate(RPNumericsProfile profile,HugoniotParams hugoniotParams) {

     if (profile.isSpecificHugoniot()) {

       if (profile.getPhysicsID().equals("Quad2")){

         return new Quad2HugoniotCurveCalc(RPNUMERICS.fluxFunction().fluxParams(),hugoniotParams.getPhasePoint());
       }


       if (profile.getPhysicsID().equals("Comb")){

         return new CombHugoniotCurveCalc((CombFluxParams)RPNUMERICS.fluxFunction().fluxParams(),hugoniotParams.getPhasePoint(),1d);

       }

    }

      HugoniotMethod hMethod = hugoniotMethodCreate(profile,hugoniotParams);

      if (hMethod instanceof HugoniotContinuationMethod)

        return new HugoniotCurveCalcND( (HugoniotContinuationMethod) hMethod);

      return new HugoniotCurveCalcND((HugoniotContourMethod)hMethod);


  }


  private static HugoniotMethod hugoniotMethodCreate(RPNumericsProfile profile,HugoniotParams params) {

    if (profile.isContourMethod()) {

      RealVector min = RPNUMERICS.boundary().getMinimums();

      RealVector max = RPNUMERICS.boundary().getMaximums();

   	  if(profile.getCurveName().equals("HugoniotComposite")) {
    		  HugoniotContourParams contourParams = null;


        	  int[] variations = profile.getVariations();

        	  double[] boundaryArray = new double[4];
        	  Boundary boundary = profile.getBoundary();

        	  RealVector minimums = boundary.getMinimums();
        	  RealVector maximums = boundary.getMaximums();

        	  double[] minimumsArray = minimums.toDouble();
        	  double[] maximumsArray = maximums.toDouble();

        	  boundaryArray[0] = minimumsArray[0];
        	  boundaryArray[1] = maximumsArray[0];
        	  boundaryArray[2] = minimumsArray[1];
        	  boundaryArray[3] = maximumsArray[1];


        	  Hugoniot2DTest[] functionsArray = new Hugoniot2DTest[1];

        	  //FluxFunction function = new TriPhaseFluxFunction();
        	  FluxFunction function = new Quad2FluxFunction(null);
              //FluxFunction function = new NativeFluxFunction (new NativeFluxFunctionFacade("/impa/home/g/cbevilac/workspace2/RPN/native/lib/libQuad.so"), new Quad2Params());

        	  try {
        		  functionsArray[0] = new  Hugoniot2DTest(function, params);
        	  } catch (Exception e) {

        	  }

        	  contourParams = new HugoniotContourParams(functionsArray, boundaryArray, variations);

        	  return new HugoniotContourMethod(contourParams);

    	  } else if(profile.getCurveName().equals("HugoniotTriangleDomain")) {

    		  HugoniotContourParams contourParams = null;

    		  int[] variations = profile.getVariations();

        	  double[] boundaryArray = new double[4];
        	  Boundary boundary = profile.getBoundary();

        	  RealVector minimums = boundary.getMinimums();
        	  RealVector maximums = boundary.getMaximums();

        	  double[] minimumsArray = minimums.toDouble();
        	  double[] maximumsArray = maximums.toDouble();

        	  boundaryArray[0] = minimumsArray[0];
        	  boundaryArray[1] = maximumsArray[0];
        	  boundaryArray[2] = minimumsArray[1];
        	  boundaryArray[3] = maximumsArray[1];


        	  Hugoniot2DTest[] functionsArray = new Hugoniot2DTest[1];

        	  FluxFunction function = new Quad2FluxFunction(null);

        	  try {
        		  functionsArray[0] = new  Hugoniot2DTest(function, params);
        	  } catch (Exception e) {

        	  }

        	  contourParams = new HugoniotContourParams(functionsArray, boundaryArray, variations);

        	  return new HugoniotContourTriangleDomainMethod(contourParams);
    	  } else if(profile.getCurveName().equals("HugoniotMultiFunction")) {

    		  HugoniotContourParams contourParams = null;

    		  int[] variations = profile.getVariations();

        	  double[] boundaryArray = new double[4];
        	  Boundary boundary = profile.getBoundary();

        	  RealVector minimums = boundary.getMinimums();
        	  RealVector maximums = boundary.getMaximums();

        	  double[] minimumsArray = minimums.toDouble();
        	  double[] maximumsArray = maximums.toDouble();

        	  boundaryArray[0] = minimumsArray[0];
        	  boundaryArray[1] = maximumsArray[0];
        	  boundaryArray[2] = minimumsArray[1];
        	  boundaryArray[3] = maximumsArray[1];

        	  FluxFunction F1 = new TestFluxFunctionF1();
        	  FluxFunction F2 = new TestFluxFunctionF2();
        	  FluxFunction F3 = new TestFluxFunctionF3();

        	  FluxFunction G1 = new TestFluxFunctionG1();
        	  FluxFunction G2 = new TestFluxFunctionG2();
        	  FluxFunction G3 = new TestFluxFunctionG3();

        	  BifurcationFluxFunction[] F = new BifurcationFluxFunction[3];

        	  F[0] = new BifurcationFluxFunction(1,1, F1, params);
        	  F[1] = new BifurcationFluxFunction(1,1, F2, params);
        	  F[2] = new BifurcationFluxFunction(1,1, F3, params);

        	  BifurcationFluxFunction[] G = new BifurcationFluxFunction[3];

        	  G[0] = new BifurcationFluxFunction(1,1, G1, params);
        	  G[1] = new BifurcationFluxFunction(1,1, G2, params);
        	  G[2] = new BifurcationFluxFunction(1,1, G3, params);

        	  BifurcationFluxFunctionCluster[] componentFunctions = new BifurcationFluxFunctionCluster[2];

        	  componentFunctions[0] = new  BifurcationFluxFunctionCluster(F, params);
        	  componentFunctions[1] = new  BifurcationFluxFunctionCluster(G, params);

        	  TestMultiFunction[] functionsArray = new TestMultiFunction[1];

        	  try {
        		  functionsArray[0] = new  TestMultiFunction(params, componentFunctions);

        		  int[][] pointers = new int[2][2];

        		  pointers[0][0] = 0;
        		  pointers[0][1] = 2;
        		  pointers[1][0] = 0;
        		  pointers[1][1] = 2;

        		  functionsArray[0].setGridFunctions(pointers);

        	  } catch (CanNotPerformCalculations e) {

        	  } catch (Exception e) {

        	  }

        	  contourParams = new HugoniotContourParams(functionsArray, boundaryArray, variations);

        	  return new BifurcationContourMethod(contourParams);
    	  }


    }



    VectorFunction  hugoniotFunction = hugoniotFunctionCreate(params,profile);
    HugoniotContinuationParams continuationParams = new HugoniotContinuationParams(hugoniotFunction);

    return new HugoniotContinuationMethod(continuationParams);


  }

  private static VectorFunction hugoniotFunctionCreate(HugoniotParams hugoniotParams,RPNumericsProfile  profile) {

      if (profile.isNativePhysics()){

      NativeVectorFunctionFacade hugoniotFacade = new NativeVectorFunctionFacade(profile.libName());

      return new NativeVectorFunction(hugoniotFacade);

    }

    return new GenericHugoniotFunction(RPNUMERICS.fluxFunction(), hugoniotParams);

  }

}
