/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.component;

import rpn.RPnDesktopPlotter;
import rpn.controller.RpCalcController;
import rpn.controller.RpController;
import rpnumerics.RpCalculation;
import rpnumerics.RpException;

public abstract class RpCalcBasedGeomFactory implements RpGeomFactory {
    //
    // Members
    //
    private RpCalculation calc_;
    private RpGeometry geom_;
    private Object geomSource_;
    private RpController ui_;
    private boolean isGeomOutOfDate_;

    //
    // Constructors/Initializers
    //
    public RpCalcBasedGeomFactory(RpCalculation calc) {
        calc_ = calc;
        // first calculation is different for some cases...
        try {
            geomSource_ = calc_.calc();
            geom_ = createGeomFromSource();
            isGeomOutOfDate_ = false;
        } catch (RpException rex) {
            RPnDesktopPlotter.showCalcExceptionDialog(rex);
        }
        installController();
    }

    protected RpController createUI() {
        return new RpCalcController();
    }

    protected void installController() {
        setUI(createUI());
        getUI().install(this);
    }

    //
    // Accessors/Mutators
    //
    public RpGeometry geom() { return geom_; }

    public Object geomSource() { return geomSource_; }

    public void setUI(RpController ui) { ui_ = ui; }

    public RpController getUI() { return ui_; }

    public RpCalculation rpCalc() { return calc_; }

    public boolean isGeomOutOfDate() { return isGeomOutOfDate_; }

    public void setGeomOutOfDate(boolean flag) { isGeomOutOfDate_ = flag; }

    //
    // Methods
    //
    protected abstract RpGeometry createGeomFromSource();

    public void updateGeom() {
        try {
            geomSource_ = calc_.recalc();
            geom_ = createGeomFromSource();
            isGeomOutOfDate_ = true;
        } catch (RpException rex) {
            RPnDesktopPlotter.showCalcExceptionDialog(rex);
        }
    }
}
