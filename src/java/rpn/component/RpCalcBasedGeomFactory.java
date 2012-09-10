/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import java.util.ArrayList;
import java.util.List;
import rpn.RPnDesktopPlotter;
import rpn.RPnPhaseSpaceAbstraction;
import rpn.component.util.GeometryGraphND;
import rpn.controller.RpCalcController;
import rpn.controller.RpController;
import rpn.controller.ui.UIController;
import rpn.controller.ui.UserInputTable;
import rpn.parser.RPnDataModule;

import rpnumerics.Area;
import rpnumerics.HugoniotCurve;
import rpnumerics.RPnCurve;

import rpnumerics.RpCalculation;
import rpnumerics.RpException;
import rpnumerics.RpSolution;
import rpnumerics.SegmentedCurve;
import wave.util.RealVector;

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
    protected abstract RpGeometry createGeomFromSource();

    public void updateGeom() {
        System.out.println("Estou no updateGeom() sem area ... ");
        try {
            geomSource_ = calc_.recalc();
            geom_ = createGeomFromSource();
            isGeomOutOfDate_ = true;
        } catch (RpException rex) {
            RPnDesktopPlotter.showCalcExceptionDialog(rex);
        }
    }

    public void updateGeom(Area area) {

        System.out.println("Area dentro do updateGeom(Area) :::::::::: " +area.toString());

        //try {
            if (area.isClosestCurve((RPnCurve) geomSource_)) {

                System.out.println("Entrou no if do updateGeom(Area) ... ");

                List segRem = new ArrayList();

                System.out.println("GeometryGraphND.indContido.size() : " +GeometryGraphND.indContido.size());

//                System.out.println("tamanho antes: "+  ((SegmentedCurve) geomSource_).segments().size());
                for (int i = 0; i < GeometryGraphND.indContido.size(); i++) {
                    int ind = Integer.parseInt((GeometryGraphND.indContido.get(i)).toString());
                    segRem.add(((SegmentedCurve) geomSource_).segments().get(ind));
                }

                ((SegmentedCurve) geomSource_).segments().removeAll(segRem);
                GeometryGraphND.indContido.clear();
                
//                // Pode ser útil na hora de fazer inclusao dos novos segmentos (para nao serem eliminados)
//                UserInputTable userInputList = UIController.instance().globalInputTable();
//                RealVector newValue = userInputList.values();
//
//                for (int i = 0; i < newValue.getSize(); i++) {
//                    GeometryGraphND.cornerRet.setElement(i, 0);
//                    newValue.setElement(i, 0.);
//                }
//                //----------------------------------------------------------------------------------------
//
//                SegmentedCurve newCurve = (SegmentedCurve) calc_.recalc(area);
//
//                ((SegmentedCurve) geomSource_).segments().addAll(newCurve.segments());


                geom_ = createGeomFromSource();
                isGeomOutOfDate_ = true;


            }

        //} catch (RpException rex) {
        //    RPnDesktopPlotter.showCalcExceptionDialog(rex);
        //}
    }
}
