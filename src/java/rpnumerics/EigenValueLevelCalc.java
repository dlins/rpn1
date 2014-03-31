/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

public class EigenValueLevelCalc extends CharacteristicPolynomialLevelCalc {

    private final int family_;
    
 


    public EigenValueLevelCalc(int family, double level, ContourParams params) {
        super(level,params);
        family_ = family;
        
        configuration_=RPNUMERICS.getConfiguration("levelcurve");
        configuration_.setParamValue("resolution", RPNUMERICS.getParamValue("hugoniotcurve", "resolution"));

    }
    
    
     protected  EigenValueLevelCalc(int family, ContourParams params) {
        super(params);
        family_ = family;
        
        configuration_=RPNUMERICS.getConfiguration("levelcurve");
        configuration_.setParamValue("resolution", RPNUMERICS.getParamValue("hugoniotcurve", "resolution"));

    }



    public RpSolution calc() throws RpException {

        EigenValueCurve result = (EigenValueCurve) calcNative(family_,getLevel());

        if (result == null) {
            throw new RpException("Error in native layer");
        }

        return result;

    }

    public int getFamily() {
        return family_;
    }

   

    private native RpSolution calcNative(int family, double level);

   
}
