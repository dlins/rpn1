/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

public class InflectionCurveCalc extends BifurcationCurveCalc {

    static private int contCC = 0;      //** declarei isso (Leandro)
    private int family_;

    //
    // Constructors/Initializers
    //

    public InflectionCurveCalc(int family){
        family_=family;
    }

   

    @Override
    public RpSolution calc() throws RpException {


    InflectionCurve result = (InflectionCurve) nativeCalc(family_);

          if (result == null) {
            throw new RpException("Error in native layer");
        }


        //** acrescentei isso (Leandro)

        if (contCC == 0) {
            System.out.println("Entrando em InflectionCalc...");

            RPnCurve.lista.add(result);
            System.out.println("Tamanho da lista: " + RPnCurve.lista.size());

            contCC += 1;
        }

        //*********************************************

        return result;
    }

  
     public int getFamilyIndex() {
        return family_;
    }

    private native RpSolution nativeCalc(int family) throws RpException;


}
