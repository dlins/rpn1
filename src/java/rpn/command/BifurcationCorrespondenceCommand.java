 /*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.command;

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
import rpn.controller.ui.RPnCorrespondencePlotter;


public class BifurcationCorrespondenceCommand extends RpModelPlotCommand {

    static public final String DESC_TEXT = "Bifurcation Correspondence";
    static private BifurcationCorrespondenceCommand instance_ = null;


    //** para classificar os segmentos (Leandro)
    static public List tipo = new ArrayList();
    //** Para a posição da string em coordenadas físicas
    static public List xStr = new ArrayList();
    static public List yStr = new ArrayList();
    //** Para ponta da seta da string na curva em coordenadas físicas
    static public List xSeta = new ArrayList();
    static public List ySeta = new ArrayList();


    private BifurcationCorrespondenceCommand() {
        super(DESC_TEXT, null ,new JToggleButton());

       
        setEnabled(true);
    }

    @Override
    public void actionPerformed(ActionEvent event) {

        UIController.instance().setState(new CLASSIFIERAGENT_CONFIG());

        Iterator<RPnPhaseSpacePanel> iterator = UIController.instance().getInstalledPanelsIterator();

        while (iterator.hasNext()) {
            RPnPhaseSpacePanel panel = iterator.next();
            RPnCorrespondencePlotter stringPlotter = new RPnCorrespondencePlotter();
            panel.addMouseListener(stringPlotter);
            panel.addMouseMotionListener(stringPlotter);
        }

    }

    public static BifurcationCorrespondenceCommand instance() {
        if (instance_ == null) {
            instance_ = new BifurcationCorrespondenceCommand();
        }
        return instance_;
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
