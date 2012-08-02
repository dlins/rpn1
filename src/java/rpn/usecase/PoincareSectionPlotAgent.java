/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.usecase;

import javax.swing.JToggleButton;
import rpn.component.*;
import wave.util.SimplexPoincareSection;
import rpn.RPnConfig;
import wave.util.RealVector;


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
        super(DESC_TEXT, RPnConfig.POINCARE,new JToggleButton());
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
