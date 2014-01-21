/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.controller;

import wave.multid.graphs.ViewPlane;
import wave.multid.view.Viewing2DTransform;
import wave.multid.view.Viewing3DTransform;
import wave.multid.graphs.Iso2EquiTransform;
import wave.multid.graphs.dcViewport;
import rpn.RPnPhaseSpacePanel;
import java.util.ArrayList;
import java.util.List;
import javax.swing.plaf.ComponentUI;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.Line2D;
import rpn.controller.ui.TRACKPOINT_CONFIG;
import rpn.controller.ui.UIController;
import rpn.command.TrackPointCommand;
import rpn.component.RpGeomFactory;
import rpnumerics.RPnCurve;
import wave.multid.Coords2D;
import wave.multid.CoordsArray;
import wave.multid.Space;
import wave.multid.view.ViewingTransform;
import wave.util.RealVector;

public class PhaseSpacePanel2DController extends ComponentUI implements PhaseSpacePanelController {
    //
    // Constants
    //
    //
    // Members
    //

    private MouseMotionController mouseMotionController_;
    private int absIndex_;
    private int ordIndex_;
    private List pointMarkBuffer_;
    private boolean absComplete_;
    private boolean ordComplete_;
    private Point dcCompletePoint_;
    private List<Polygon> selectionAreas_;
    private List<Line2D.Double> stringArrows_;
    private List<String> typeStrings_;
    private List<Line2D.Double> velocityArrows_;
    private List<String> velocityStrings_;

    //
    // Constructors/Initializers
    //
    public PhaseSpacePanel2DController(int absIndex, int ordIndex) {
        mouseMotionController_ = new MouseMotionController();
        absIndex_ = absIndex;
        ordIndex_ = ordIndex;
        absComplete_ = false;
        ordComplete_ = false;
        dcCompletePoint_ = new Point(0, 0);
        pointMarkBuffer_ = new ArrayList();
        selectionAreas_ = new ArrayList<Polygon>();
        stringArrows_ = new ArrayList<Line2D.Double>();
        typeStrings_ = new ArrayList<String>();
        velocityArrows_ = new ArrayList<Line2D.Double>();
        velocityStrings_ = new ArrayList<String>();

    }

    //
    // Inner Classes
    //
    class MouseMotionController extends MouseMotionAdapter {

        @Override
        public void mouseMoved(MouseEvent event) {
            if (event.getComponent() instanceof RPnPhaseSpacePanel) {
                RPnPhaseSpacePanel panel = (RPnPhaseSpacePanel) event.getComponent();
                int xCursorPos = event.getPoint().x;
                int yCursorPos = event.getPoint().y;
                if ((UIController.instance().getState() instanceof TRACKPOINT_CONFIG)
                        && (panel.scene().getViewingTransform().projectionMap().getDomain().getDim() == rpnumerics.RPNUMERICS.domainDim())) {
                    Coords2D dcCoords = new Coords2D(xCursorPos, yCursorPos);
                    CoordsArray wcCoords = new Coords2D();
                    panel.scene().getViewingTransform().dcInverseTransform(dcCoords, wcCoords);
                    TrackPointCommand.instance().trackPoint(wcCoords);

                }

                if (absComplete_) {
                    xCursorPos = new Double(dcCompletePoint_.getX()).intValue();
                }
                if (ordComplete_) {
                    yCursorPos = new Double(dcCompletePoint_.getY()).intValue();
                }

                panel.setCursorPos(new Point(xCursorPos, yCursorPos));

                //1 curva selecionada. Usar o ponto mais proximo como entrada (Apenas 2D)
                if (UIController.instance().getSelectedGeometriesList().size() == 1) {
                    Coords2D dcCoords = new Coords2D(xCursorPos, yCursorPos);
                    CoordsArray coordsWC = new CoordsArray(new Space("", rpnumerics.RPNUMERICS.domainDim()));
                    panel.scene().getViewingTransform().dcInverseTransform(dcCoords, coordsWC);

                    RealVector wcCoordsVector = new RealVector(coordsWC.getCoords());
                    RpGeomFactory geomFactory = UIController.instance().getSelectedGeometriesList().get(0).geomFactory();
                    RPnCurve curve = (RPnCurve) geomFactory.geomSource();
                    RealVector closestPoint = curve.findClosestPoint(wcCoordsVector);
                    for (int i = 0; i < closestPoint.getSize(); i++) {
                        UIController.instance().globalInputTable().setElement(i, closestPoint.getElement(i));
                    }
                    
//                    System.out.println("Ponto mais proximo: "+closestPoint);
                    ViewingTransform viewingTransform = panel.scene().getViewingTransform();
                    Coords2D wcCoords = new Coords2D(closestPoint.getElement(0), closestPoint.getElement(1));
                    Coords2D painelCoords = new Coords2D();
                    viewingTransform.viewPlaneTransform(wcCoords, painelCoords);
                   
                    Point point = new Point((int) painelCoords.getX(), (int) painelCoords.getY());
                    List pointMarkBuffer = panel.getCastedUI().pointMarkBuffer();
                    if (pointMarkBuffer.isEmpty()) {
                        pointMarkBuffer.add(point);
                    } else {
                        pointMarkBuffer.set(pointMarkBuffer.size() - 1, point);
                    }

                }
                panel.repaint();
            }
        }
    }

    class PanelSizeController extends ComponentAdapter {

        @Override
        public void componentResized(ComponentEvent event) {
            if (event.getComponent() instanceof RPnPhaseSpacePanel) {
                RPnPhaseSpacePanel panel = (RPnPhaseSpacePanel) event.getComponent();

                int wPanel = panel.getWidth();
                int hPanel = panel.getHeight();
                dcViewport vp = panel.scene().getViewingTransform().viewPlane().getViewport();
                vp.height = hPanel;
                vp.width = wPanel;
                ViewPlane vPlane = new ViewPlane(vp, panel.scene().getViewingTransform().viewPlane().getWindow());


                if (panel.scene().getViewingTransform() instanceof Viewing3DTransform) {
                    Viewing3DTransform viewTrans = (Viewing3DTransform) panel.scene().getViewingTransform();
                    Viewing3DTransform new3DTransform = new Viewing3DTransform(viewTrans.projectionMap(), vPlane, viewTrans.viewReferencePoint(), viewTrans.zCoord());
                    panel.scene().setViewingTransform(new3DTransform);

                }

                if (panel.scene().getViewingTransform() instanceof Viewing2DTransform) {

                    try {
                        Iso2EquiTransform viewTrans = (Iso2EquiTransform) panel.scene().getViewingTransform();

                        Iso2EquiTransform newIsoTransform = new Iso2EquiTransform(viewTrans.projectionMap(),
                                vPlane);
                        panel.scene().setViewingTransform(newIsoTransform);

                    } catch (ClassCastException ex) {
                        Viewing2DTransform vt = (Viewing2DTransform) panel.scene().getViewingTransform();
                        Viewing2DTransform new2DTransform = new Viewing2DTransform(panel.scene().
                                getViewingTransform().projectionMap(), vPlane);
                        panel.scene().setViewingTransform(new2DTransform);

                    }

                }


                panel.scene().update();
                panel.updateGraphicsUtil();
                panel.repaint();
            }

        }
    }

    //
    // Accessors/Mutators
    //
    public int getAbsIndex() {
        return absIndex_;
    }

    public int getOrdIndex() {
        return ordIndex_;
    }

    public List pointMarkBuffer() {
        return pointMarkBuffer_;
    }

    public List<Polygon> getSelectionAreas() {
        return selectionAreas_;
    }

    public List<String> getTypeString() {
        return typeStrings_;
    }

    public List<String> getVelocityString() {
        return velocityStrings_;
    }

    public Point get_dc_CompletePoint() {
        return dcCompletePoint_;
    }

    public void set_dc_CompletePoint(Point point) {
        dcCompletePoint_ = point;
    }

    public boolean isAbsComplete() {
        return absComplete_;
    }

    public boolean isOrdComplete() {
        return ordComplete_;
    }

    public void setAbsComplete(boolean complete) {
        absComplete_ = complete;
    }

    public void setOrdComplete(boolean complete) {
        ordComplete_ = complete;
    }

    //
    // Methods
    //
    public void install(RPnPhaseSpacePanel panel) {
        panel.addMouseMotionListener(mouseMotionController_);
        panel.addComponentListener(new PanelSizeController());

    }

    public void uninstall(RPnPhaseSpacePanel panel) {
        panel.removeMouseMotionListener(mouseMotionController_);

    }

    public void resetCursorCoords() {
        absComplete_ = false;
        ordComplete_ = false;
    }

    public void evaluateCursorCoords(RPnPhaseSpacePanel clickedPanel, Point point) {
        // TODO this will require dc frame dimensions to be the same
        // check absCoords for our both indices
        if (!absComplete_) {
            if (((PhaseSpacePanelController) clickedPanel.getCastedUI()).getAbsIndex() == absIndex_) {
                absComplete_ = true;
                dcCompletePoint_.x = point.x;
            } else if (((PhaseSpacePanelController) clickedPanel.getCastedUI()).getOrdIndex() == absIndex_) {
                absComplete_ = true;
                dcCompletePoint_.x = point.y;
            }
        }
        // check ordCoords for our both indices
        if (!ordComplete_) {
            if (((PhaseSpacePanelController) clickedPanel.getCastedUI()).getOrdIndex() == ordIndex_) {
                ordComplete_ = true;
                dcCompletePoint_.y = point.y;
            } else if (((PhaseSpacePanelController) clickedPanel.getCastedUI()).getAbsIndex() == ordIndex_) {
                ordComplete_ = true;
                dcCompletePoint_.y = point.x;
            }
        }
        if (isOrdComplete() && isAbsComplete()) {
            pointMarkBuffer().add(new Point(get_dc_CompletePoint()));
        }
    }
}
