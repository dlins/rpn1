/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.usecase;

import wave.util.RealVector;
import wave.util.LoopConstruct;
import wave.util.LoopIterator;
import wave.util.Boundary;
import wave.util.GridProfile;
import wave.util.MultiGrid;
import wave.util.GridIterator;
import wave.multid.model.MultiGeometryImpl;
import rpnumerics.*;
import rpn.RPnPhaseSpaceAbstraction;
import rpn.component.*;
import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import java.util.Iterator;
import rpn.controller.ui.*;
import rpn.controller.PhaseSpacePanel2DController;

public class FillPhaseSpaceAgent extends AbstractAction {
    static public final String DESC_TEXT = "Fills up the Phase Space";
    static public final int DEFAULT_NUMOFNODES = 100;
    private static FillPhaseSpaceAgent instance_ = null;

    protected FillPhaseSpaceAgent() {
        super(DESC_TEXT, null);
    }

    public void actionPerformed(ActionEvent action) {
        Boundary bounds = RPNUMERICS.boundary();
        GridProfile[] profiles = new GridProfile[RPNUMERICS.domainDim()];
        for (int i = 0; i < RPNUMERICS.domainDim(); i++)
            profiles[i] = new GridProfile(bounds.getMinimums().getElement(i), bounds.getMaximums().getElement(i), DEFAULT_NUMOFNODES);
        MultiGrid grid = new MultiGrid(profiles,rpnumerics.RPNUMERICS.domainDim());
        GridIterator iterator = grid.iterator();
        while (iterator.hasNext()) {
            // Orbits needs just one input
            RealVector[] coords = new RealVector[1];
            coords[0] = (RealVector)iterator.next();
            UIController.instance().userInputComplete(coords[0]);
            if (bounds.inside(coords[0])) {
                // FORWARD
                OrbitGeom forward = (OrbitGeom)ForwardOrbitPlotAgent.instance().createRpGeometry(coords);
                rpn.parser.RPnDataModule.PHASESPACE.plot(forward);
                // BACKWARD
                OrbitGeom backward = (OrbitGeom)BackwardOrbitPlotAgent.instance().createRpGeometry(coords);
                rpn.parser.RPnDataModule.PHASESPACE.plot(backward);
                // good for multithread
                try {
                    int fPoints = ((Orbit)forward.geomFactory().geomSource()).getPoints().length;
                    for (int i = 0; i < fPoints; i++)
                        iterator.remove(((Orbit)forward.geomFactory().geomSource()).getPoints() [i].getCoords());
                } catch (IllegalArgumentException ex) { }
                try {
                    int bPoints = ((Orbit)backward.geomFactory().geomSource()).getPoints().length;
                    for (int i = 0; i < bPoints; i++)
                        iterator.remove(((Orbit)backward.geomFactory().geomSource()).getPoints() [i].getCoords());
                } catch (IllegalArgumentException ex) { }
            }
        }
    }

    public static FillPhaseSpaceAgent instance() {
        if (instance_ == null) {
            instance_ = new FillPhaseSpaceAgent();
        }
        return instance_;
    }
}
