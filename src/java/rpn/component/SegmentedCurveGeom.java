/*
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
import rpn.component.util.GraphicsUtil;
import wave.multid.CoordsArray;
import wave.multid.DimMismatchEx;
import wave.multid.Space;
import wave.multid.model.AbstractPath;
import wave.multid.model.AbstractPathIterator;
import wave.multid.model.BoundingBox;
import wave.multid.model.MultiGeometry;
import wave.multid.model.MultiPolyLine;
import wave.multid.view.ViewingAttr;

public abstract class SegmentedCurveGeom implements MultiGeometry, RpGeometry {

    protected Space space_;
    protected BoundingBox boundary_;
    private List<MultiPolyLine> segmentsList_;
    private RpGeomFactory factory_;
    protected ViewingAttr VIEWING_ATTR = new ViewingAttr(Color.white);
    private List<GraphicsUtil> annotationsList_;

    public SegmentedCurveGeom(MultiPolyLine[] segArray, RpGeomFactory factory) {

        segmentsList_ = new ArrayList();
        annotationsList_ = new ArrayList<GraphicsUtil>();
        for (int i = 0; i < segArray.length; i++) {
            segmentsList_.add(segArray[i]);
        }
        factory_ = factory;
        space_ = rpnumerics.RPNUMERICS.domain();
        try {
            boundary_ = new BoundingBox(new CoordsArray(space_), new CoordsArray(space_));
        } catch (DimMismatchEx dex) {
            dex.printStackTrace();
        }

    }

    @Override
    public void addAnnotation(GraphicsUtil annotation) {

        annotationsList_.add(annotation);

    }

    public void setLastAnnotation(GraphicsUtil annotation) {
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

    public AbstractPathIterator getPathIterator() {
        AbstractPath nullPath = new AbstractPath(getSpace());
        return nullPath.getPathIterator();
    }

    public Space getSpace() {
        return space_;
    }

    public void print(FileWriter cout) {
    }

    public void load(FileReader cin) {
    }

    public AbstractPathIterator getPathIterator(wave.multid.map.Map map) throws DimMismatchEx {
        AbstractPath nullPath = new AbstractPath(getSpace());
        return nullPath.getPathIterator(map);
    }

    public BoundingBox getBoundary() {
        return boundary_;
    }

    public ViewingAttr viewingAttr() {
        return VIEWING_ATTR;
    }

    public Iterator getRealSegIterator() {
        return segmentsList_.iterator();
    }

    public void lowLight() {

        for (MultiPolyLine object : segmentsList_) {
            object.lowLight();
        }

    }

    public void highLight() {
        for (MultiPolyLine object : segmentsList_) {
            object.highLight();
        }

    }

    @Override
    public void setVisible(boolean visible) {
        viewingAttr().setVisible(visible);
        for (MultiPolyLine object : segmentsList_) {
            object.setVisible(visible);
        }
    }

    @Override
    public void setSelected(boolean selected) {
        viewingAttr().setSelected(selected);
        for (MultiPolyLine object : segmentsList_) {
            object.setSelected(selected);
        }
    }

    public RpGeomFactory geomFactory() {
        return factory_;
    }

    public void applyMap(wave.multid.map.Map map) throws DimMismatchEx {
    }

    @Override
    public boolean isVisible() {
        return viewingAttr().isVisible();
    }

    @Override
    public boolean isSelected() {
        return viewingAttr().isSelected();
    }

}
