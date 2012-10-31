/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rpn.usecase;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.JToggleButton;
import rpn.RPnPhaseSpacePanel;
import rpn.component.RpGeometry;
import rpn.controller.ui.UIController;
import wave.util.RealVector;
import rpn.controller.ui.CLASSIFIERAGENT_CONFIG;
import rpn.controller.ui.RPnStringPlotter;

/**
 *
 * @author moreira
 */
public class ClassifierAgent extends RpModelPlotAgent {

    static public final String DESC_TEXT = "Classify";
    static private ClassifierAgent instance_ = null;
    private JToggleButton button_;

    //** para classificar os segmentos (Leandro)
    static public List tipo = new ArrayList();
    //** Para a posição da string em coordenadas físicas
    static public List xStr = new ArrayList();
    static public List yStr = new ArrayList();
    //** Para ponta da seta da string na curva em coordenadas físicas
    static public List xSeta = new ArrayList();
    static public List ySeta = new ArrayList();


    private ClassifierAgent() {
        super(DESC_TEXT, null ,new JToggleButton());

        button_ = new JToggleButton(this);
        button_.setToolTipText(DESC_TEXT);
        button_.setFont(rpn.RPnConfigReader.MODELPLOT_BUTTON_FONT);
        setEnabled(true);
    }

    @Override
    public void actionPerformed(ActionEvent event) {

        UIController.instance().setState(new CLASSIFIERAGENT_CONFIG());

        Iterator<RPnPhaseSpacePanel> iterator = UIController.instance().getInstalledPanelsIterator();

        while (iterator.hasNext()) {
            RPnPhaseSpacePanel panel = iterator.next();
            RPnStringPlotter stringPlotter = new RPnStringPlotter();
            panel.addMouseListener(stringPlotter);
            panel.addMouseMotionListener(stringPlotter);
        }

    }

    public static ClassifierAgent instance() {
        if (instance_ == null) {
            instance_ = new ClassifierAgent();
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
