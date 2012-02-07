/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rpn.component.util;

import java.awt.Point;
import rpn.controller.ui.UIController;
import rpn.controller.ui.UI_ACTION_SELECTED;
import rpn.usecase.AreaSelectionAgent;
import rpn.usecase.BifurcationRefineAgent;
import wave.multid.Coords2D;
import wave.multid.view.ViewingTransform;
import wave.util.RealVector;

/**
 *
 * @author moreira
 */


public class AREASELECTION_CONFIG2 extends UI_ACTION_SELECTED {

    private int pointsSelected_;
    private ViewingTransform viewingTransf_;
    private int xResolution_;
    private int yResolution_;
    private RealVector areaTop_;
    private RealVector areaDown_;


    public AREASELECTION_CONFIG2() {

        super(AreaSelectionAgent2.instance());
        pointsSelected_ = 0;
        System.out.println("Construtor de AREASELECTION_CONFIG2");
        
    }




}