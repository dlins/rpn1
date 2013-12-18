/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import rpn.configuration.CommandConfiguration;

public class SubInflectionCurveCalc extends ContourCurveCalc {

    //
    // Constructors/Initializers
    //
    public SubInflectionCurveCalc(){
        super(new ContourParams());

        
    }


    public RpSolution calc() throws RpException {


        SubInflectionCurve result = (SubInflectionCurve) nativeCalc();

          if (result == null) {
            throw new RpException("Error in native layer");
        }

        return result;
    }

    private native RpSolution nativeCalc() throws RpException;


}
