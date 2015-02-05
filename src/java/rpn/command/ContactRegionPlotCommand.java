/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.command;

import java.awt.event.ActionEvent;
import javax.swing.JButton;
import rpn.component.*;
import rpn.controller.ui.UIController;
import rpn.controller.ui.UI_ACTION_SELECTED;
import rpn.parser.RPnDataModule;
import rpnumerics.*;
import wave.util.RealVector;

public class ContactRegionPlotCommand extends RpModelPlotCommand {
    //
    // Constants
    //

    static public final String DESC_TEXT = "Contact Region";
    //
    // Members
    //
    static private ContactRegionPlotCommand instance_ = null;

    //
    // Constructors/Initializers
    //
    protected ContactRegionPlotCommand() {
        super(DESC_TEXT, null, new JButton());
    }

    @Override
    public void actionPerformed(ActionEvent event) {

        UI_ACTION_SELECTED action = new UI_ACTION_SELECTED(this);
        action.userInputComplete(UIController.instance());// No input needed

    }

    public RpGeometry createRpGeometry(RealVector[] input) {

        int family = Integer.parseInt(RPNUMERICS.getConfiguration("contactregion").getParam("family"));
        PhysicalBoundaryFactory factory = new PhysicalBoundaryFactory(new ContactRegionCalc(family));
        return factory.geom();

    }

    @Override
    public void execute() {

//
//        PhysicalBoundaryFactory factory = new PhysicalBoundaryFactory(new PhysicalBoundaryCalc());

//        RPnDataModule.PHASESPACE.join(factory.geom());
        super.execute();
        RpGeometry geom = createRpGeometry(null);
        RPnDataModule.LEFTPHASESPACE.join(geom);
        RPnDataModule.RIGHTPHASESPACE.join(geom);

        

    }

    static public ContactRegionPlotCommand instance() {
        if (instance_ == null) {
            instance_ = new ContactRegionPlotCommand();
        }
        return instance_;
    }
}
