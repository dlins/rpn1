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
import rpnumerics.RPNUMERICS;

/**
 *
 * @author moreira
 */
public class ClassifierAgent extends RpModelActionAgent {

    static public final String DESC_TEXT = "Classify";
    static private ClassifierAgent instance_ = null;
    private JToggleButton button_;
    
    //** para classificar os segmentos (Leandro)
    static public List tipo = new ArrayList();
    //** Para a posição da string em coordenadas físicas
    static public List xStr = new ArrayList();
    static public List yStr = new ArrayList();
    //** Para a posição da string em coordenadas do dispositivo
    static public List xDevStr = new ArrayList();
    static public List yDevStr = new ArrayList();
    //** Para ponta da seta da string na curva em coordenadas físicas
    static public List xSeta = new ArrayList();
    static public List ySeta = new ArrayList();
    //** Para ponta da seta da string em coordenadas do dispositivo
    static public List xDevSeta = new ArrayList();
    static public List yDevSeta = new ArrayList();

    //** Para indicar as curvas com classificadores
    static public List indCurvaCla = new ArrayList();       // vai ser comum para remocao e ocultacao
    static public List paraRemoverGeomCla = new ArrayList();
    static public List paraRemoverIndCla = new ArrayList(); // este é usado efetivamente para remocao
    static public List paraOcultarIndCla = new ArrayList(); // este é usado efetivamente para ocultacao
    static public List strView = new ArrayList();           // este é usado efetivamente para ocultacao


    private ClassifierAgent() {
        super(DESC_TEXT, null);

        button_ = new JToggleButton(this);
        //button_.setToolTipText(DESC_TEXT);

        button_.setToolTipText("<html>After selecting this option, you<br>"              //*** Leandro
                                   + "should take two clicks on the panel:<br>"
                                   + "the first click determine the point<br>"
                                   + "of interest in a curve; the second<br>"
                                   + "click determine the location of the label.</html>");

        button_.setFont(rpn.RPnConfigReader.MODELPLOT_BUTTON_FONT);
        setEnabled(true);
    }

    @Override
    public void actionPerformed(ActionEvent event) {

       UIController.instance().setState(new CLASSIFIERAGENT_CONFIG());
       System.out.println("Estou no actionPerformed do ClassifierAgent");

    }

    public static ClassifierAgent instance() {
        if (instance_ == null) {
            instance_ = new ClassifierAgent();
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


    public static void clearClassifiers() {
        ControlClick.clearpMarca();
        indCurvaCla.clear();
        tipo.clear();
        xDevSeta.clear();
        xDevStr.clear();
        xSeta.clear();
        xStr.clear();
        yDevSeta.clear();
        yDevStr.clear();
        ySeta.clear();
        yStr.clear();
    }

    public static void clearClassifiers(List paraRemover) {
        for (int i = 0; i < paraRemover.size(); i++) {
            int index = (Integer)paraRemover.get(i);
            xDevSeta.remove(index);
            xDevStr.remove(index);
            xSeta.remove(index);
            xStr.remove(index);
            yDevSeta.remove(index);
            yDevStr.remove(index);
            ySeta.remove(index);
            yStr.remove(index);
            
            xDevSeta.add(index, 0.);
            xDevStr.add(index, 0.);
            xSeta.add(index, 0.);
            xStr.add(index, 0.);
            yDevSeta.add(index, 0.);
            yDevStr.add(index, 0.);
            ySeta.add(index, 0.);
            yStr.add(index, 0.);
        }
    }

    public static void hideClassifiers(List paraOcultar) {
        for (int i = 0; i < paraOcultar.size(); i++) {
            int index = (Integer)paraOcultar.get(i);
            strView.set(index, -1);
        }
    }

    public static void viewClassifiers(List paraOcultar) {
        for (int i = 0; i < paraOcultar.size(); i++) {
            int index = (Integer)paraOcultar.get(i);
            strView.set(index, 1);
        }
    }

    

}
