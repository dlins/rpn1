/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.controller.phasespace;

import java.util.ArrayList;
import java.util.List;
import rpn.component.RpGeometry;
import rpn.RPnPhaseSpaceAbstraction;
import rpn.component.XZeroGeom;
import rpn.component.HugoniotCurveGeom;
import rpn.component.PoincareSectionGeom;
import rpn.component.StationaryPointGeom;
import rpn.command.*;

public class NumConfigReadyImpl extends NumConfigImpl
        implements NUMCONFIG_READY {

    //
    // Members
    //
    private XZeroGeom xzeroGeom_;
    private boolean plotManifold_ = false;
    List<StationaryPointGeom> stationaryGeom;

    //
    // Constructors
    //
    public NumConfigReadyImpl(HugoniotCurveGeom hugoniotGeom, XZeroGeom geom, boolean plotManifold) {
        super(hugoniotGeom);
        //System.out.println("//*** Construtor de NumConfigReadyImpl ***********");
        xzeroGeom_ = geom;
        stationaryGeom = new ArrayList<StationaryPointGeom>();
        plotManifold_ = plotManifold;

        // ENABLED
        TransitionalLinePlotCommand.instance().setEnabled(true);
        PoincareSectionPlotCommand.instance().setEnabled(true);
        StationaryPointPlotCommand.instance().setEnabled(true);
        ChangeDirectionCommand.instance().setEnabled(true);
        ChangeSigmaCommand.instance().setEnabled(true);
        ChangeFluxParamsCommand.instance().setEnabled(true);
        ClearPhaseSpaceCommand.instance().setEnabled(true);
        FillPhaseSpaceCommand.instance().setEnabled(true);
        InvariantPlotCommand.instance().setEnabled(true);
        ChangeXZeroCommand.instance().setEnabled(true);
        // DISABLED
        BackwardManifoldPlotCommand.instance().setEnabled(false);
        ConnectionManifoldPlotCommand.instance().setEnabled(false);
        FindProfileCommand.instance().setEnabled(false);


    }

    //
    // Accessors/Mutators
    //
    //
    // Methods
    //
    public XZeroGeom xzeroGeom() {
        return xzeroGeom_;
    }

    public List<StationaryPointGeom> getStationaryPointGeomList() {
        return stationaryGeom;
    }

    public boolean isPlotManifold() {
        return plotManifold_;
    }

    public void setPlotManifold(boolean plotManifold) {

        plotManifold_ = plotManifold;
    }

    @Override
    public void plot(RPnPhaseSpaceAbstraction phaseSpace, RpGeometry geom) {

        super.plot(phaseSpace, geom);


        if (geom instanceof PoincareSectionGeom) {
            System.out.println("Testando se geom instanceof PoincareSectionGeom ... ");
            phaseSpace.changeState(new PoincareReadyImpl(hugoniotGeom(), xzeroGeom(), (PoincareSectionGeom) geom, false));
        }
        //phaseSpace.changeState(new PoincareReadyImpl(hugoniotGeom(), xzeroGeom(), (PoincareSectionGeom)geom));

        //System.out.println("Saiu do plot de NumConfigReadyImpl");
    }
}


