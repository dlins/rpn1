/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.usecase;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JCheckBoxMenuItem;
import rpn.RPnCurvesList;
import rpn.RPnPhaseSpaceAbstraction;
import rpn.RPnPhaseSpaceFrame;
import rpn.RPnUIFrame;
import rpn.component.BifurcationCurveGeom;
import rpn.component.RpCalcBasedGeomFactory;
import rpn.component.RpGeometry;
import rpn.controller.ui.UIController;
import rpn.parser.RPnDataModule;
import wave.util.RealVector;

public class EnableLRPhaseSpaceAgent extends RpModelPlotAgent implements Observer {
    //
    // Constants
    //

    static public final String DESC_TEXT = "Enable/Disable L R Phase Space";
    //
    // Members
    //
    private static EnableLRPhaseSpaceAgent instance_ = null;
    private List<RpGeometry> selectedList_;

    //
    // Constructors
    //
    protected EnableLRPhaseSpaceAgent() {
//        super(DESC_TEXT);
        super(DESC_TEXT, null, new JCheckBoxMenuItem(DESC_TEXT, true));

        setEnabled(true);
        selectedList_ = new ArrayList();

    }

    public void actionPerformed(ActionEvent event) {
        boolean LRPhaseSpaceEnable = UIController.instance().isAuxPanelsEnabled();

        for (RPnPhaseSpaceFrame frame : RPnUIFrame.getAuxFrames()) {
            frame.setVisible(!LRPhaseSpaceEnable);
        }

        enableObserver(false, RPnDataModule.PHASESPACE);
        enableObserver(false, RPnDataModule.RIGHTPHASESPACE);
        enableObserver(false, RPnDataModule.LEFTPHASESPACE);


        if (!LRPhaseSpaceEnable) {
            moveGeometriesToLRPhaseSpace();
        } else {
            moveGeometriesToPhaseSpace();
        }

        UIController.instance().setAuxPanels(!LRPhaseSpaceEnable);

        RPnDataModule.updatePhaseSpaces();

        instance_.selectedList_.clear();


        enableObserver(true, RPnDataModule.PHASESPACE);
        enableObserver(true, RPnDataModule.RIGHTPHASESPACE);
        enableObserver(true, RPnDataModule.LEFTPHASESPACE);



    }

    @Override
    public RpGeometry createRpGeometry(RealVector[] coords) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void execute() {
    }

    public void unexecute() {
    }

    static public EnableLRPhaseSpaceAgent instance() {
        if (instance_ == null) {
            instance_ = new EnableLRPhaseSpaceAgent();
        }
        return instance_;
    }

    @Override
    public void update(Observable o, Object arg) {

        instance_.selectedList_.clear();

        fillSelectedGeometryList(RPnDataModule.PHASESPACE);
        fillSelectedGeometryList(RPnDataModule.LEFTPHASESPACE);
        fillSelectedGeometryList(RPnDataModule.RIGHTPHASESPACE);

    }

    private void enableObserver(boolean enabled, RPnPhaseSpaceAbstraction phaseSpaceAbstraction) {

        Iterator<RPnCurvesList> curvesListIterator = phaseSpaceAbstraction.curvesListIterator();

        while (curvesListIterator.hasNext()) {
            RPnCurvesList rPnCurvesList = curvesListIterator.next();
            if (enabled) {
                 rPnCurvesList.addObserver(this);
               
            } else {
                rPnCurvesList.deleteObserver(this);
            }
        }

    }

    private void fillSelectedGeometryList(RPnPhaseSpaceAbstraction phaseSpace) {

        Iterator<RpGeometry> iterator = phaseSpace.getGeomObjIterator();

        while (iterator.hasNext()) {
            RpGeometry rpGeometry = iterator.next();

            if (!(rpGeometry instanceof BifurcationCurveGeom) && rpGeometry.viewingAttr().isSelected()) {
                instance_.selectedList_.add(rpGeometry);
            }

        }
    }

    private void moveGeometriesToLRPhaseSpace() {


        for (int i = 0; i < selectedList_.size(); i++) {
            RpGeometry rpGeometry = selectedList_.get(i);

            RpCalcBasedGeomFactory factory = (RpCalcBasedGeomFactory) rpGeometry.geomFactory();

            if (!RPnDataModule.LEFTPHASESPACE.contains(rpGeometry)) {
                RPnDataModule.LEFTPHASESPACE.join(factory.createGeomFromSource());
            }
            if (!RPnDataModule.RIGHTPHASESPACE.contains(rpGeometry)) {
                RPnDataModule.RIGHTPHASESPACE.join(factory.createGeomFromSource());
            }

        }


    }

    private void moveGeometriesToPhaseSpace() {

        for (int i = 0; i < selectedList_.size(); i++) {
            RpGeometry rpGeometry = selectedList_.get(i);

            if (!RPnDataModule.PHASESPACE.contains(rpGeometry)) {
                RpCalcBasedGeomFactory factory = (RpCalcBasedGeomFactory) rpGeometry.geomFactory();
                RPnDataModule.PHASESPACE.join(factory.createGeomFromSource());
            }
            RPnDataModule.LEFTPHASESPACE.remove(rpGeometry);
            RPnDataModule.RIGHTPHASESPACE.remove(rpGeometry);
        }


    }
}
