/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import rpn.component.util.GraphicsUtil;
import rpn.component.util.Label;
import rpn.component.util.LinePlotted;
import rpnumerics.Diagram;
import rpnumerics.DiagramLine;
import wave.multid.model.MultiPolyLine;
import wave.multid.CoordsArray;
import wave.multid.view.GeomObjView;
import wave.multid.view.ViewingTransform;
import wave.multid.DimMismatchEx;
import wave.multid.Space;
import wave.multid.model.MultiGeometryImpl;
import wave.multid.view.ViewingAttr;
import wave.util.RealSegment;
import wave.util.RealVector;

public class DiagramGeom extends MultiGeometryImpl implements RpGeometry {

    // Members
    //
    private RpGeomFactory factory_;
    private List<GraphicsUtil> annotationsList_;
    private List<MultiPolyLine> diagramsList_;

    //
    // Constructors
    //
    public DiagramGeom(List<MultiPolyLine> diagramsList, RpDiagramFactory factory) {
        super(new Space("Diagram", 2), new ViewingAttr(Color.white));
        diagramsList_ = diagramsList;
        annotationsList_ = new ArrayList<GraphicsUtil>();
        factory_ = factory;

    }

    //
    // Accessors/Mutators
    //
    @Override
    public GeomObjView createView(ViewingTransform transf) throws DimMismatchEx {

        return new DiagramView(this, transf, viewingAttr());

    }

    public RpGeomFactory geomFactory() {
        return factory_;
    }

    public Iterator<MultiPolyLine> diagramsIterator() {
        return diagramsList_.iterator();
    }

    @Override
    public void setVisible(boolean visible) {

        for (MultiPolyLine graphicsUtil : diagramsList_) {

            graphicsUtil.setVisible(visible);
        }

        for (GraphicsUtil graphicsUtil : annotationsList_) {

            graphicsUtil.getViewingAttr().setVisible(visible);

        }

    }

    @Override
    public void setSelected(boolean selected) {

        for (MultiPolyLine graphicsUtil : diagramsList_) {

            graphicsUtil.setVisible(selected);
        }

        for (GraphicsUtil graphicsUtil : annotationsList_) {

            graphicsUtil.getViewingAttr().setSelected(selected);

        }

    }

    @Override
    public void addAnnotation(GraphicsUtil annotation) {

        annotationsList_.add(annotation);

    }

    public void removeLastAnnotation() {
        if (!annotationsList_.isEmpty()) {
            annotationsList_.remove(annotationsList_.size() - 1);
        }

    }

    public RealVector getMin() {
        Diagram geomSource = (Diagram) factory_.geomSource();

        double minX = 0;
        double minY = 0;
        List<DiagramLine> lines = geomSource.getLines();

        for (DiagramLine diagramLine : lines) {
            List<List<RealVector>> coords = diagramLine.getCoords();

            for (List<RealVector> list : coords) {

                for (RealVector realVector : list) {

                    if (realVector.getElement(0) < minX) {
                        minX = realVector.getElement(0);
                    }

                    if (realVector.getElement(1) < minY) {
                        minY = realVector.getElement(1);
                    }

                }
            }
        }

        RealVector limits = new RealVector(2);
        limits.setElement(0, minX);
        limits.setElement(1, minY);
        return limits;

    }

    public RealVector getPointByIndex(int diagramLineIndex, double x) {

        
        RealVector origin = new RealVector(2);
        Diagram geomSource = (Diagram) factory_.geomSource();
        List<DiagramLine> lines = geomSource.getLines();
        DiagramLine diagramLine = lines.get(diagramLineIndex);
        List<RealSegment> segments = diagramLine.getSegments();

        for (int i = 0; i < segments.size(); i++) {
            RealSegment realSegment = segments.get(i);

            if (realSegment.p1().getElement(0) == x) {
                return realSegment.p1();
                
            }
//            } else {
//                return realSegment.p2();
//            }

        }

        return origin;
    }

    public RealVector getMax() {
        Diagram geomSource = (Diagram) factory_.geomSource();

        double maxX = 0;
        double maxY = 0;
        List<DiagramLine> lines = geomSource.getLines();

        for (DiagramLine diagramLine : lines) {
            List<List<RealVector>> coords = diagramLine.getCoords();

            for (List<RealVector> list : coords) {

                for (RealVector realVector : list) {

                    if (realVector.getElement(0) > maxX) {
                        maxX = realVector.getElement(0);
                    }

                    if (realVector.getElement(1) > maxY) {
                        maxY = realVector.getElement(1);
                    }

                }

            }

        }

        RealVector limits = new RealVector(2);
        limits.setElement(0, maxX);
        limits.setElement(1, maxY);
        return limits;

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
    public void showSpeed(CoordsArray curvePoint, CoordsArray wcPoint, ViewingTransform transform) {

        List<Object> lineElements = new ArrayList<Object>();

        lineElements.add(new RealVector(curvePoint.getCoords()));
        lineElements.add(new RealVector(wcPoint.getCoords()));
        lineElements.add("Teste");

        LinePlotted speed = new LinePlotted(lineElements, transform, new ViewingAttr(Color.white));
        addAnnotation(speed);

    }

    @Override
    public void showClassification(CoordsArray curvePoint, CoordsArray wcPoint, ViewingTransform transform) {

    }

    @Override
    public boolean isVisible() {

        boolean isVisible = false;
        for (MultiPolyLine graphicsUtil : diagramsList_) {

            isVisible = graphicsUtil.isVisible();
        }

        return isVisible;

    }

    @Override
    public boolean isSelected() {

        boolean isSelected = false;
        for (MultiPolyLine graphicsUtil : diagramsList_) {

            isSelected = graphicsUtil.isSelected();
        }

        return isSelected;

    }
}
