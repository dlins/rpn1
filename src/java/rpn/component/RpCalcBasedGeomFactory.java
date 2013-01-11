/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import java.util.ArrayList;
import java.util.List;
import rpn.RPnDesktopPlotter;
import rpn.controller.RpCalcController;
import rpn.controller.RpController;

import rpnumerics.Area;
import rpnumerics.RPnCurve;

import rpnumerics.RpCalculation;
import rpnumerics.RpException;
import rpnumerics.RpSolution;
import wave.util.RealSegment;

public abstract class RpCalcBasedGeomFactory implements RpGeomFactory {
    //
    // Members
    //

    protected RpCalculation calc_;
    protected RpGeometry geom_;
    protected Object geomSource_;
    private RpController ui_;
    protected boolean isGeomOutOfDate_;

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
            installController();

        } catch (RpException rex) {

            RPnDesktopPlotter.showCalcExceptionDialog(rex);
        }


    }

    public RpCalcBasedGeomFactory(RpCalculation calc, RpSolution solution) {
        calc_ = calc;
        geomSource_ = solution;
        geom_ = createGeomFromSource();
        isGeomOutOfDate_ = false;
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
    public RpGeometry geom() {
        return geom_;
    }

    public Object geomSource() {
        return geomSource_;
    }

    public void setUI(RpController ui) {
        ui_ = ui;
    }

    public RpController getUI() {
        return ui_;
    }

    public RpCalculation rpCalc() {
        return calc_;
    }

    public boolean isGeomOutOfDate() {
        return isGeomOutOfDate_;
    }

    public void setGeomOutOfDate(boolean flag) {
        isGeomOutOfDate_ = flag;
    }

    //
    // Methods
    //
    public abstract RpGeometry createGeomFromSource();

    public void updateGeom() {
//        System.out.println("Estou no updateGeom() sem area ... ");
        try {
//            geomSource_=null;
//            geom_=null;
//            System.gc();
            geomSource_ = calc_.recalc();
            geom_ = createGeomFromSource();
            isGeomOutOfDate_ = true;
        } catch (RpException rex) {
            RPnDesktopPlotter.showCalcExceptionDialog(rex);
        }
    }

    public void updateGeom(List<Area> areaToRefine, List<Integer> segmentsToRemove) {

        List<RealSegment> segRem = new ArrayList<RealSegment>();

        RPnCurve curve = (RPnCurve) geomSource();

        for (Integer i : segmentsToRemove) {
            segRem.add(curve.segments().get(i));
        }

        curve.segments().removeAll(segRem);

        try {
            RPnCurve newCurve = (RPnCurve) calc_.recalc(areaToRefine);

            ((RPnCurve) geomSource_).segments().addAll(newCurve.segments());
        } catch (RpException ex) {
            ex.printStackTrace();
        }

        geom_ = createGeomFromSource();

        isGeomOutOfDate_ = true;
    }
}
