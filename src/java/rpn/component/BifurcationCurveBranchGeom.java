/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import java.awt.Color;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import rpn.component.util.CorrespondenceMark;
import rpn.component.util.GraphicsUtil;
import wave.multid.CoordsArray;
import wave.multid.DimMismatchEx;
import wave.multid.Space;
import wave.multid.map.Map;
import wave.multid.model.AbstractPath;
import wave.multid.model.AbstractPathIterator;
import wave.multid.model.BoundingBox;
import wave.multid.view.ViewingAttr;
import wave.multid.view.ViewingTransform;

public abstract class BifurcationCurveBranchGeom implements RpGeometry {
    
    public final static int LEFTRIGHT = 0;
    public final static int RIGHTLEFT = 1;
    public final static int NONE = 2;
    
    private int correspondenceDirection_;
    private final List<GraphicsUtil> annotationsList_;
    private RpGeomFactory factory_;
    private ViewingAttr viewingAttr_;
    private Space space_;
    private BoundingBox boundary_;
    
    public BifurcationCurveBranchGeom(BifurcationCurveGeomFactory factory) {
        annotationsList_ = new ArrayList<GraphicsUtil>();
        viewingAttr_ = new ViewingAttr(Color.white);
        factory_ = factory;
        space_ = new Space("Auxiliar Space", rpnumerics.RPNUMERICS.domainDim());
        try {
            boundary_ = new BoundingBox(new CoordsArray(space_), new CoordsArray(space_));
        } catch (DimMismatchEx dex) {
            dex.printStackTrace();
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
    public void setSelected(boolean selected) {
        viewingAttr_.setSelected(selected);
        for (BifurcationCurveBranchGeom object : getBifurcationListGeom()) {
            object.setSelected(selected);
        }
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
    public void setVisible(boolean visible) {
        viewingAttr_.setSelected(visible);
        for (BifurcationCurveBranchGeom object : getBifurcationListGeom()) {
            object.setVisible(visible);
        }
    }
    
    public int getCorrespondenceDirection() {
        return correspondenceDirection_;
    }
    
    @Override
    public void addAnnotation(GraphicsUtil annotation) {
        
        annotationsList_.add(annotation);
        
    }
    
    @Override
    public void clearAnnotations() {
        annotationsList_.clear();
    }
    
    @Override
    public Iterator<GraphicsUtil> getAnnotationIterator() {
        return annotationsList_.iterator();
    }
    
    @Override
    public void removeLastAnnotation() {
        if (!annotationsList_.isEmpty()) {
            annotationsList_.remove(annotationsList_.size() - 1);
        }
    }
    
    public void clearCorrespondenceMarks() {
        
        for (int i = 0; i < annotationsList_.size(); i++) {
            GraphicsUtil graphicsUtil = annotationsList_.get(i);
            if (graphicsUtil instanceof CorrespondenceMark) {
                annotationsList_.remove(i);
            }
            
        }
        
    }
    
    @Override
    public void removeAnnotation(GraphicsUtil selectedAnnotation) {
        annotationsList_.remove(selectedAnnotation);
    }
    
    public void setCorrespondenceDirection(int correspondenceDirection) {
        correspondenceDirection_ = correspondenceDirection;
    }
    
    abstract List<BifurcationCurveBranchGeom> getBifurcationListGeom();
    
    public abstract void showCorrespondentPoint(CoordsArray coordsWC, ViewingTransform viewingTransform);
    
}
