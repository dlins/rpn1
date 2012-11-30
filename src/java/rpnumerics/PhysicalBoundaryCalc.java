/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import rpn.configuration.CommandConfiguration;
import rpn.configuration.Configuration;
import rpn.configuration.ConfigurationProfile;

public class PhysicalBoundaryCalc implements RpCalculation {

    
    protected Configuration configuration_;
    
    public PhysicalBoundaryCalc() {
        
        
        String className = getClass().getSimpleName().toLowerCase();

        String curveName = className.replace("calc", "");

        configuration_ = new CommandConfiguration(curveName);

        
        
    }
  
    
    public RpSolution recalc() throws RpException {
        return calc();
    }

    public RpSolution calc() throws RpException {

        PhysicalBoundary curve =  (PhysicalBoundary)calcNative();

        if (curve ==null) throw new RpException("Error in native layer");

        return curve;
    }

    public RpSolution recalc(Area area) throws RpException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private native RpSolution calcNative();

    public Configuration getConfiguration() {
        return configuration_;
    }
}
