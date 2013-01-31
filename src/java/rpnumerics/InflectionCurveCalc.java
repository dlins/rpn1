/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

public class InflectionCurveCalc extends ContourCurveCalc {

    private int family_;

    //
    // Constructors/Initializers
    //
    public InflectionCurveCalc(ContourParams params, int family) {
        super(params);
        family_ = family;
        
        configuration_=RPNUMERICS.getConfiguration("inflectioncurve");
        
        
        

    }

    @Override
    public RpSolution calc() throws RpException {


        InflectionCurve result = (InflectionCurve) nativeCalc(family_);

        if (result == null) {
            throw new RpException("Error in native layer");
        }


        return result;
    }

    public int getFamilyIndex() {
        return family_;
    }

    private native RpSolution nativeCalc(int family) throws RpException;
}
