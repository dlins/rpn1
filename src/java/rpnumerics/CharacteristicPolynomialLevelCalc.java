/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

public abstract class CharacteristicPolynomialLevelCalc extends ContourCurveCalc {

   
    private double level_;

    protected CharacteristicPolynomialLevelCalc(double level, ContourParams params) {
        super(params);

        level_ = level;

        configuration_=RPNUMERICS.getConfiguration("levelcurve");
        configuration_.setParamValue("resolution", RPNUMERICS.getParamValue("bifurcationcurve", "resolution"));

    }

    protected CharacteristicPolynomialLevelCalc(ContourParams params) {
        super(params);
       
    }
    public double getLevel() {
        return level_;
    }


   

  

  
   
}
