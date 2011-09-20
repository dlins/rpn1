/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

public class CoincidenceCurveCalc extends BifurcationCurveCalc {

    static private int contCC = 0;      //** declarei isso (Leandro)

    //
    // Constructors/Initializers
    //
    public CoincidenceCurveCalc(){
    }

   

    @Override
    public RpSolution calc() throws RpException {


    CoincidenceCurve result = (CoincidenceCurve) nativeCalc();

          if (result == null) {
            throw new RpException("Error in native layer");
        }


        //** acrescentei isso (Leandro)

        if (contCC == 0) {
            System.out.println("Entrando em CoincidenceCurveCalc...");

            RPnCurve.lista.add(result);
            System.out.println("Tamanho da lista: " + RPnCurve.lista.size());

            contCC += 1;
        }

        //*********************************************

        return result;
    }

    private native RpSolution nativeCalc() throws RpException;


}
