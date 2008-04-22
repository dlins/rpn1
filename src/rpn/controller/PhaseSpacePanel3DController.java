/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.controller;

import rpn.RPnPhaseSpacePanel;
import rpn.RPnUserDepthCoordInputDialog;
import java.awt.Point;

public class PhaseSpacePanel3DController extends PhaseSpacePanel2DController {
    //
    // Constants
    //
    //
    // Members
    //
    private int depthIndex_;
    private boolean depthComplete_;

    //
    // Constructors/Initializers
    //
    public PhaseSpacePanel3DController(int absIndex, int ordIndex, int depthIndex) {
        super(absIndex, ordIndex);
        depthIndex_ = depthIndex;
        depthComplete_ = false;
    }

    //
    // Accessors/Mutators
    //
    public int getDepthIndex() { return depthIndex_; }

    //
    // Methods
    //
    public void resetCursorCoords() {
        super.resetCursorCoords();
        depthComplete_ = false;
    }

    static public double userDepthInput() {
        RPnUserDepthCoordInputDialog dialog = new RPnUserDepthCoordInputDialog(null, true);
        dialog.setVisible(true);
        return dialog.coord();
    }
}
