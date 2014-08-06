package rpn.controller.phasespace.riemannprofile;

import java.awt.Color;
import java.util.Iterator;
import rpn.RPnPhaseSpacePanel;
import rpn.command.RiemannProfileCommand;
import rpn.component.DiagramGeom;
import rpn.component.RpDiagramFactory;
import rpn.component.RpGeometry;
import rpn.component.WaveCurveGeom;
import rpn.component.util.AreaSelected;
import rpn.component.util.GraphicsUtil;
import rpn.controller.ui.UIController;
import rpn.parser.RPnDataModule;
import rpnumerics.Area;
import rpnumerics.Diagram;
import rpnumerics.RiemannProfileCalc;
import rpnumerics.WaveCurve;
import wave.multid.view.ViewingAttr;
import wave.multid.view.ViewingTransform;
import wave.util.RealVector;

public class StandardRiemannProfileState implements RiemannProfileState, RiemannProfileReady {

    private GraphicsUtil area_;
    private final WaveCurveGeom firstWaveCurve_;
    private final WaveCurveGeom secondWaveCurve_;

    public StandardRiemannProfileState(WaveCurveGeom firstWaveCurve, WaveCurveGeom secondWaveCurve, GraphicsUtil area) {

        firstWaveCurve_ = firstWaveCurve;
        secondWaveCurve_ = secondWaveCurve;
        RiemannProfileCommand.instance().setEnabled(true);
        area_ = area;

    }

    @Override
    public void add(RpGeometry geom) {

    }

    public void remove(RpGeometry geom) {

    }

    public void select(GraphicsUtil area) {

        area_ = area;

    }

    public WaveCurveGeom getFirstWaveCurve() {
        return firstWaveCurve_;
    }

    public WaveCurveGeom getSecondWaveCurve() {
        return secondWaveCurve_;
    }

    public GraphicsUtil getSelection() {
        return area_;
    }

    public DiagramGeom calcProfile() {

        WaveCurve firstWaveCurve = (WaveCurve) getFirstWaveCurve().geomFactory().geomSource();
        WaveCurve secondWaveCurve = (WaveCurve) getSecondWaveCurve().geomFactory().geomSource();

        AreaSelected selection = (AreaSelected) area_;

        int[] waveCurvesID = new int[2];
        waveCurvesID[0] = firstWaveCurve.getId();
        waveCurvesID[1] = secondWaveCurve.getId();
        
        RiemannProfileCalc rc = new RiemannProfileCalc(new Area(selection), waveCurvesID);

        RpDiagramFactory factory = new RpDiagramFactory(rc);
 
        DiagramGeom geom = (DiagramGeom) factory.geom();

        return geom;

    }
    
    
    @Override
     public void updateRiemannProfile() {//TODO Tratar melhor o caso de erro na intersecao das curvas


        
        if(RPnDataModule.RIEMANNPHASESPACE.getLastGeometry()==null)return;

        RPnDataModule.RIEMANNPHASESPACE.remove(0);
        
        DiagramGeom diagramGeom = calcProfile();

        if(diagramGeom==null)return;
        
        
        Diagram diagram = (Diagram) diagramGeom.geomFactory().geomSource();

        String info = diagram.getInfo();

        RealVector center = new RealVector(info);

        Iterator<RPnPhaseSpacePanel> installedPanelsIterator = UIController.instance().getInstalledPanelsIterator();

        while (installedPanelsIterator.hasNext()) {
            RPnPhaseSpacePanel rPnPhaseSpacePanel = installedPanelsIterator.next();

            AreaSelected newArea = createNewArea(center, rPnPhaseSpacePanel.scene().getViewingTransform());
            select(newArea);
            rPnPhaseSpacePanel.setLastGraphicsUtil(newArea);

            rPnPhaseSpacePanel.repaint();

        }

        RPnDataModule.RIEMANNPHASESPACE.join(diagramGeom);
        RPnDataModule.RIEMANNPHASESPACE.update();
        RiemannProfileCommand.instance().updateRiemannFrame();
        
     

    }
    
    
    
     private AreaSelected createNewArea(RealVector center, ViewingTransform transform) {

        double delta = 0.1;

        RealVector p1 = new RealVector(2);

        p1.setElement(0, center.getElement(0) - delta);
        p1.setElement(1, center.getElement(1) + delta);

        RealVector p2 = new RealVector(2);

        p2.setElement(0, center.getElement(0) + delta);
        p2.setElement(1, center.getElement(1) + delta);

        RealVector p3 = new RealVector(2);

        p3.setElement(0, center.getElement(0) + delta);
        p3.setElement(1, center.getElement(1) - delta);

        RealVector p4 = new RealVector(2);

        p4.setElement(0, center.getElement(0) - delta);
        p4.setElement(1, center.getElement(1) - delta);

        RealVector vertices[] = new RealVector[4];

        vertices[0] = p1;
        vertices[1] = p2;
        vertices[2] = p3;
        vertices[3] = p4;

        return new AreaSelected(vertices, transform, new ViewingAttr(Color.red));

    }
    

}
