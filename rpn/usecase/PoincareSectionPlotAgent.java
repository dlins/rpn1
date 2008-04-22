/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.usecase;

import rpn.RPnPhaseSpaceAbstraction;
import rpn.component.*;
import rpnumerics.OrbitCalc;
import rpnumerics.OrbitPoint;
import wave.util.SimplexPoincareSection;
import wave.util.RealVector;
import javax.swing.ImageIcon;
import wave.util.RealVector;
import rpn.controller.PhaseSpacePanel2DController;
import rpn.RPnConfigReader;

public class PoincareSectionPlotAgent extends RpModelPlotAgent {
    //
    // Constants
    //
    static public final String DESC_TEXT = "Poincare Section";

    //
    // Members
    //
    static private PoincareSectionPlotAgent instance_ = null;

    //
    // Constructors/Initializers
    //
    protected PoincareSectionPlotAgent() {
        super(DESC_TEXT, RPnConfigReader.POINCARE);
    }

    public RpGeometry createRpGeometry(RealVector[] input) {
        return new PoincareSectionGeomFactory(new SimplexPoincareSection(input)).geom();
    }

    static public PoincareSectionPlotAgent instance() {
        if (instance_ == null)
            instance_ = new PoincareSectionPlotAgent();
        return instance_;
    }
}
