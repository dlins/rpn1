/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

public class SubInflectionCurveCalc extends BifurcationCurveCalc {

    static private int contSI = 0;      //** declarei isso (Leandro)

    //
    // Constructors/Initializers
    //
    public SubInflectionCurveCalc(){
    }

    public RpSolution recalc() throws RpException {
return null;
//        return calc();

    }


    public RpSolution calc() throws RpException {


        SubInflectionCurve result = (SubInflectionCurve) nativeCalc();

          if (result == null) {
            throw new RpException("Error in native layer");
        }

        //** acrescentei isso (Leandro)
        if (contSI == 0) {
            System.out.println("Entrando em SubInflectionCurveCalc...");

            RPnCurve.lista.add(result);
            System.out.println("Tamanho da lista: " + RPnCurve.lista.size());

            contSI += 1;
        }
        //*********************************************

        return result;
    }

    private native RpSolution nativeCalc() throws RpException;


}
