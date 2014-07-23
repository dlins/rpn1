package rpn.component;

import java.awt.Graphics2D;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import rpn.component.util.GraphicsUtil;
import wave.multid.model.MultiGeometryImpl;
import wave.multid.view.ViewingTransform;
import wave.multid.DimMismatchEx;
import wave.multid.model.MultiPolyLine;
import wave.multid.view.PolyLine;
import wave.multid.view.ViewingAttr;

public class DiagramView extends PolyLine {

    public DiagramView(MultiGeometryImpl geom, ViewingTransform transf,
            ViewingAttr attr) throws DimMismatchEx {
        super(geom, transf, attr);

    }

   

    @Override
    public void draw(Graphics2D g) {

        DiagramGeom diagram = ((DiagramGeom) getAbstractGeom());
        Iterator<MultiPolyLine> diagramsIterator = diagram.diagramsIterator();

      

        while (diagramsIterator.hasNext()) {
            try {
                MultiPolyLine multiPolyLine = diagramsIterator.next();
                multiPolyLine.createView(getViewingTransform()).draw(g);

            } catch (DimMismatchEx ex) {
                Logger.getLogger(DiagramView.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        Iterator<GraphicsUtil> annotationIterator = diagram.getAnnotationIterator();

        while (annotationIterator.hasNext()) {
            GraphicsUtil graphicsUtil = annotationIterator.next();
            graphicsUtil.update(getViewingTransform());
            graphicsUtil.getViewingAttr().setVisible(diagram.viewingAttr().isVisible());
            g.setColor(graphicsUtil.getViewingAttr().getColor());
            graphicsUtil.draw(g);

        }

    }

}
