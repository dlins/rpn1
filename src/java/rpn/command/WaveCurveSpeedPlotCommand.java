/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.command;

import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JButton;
import rpn.RPnMenuCommand;
import rpn.ui.diagram.RPnDiagramFrame;
import rpn.component.*;
import rpn.controller.ui.UIController;
import rpn.controller.ui.UI_ACTION_SELECTED;
import rpn.parser.RPnDataModule;
import rpnumerics.*;
import wave.util.RealVector;

public class WaveCurveSpeedPlotCommand extends RpModelPlotCommand implements Observer, RPnMenuCommand, WindowListener {
    //
    // Constants
    //

    static public final String DESC_TEXT = "Wave Curve Speed Plot";
    //
    // Members
    //
    static private WaveCurveSpeedPlotCommand instance_ = null;

    private List<RpGeometry> selectedCurves;
    private RPnDiagramFrame speedGraphicsFrame_;

    //
    // Constructors/Initializers
    //
    protected WaveCurveSpeedPlotCommand() {
        super(DESC_TEXT, null, new JButton());

    }

    @Override
    public void actionPerformed(ActionEvent event) {
//        RPnDataModule.SPEEDGRAPHICSPHASESPACE.clear();        
        UI_ACTION_SELECTED action = new UI_ACTION_SELECTED(this);
        action.userInputComplete(UIController.instance());// No input needed

        

    }

    public RpGeometry createRpGeometry(RealVector[] input) {
        return null;
    }

    static public WaveCurveSpeedPlotCommand instance() {
        if (instance_ == null) {
            instance_ = new WaveCurveSpeedPlotCommand();
        }
        return instance_;
    }

    @Override
    public void update(Observable o, Object arg) {
        
        if (UIController.instance().getSelectedGeometriesList().size() == 1){
            boolean enable = ( UIController.instance().getSelectedGeometriesList().get(0) instanceof WaveCurveGeom);
            setEnabled(enable);
        }



    }

    @Override
    public void execute() {

        selectedCurves = UIController.instance().getSelectedGeometriesList();

        if (!selectedCurves.isEmpty()) {
            RpGeometry curveSelected = selectedCurves.get(0);

            WaveCurveBranch waveCurveBranch = (WaveCurveBranch) curveSelected.geomFactory().geomSource();

            RpDiagramFactory factory = new RpDiagramFactory(waveCurveBranch);
            DiagramGeom geom = (DiagramGeom) factory.geom();

            geom.setRelater(new SpeedDiagramRelater());
            RPnDataModule.SPEEDGRAPHICSPHASESPACE.join(geom);
            
            
            Diagram diagram = (Diagram) geom.geomFactory().geomSource();
            
            
            String [] fieldNames = new String[diagram.getLines().size()];
            
            for (int i = 0; i <diagram.getLines().size(); i++) {
                fieldNames[i]= diagram.getLine(i).getName();
            }

            speedGraphicsFrame_ = new RPnDiagramFrame(RPnDataModule.SPEEDGRAPHICSPHASESPACE, "xi", fieldNames, this);

            speedGraphicsFrame_.updateScene(geom.getMin(), geom.getMax());

            speedGraphicsFrame_.setVisible(true);

            UIController.instance().getSelectedGeometriesList().clear();

            UIController.instance().getActivePhaseSpace().updateCurveSelection();
        }

    }

    @Override
    public void finalizeApplication() {

        System.out.println("Chamando finalize");

    }

    @Override
    public void networkCommand() {

    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {
        System.out.println("chamando closing");
        RPnDataModule.SPEEDGRAPHICSPHASESPACE.clear();

    }

    @Override
    public void windowClosed(WindowEvent e) {
        System.out.println("chamando closed");
        setEnabled(false);
        RPnDataModule.SPEEDGRAPHICSPHASESPACE.clear();
        speedGraphicsFrame_.dispose();
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

    public void updateDiagramView(DiagramGeom diagramGeom) {

        speedGraphicsFrame_.updateScene(diagramGeom.getMin(), diagramGeom.getMax());

    }
}
