/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import wave.multid.view.*;
import wave.multid.*;

public class EllipticBoundaryGeom extends SegmentedCurveGeom {
   

    //
    // Constructors
    //
    public EllipticBoundaryGeom(RealSegGeom[] segArray, EllipticBoundaryFactory factory) {

        super(segArray, factory);

    }


    public GeomObjView createView(ViewingTransform transf) throws DimMismatchEx {
        return new EllipticBoundaryView(this, transf, viewingAttr());
    }

    @Override
    public void showSpeed(CoordsArray curvePoint, CoordsArray wcPoint, ViewingTransform transform) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

   


}
