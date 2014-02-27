/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import rpn.component.util.CorrespondenceMark;
import rpn.component.util.GraphicsUtil;
import wave.multid.CoordsArray;
import wave.multid.view.ViewingTransform;

public abstract class BifurcationCurveBranchGeom implements RpGeometry{
    
    public final static int LEFTRIGHT =0;
    public final static int RIGHTLEFT =1;
    public final static int NONE =2;
    
    private int correspondenceDirection_;
    private final List<GraphicsUtil> annotationsList_;

    public BifurcationCurveBranchGeom() {
            annotationsList_ = new ArrayList<GraphicsUtil>();
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
    
    public void clearCorrespondenceMarks(){
        
        for (int i = 0; i < annotationsList_.size(); i++) {
            GraphicsUtil graphicsUtil = annotationsList_.get(i);
            if (graphicsUtil instanceof CorrespondenceMark)
                annotationsList_.remove(i);
            
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
