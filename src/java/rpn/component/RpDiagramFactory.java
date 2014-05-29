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
import rpn.controller.RpCalcController;
import rpn.controller.RpController;
import rpnumerics.Area;
import rpnumerics.Diagram;
import rpnumerics.DiagramLine;
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
        calc_ = calc;
        geom_ = createDiagramFromSource();
        isGeomOutOfDate_ = false;

        geomSource_ = calc.createDiagramSource();
    }

    @Override
    public void updateGeom() {
        geomSource_ = calc_.updateDiagramSource();
        geom_ = createDiagramFromSource();
        isGeomOutOfDate_ = true;
    }

    public RpGeometry createDiagramFromSource() {
        
        


        Diagram solution = (Diagram) calc_.createDiagramSource();
        ArrayList<MultiPolyLine> diagramLinesList = new ArrayList<MultiPolyLine>();
        List<DiagramLine> linesList = solution.getLines();

        for (DiagramLine line : linesList) {
            List<List<RealVector>> lineCoords = line.getCoords();


            for (List<RealVector> linePart : lineCoords) {
                CoordsArray[] diagramCoords = MultidAdapter.converseRealVectorListToCoordsArray(linePart);
                MultiPolyLine diagramLine = new MultiPolyLine(diagramCoords, new ViewingAttr(Color.white));
                diagramLinesList.add(diagramLine);

            }

        }

        DiagramGeom diagramGeom = new DiagramGeom(diagramLinesList, this);

        return diagramGeom;

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
