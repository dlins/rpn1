/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;


import java.util.List;

import rpn.configuration.CommandConfiguration;
import rpn.configuration.Configuration;

public class ContactRegionCalc extends PhysicalBoundaryCalc {

    
    protected Configuration configuration_;
    
    private int family_;
    
    public ContactRegionCalc(int family) {
        
        family_=family;
        String className = getClass().getSimpleName().toLowerCase();

        String curveName = className.replace("calc", "");

        configuration_ = new CommandConfiguration(curveName);

        
        
    }
  
    
    public RpSolution recalc() throws RpException {
        return calc();
    }

    public RpSolution calc() throws RpException {

        PhysicalBoundary curve =  (PhysicalBoundary)calcNative(family_);

        if (curve ==null) throw new RpException("Error in native layer");

        return curve;
    }

    public RpSolution recalc(Area area) throws RpException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private native RpSolution calcNative(int family);


    public RpSolution recalc(List<Area> area) throws RpException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    public Configuration getConfiguration() {
        return configuration_;

    }

    @Override
    public void setReferencePoint(OrbitPoint referencePoint) {
        
    }
}