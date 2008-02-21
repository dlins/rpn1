
/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn;

import wave.multid.Coords2D;
import wave.multid.CoordsArray;
import wave.multid.view.ViewingTransform;
import wave.multid.map.ProjectionMap;
import javax.swing.JLabel;
import java.text.NumberFormat;
import java.awt.event.*;
import java.awt.*;

public class RPnCursorMonitor extends JLabel {
    //
    // Members
    //
    private MouseMotionController mouseMotionController_;
    private NumberFormat formatter_;

    //
    // Constructor
    //
    public RPnCursorMonitor() {
        setText("(0,0)");
        this.setForeground(Color.lightGray);
        mouseMotionController_ = new MouseMotionController();
        formatter_ = NumberFormat.getInstance();
        formatter_.setMaximumFractionDigits(2);
    }

    class MouseMotionController extends MouseMotionAdapter {
        //
        // MouseMotion Adapter response
        //
        public void mouseMoved(MouseEvent e) {
            RPnPhaseSpacePanel scenePane = (RPnPhaseSpacePanel)e.getComponent();
            updateCursorPos(scenePane, e.getPoint());
        }
    }


    //
    // Accessors/Mutators
    //
    public MouseMotionAdapter getMouseMotionController() { return mouseMotionController_; }

    //
    // Methods
    //
    public void updateCursorPos(RPnPhaseSpacePanel pane, Point pos) {
        ViewingTransform transf = pane.scene().getViewingTransform();
        Coords2D dcMousePos = new Coords2D(pos.getX(), pos.getY());
        CoordsArray wcMousePos = new CoordsArray(transf.projectionMap().getDomain());
        if (pane.getCastedUI().isAbsComplete())
            dcMousePos.setElement(0, pane.getCastedUI().get_dc_CompletePoint().x);
        if (pane.getCastedUI().isOrdComplete())
            dcMousePos.setElement(1, pane.getCastedUI().get_dc_CompletePoint().y);
        transf.dcInverseTransform(dcMousePos, wcMousePos);
        // we are working with PLANE
        setText(createCoordsString(wcMousePos, transf.projectionMap()));
    }

    protected String createCoordsString(CoordsArray wcCoords, ProjectionMap projMap) {
        StringBuffer formattedCoord = new StringBuffer();
        formattedCoord.append("(");
        for (int i = 0; i < projMap.getCodomain().getDim(); i++)
            formattedCoord.append(" " + formatter_.format(wcCoords.getElement(projMap.getCompIndexes() [i])));
        formattedCoord.append(" )");
        return formattedCoord.toString();
    }
}
