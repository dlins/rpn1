/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

public class RarefactionOrbitCalc extends OrbitCalc implements RpCalculation {
    //
    // Constants
    //
    //
    // Members
    //

//    private PhasePoint start_;
//    private int timeDirection_;
//    private String methodName_;
//    private String flowName_;
//    private int familyIndex_;
//

    //
    // Constructors/Initializers
    //
    public RarefactionOrbitCalc(PhasePoint point, int familyIndex,int timeDirection) {

        super(new OrbitPoint(point), familyIndex, timeDirection);
       
    }

    //
    // Accessors/Mutators
    //
   
    //
    // Methods
    //
    public RpSolution recalc() throws RpException {
        return calc();

    }

    public RpSolution calc() throws RpException {

        RarefactionOrbit result;
        if (getDirection()== 0) {

            RarefactionOrbit resultForward = (RarefactionOrbit) calc("methodName_", "flowName_", getStart(), getFamilyIndex(), 20);

            RarefactionOrbit resultBackward = (RarefactionOrbit) calc("methodName_", "flowName_", getStart(), getFamilyIndex(), 22);

            if (resultBackward == null || resultForward == null) {
                throw new RpException("Error in native layer");
            }


            Orbit resultComplete = RarefactionOrbit.concat(resultBackward, resultForward,getFamilyIndex());
            result = new RarefactionOrbit(resultComplete.getPoints(), resultComplete.getFamilyIndex(),resultComplete.getDirection());

            return result;


        }

        result = (RarefactionOrbit) calc("methodName_", "flowName_", getStart(), getFamilyIndex(), getDirection());


        if (result == null) {
            throw new RpException("Error in native layer");
        }
      

        //** acrescentei isso (Leandro)
            RPnCurve.lista.add(result);
            System.out.println("Tamanho da lista: " + RPnCurve.lista.size());
        //***

        return result;


    }

    private native RpSolution calc(String methodName, String flowName, PhasePoint initialpoint, int familyIndex, int timeDirection) throws RpException;

   
}
