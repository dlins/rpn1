/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;


public class WaveCurveCalc extends WaveCurveOrbitCalc {


    //
    // Constants
    //

    // Constructors/Initializers
    //
    
    private int edge_;
    private boolean fromBoundary_;
    
    public WaveCurveCalc(PhasePoint point, int familyIndex, int timeDirection) {

        super(new OrbitPoint(point), familyIndex, timeDirection);

        fromBoundary_=false;
    }
    
    
    public WaveCurveCalc(PhasePoint point, int familyIndex, int timeDirection, int edge) {

        super(new OrbitPoint(point), familyIndex, timeDirection);
        edge_=edge;
        fromBoundary_=true;

    }
    
    
    
    
    

    //
    // Accessors/Mutators
    //
    //
    // Methods
    //
    @Override
    public RpSolution calc() throws RpException {
        WaveCurve result;
        
        if(!fromBoundary_)
      
        result = (WaveCurve)nativeCalc(getStart(), getFamilyIndex(), getDirection());

        else 
        
        result = (WaveCurve)boundaryNativeCalc(getStart(), getFamilyIndex(), getDirection(),edge_);
        
        if (result == null) {
            throw new RpException("Error in native layer");
        }

        result.setReferencePoint(getStart());
        
        return result;
    }

    private native RpSolution nativeCalc(OrbitPoint initialPoint, int family, int timeDirection);
    
    private native RpSolution boundaryNativeCalc(OrbitPoint initialPoint, int family, int timeDirection, int edge);


    public RpSolution recalc(Area area) throws RpException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
