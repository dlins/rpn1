/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.command;

import java.awt.event.ActionEvent;
import javax.swing.JToggleButton;
import rpn.RPnStateInformationFrame;
import rpn.controller.ui.TRACKPOINT_CONFIG;
import rpn.controller.ui.UIController;
import rpnumerics.RPnStateInfo;
import rpnumerics.StateInformation;
import wave.multid.CoordsArray;
import wave.util.RealVector;

public class TrackPointCommand extends RpModelActionCommand {

    static public final String DESC_TEXT = "Track Point";
    static private TrackPointCommand instance_ = null;
    private JToggleButton button_;
    private RPnStateInfo info_;
    private RPnStateInformationFrame testFrame_;

    private TrackPointCommand() {
        super(DESC_TEXT, null);
        
        button_ = new JToggleButton(this);
        button_.setToolTipText(DESC_TEXT);
        button_.setFont(rpn.RPnConfigReader.MODELPLOT_BUTTON_FONT);
        setEnabled(true);
        testFrame_ = new RPnStateInformationFrame();


    }

    public static TrackPointCommand instance() {
        if (instance_ == null) {
            instance_ = new TrackPointCommand();
        }
        return instance_;
    }

    public JToggleButton getContainer() {
        return button_;
    }

    @Override
    public void unexecute() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void execute() {

        StateInformation stateInformation = new StateInformation();

        info_ = stateInformation.getStateInformation(UIController.instance().globalInputTable().values());
        testFrame_.update();

        System.out.println("Chamando execute do track agent");

    }

    @Override
    public void actionPerformed(ActionEvent event) {

        if (button_.isSelected()) {
            testFrame_.setVisible(true);
            TRACKPOINT_CONFIG trackPointConfig = new TRACKPOINT_CONFIG();
            UIController.instance().setState(trackPointConfig);

        } else {
            testFrame_.setVisible(false);

        }

    }

    //TODO Expandir para n dimensoes . No caso de n > 2 o ponto passado para getStateInformation será
    //composto pelas coordenadas que existem na tabela de entradas + wcCoords
    public void trackPoint(CoordsArray wcCoords) {


        if (button_.isSelected()) {
//            if (UIController.instance().globalInputTable().values().getSize() == rpnumerics.RPNUMERICS.domainDim()) {
                
                if (UIController.instance().getActivePhaseSpace().getSpace().getDim() == rpnumerics.RPNUMERICS.domainDim()) {
                StateInformation stateInformation = new StateInformation();

                RealVector realVectorWC = new RealVector(wcCoords.getCoords());
                info_ = stateInformation.getStateInformation(realVectorWC);
                testFrame_.update();

            }


        }
    }

    public RPnStateInfo getInfo() {
        return info_;
    }


}


