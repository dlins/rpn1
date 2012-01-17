/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

public class CompositeCalc extends OrbitCalc  {
    //
    // Constants
    //
    //
    // Members
    //

 
    //
    // Constructors/Initializers
    //
  

    public CompositeCalc(OrbitPoint point, int familyIndex, int increase) {
        super(point, familyIndex,increase);
    }
    //
    // Methods
    //
    @Override
    public RpSolution recalc() throws RpException {

        return calc();

    }

    @Override
    public RpSolution calc() throws RpException {

        CompositeCurve result = null;

        result = (CompositeCurve) nativeCalc(getStart(), getDirection(), getFamilyIndex());
        if (result == null) {
            throw new RpException("Error in native layer");
        }

        return result;
    }

    private native RpSolution nativeCalc(PhasePoint start, int increase,int familyIndex) throws RpException;
}
