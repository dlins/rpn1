/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.component;

import wave.multid.model.MultiPolyLine;
import wave.multid.CoordsArray;
import wave.multid.view.GeomObjView;
import wave.multid.view.ViewingTransform;
import wave.multid.DimMismatchEx;

public class RiemannProfileGeom extends MultiPolyLine implements RpGeometry {
   
    // Members
    //
    private RpGeomFactory factory_;


    //
    // Constructors
    //
    public RiemannProfileGeom(CoordsArray[] source, RiemannProfileGeomFactory factory) {
        super(source, factory.selectViewingAttr());
        factory_ = factory;
    }

    //
    // Accessors/Mutators
    //

    @Override
    public GeomObjView createView(ViewingTransform transf) throws DimMismatchEx {

        return new RiemannProfileGeomView(this, transf, viewingAttr());

    }

    public RpGeomFactory geomFactory() {
        return factory_;
    }

   
}
