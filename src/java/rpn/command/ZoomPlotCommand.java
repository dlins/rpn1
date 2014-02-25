/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.command;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.swing.JButton;
import rpn.RPnMenuCommand;
import rpn.RPnPhaseSpaceAbstraction;
import rpn.RPnPhaseSpaceFrame;
import rpn.RPnPhaseSpacePanel;
import rpn.RPnProjDescriptor;
import rpn.component.RpGeometry;
import rpn.component.util.AreaSelected;
import rpn.component.util.Label;
import rpn.controller.ui.UIController;
import rpnumerics.RPNUMERICS;
import wave.multid.Space;
import wave.multid.view.Scene;
import wave.util.Boundary;
import wave.util.RealVector;
import wave.util.RectBoundary;

public class ZoomPlotCommand extends RpModelPlotCommand implements RPnMenuCommand, WindowListener {

    static public final String DESC_TEXT = "Zoom";
    static private ZoomPlotCommand instance_ = null;
    private final HashMap<RPnPhaseSpaceFrame, AreaSelected> areaZoomFrameMap_;
    private AreaSelected selectedArea_;
    private RPnPhaseSpacePanel originalPanel_;

    private ZoomPlotCommand() {

        super(DESC_TEXT, null);
        areaZoomFrameMap_ = new HashMap<RPnPhaseSpaceFrame, AreaSelected>();
    }

    @Override
    public void execute() {

        List<RealVector> wcVertices = selectedArea_.getWCVertices();

        RealVector min = wcVertices.get(0);
        RealVector max = wcVertices.get(2);
        Boundary boundary = new RectBoundary(min, max);

        Scene scene = phaseSpaceFrameZoom(boundary);

        RPnPhaseSpaceFrame zoomFrame = new RPnPhaseSpaceFrame(scene, this);

        String areaID = selectedArea_.getID();

        zoomFrame.addWindowListener(this);
        UIController.instance().install(zoomFrame.phaseSpacePanel());
        zoomFrame.setTitle(areaID);
        zoomFrame.pack();
        zoomFrame.setVisible(true);

        
        RealVector labelPosition = calculateLabelPosition(min, max);
        List<Object> wcObject = new ArrayList<Object>();
        wcObject.add(labelPosition);
        wcObject.add(areaID);

        zoomFrame.phaseSpacePanel().addGraphicUtil(new Label(wcObject, scene.getViewingTransform(), selectedArea_.getViewingAttr()));

        areaZoomFrameMap_.put(zoomFrame, selectedArea_);

    }

    public void setAreaAndPanel(AreaSelected area, RPnPhaseSpacePanel panel) {
        selectedArea_ = area;
        originalPanel_ = panel;

    }

    @Override
    public void actionPerformed(ActionEvent event) {

    }

    public Scene phaseSpaceFrameZoom(Boundary boundary) {

        wave.multid.graphs.ClippedShape clipping = new wave.multid.graphs.ClippedShape(boundary);

        int width = originalPanel_.scene().getViewingTransform().viewPlane().getViewport().width;
        int height = originalPanel_.scene().getViewingTransform().viewPlane().getViewport().height;

        System.out.println("Retangular: " + clipping.isRectangular());
        Space zoomSpace = new Space("", RPNUMERICS.domainDim());
        int[] testeArrayIndex = {0, 1};

        // tanto faz a meu ver a proporcao em DC...
        RPnProjDescriptor projDescriptor = new RPnProjDescriptor(zoomSpace, "", width, height, testeArrayIndex, false);

        wave.multid.view.ViewingTransform viewingTransf = projDescriptor.createTransform(clipping);

        try {

            RPnPhaseSpaceAbstraction phaseSpace = (RPnPhaseSpaceAbstraction) originalPanel_.scene().getAbstractGeom();

            Scene scene = phaseSpace.createScene(viewingTransf,
                    new wave.multid.view.ViewingAttr(Color.black));

            return scene;

        } catch (wave.multid.DimMismatchEx dex) {
            dex.printStackTrace();
        }

        return null;

    }

    public static ZoomPlotCommand instance() {
        if (instance_ == null) {
            instance_ = new ZoomPlotCommand();
        }
        return instance_;
    }

    @Override
    public void unexecute() {
    }

    @Override
    public RpGeometry createRpGeometry(RealVector[] coords) {
        return null;
    }

    @Override
    public void finalizeApplication() {

    }

    @Override
    public void networkCommand() {
        System.out.println("Network command");
    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {
        RPnPhaseSpaceFrame frame = (RPnPhaseSpaceFrame) e.getSource();
        AreaSelected zoomArea = areaZoomFrameMap_.get(frame);
        Iterator<RPnPhaseSpacePanel> installedPanelsIterator = UIController.instance().getInstalledPanelsIterator();

        while (installedPanelsIterator.hasNext()) {
            RPnPhaseSpacePanel rPnPhaseSpacePanel = installedPanelsIterator.next();

            rPnPhaseSpacePanel.removeGraphicsUtil(zoomArea);
            rPnPhaseSpacePanel.repaint();

        }

        areaZoomFrameMap_.remove(frame);

        frame.dispose();

    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }

    private RealVector calculateLabelPosition(RealVector minBoundary, RealVector maxBoundary) {

        RealVector lowerLeftVertice = new RealVector(minBoundary);
        RealVector upperRightVertice = new RealVector(maxBoundary);

        RealVector upperLeftVertice = new RealVector(selectedArea_.getWCVertices().get(3));

        RealVector p = new RealVector(lowerLeftVertice);

        RealVector q = new RealVector(upperLeftVertice);

        q.scale(0.95);

        p.scale(0.05);

        p.add(q);

        double y = p.getElement(1);

        p = new RealVector(upperRightVertice);
        q = new RealVector(upperLeftVertice);

        q.scale(0.95);

        p.scale(0.05);

        p.add(q);

        double x = p.getElement(0);

        RealVector result = new RealVector(x+ " " + y);

        return result;

    }

}
