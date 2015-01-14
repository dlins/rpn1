/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.command;

import wave.util.RealVector;
import wave.util.Boundary;
import wave.util.GridProfile;
import wave.util.MultiGrid;
import wave.util.GridIterator;
import rpnumerics.*;
import rpn.component.*;
import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
import rpn.controller.phasespace.NUMCONFIG;
import rpn.controller.phasespace.NumConfigImpl;
import rpn.controller.phasespace.NumConfigReadyImpl;
import rpn.controller.ui.*;
import rpn.parser.RPnDataModule;

public class FillPhaseSpaceCommand extends AbstractAction {
    static public final String DESC_TEXT = "Fills up the Phase Space";
    static public final int DEFAULT_NUMOFNODES = 10;
    private static FillPhaseSpaceCommand instance_ = null;
    private boolean bothDir_ = false;

    protected FillPhaseSpaceCommand() {
        super(DESC_TEXT, null);
    }


    // --- MÉTODO ORIGINAL
//    public void actionPerformed(ActionEvent action) {
//        Boundary bounds = RPNUMERICS.boundary();
//        GridProfile[] profiles = new GridProfile[RPNUMERICS.domainDim()];
//        for (int i = 0; i < RPNUMERICS.domainDim(); i++)
//            profiles[i] = new GridProfile(bounds.getMinimums().getElement(i), bounds.getMaximums().getElement(i), DEFAULT_NUMOFNODES);
//        MultiGrid grid = new MultiGrid(profiles,rpnumerics.RPNUMERICS.domainDim());
//        GridIterator iterator = grid.iterator();
//        while (iterator.hasNext()) {
//            // Orbits needs just one input
//            RealVector[] coords = new RealVector[1];
//            coords[0] = (RealVector)iterator.next();
//            UIController.instance().userInputComplete(coords[0]);
//            if (bounds.inside(coords[0])) {
//                // FORWARD
//                OrbitGeom forward = (OrbitGeom)ForwardOrbitPlotCommand.instance().createRpGeometry(coords);
//                rpn.parser.RPnDataModule.PHASESPACE.plot(forward);
//                // BACKWARD
//                OrbitGeom backward = (OrbitGeom)BackwardOrbitPlotCommand.instance().createRpGeometry(coords);
//                rpn.parser.RPnDataModule.PHASESPACE.plot(backward);
//                // good for multithread
//                try {
//                    int fPoints = ((Orbit)forward.geomFactory().geomSource()).getPoints().length;
//                    for (int i = 0; i < fPoints; i++)
//                        iterator.remove(((Orbit)forward.geomFactory().geomSource()).getPoints() [i].getCoords());
//                } catch (IllegalArgumentException ex) { }
//                try {
//                    int bPoints = ((Orbit)backward.geomFactory().geomSource()).getPoints().length;
//                    for (int i = 0; i < bPoints; i++)
//                        iterator.remove(((Orbit)backward.geomFactory().geomSource()).getPoints() [i].getCoords());
//                } catch (IllegalArgumentException ex) { }
//            }
//        }
//    }
    // -------------------


    // --- MÉTODO SOFRENDO ALTERAÇÃO
    // --- AQUI EM 06FEV
    public void actionPerformed(ActionEvent action) {

        setBothDir(true);
        
        String strNodes = JOptionPane.showInputDialog("Resolution:");
        int numOfNodes  = Integer.parseInt(strNodes);

        Boundary bounds = RPNUMERICS.boundary();
        GridProfile[] profiles = new GridProfile[RPNUMERICS.domainDim()];
        for (int i = 0; i < RPNUMERICS.domainDim(); i++)
            profiles[i] = new GridProfile(bounds.getMinimums().getElement(i), bounds.getMaximums().getElement(i), numOfNodes);
        MultiGrid grid = new MultiGrid(profiles,rpnumerics.RPNUMERICS.domainDim());
        GridIterator iterator = grid.iterator();
        while (iterator.hasNext()) {    // loop com (numOfNodes)^2 passagens, no máximo
            // Orbits needs just one input
            RealVector[] coords = new RealVector[1];
            coords[0] = (RealVector)iterator.next();
            UIController.instance().userInputComplete(coords[0]);
            if (bounds.inside(coords[0])) {

                OrbitGeom orbitGeom = (OrbitGeom)TransitionalLinePlotCommand.instance().createRpGeometry(coords);

                try {
                    int fPoints = ((Orbit) orbitGeom.geomFactory().geomSource()).getPoints().length;
                    for (int i = 0; i < fPoints; i++)
                        iterator.remove(((Orbit) orbitGeom.geomFactory().geomSource()).getPoints()[i].getCoords());
                } catch (IllegalArgumentException ex) { }

                rpn.parser.RPnDataModule.PHASESPACE.plot(orbitGeom);

            }
        }
        setBothDir(false);
    }
    // -------------------


    public boolean isBothDir() {
        return bothDir_;
    }

    public void setBothDir(boolean bothDir) {
        bothDir_ = bothDir;
    }
    

    public static FillPhaseSpaceCommand instance() {
        if (instance_ == null) {
            instance_ = new FillPhaseSpaceCommand();
        }
        return instance_;
    }
}
