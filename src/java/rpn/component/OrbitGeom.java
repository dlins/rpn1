/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import rpn.component.util.GraphicsUtil;
import wave.multid.model.MultiPolyLine;
import wave.multid.CoordsArray;
import wave.multid.view.GeomObjView;
import wave.multid.view.ViewingTransform;
import wave.multid.DimMismatchEx;
import wave.multid.model.MultiPoint;

public class OrbitGeom extends MultiPolyLine implements RpGeometry {

    // Members
    //
    private RpGeomFactory factory_;
    private MultiPoint starPoint_;
    private List<GraphicsUtil> annotationsList_;


    //
    // Constructors
    //
    public OrbitGeom(CoordsArray[] source, OrbitGeomFactory factory) {
        super(source, factory.selectViewingAttr());
        factory_ = factory;
        annotationsList_ = new ArrayList<GraphicsUtil>();


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
    
    
    @Override
    public void addAnnotation(GraphicsUtil annotation) {

        annotationsList_.add(annotation);

    }

    public void removeLastAnnotation() {
        if (!annotationsList_.isEmpty()) 
            annotationsList_.remove(annotationsList_.size()-1);
       

    }

    public void clearAnnotations() {
        annotationsList_.clear();
    }

    @Override
    public Iterator<GraphicsUtil> getAnnotationIterator() {
        return annotationsList_.iterator();
    }

    @Override
    public void removeAnnotation(GraphicsUtil selectedAnnotation) {
       annotationsList_.remove(selectedAnnotation);
    }

    @Override
    public void showSpeed(CoordsArray curvePoint, CoordsArray wcPoint,ViewingTransform transform) {

    }

    @Override
    public void showClassification(CoordsArray curvePoint, CoordsArray wcPoint, ViewingTransform transform) {
     
    }

}
