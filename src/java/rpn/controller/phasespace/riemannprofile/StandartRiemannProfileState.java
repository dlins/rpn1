package rpn.controller.phasespace.riemannprofile;

import java.awt.Color;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Iterator;
import rpn.RPnMenuCommand;
import rpn.RPnPhaseSpaceAbstraction;
import rpn.RPnPhaseSpacePanel;
import rpn.RPnProjDescriptor;
import rpn.RPnRiemannFrame;
import rpn.command.DomainSelectionCommand;
import rpn.command.RiemannProfileCommand;
import rpn.component.DiagramGeom;
import rpn.component.RpDiagramFactory;
import rpn.component.RpGeometry;
import rpn.component.WaveCurveGeom;
import rpn.component.util.AreaSelected;
import rpn.controller.ui.UIController;
import rpnumerics.Area;
import rpnumerics.RPNUMERICS;
import rpnumerics.RiemannProfileCalc;
import rpnumerics.WaveCurve;
import wave.multid.DimMismatchEx;
import wave.multid.Space;
import wave.util.RealVector;
import wave.util.RectBoundary;

public class StandartRiemannProfileState extends RiemannProfileState implements RPnMenuCommand, WindowListener {

    private RPnPhaseSpaceAbstraction phaseSpace_;

    private RPnRiemannFrame speedGraphicsFrame_;
    private final WaveCurveGeom firstWaveCurve_;
    private final WaveCurveGeom secondWaveCurve_;

    public StandartRiemannProfileState(WaveCurveGeom firstWaveCurve, WaveCurveGeom secondWaveCurve) {
        
        firstWaveCurve_=firstWaveCurve;
        secondWaveCurve_=secondWaveCurve;
        DomainSelectionCommand.instance().setEnabled(true);
        RiemannProfileCommand.instance().setEnabled(true);

    }

    @Override
    public void plot(RPnPhaseSpaceAbstraction phaseSpace, RpGeometry geom) {

        WaveCurve firstWaveCurveSource = (WaveCurve) firstWaveCurve_.geomFactory().geomSource();

        WaveCurve secondWaveCurveSource = (WaveCurve) secondWaveCurve_.geomFactory().geomSource();

        phaseSpace_ = phaseSpace;

        int[] waveCurvesID = new int[2];

        waveCurvesID[0] = firstWaveCurveSource.getId();
        waveCurvesID[1] = secondWaveCurveSource.getId();

        Iterator<RPnPhaseSpacePanel> panelsIterator = UIController.instance().getInstalledPanelsIterator();
        while (panelsIterator.hasNext()) {
            RPnPhaseSpacePanel rPnPhaseSpacePanel = panelsIterator.next();

            for (AreaSelected sArea : rPnPhaseSpacePanel.getSelectedAreas()) {
                Area selectedArea = new Area(sArea);
                RiemannProfileCalc riemannProfileCalc = new RiemannProfileCalc(selectedArea, waveCurvesID);

                phaseSpace.clear();

                String Xlimits[] = RPNUMERICS.getParamValue("riemannprofile", "speedrange").split(" ");

                String Ylimits[] = RPNUMERICS.getParamValue("riemannprofile", "Yrange").split(" ");

                RealVector min = new RealVector(Xlimits[0] + " " + Ylimits[0]);
                RealVector max = new RealVector(Xlimits[1] + " " + Ylimits[1]);

                RpDiagramFactory factory = new RpDiagramFactory(riemannProfileCalc);
                DiagramGeom diagramGeom = (DiagramGeom) factory.geom();

                phaseSpace.join(diagramGeom);

                updateSpeedGraphicsFrame(min, max);

            }

        }

    }

    private void updateSpeedGraphicsFrame(RealVector profileMin, RealVector profileMax) {

        RectBoundary boundary = new RectBoundary(profileMin, profileMax);
        Space riemanProfileSpace = new Space("SpeedGraphics", 2);

        int[] riemannProfileIndices = {0, 1};

        wave.multid.graphs.ClippedShape clipping = new wave.multid.graphs.ClippedShape(boundary);
        RPnProjDescriptor projDescriptor = new RPnProjDescriptor(riemanProfileSpace, "SpeedGraphicsSpace", 400, 400, riemannProfileIndices, false);
        wave.multid.view.ViewingTransform riemanTesteTransform = projDescriptor.createTransform(clipping);

        try {
            wave.multid.view.Scene riemannScene = phaseSpace_.createScene(riemanTesteTransform, new wave.multid.view.ViewingAttr(Color.black));
            speedGraphicsFrame_ = new RPnRiemannFrame(riemannScene, this);
            speedGraphicsFrame_.addWindowListener(this);
            speedGraphicsFrame_.pack();
            speedGraphicsFrame_.setVisible(true);

        } catch (DimMismatchEx ex) {
            ex.printStackTrace();
        }

    }

    public void finalizeApplication() {
    }

    public void networkCommand() {

    }

    public void windowOpened(WindowEvent e) {

    }

    public void windowClosing(WindowEvent e) {

        phaseSpace_.clear();

    }

    public void windowClosed(WindowEvent e) {

        phaseSpace_.clear();
        speedGraphicsFrame_.dispose();

    }

    public void windowIconified(WindowEvent e) {

    }

    public void windowDeiconified(WindowEvent e) {

    }

    public void windowActivated(WindowEvent e) {

    }

    public void windowDeactivated(WindowEvent e) {

    }

}
