/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

public class InflectionCurveCalc extends BifurcationCurveCalc {

    private int family_;

    //
    // Constructors/Initializers
    //
    public InflectionCurveCalc(BifurcationParams params, int family) {
        super(params);
        family_ = family;

    }

    @Override
    public RpSolution calc() throws RpException {

        int[] resolution = getParams().getResolution();

        InflectionCurve result = (InflectionCurve) nativeCalc(family_, resolution);

        if (result == null) {
            throw new RpException("Error in native layer");
        }


        return result;
    }

    public int getFamilyIndex() {
        return family_;
    }

    private native RpSolution nativeCalc(int family, int[] resolution) throws RpException;
}
