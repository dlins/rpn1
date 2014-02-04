/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.command;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.geom.Path2D;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JButton;
import rpn.RPnMenuCommand;
import rpn.RPnPhaseSpaceAbstraction;
import rpn.RPnPhaseSpaceFrame;
import rpn.RPnPhaseSpacePanel;
import rpn.RPnProjDescriptor;
import rpn.component.RpGeometry;
import rpn.component.util.AreaSelected;
import rpn.controller.ui.UIController;
import rpn.parser.RPnDataModule;
import rpnumerics.RPNUMERICS;
import wave.multid.Space;
import wave.multid.view.Scene;
import wave.multid.view.Viewing2DTransform;
import wave.util.Boundary;
import wave.util.RealVector;
import wave.util.RectBoundary;

public class ZoomPlotCommand extends RpModelPlotCommand implements Observer, RPnMenuCommand {

    static public final String DESC_TEXT = "Zoom";
    static private ZoomPlotCommand instance_ = null;

    private ZoomPlotCommand() {
        super(DESC_TEXT, null, new JButton());
    }

    @Override
    public void actionPerformed(ActionEvent event) {

        Iterator<RPnPhaseSpacePanel> installedPanelsIterator = UIController.instance().getInstalledPanelsIterator();
        RPnPhaseSpacePanel tempPanel = null;
        while (installedPanelsIterator.hasNext()) {
            RPnPhaseSpacePanel panel = installedPanelsIterator.next();

            if (!panel.getSelectedAreas().isEmpty()) {
                tempPanel = panel;

            }

        }
        List<AreaSelected> selectedAreas = tempPanel.getSelectedAreas();

        for (AreaSelected areaSelected : selectedAreas) {

            List<RealVector> wcVertices = areaSelected.getWCVertices();
            
            RealVector min = wcVertices.get(0);
            RealVector max =  wcVertices.get(2);
//            
            Boundary teste = RPNUMERICS.boundary();
            
            
            System.out.println(teste.getMinimums());
            System.out.println(teste.getMaximums());
            
//            
//            
//            double xMin = min.getElement(0)- (min.getElement(1)/Math.sqrt(3.0));
//            double yMin = (2/Math.sqrt(3.0))*min.getElement(1);
//            
//            
//            double xMax = max.getElement(0)- (max.getElement(1)/Math.sqrt(3.0));
//            double yMax = (2/Math.sqrt(3.0))*max.getElement(1);
//            
//            
//            
//            RealVector newMin = new RealVector(2);
//            
//            newMin.setElement(0,xMin);
//            newMin.setElement(1,yMin);
//            
//            RealVector newMax = new RealVector(2);
//            
//            newMax.setElement(0,xMax);
//            newMax.setElement(1,yMax);
//            
////            
//            
            

            
            for (int i = 0; i < 4; i++) {
                
                System.out.println(wcVertices.get(i));
                
            }
            
            
            Boundary boundary = new RectBoundary(min,max);

            Scene scene = phaseSpaceFrameZoom(boundary);

            rpn.RPnPhaseSpaceFrame frame = new RPnPhaseSpaceFrame(scene, this);
            UIController.instance().install(frame.phaseSpacePanel());
            frame.pack();
            frame.setVisible(true);
        }



    }

    public Scene phaseSpaceFrameZoom(Boundary boundary) {

        wave.multid.graphs.ClippedShape clipping = new wave.multid.graphs.ClippedShape(boundary);

        System.out.println("Retangular: "+clipping.isRectangular());
        Space zoomSpace = new Space("", RPNUMERICS.domainDim());
        int[] testeArrayIndex = {0, 1};
        RPnProjDescriptor projDescriptor = new RPnProjDescriptor(zoomSpace, "", 700, 700, testeArrayIndex, true);
        wave.multid.view.ViewingTransform viewingTransf = projDescriptor.createTransform(clipping);

      
        try {
            Scene scene = RPnDataModule.PHASESPACE.createScene(viewingTransf,
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
    public void execute() {
    }

    @Override
    public RpGeometry createRpGeometry(RealVector[] coords) {
        return null;
    }

    @Override
    public void update(Observable o, Object arg) {
    }

    @Override
    public void finalizeApplication() {
        System.out.println("Finalize app");
    }

    @Override
    public void networkCommand() {
        System.out.println("Network command");
    }
}
