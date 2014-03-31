/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

public class WaveCurveCalc extends WaveCurveOrbitCalc {

    public static int DOMAIN = 0;
    public static int BOUNDARY = 1;
    public static int INFLECTION = 2;
    //
    // Constants
    //
    // Constructors/Initializers
    //
    private final int edge_;

    private final int origin_;


    public WaveCurveCalc(PhasePoint point, int familyIndex, int timeDirection, int origin, int edge) {

        super(new OrbitPoint(point), familyIndex, timeDirection);
        edge_ = edge;
        origin_ = origin;
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
      
            result = (WaveCurve) nativeCalc(getStart(), getFamilyIndex(), getDirection(), origin_, edge_);
       
        if (result == null) {
            throw new RpException("Error in native layer");
        }

        result.setReferencePoint(getStart());

        return result;
    }


    private native RpSolution nativeCalc(OrbitPoint initialPoint, int family, int timeDirection, int origin, int edge);

   
}
