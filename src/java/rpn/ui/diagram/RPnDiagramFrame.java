/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.ui.diagram;

import rpn.ui.diagram.DiagramLabel;
import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.*;
import rpn.RPnMenuCommand;
import rpn.RPnPhaseSpaceAbstraction;
import rpn.RPnPhaseSpacePanel;
import rpn.RPnProjDescriptor;
import rpn.component.RpGeometry;
import rpn.controller.ui.UIController;
import rpnumerics.RPNUMERICS;
import wave.multid.DimMismatchEx;
import wave.multid.Space;
import wave.util.RealVector;
import wave.util.RectBoundary;

public class RPnDiagramFrame extends JFrame implements WindowListener {

    private JPanel contentPane;
    private RPnPhaseSpacePanel phaseSpacePanel_;
    GridLayout borderLayout1 = new GridLayout(2, 1);
    JPanel statusPanel = new JPanel();
    JPanel southPanel_ = new JPanel();
    JPanel westPanel_ = new JPanel();
    JPanel jPanel4 = new JPanel();
    JPanel jPanel5 = new JPanel();

    BorderLayout borderLayout2 = new BorderLayout(10, 10);
    RPnMenuCommand commandMenu_ = null;

    private DiagramLabel cursorMonitor_;

    private DiagramLabel yMonitor_;

    private DiagramLabel relaterPoint_;
    private RPnPhaseSpaceAbstraction phaseSpace_;

    public RPnDiagramFrame(RPnPhaseSpaceAbstraction phaseSpace, String xName, String[] yName, RPnMenuCommand command) {

        disableEvents(AWTEvent.WINDOW_EVENT_MASK);
        disableEvents(AWTEvent.COMPONENT_EVENT_MASK);

        phaseSpace_ = phaseSpace;
        setTitle(phaseSpace.getName());

        commandMenu_ = command;
        try {
            cursorMonitor_ = new DiagramLabel(xName);
            yMonitor_ = new DiagramLabel(xName);

            for (int i = 0; i < yName.length; i++) {
                yMonitor_.setFieldName(i, yName[i]);

            }

            relaterPoint_ = new DiagramLabel("Phase Space Point");
            
            for (int i = 0; i < RPNUMERICS.domainDim(); i++) {
                relaterPoint_.setFieldName(i, String.valueOf(i));
                
            }
            
            phaseSpacePanel_ = new RPnDiagramPanel(cursorMonitor_, yMonitor_, relaterPoint_);

            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }

        addWindowListener(this);
    }

    public void updateScene(RealVector profileMin, RealVector profileMax) {

        profileMax.setElement(1, profileMax.getElement(1) + (0.2 * profileMax.getElement(1)));

        RectBoundary boundary = new RectBoundary(profileMin, profileMax);
        Space riemanProfileSpace = new Space("SpeedGraphics", 2);

        int[] riemannProfileIndices = {0, 1};

        wave.multid.graphs.ClippedShape clipping = new wave.multid.graphs.ClippedShape(boundary);
        RPnProjDescriptor projDescriptor = new RPnProjDescriptor(riemanProfileSpace, "SpeedGraphicsSpace", 400, 400, riemannProfileIndices, false);
        wave.multid.view.ViewingTransform riemanTesteTransform = projDescriptor.createTransform(clipping);

        try {
            wave.multid.view.Scene diagramScene = phaseSpace_.createScene(riemanTesteTransform, new wave.multid.view.ViewingAttr(Color.black));
            contentPane.remove(phaseSpacePanel_);

            phaseSpacePanel_ = new RPnDiagramPanel(diagramScene, cursorMonitor_, yMonitor_, relaterPoint_);
            contentPane.add(phaseSpacePanel_, BorderLayout.CENTER);
            validate();

        } catch (DimMismatchEx ex) {
            ex.printStackTrace();
        }

    }

    //Component initialization
    private void jbInit() throws Exception {

        contentPane = (JPanel) this.getContentPane();
        contentPane.setLayout(borderLayout2);

        contentPane.add(phaseSpacePanel_, BorderLayout.CENTER);

        southPanel_.add(cursorMonitor_.getSpeed());
        southPanel_.setBackground(Color.black);
        contentPane.add(southPanel_, BorderLayout.SOUTH);

        westPanel_.setLayout(new BorderLayout(10, 10));
        westPanel_.add(yMonitor_.getTextField(), BorderLayout.NORTH);
        westPanel_.add(relaterPoint_.getTextField(), BorderLayout.SOUTH);
        westPanel_.setBackground(Color.black);

        contentPane.add(westPanel_, BorderLayout.WEST);

        setSize(400, 400);

        setFocusable(true);

    }

    public RPnPhaseSpacePanel phaseSpacePanel() {
        return phaseSpacePanel_;
    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {

        Iterator<RpGeometry> geometryIterator = phaseSpace_.getGeomObjIterator();

        ArrayList<RpGeometry> toRemove = new ArrayList<RpGeometry>();

        while (geometryIterator.hasNext()) {
            toRemove.add(geometryIterator.next());
        }

        for (RpGeometry rpGeometry : toRemove) {

            phaseSpace_.remove(rpGeometry);

        }

        Iterator<RPnPhaseSpacePanel> installedPanelsIterator = UIController.instance().getInstalledPanelsIterator();

        while (installedPanelsIterator.hasNext()) {
            RPnPhaseSpacePanel rPnPhaseSpacePanel = installedPanelsIterator.next();
            rPnPhaseSpacePanel.getCastedUI().pointMarkBuffer().clear();
            rPnPhaseSpacePanel.repaint();

        }

        UIController.instance().getSelectedGeometriesList().clear();
        UIController.instance().globalInputTable().reset();

        UIController.instance().getActivePhaseSpace().updateCurveSelection();

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
}
