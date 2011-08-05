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
            installController();

        } catch (RpException rex) {

            RPnDesktopPlotter.showCalcExceptionDialog(rex);
        }


    }

    protected RpController createUI() {
        return new RpCalcController();
    }

    protected void installController() {
        setUI(createUI());
        getUI().install(this);
    }

    public static String createMatlabColorTable() {

        StringBuffer buffer = new StringBuffer();

        buffer.append("toc=[");

        double toc[][] = {{128, 128, 128}, //*** tipo 0 ao tipo 15 = para segmentos de Hugoniot
            {128, 128, 128}, //***
            {255, 0, 0}, //***
            {247, 151, 55}, //***
            {128, 128, 128}, //***
            {128, 128, 128}, //***
            {128, 128, 128}, //***
            {128, 128, 128}, //***
            {255, 0, 255}, //***
            {128, 128, 128}, //***
            {18, 153, 1}, //***
            {0, 0, 255}, //***
            {128, 128, 128}, //***
            {128, 128, 128}, //***
            {0, 255, 255}, //***
            {128, 128, 128}, //***
            {243, 123, 46}, // tipo 16 = SubInflection
            {20, 43, 140}, // tipo 17 = Coincidence
            {0, 255, 0}, // tipo 18 = {uma parte de Double Contact, BuckleyLeverettin}
            {248, 17, 47}, // tipo 19 = a outra parte de Double Contact
            {0, 153, 153} // tipo 20 = {Coincidence Extension, SubInflection Extension, Boundary Extension}
        };


        for (int i = 0; i < toc.length; i++) {

            for (int j = 0; j < 3; j++) {
                buffer.append("  " + toc[i][j] / 255.0 + " ");
            }
            buffer.append(";\n");
        }
        buffer.append("];\n\n");

        return buffer.toString();
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
        try {
            geomSource_ = calc_.recalc();
            geom_ = createGeomFromSource();
            isGeomOutOfDate_ = true;
        } catch (RpException rex) {
            RPnDesktopPlotter.showCalcExceptionDialog(rex);
        }
    }
}
