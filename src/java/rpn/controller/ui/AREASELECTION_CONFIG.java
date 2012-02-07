/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.controller.ui;

import java.awt.Point;
import rpn.usecase.AreaSelectionAgent;
import rpn.usecase.BifurcationRefineAgent;
//import rpn.usecase.RpModelPlotAgent;
//import rpnumerics.RPNUMERICS;
import wave.multid.Coords2D;
import wave.multid.view.ViewingTransform;
import wave.util.RealVector;
//import wave.util.Boundary;
//import wave.util.IsoTriang2DBoundary;
//import wave.util.RectBoundary;

public class AREASELECTION_CONFIG extends UI_ACTION_SELECTED {

   

    public AREASELECTION_CONFIG() {
        super(AreaSelectionAgent.instance());
        System.out.println("Construtor de AREASELECTION_CONFIG");

    }



}
