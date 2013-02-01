/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.command;

import java.util.Collection;
import wave.util.RealVector;
import wave.util.Boundary;
import wave.util.GridProfile;
import wave.util.MultiGrid;
import wave.util.GridIterator;
import rpnumerics.*;
import rpn.component.*;
import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import rpn.controller.ui.*;
import wave.multid.CoordsArray;
import wave.util.RealSegment;

public class FillPhaseSpaceCommand extends AbstractAction {
    static public final String DESC_TEXT = "Fills up the Phase Space";
    static public final int DEFAULT_NUMOFNODES = 10;
    private static FillPhaseSpaceCommand instance_ = null;

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
    public void actionPerformed(ActionEvent action) {

        // ---
        String strNodes = JOptionPane.showInputDialog("Resolution:");
        int numOfNodes  = Integer.parseInt(strNodes);
        // ---

        Boundary bounds = RPNUMERICS.boundary();
        GridProfile[] profiles = new GridProfile[RPNUMERICS.domainDim()];
        for (int i = 0; i < RPNUMERICS.domainDim(); i++)
            profiles[i] = new GridProfile(bounds.getMinimums().getElement(i), bounds.getMaximums().getElement(i), numOfNodes);
        MultiGrid grid = new MultiGrid(profiles,rpnumerics.RPNUMERICS.domainDim());
        GridIterator iterator = grid.iterator();
        while (iterator.hasNext()) {
            // Orbits needs just one input
            RealVector[] coords = new RealVector[1];
            coords[0] = (RealVector)iterator.next();
            UIController.instance().userInputComplete(coords[0]);
            if (bounds.inside(coords[0])) {
                // FORWARD
                OrbitGeom forward = (OrbitGeom)ForwardOrbitPlotCommand.instance().createRpGeometry(coords);


                // ---- tentar remover parte da órbita

                int nPoints = ((Orbit)forward.geomFactory().geomSource()).getPoints().length;
                System.out.println("Tamanho da órbita : " +nPoints);
                int count = nPoints/2;
                OrbitPoint[] toRemove = new OrbitPoint[count];
                for (int i=0; i<nPoints; i++) {
                    if (i<count) {
                        toRemove[i] = ((Orbit)forward.geomFactory().geomSource()).getPoints()[i];
                    }
                }

                RpGeomFactory factory = forward.geomFactory();
                RPnCurve curve = (RPnCurve) factory.geomSource();
                //System.out.println("Tamanho de curve antes de remover: " +curve.segments().size());
                CoordsArray[] orbitArray = MultidAdapter.converseOrbitToCoordsArray((Orbit) curve);
                List<RealSegment> orbitSegments = MultidAdapter.converseCoordsArrayToRealSegments(orbitArray);
                System.out.println("Tamanho de curve antes de remover: " +orbitSegments.size());

                CoordsArray[] coordsArray = MultidAdapter.converseOrbitPointsToCoordsArray(toRemove);
                List<RealSegment> realSegments = MultidAdapter.converseCoordsArrayToRealSegments(coordsArray);
                System.out.println("Tamanho de realSegments : " +realSegments.size());
                //curve.segments().removeAll(realSegments);
                //System.out.println("Tamanho de curve depois de remover: " +curve.segments().size());

                orbitSegments.removeAll(realSegments);
                System.out.println("Tamanho de curve depois de remover: " +orbitSegments.size());


                //rpn.parser.RPnDataModule.PHASESPACE.plot((OrbitGeom)curve);
                //-------------------------------------


                //rpn.parser.RPnDataModule.PHASESPACE.plot(forward);
                // BACKWARD
                //OrbitGeom backward = (OrbitGeom)BackwardOrbitPlotCommand.instance().createRpGeometry(coords);
                //rpn.parser.RPnDataModule.PHASESPACE.plot(backward);
                // good for multithread
                try {
                    int fPoints = ((Orbit)forward.geomFactory().geomSource()).getPoints().length;
                    for (int i = 0; i < fPoints; i++)
                        iterator.remove(((Orbit)forward.geomFactory().geomSource()).getPoints() [i].getCoords());
                } catch (IllegalArgumentException ex) { }
//                try {
//                    int bPoints = ((Orbit)backward.geomFactory().geomSource()).getPoints().length;
//                    for (int i = 0; i < bPoints; i++)
//                        iterator.remove(((Orbit)backward.geomFactory().geomSource()).getPoints() [i].getCoords());
//                } catch (IllegalArgumentException ex) { }
            }
        }
    }
    // -------------------


    // --- APENAS PARA TESTAR FILL_PHASE_SPACE COM INTEGRAIS
//    public void actionPerformed(ActionEvent action) {
//
//        // ---
//        String strNodes = JOptionPane.showInputDialog("Resolution:");
//        int numOfNodes  = Integer.parseInt(strNodes);
//        // ---
//
//        Boundary bounds = RPNUMERICS.boundary();
//        GridProfile[] profiles = new GridProfile[RPNUMERICS.domainDim()];
//        for (int i = 0; i < RPNUMERICS.domainDim(); i++)
//            //profiles[i] = new GridProfile(bounds.getMinimums().getElement(i), bounds.getMaximums().getElement(i), DEFAULT_NUMOFNODES);
//            profiles[i] = new GridProfile(bounds.getMinimums().getElement(i), bounds.getMaximums().getElement(i), numOfNodes);
//        MultiGrid grid = new MultiGrid(profiles,rpnumerics.RPNUMERICS.domainDim());
//        GridIterator iterator = grid.iterator();
//        while (iterator.hasNext()) {
//            // Orbits needs just one input
//            RealVector[] coords = new RealVector[1];
//            coords[0] = (RealVector)iterator.next();
//            UIController.instance().userInputComplete(coords[0]);
//            //if (bounds.inside(coords[0])  &&  coords[0].getElement(0)!=coords[0].getElement(1)) {
//            if (bounds.inside(coords[0])) {
//                OrbitGeom fwdIntegral = (OrbitGeom)IntegralCurvePlotCommand.instance().createRpGeometry(coords);
//                rpn.parser.RPnDataModule.PHASESPACE.plot(fwdIntegral);
//            }
//        }
//    }




    public static FillPhaseSpaceCommand instance() {
        if (instance_ == null) {
            instance_ = new FillPhaseSpaceCommand();
        }
        return instance_;
    }
}
