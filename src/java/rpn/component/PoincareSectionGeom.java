/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import wave.multid.model.MultiPolygon;
import wave.multid.view.ViewingAttr;
import wave.multid.CoordsArray;
import java.awt.Color;
import java.util.Iterator;
import rpn.component.util.GraphicsUtil;
import wave.multid.view.ViewingTransform;

public class PoincareSectionGeom extends MultiPolygon implements RpGeometry {
    //
    // Constants
    //

    public static ViewingAttr VIEWING_ATTR = new ViewingAttr(Color.white);
    //
    // Members
    //
    private RpGeomFactory factory_;


    //
    // Constructors
    //
    public PoincareSectionGeom(CoordsArray[] source, PoincareSectionGeomFactory factory) {
        super(source, VIEWING_ATTR);
        factory_ = factory;
    }

    //
    // Accessors/Mutators
    //
    public RpGeomFactory geomFactory() {
        return factory_;
    }

    @Override
    public void addAnnotation(GraphicsUtil annotation) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Iterator<GraphicsUtil> getAnnotationIterator() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void clearAnnotations() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void removeLastAnnotation() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void removeAnnotation(GraphicsUtil selectedAnnotation) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void showSpeed(CoordsArray curvePoint, CoordsArray wcPoint, ViewingTransform transform) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void showClassification(CoordsArray curvePoint, CoordsArray wcPoint, ViewingTransform transform) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

   

    
}
