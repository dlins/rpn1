/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

public class HysteresisCurveCalc extends BifurcationCurveCalc {

    static private int contCC = 0;      //** declarei isso (Leandro)
    private int family_;

    //
    // Constructors/Initializers
    //

    public HysteresisCurveCalc(int family){
        family_=family;
    }

   

    @Override
    public RpSolution calc() throws RpException {


    HysteresisCurve result = (HysteresisCurve) nativeCalc(family_);

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

    private native RpSolution nativeCalc(int family) throws RpException;





}
