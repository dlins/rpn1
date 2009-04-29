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
    
    public static ContourBifurcation createContourBifurcation(BifurcationParams bifurcationParams) {
    	
    	 switch(RPNUMERICS.domainDim()){
         
         case 2:
        // preencher	 
    	 }
    	 
    	return null;
    }
}
