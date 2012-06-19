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

        RpSolution result = nativeCalc(getStart(), getFamilyIndex(), getDirection());
        

        if (result == null) {
            throw new RpException("Error in native layer");
        }

        return result;
    }

    private native RpSolution nativeCalc(OrbitPoint initialPoint, int family, int timeDirection);
}
