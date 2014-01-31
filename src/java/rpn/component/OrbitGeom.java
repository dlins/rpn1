/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.component;

import java.awt.Color;
import rpnumerics.Orbit;
import rpnumerics.OrbitPoint;
import wave.multid.model.MultiPolyLine;
import wave.multid.CoordsArray;
import wave.multid.view.GeomObjView;
import wave.multid.view.ViewingTransform;
import wave.multid.DimMismatchEx;
import wave.multid.model.MultiPoint;
import wave.multid.view.ViewingAttr;

public class OrbitGeom extends MultiPolyLine implements RpGeometry {
   
    // Members
    //
    private RpGeomFactory factory_;
    private MultiPoint starPoint_;


    //
    // Constructors
    //
    public OrbitGeom(CoordsArray[] source, OrbitGeomFactory factory) {
        super(source, factory.selectViewingAttr());
        factory_ = factory;
        
    }

    public MultiPoint getStarPoint() {
        return starPoint_;
    }

    public void setStarPoint(MultiPoint starPoint) {
        starPoint_ = starPoint;
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
