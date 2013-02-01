/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rpn.command;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.JToggleButton;
import rpn.RPnPhaseSpacePanel;
import rpn.component.RpGeometry;
import rpn.controller.ui.RPnVelocityPlotter;
import rpn.controller.ui.UIController;

import wave.util.RealVector;
import rpn.controller.ui.VELOCITYAGENT_CONFIG;

/**
 *
 * @author moreira
 */
public class VelocityCommand extends RpModelPlotCommand {

    static public final String DESC_TEXT = "Velocity";
    static private VelocityCommand instance_ = null;
    private JToggleButton button_;

    static public List vel = new ArrayList();
    //** Para a posição da velocidade em coordenadas físicas
    static public List xVel = new ArrayList();
    static public List yVel = new ArrayList();
    //** Para ponta da seta da velocidade na curva em coordenadas físicas
    static public List xSetaVel = new ArrayList();
    static public List ySetaVel = new ArrayList();


    private VelocityCommand() {
        super(DESC_TEXT, null ,new JToggleButton());

        button_ = new JToggleButton(this);
        button_.setToolTipText(DESC_TEXT);

        button_.setFont(rpn.RPnConfigReader.MODELPLOT_BUTTON_FONT);
        setEnabled(true);
    }

    @Override
    public void actionPerformed(ActionEvent event) {

        UIController.instance().setState(new VELOCITYAGENT_CONFIG());

        Iterator<RPnPhaseSpacePanel> iterator = UIController.instance().getInstalledPanelsIterator();

        while (iterator.hasNext()) {
            RPnPhaseSpacePanel panel = iterator.next();
            RPnVelocityPlotter arrowPlotter = new RPnVelocityPlotter();
            panel.addMouseListener(arrowPlotter);
            panel.addMouseMotionListener(arrowPlotter);
        }

    }

    public static VelocityCommand instance() {
        if (instance_ == null) {
            instance_ = new VelocityCommand();
        }
        return instance_;
    }

    @Override
    public JToggleButton getContainer() {
        return button_;
    }


    @Override
    public void execute() {

    }

    @Override
    public void unexecute() {
        throw new UnsupportedOperationException("Not supported yet.");
    }


    @Override
    public RpGeometry createRpGeometry(RealVector[] coords) {
        throw new UnsupportedOperationException("Not supported yet.");
    }




}
