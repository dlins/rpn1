/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.List;
import wave.multid.view.Scene;

import javax.swing.*;
import rpn.parser.RPnDataModule;
import rpnumerics.RPNUMERICS;
import wave.multid.DimMismatchEx;
import wave.multid.Space;
import wave.util.RealVector;
import wave.util.RectBoundary;

public class RPnDiagramFrame extends JFrame {

    private JPanel contentPane;
    private RPnPhaseSpacePanel phaseSpacePanel = null;
    GridLayout borderLayout1 = new GridLayout(2, 1);
    JPanel statusPanel = new JPanel();
    JPanel jPanel2 = new JPanel();
    JPanel jPanel3 = new JPanel();
    JPanel jPanel4 = new JPanel();
    JPanel jPanel5 = new JPanel();

    BorderLayout borderLayout2 = new BorderLayout();
    RPnMenuCommand commandMenu_ = null;
    private Dimension frameSize_ = null;

    private RPnDiagramXMonitor cursorMonitor_;
    private RPnPhaseSpaceAbstraction phaseSpace_;

    //    int sliderValue_;
    public RPnDiagramFrame(Scene scene, RPnMenuCommand command) {

        disableEvents(AWTEvent.WINDOW_EVENT_MASK);
        disableEvents(AWTEvent.COMPONENT_EVENT_MASK);

        commandMenu_ = command;
        try {
            cursorMonitor_ = new RPnDiagramXMonitor(RPNUMERICS.domainDim());
            phaseSpacePanel = new RPnDiagramPanel(scene, cursorMonitor_);

            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    public RPnDiagramFrame(RPnPhaseSpaceAbstraction phaseSpace,RPnMenuCommand command) {

        disableEvents(AWTEvent.WINDOW_EVENT_MASK);
        disableEvents(AWTEvent.COMPONENT_EVENT_MASK);

        phaseSpace_=phaseSpace;
        
        commandMenu_ = command;
        try {
            cursorMonitor_ = new RPnDiagramXMonitor(RPNUMERICS.domainDim());
            phaseSpacePanel = new RPnDiagramPanel(cursorMonitor_);

            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    
    
    

    public void updateScene(RealVector profileMin, RealVector profileMax) {

        profileMax.setElement(1, profileMax.getElement(1)+(0.2*profileMax.getElement(1)));
        

        
        RectBoundary boundary = new RectBoundary(profileMin, profileMax);
        Space riemanProfileSpace = new Space("SpeedGraphics", 2);

        int[] riemannProfileIndices = {0, 1};

        wave.multid.graphs.ClippedShape clipping = new wave.multid.graphs.ClippedShape(boundary);
        RPnProjDescriptor projDescriptor = new RPnProjDescriptor(riemanProfileSpace, "SpeedGraphicsSpace", 400, 400, riemannProfileIndices, false);
        wave.multid.view.ViewingTransform riemanTesteTransform = projDescriptor.createTransform(clipping);

        try {
            wave.multid.view.Scene diagramScene = phaseSpace_.createScene(riemanTesteTransform, new wave.multid.view.ViewingAttr(Color.black));
            contentPane.remove(phaseSpacePanel);

            phaseSpacePanel = new RPnDiagramPanel(diagramScene, cursorMonitor_);
            contentPane.add(phaseSpacePanel,BorderLayout.CENTER);
            validate();

        } catch (DimMismatchEx ex) {
            ex.printStackTrace();
        }

    }

    //Component initialization
    private void jbInit() throws Exception {

        contentPane = (JPanel) this.getContentPane();
        contentPane.setLayout(borderLayout2);

        contentPane.add(phaseSpacePanel, BorderLayout.CENTER);

        jPanel2.add(cursorMonitor_.getSpeed());
        jPanel2.setBackground(Color.black);

        contentPane.add(jPanel2, BorderLayout.SOUTH);

        setSize(400, 400);

        setFocusable(true);

    }

    public RPnPhaseSpacePanel phaseSpacePanel() {
        return phaseSpacePanel;
    }
}
