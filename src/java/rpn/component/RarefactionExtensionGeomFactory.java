/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import rpn.controller.RarefactionExtensionController;
import rpn.controller.RpController;
import rpnumerics.Orbit;
import rpnumerics.RarefactionExtensionCalc;
import rpnumerics.RarefactionExtensionCurve;
import wave.util.RealSegment;
import wave.util.RealVector;

public class RarefactionExtensionGeomFactory extends RpCalcBasedGeomFactory {
    //
    // Constants
    //
    //
    // Members
    //
    //
    // Constructors/Initializers
    //

    public RarefactionExtensionGeomFactory(RarefactionExtensionCalc calc) {
        super(calc);
    }
    //
    // Accessors/Mutators
    //
    //
    // Methods
    //

    @Override
    public RpGeometry createGeomFromSource() {

        RarefactionExtensionCurve curve = (RarefactionExtensionCurve) geomSource();

       
        int resultSize = curve.segments().size();

        RealSegGeom[] hugoniotArray = new RealSegGeom[resultSize];
        for (int i = 0; i < resultSize; i++) {
            hugoniotArray[i] = new RealSegGeom((RealSegment) curve.segments().get(i));


        }

        return new RarefactionExtensionGeom(hugoniotArray, this);

    }


    @Override
     protected RpController createUI() {
        return new RarefactionExtensionController();
    }

   
}
