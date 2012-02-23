/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import java.util.ArrayList;
import java.util.List;
import rpn.RPnDesktopPlotter;
import rpn.component.util.GeometryGraphND;
import rpn.controller.RpCalcController;
import rpn.controller.RpController;

import rpnumerics.Area;
import rpnumerics.RPnCurve;

import rpnumerics.RpCalculation;
import rpnumerics.RpException;
import rpnumerics.RpSolution;
import rpnumerics.SegmentedCurve;

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

//    public static String createMatlabColorTable() {
//
//        StringBuffer buffer = new StringBuffer();
//
//
//        int red16 = SubInflectionCurveGeom.COLOR.getRed();
//        int blue16 = SubInflectionCurveGeom.COLOR.getBlue();
//        int green16 = SubInflectionCurveGeom.COLOR.getGreen();
//
//        int red17 = CoincidenceCurveGeom.COLOR.getRed();
//        int blue17 = CoincidenceCurveGeom.COLOR.getBlue();
//        int green17 = CoincidenceCurveGeom.COLOR.getGreen();
//
//        int red20 = CoincidenceExtensionCurveGeom.COLOR.getRed();
//        int blue20 = CoincidenceExtensionCurveGeom.COLOR.getBlue();
//        int green20 = CoincidenceExtensionCurveGeom.COLOR.getGreen();
//
//        buffer.append("toc=[");
//
//        double toc[][] = {{128, 128, 128}, //*** tipo 0 ao tipo 15 = para segmentos de Hugoniot
//            {128, 128, 128}, //***
//            {255, 0, 0}, //***
//            {247, 151, 55}, //***
//            {128, 128, 128}, //***
//            {128, 128, 128}, //***
//            {128, 128, 128}, //***
//            {128, 128, 128}, //***
//            {255, 0, 255}, //***
//            {128, 128, 128}, //***
//            {18, 153, 1}, //***
//            {0, 0, 255}, //***
//            {128, 128, 128}, //***
//            {128, 128, 128}, //***
//            {0, 255, 255}, //***
//            {128, 128, 128}, //***
//            {red16, green16, blue16}, // tipo 16 = SubInflection, era  {243, 123, 46}
//            {red17, green17, blue17}, // tipo 17 = Coincidence, era  {20, 43, 140}
//            {0, 255, 0}, // tipo 18 = {uma parte de Double Contact, BuckleyLeverettin}
//            {248, 17, 47}, // tipo 19 = a outra parte de Double Contact
//            {red20, green20, blue20} // tipo 20 = {Coincidence Extension, SubInflection Extension, Boundary Extension}
//        };
//
//
//        for (int i = 0; i < toc.length; i++) {
//
//            for (int j = 0; j < 3; j++) {
//                buffer.append("  " + toc[i][j] / 255.0 + " ");
//            }
//            buffer.append(";\n");
//        }
//        buffer.append("];\n\n");
//
//        return buffer.toString();
//    }
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
        try {
            geomSource_ = calc_.recalc();
            geom_ = createGeomFromSource();
            isGeomOutOfDate_ = true;
        } catch (RpException rex) {
            RPnDesktopPlotter.showCalcExceptionDialog(rex);
        }
    }

    public void updateGeom(Area area) {
        try {
            if (area.isClosestCurve((RPnCurve) geomSource_)) {

                List segRem = new ArrayList();

                System.out.println("indContido : " + GeometryGraphND.indContido.size());

                for (int i = 0; i < GeometryGraphND.indContido.size(); i++) {
                    int ind = Integer.parseInt((GeometryGraphND.indContido.get(i)).toString());
                    segRem.add(((SegmentedCurve) geomSource_).segments().get(ind));
                }

                ((SegmentedCurve) geomSource_).segments().removeAll(segRem);
                GeometryGraphND.indContido.clear();

                for (int i = 0; i < GeometryGraphND.targetPoint.getSize(); i++) {        // Pode ser útil na hora de fazer inclusao dos novos segmentos (para nao serem eliminados)
                    GeometryGraphND.cornerRet.setElement(i, 0);
                    GeometryGraphND.targetPoint.setElement(i, 0.);
                }

                SegmentedCurve newCurve = (SegmentedCurve) calc_.recalc(area);

                ((SegmentedCurve) geomSource_).segments().addAll(newCurve.segments());

                geom_ = createGeomFromSource();
                isGeomOutOfDate_ = true;
            }

        } catch (RpException rex) {
            RPnDesktopPlotter.showCalcExceptionDialog(rex);
        }
    }
}
