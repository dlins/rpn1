/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */


package rpn.component;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import rpn.component.util.GraphicsUtil;
import wave.multid.*;
import wave.multid.map.Map;
import wave.multid.model.AbstractPath;
import wave.multid.model.AbstractPathIterator;
import wave.multid.model.BoundingBox;
import wave.multid.model.MultiGeometry;
import wave.multid.model.MultiPolyLine;
import wave.multid.view.*;

public class BifurcationCurveGeom implements MultiGeometry, RpGeometry {

    private RpGeomFactory factory_;
    private ViewingAttr viewingAttr_;
    private List<MultiPolyLine> segList_;
    private Space space_;
    private BoundingBox boundary_;
    private RpGeometry otherSide_;
    private List<GraphicsUtil> annotationsList_;
    


    public BifurcationCurveGeom(RealSegGeom[] segArray, BifurcationCurveGeomFactory factory) {

        viewingAttr_=segArray[0].viewingAttr();
        
//        System.out.println("Tamanho do array no construtor: "+segArray.length);

        segList_ = new ArrayList();
        for (int i = 0; i < segArray.length; i++) {
//            System.out.println("Segmento no array: "+segArray[i]);
            segList_.add(segArray[i]);
        }
        factory_ = factory;
        space_ = new Space("Auxiliar Space", rpnumerics.RPNUMERICS.domainDim() );
        try {
            boundary_ = new BoundingBox(new CoordsArray(space_), new CoordsArray(space_));
        } catch (DimMismatchEx dex) {
            dex.printStackTrace();
        }
        
    }


    // ************************************************
    public Iterator getRealSegIterator() {
        return segList_.iterator();
    }
    // ************************************************



    public GeomObjView createView(ViewingTransform transf) throws DimMismatchEx {
        return new BifurcationCurveView(this, transf, viewingAttr());
    }
    
    
     public void lowLight() {

        for (MultiPolyLine object : segList_) {
            object.lowLight();
        }

    }

    public void highLight() {
        for (MultiPolyLine object : segList_) {
            object.highLight();
        }

    }
    
    
    

    public RpGeomFactory geomFactory() {
        return factory_;
    }

    public AbstractPathIterator getPathIterator() {
        AbstractPath nullPath = new AbstractPath(getSpace());
        return nullPath.getPathIterator();
    }

    public AbstractPathIterator getPathIterator(Map map) throws DimMismatchEx {
        AbstractPath nullPath = new AbstractPath(getSpace());
        return nullPath.getPathIterator(map);
    }

    public ViewingAttr viewingAttr() {
        return viewingAttr_;
    }

    public RpGeometry getOtherSide() {
        return otherSide_;
    }

    public void setOtherSide(RpGeometry otherSide) {
        otherSide_ = otherSide;
    }





    public Iterator getBifurcationSegmentsIterator() {
        return segList_.iterator();
    }

    public BoundingBox getBoundary() {
        return boundary_;
    }

    public Space getSpace() {
        return space_;
    }

    //
    // Methods
    //
    public void applyMap(Map map) {
    }

    public void print(FileWriter cout) {
    }

    public void load(FileReader cin) {
    }

    @Override
    public void setVisible(boolean visible) {
       for (MultiPolyLine object : segList_) {
            object.setVisible(visible);
        }
    }

    @Override
    public void setSelected(boolean selected) {
       viewingAttr_.setSelected(selected);
    }

    @Override
    public boolean isVisible() {
        return viewingAttr_.isVisible();
    }

    @Override
    public boolean isSelected() {
       return viewingAttr_.isSelected();
    }
    
    
     @Override
    public void addAnnotation(GraphicsUtil annotation) {

        if (annotationsList_.isEmpty()) {
            annotationsList_.add(annotation);
        } else {
            annotationsList_.set(annotationsList_.size() - 1, annotation);
        }
    }

    public void clearAnnotations() {
        annotationsList_.clear();
    }

    @Override
    public Iterator<GraphicsUtil> getAnnotationIterator() {
        return annotationsList_.iterator();
    }

    @Override
    public void setLastAnnotation(GraphicsUtil plotted) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
    
   
   
   
}
