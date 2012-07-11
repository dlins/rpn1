/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;


public class WaveCurveCalc extends WaveCurveOrbitCalc {
//public class WaveCurveCalc implements RpCalculation {

    //
    // Constants
    //

    // Constructors/Initializers
    //
    public WaveCurveCalc(PhasePoint point, int familyIndex, int timeDirection) {

        super(new OrbitPoint(point), familyIndex, timeDirection);

    }

    //
    // Accessors/Mutators
    //
    //
    // Methods
    //
    @Override
    public RpSolution calc() throws RpException {
        System.out.println("Entrou no calc() de WaveCurveCalc.................");
        System.out.println("getStart() : " +getStart());
        System.out.println("getFamilyIndex() : " +getFamilyIndex());
        System.out.println("getDirection() : " +getDirection());

        RpSolution result = nativeCalc(getStart(), getFamilyIndex(), getDirection());

        
        if (result == null) {
            throw new RpException("Error in native layer");
        }

        return result;
    }

    private native RpSolution nativeCalc(OrbitPoint initialPoint, int family, int timeDirection);


    public RpSolution recalc(Area area) throws RpException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
