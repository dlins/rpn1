/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics.methods;

import rpnumerics.*;
import rpnumerics.methods.contour.*;
import rpnumerics.methods.contour.functionsobjects.*;
import rpnumerics.methods.contour.markedhypercubes.*;
import rpnumerics.methods.contour.samplefunctions.*;

public class ContourFactory {

    public static ContourHugoniot createContourHugoniot(HugoniotParams hugoniotParams) {

        ContourHugoniot contourHugoniot = null;
        Hugoniot2DTest hugoniot2DTest = null;

        
        switch(RPNUMERICS.domainDim()){
            
            case 2:

                try {
                    hugoniot2DTest = new Hugoniot2DTest(hugoniotParams.getFluxFunction(), hugoniotParams);
                } catch (InvalidHugoniotFunctionsRelation ex) {
                    ex.printStackTrace();
                }

                contourHugoniot = new ContourHugoniot(hugoniot2DTest, new NullHyperCubeErrorTreatment());
                break;
                
                default:
                    System.out.print("Dimension " + RPNUMERICS.domainDim()+" is not implemented yet for contour method");
        }
        
        return contourHugoniot;

    }
    
    public static ContourBifurcation createContourBifurcation(ContourParams bifurcationParams) {
    	
    	/* switch(RPNUMERICS.domainDim()){
         
         case 2:
        // preencher	 
    	 }
    	 
    	return null; */
        
                  HugoniotParams params = null;
                  //FluxFunction F1 = new QuadFluxFunction_1();

                  FluxFunction F1 = new FluxFunction();
                  
        	  //FluxFunction F2 = new QuadFluxFunction_2();
        	  //FluxFunction F3 = new TestFluxFunctionF3();

        	  /*FluxFunction G1 = new TestFluxFunctionG1();
        	  FluxFunction G2 = new TestFluxFunctionG2();
        	  FluxFunction G3 = new TestFluxFunctionG3();*/

        	  BifurcationFluxFunction[] F = new BifurcationFluxFunction[1];

        	  F[0] = new BifurcationFluxFunction(2,2, F1, params);
        	  //F[1] = new BifurcationFluxFunction(2,1, F2, params);
        	 // F[2] = new BifurcationFluxFunction(2,1, F3, params);

        	  //BifurcationFluxFunction[] G = new BifurcationFluxFunction[3];

        	  //G[0] = new BifurcationFluxFunction(2,1, G1, params);
        	  //G[1] = new BifurcationFluxFunction(2,1, G2, params);
        	  //G[2] = new BifurcationFluxFunction(2,1, G3, params);

        	  BifurcationFluxFunctionCluster[] componentFunctions = new BifurcationFluxFunctionCluster[1];

        	  componentFunctions[0] = new  BifurcationFluxFunctionCluster(F, params);
        	  //componentFunctions[1] = new  BifurcationFluxFunctionCluster(G, params);

        	  DoubleContact[] functionsArray = new DoubleContact[1];

        	  try {
        		 functionsArray[0] = new  DoubleContact(params, componentFunctions);

                          //functionsArray[0] = new  Contact2D(null, params);

        		  int[][] pointers = new int[2][2];

        		  pointers[0][0] = 0;
        		  pointers[0][1] = 2;
        		  pointers[1][0] = 0;
        		  pointers[1][1] = 2;

//        		  functionsArray[0].setGridFunctions(pointers);

        	 // } catch (CanNotPerformCalculations e) {

        	  } catch (Exception e) {

        	  }

        return new ContourBifurcation((MultiBifurcationFunction) functionsArray[0], 1, null); //

    }
}