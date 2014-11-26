/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import wave.multid.model.MultiPoint;
import wave.multid.view.*;
import wave.multid.CoordsArray;
import wave.multid.DimMismatchEx;
import java.awt.Color;
import java.util.Iterator;
import rpn.component.util.GraphicsUtil;

public class StationaryPointGeom extends MultiPoint implements RpGeometry {
    //
    // Constants
    //

    public static ViewingAttr VIEWING_ATTR = new ViewingAttr(Color.white);
    //
    // Members
    //
    private RpGeomFactory factory_;

    private final int ALFA_DOWN = 20;
    private final int ALFA_UP = 255;


    //
    // Constructors
    //
    public StationaryPointGeom(CoordsArray source, StationaryPointGeomFactory factory) {
        super(source, VIEWING_ATTR);
        factory_ = factory;

        //StationaryPoint statPoint = (StationaryPoint) factory_.geomSource();

    }

    //
    // Accessors/Mutators
    //
    public RpGeomFactory geomFactory() {
        return factory_;
    }

    //
    // Methods
    //
    @Override
    public GeomObjView createView(ViewingTransform transf) throws DimMismatchEx {
        return new StationaryPointView(this, transf, viewingAttr());
    }


    // --- 14FEV : Acrescentei estes métodos
    public void lowLight() {
        Color newColor = new Color(viewingAttr().getColor().getRed(), viewingAttr().getColor().getGreen(), viewingAttr().getColor().getBlue(), ALFA_DOWN);
        viewingAttr().setColor(newColor);
    }

    public void highLight() {
        Color newColor = new Color(viewingAttr().getColor().getRed(), viewingAttr().getColor().getGreen(), viewingAttr().getColor().getBlue(), ALFA_UP);
        viewingAttr().setColor(newColor);
    }
    // -------------------------------------

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
