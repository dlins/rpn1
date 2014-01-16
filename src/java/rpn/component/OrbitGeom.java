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

public class OrbitGeom extends MultiPolyLine implements RpGeometry {
   
    // Members
    //
    private RpGeomFactory factory_;


    //
    // Constructors
    //
    public OrbitGeom(CoordsArray[] source, OrbitGeomFactory factory) {
        super(source, factory.selectViewingAttr());
        factory_ = factory;
    }

    //
    // Accessors/Mutators
    //

    @Override
    public GeomObjView createView(ViewingTransform transf) throws DimMismatchEx {

        return new OrbitGeomView(this, transf, viewingAttr());

    }

    public RpGeomFactory geomFactory() {
        return factory_;
    }

    @Override
    public void setVisible(boolean visible) {
        viewingAttr().setVisible(visible);
    }

    @Override
    public void setSelected(boolean selected) {
        viewingAttr().setSelected(selected);
    }

   
}
