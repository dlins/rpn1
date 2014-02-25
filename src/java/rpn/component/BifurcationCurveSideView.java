package rpn.component;

import java.awt.Graphics2D;
import java.util.Iterator;
import java.util.List;
import rpn.component.util.GraphicsUtil;

import wave.multid.DimMismatchEx;
import wave.multid.model.MultiPolyLine;
import wave.multid.view.GeomObjView;
import wave.multid.view.ViewingAttr;
import wave.multid.view.ViewingTransform;

public class BifurcationCurveSideView extends GeomObjView {

    public BifurcationCurveSideView(BifurcationCurveBranchGeom abstractGeom,
            ViewingTransform transf,
            ViewingAttr viewAttr) throws DimMismatchEx {

        super(abstractGeom, transf, viewAttr);

    }
   

    //Original update method
    public void update() {
        viewList_.clear();
        Iterator geomListIterator = ((BifurcationCurveGeomSide) getAbstractGeom()).getBifurcationListGeom().iterator();
        while (geomListIterator.hasNext()) {
            BifurcationCurveGeomSide geomObj = (BifurcationCurveGeomSide) geomListIterator.next();
            List<BifurcationCurveBranchGeom> side = geomObj.getBifurcationListGeom();
            Iterator<BifurcationCurveBranchGeom> iterator = side.iterator();
            while (iterator.hasNext()) {
                BifurcationCurveGeomSide bifurcationCurveBranchGeom = (BifurcationCurveGeomSide) iterator.next();
               try {
                    List<MultiPolyLine> segList = bifurcationCurveBranchGeom.getSegList();
                    for (int i = 0; i < segList.size(); i++) {
                       MultiPolyLine multiPolyLine = segList.get(i);
                       viewList_.add(multiPolyLine.createView(getViewingTransform()));                       
                   }

            } catch (DimMismatchEx dex) {
                dex.printStackTrace();
            } 
                
            }
            
        }
    }
    
     @Override
    public void draw(Graphics2D g){
        
        super.draw(g);
        
        BifurcationCurveGeomSide bifurcationGeomSide = (BifurcationCurveGeomSide)getAbstractGeom();
        
         Iterator<GraphicsUtil> annotationIterator = bifurcationGeomSide.getAnnotationIterator();

        while (annotationIterator.hasNext()) {
            GraphicsUtil graphicsUtil = annotationIterator.next();
            graphicsUtil.update(getViewingTransform());
            graphicsUtil.getViewingAttr().setVisible(bifurcationGeomSide.viewingAttr().isVisible());
            g.setColor(graphicsUtil.getViewingAttr().getColor());
            graphicsUtil.draw(g);

        }
        
    }
    
    
    
    
}
