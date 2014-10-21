/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.controller;

import wave.multid.graphs.ViewPlane;

import wave.multid.graphs.dcViewport;
import wave.multid.*;
import wave.util.*;
import rpn.RPnPhaseSpacePanel;
import java.util.ArrayList;
import java.util.List;
import javax.swing.plaf.ComponentUI;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseAdapter;
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
import rpnumerics.TransitionalLine;
import wave.multid.Coords2D;
import wave.multid.CoordsArray;
import wave.multid.Space;

import wave.multid.graphs.wcWindow;
import wave.multid.view.ViewingTransform;
import wave.util.RealVector;


public class PhaseSpacePanel2DController extends ComponentUI implements PhaseSpacePanelController {

    //
    // Constants
    //
    public static int LASTINPUT_DISTANCE = 3;

    //
    // Members
    //

    private MouseMotionController mouseMotionController_;
    private MouseController mouseController_;
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
        mouseController_ = new MouseController();
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

    class MouseController extends MouseAdapter {


	public void mouseEntered(MouseEvent event){
            if (event.getComponent() instanceof RPnPhaseSpacePanel) {
                RPnPhaseSpacePanel panel = (RPnPhaseSpacePanel) event.getComponent();
	    	panel.repaint();
	    }


	}

	public void mouseExited(MouseEvent event){
            if (event.getComponent() instanceof RPnPhaseSpacePanel) {
                RPnPhaseSpacePanel panel = (RPnPhaseSpacePanel) event.getComponent();
	    	panel.repaint();
	    }

	}

    }

    class MouseMotionController extends MouseMotionAdapter {

        @Override
        public void mouseMoved(MouseEvent event) {


	    
            if (event.getComponent() instanceof RPnPhaseSpacePanel) {
                RPnPhaseSpacePanel panel = (RPnPhaseSpacePanel) event.getComponent();
                int xCursorPos = event.getPoint().x;
                int yCursorPos = event.getPoint().y;


                if ((UIController.instance().getState() instanceof TRACKPOINT_CONFIG )&&
                        (panel.scene().getViewingTransform().projectionMap().getDomain().getDim()==rpnumerics.RPNUMERICS.domainDim())){


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

                //Selected curve . Closest point as input (only 2D)
                if (UIController.instance().getSelectedGeometriesList().size() == 1) {
                    Coords2D dcCoords = new Coords2D(xCursorPos, yCursorPos);
                    CoordsArray coordsWC = new CoordsArray(new Space("", rpnumerics.RPNUMERICS.domainDim()));
                    panel.scene().getViewingTransform().dcInverseTransform(dcCoords, coordsWC);

                    RealVector wcCoordsVector = new RealVector(coordsWC.getCoords());
                    RpGeomFactory geomFactory = UIController.instance().getSelectedGeometriesList().get(0).geomFactory();
                    RPnCurve curve = (RPnCurve) geomFactory.geomSource();
                    
                    if (!(curve instanceof TransitionalLine)){//TODO To refactor !
                           System.out.println("Curve mais proxima: "+curve);
                    RealVector closestPoint = curve.findClosestPoint(wcCoordsVector);
                    for (int i = 0; i < closestPoint.getSize(); i++) {
                        UIController.instance().globalInputTable().setElement(i, closestPoint.getElement(i));
                    }

//                    System.out.println("Ponto mais proximo: "+closestPoint);
                    ViewingTransform viewingTransform = panel.scene().getViewingTransform();

                    CoordsArray wcCoords = new CoordsArray(closestPoint);
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
//                 
                }


		// this is for user friendly orientation of the last input and SHOULD NOT BE HERE...
		RealVector lastValues = UIController.instance().globalInputTable().lastValues();

		Coords2D dcCoords = new Coords2D();
		CoordsArray wcCoords = new Coords2D(lastValues.toDouble());
		panel.scene().getViewingTransform().viewPlaneTransform(wcCoords, dcCoords);

		int xLastInputCursorPos = new Double(dcCoords.getX()).intValue();
		int yLastInputCursorPos = new Double(dcCoords.getY()).intValue();


		if (Math.abs(xCursorPos - xLastInputCursorPos) < LASTINPUT_DISTANCE && Math.abs(yCursorPos - yLastInputCursorPos) < LASTINPUT_DISTANCE) 

			panel.setShowLastInputCursorPosHighlight(true);
		else
			panel.setShowLastInputCursorPosHighlight(false);

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
   
                dcViewport newViewport = new dcViewport(new Point(),wPanel,hPanel,20);
                wcWindow currWindow = panel.scene().getViewingTransform().viewPlane().getWindow();
                        
                panel.scene().getViewingTransform().setViewPlane(new ViewPlane(newViewport,currWindow));
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
        panel.addMouseListener(mouseController_);
        panel.addComponentListener(new PanelSizeController());

    }

    public void uninstall(RPnPhaseSpacePanel panel) {
        panel.removeMouseMotionListener(mouseMotionController_);
        panel.removeMouseListener(mouseController_);
        panel.removeComponentListener(new PanelSizeController());

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
