/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rpn.component.util;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JToggleButton;
import rpn.controller.ui.UIController;
import rpn.usecase.RpModelActionAgent;

/**
 *
 * @author moreira
 */
public class VelocityAgent extends RpModelActionAgent {

    static public final String DESC_TEXT = "Velocity";
    static private VelocityAgent instance_ = null;
    private JToggleButton button_;

    static public List vel = new ArrayList();
    //** Para a posição da velocidade em coordenadas físicas
    static public List xVel = new ArrayList();
    static public List yVel = new ArrayList();
    //** Para a posição da velocidade em coordenadas do dispositivo
    static public List xDevVel = new ArrayList();
    static public List yDevVel = new ArrayList();
    //** Para ponta da seta da velocidade na curva em coordenadas físicas
    static public List xSetaVel = new ArrayList();
    static public List ySetaVel = new ArrayList();
    //** Para ponta da seta da velocidade na curva em coordenadas físicas
    static public List xDevSetaVel = new ArrayList();
    static public List yDevSetaVel = new ArrayList();

    //** Para indicar as curvas com velocidades
    static public List indCurvaVel = new ArrayList();
    static public List paraRemoverGeomVel = new ArrayList();
    static public List paraRemoverIndVel = new ArrayList();
    static public List paraOcultarIndVel = new ArrayList();
    static public List velView = new ArrayList();


    private VelocityAgent() {
        super(DESC_TEXT, null);

        button_ = new JToggleButton(this);
        button_.setToolTipText(DESC_TEXT);

        button_.setFont(rpn.RPnConfigReader.MODELPLOT_BUTTON_FONT);
        setEnabled(true);
    }

    @Override
    public void actionPerformed(ActionEvent event) {

       UIController.instance().setState(new VELOCITYAGENT_CONFIG());

    }

    public static VelocityAgent instance() {
        if (instance_ == null) {
            instance_ = new VelocityAgent();
        }
        return instance_;
    }

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


    public static void clearVelocities() {
        ControlClick.clearpMarca();
        indCurvaVel.clear();
        vel.clear();
        xDevSetaVel.clear();
        xDevVel.clear();
        xSetaVel.clear();
        xVel.clear();
        yDevSetaVel.clear();
        yDevVel.clear();
        ySetaVel.clear();
        yVel.clear();
    }


    public static void hideVelocities(List paraOcultar) {
        for (int i = 0; i < paraOcultar.size(); i++) {
            int index = (Integer)paraOcultar.get(i);
            velView.set(index, -1);
        }
    }

    public static void viewVelocities(List paraOcultar) {
        for (int i = 0; i < paraOcultar.size(); i++) {
            int index = (Integer)paraOcultar.get(i);
            velView.set(index, 1);
        }
    }

    public static void clearVelocities(List paraRemover) {
        for (int i = 0; i < paraRemover.size(); i++) {
            int index = (Integer)paraRemover.get(i);
            xDevSetaVel.remove(index);
            xDevVel.remove(index);
            xSetaVel.remove(index);
            xVel.remove(index);
            yDevSetaVel.remove(index);
            yDevVel.remove(index);
            ySetaVel.remove(index);
            yVel.remove(index);

            xDevSetaVel.add(index, 0.);
            xDevVel.add(index, 0.);
            xSetaVel.add(index, 0.);
            xVel.add(index, 0.);
            yDevSetaVel.add(index, 0.);
            yDevVel.add(index, 0.);
            ySetaVel.add(index, 0.);
            yVel.add(index, 0.);
        }

    }

    


}
