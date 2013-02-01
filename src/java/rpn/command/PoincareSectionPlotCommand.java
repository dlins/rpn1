/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.command;

import javax.swing.JToggleButton;
import rpn.component.*;
import wave.util.SimplexPoincareSection;
import rpn.configuration.RPnConfig;
import wave.util.RealVector;


public class PoincareSectionPlotCommand extends RpModelPlotCommand {
    //
    // Constants
    //
    static public final String DESC_TEXT = "Poincare Section";

    //
    // Members
    //
    static private PoincareSectionPlotCommand instance_ = null;

    //
    // Constructors/Initializers
    //
    protected PoincareSectionPlotCommand() {
        super(DESC_TEXT, RPnConfig.POINCARE,new JToggleButton());
    }


    public RpGeometry createRpGeometry(RealVector[] input) {

        return new PoincareSectionGeomFactory(new SimplexPoincareSection(input)).geom();
    }

    static public PoincareSectionPlotCommand instance() {
        if (instance_ == null)
            instance_ = new PoincareSectionPlotCommand();
        return instance_;
    }
}
