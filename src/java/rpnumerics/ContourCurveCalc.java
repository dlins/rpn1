/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import java.util.List;

import rpn.configuration.Configuration;

public abstract class ContourCurveCalc implements RpCalculation {

    private ContourParams params_;
    protected Configuration configuration_;
    private final String methodName_;

    //
    // Constructors
    //
    public ContourCurveCalc(ContourParams params) {
        params_ = params;

        methodName_="IMPLICIT";

    }

     public ContourCurveCalc(ContourParams params,String methodName) {
        params_ = params;

        methodName_=methodName;

    }
    
     
      public String getMethodName() {
        return methodName_;
    }
    
    public RpSolution recalc() throws RpException {
        return calc();
    }

    //
    // Accessors/Mutators
    //
    public RpSolution recalc(List<Area> area) throws RpException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public ContourParams getParams() {
        return params_;
    }

    public Configuration getConfiguration() {
        return configuration_;
    }
}
