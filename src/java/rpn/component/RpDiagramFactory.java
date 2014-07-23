/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rpn.component;

import java.awt.Color;
import java.util.ArrayList;
import rpnumerics.RpDiagramCalc;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import rpn.RPnDesktopPlotter;
import rpn.controller.RpCalcController;
import rpn.controller.RpController;
import rpnumerics.Area;
import rpnumerics.Diagram;
import rpnumerics.DiagramLine;
import rpnumerics.RpException;
import rpnumerics.RpSolution;
import wave.multid.CoordsArray;
import wave.multid.model.MultiPolyLine;
import wave.multid.view.ViewingAttr;
import wave.util.RealVector;

/**
 *
 * @author edsonlan
 */
public final class RpDiagramFactory implements RpGeomFactory {

    private RpDiagramCalc calc_;
    private RpGeometry geom_;
    private RpSolution geomSource_;
    private boolean isGeomOutOfDate_;
    private RpController ui_;

    public RpDiagramFactory(RpDiagramCalc calc) {
        try {
            calc_ = calc;
            geom_ = createDiagramFromSource();
            isGeomOutOfDate_ = false;

            geomSource_ = calc.createDiagramSource();
        } catch (RpException ex) {
            RPnDesktopPlotter.showCalcExceptionDialog(ex);
        }
    }

    @Override
    public void updateGeom() {
        try {
            geomSource_ = calc_.updateDiagramSource();
            geom_ = createDiagramFromSource();
            isGeomOutOfDate_ = true;

        } catch (RpException ex) {
            RPnDesktopPlotter.showCalcExceptionDialog(ex);

        }
    }

    public RpGeometry createDiagramFromSource() throws RpException {

        Diagram solution = null;

        solution = (Diagram) calc_.createDiagramSource();

        ArrayList<MultiPolyLine> diagramLinesList = new ArrayList<MultiPolyLine>();
        List<DiagramLine> linesList = solution.getLines();

        for (DiagramLine line : linesList) {
            List<List<RealVector>> lineCoords = line.getCoords();

            int index = 0;
            for (List<RealVector> linePart : lineCoords) {
                CoordsArray[] diagramCoords = MultidAdapter.converseRealVectorListToCoordsArray(linePart);
                MultiPolyLine diagramLine = new MultiPolyLine(diagramCoords, new ViewingAttr(Color.white));
                System.out.println(line);
                int type = line.getType(index);

                diagramLine.viewingAttr().setColor(colorChooser(type));
                diagramLinesList.add(diagramLine);
                index++;

            }

        }

        DiagramGeom diagramGeom = new DiagramGeom(diagramLinesList, this);

        return diagramGeom;

    }

    public static Color colorChooser(int index) {

        switch (index) {

            case 0:
                return Color.white;

            case 1:
                return Color.blue;

            case 2:
                return Color.green;

            case 3:
                return Color.MAGENTA;
            case 4:
                return Color.gray;
            case 5:
                return Color.CYAN;

            default:
                return Color.PINK;

        }

    }

    @Override
    public void updateGeom(List<Area> area, List<Integer> segmentsToRemove) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public RpGeometry geom() {
        return geom_;
    }

    @Override
    public Object geomSource() {
        return geomSource_;
    }

    @Override
    public boolean isGeomOutOfDate() {
        return isGeomOutOfDate_;
    }

    protected RpController createUI() {
        return new RpCalcController();
    }

    @Override
    public void setGeomOutOfDate(boolean flag) {
        isGeomOutOfDate_ = flag;
    }

    protected void installController() {
        setUI(createUI());
        getUI().install(this);
    }

    public RpDiagramCalc rpDiagramCalc() {
        return calc_;
    }

    @Override
    public void setUI(RpController ui) {
        ui_ = ui;
    }

    @Override
    public RpController getUI() {
        return ui_;
    }

    @Override
    public String toXML() {
        return "Calling to XML";
    }

}
