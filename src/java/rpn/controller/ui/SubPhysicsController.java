/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rpn.controller.ui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import rpn.RPnPhaseSpaceFrame;
import rpnumerics.RPNUMERICS;

/**
 *
 * @author edsonlan
 */
public class SubPhysicsController  {

    private RPnPhaseSpaceFrame framesMatrix_[][];
    private int subPhysicsNumber_;

    public SubPhysicsController(int subPhysicsNumber_) {
        this.subPhysicsNumber_ = subPhysicsNumber_;
        framesMatrix_ = new RPnPhaseSpaceFrame[subPhysicsNumber_][RPNUMERICS.domainDim() - 1];


    }

    public void setFrame(int frameRow, int frameColumn, RPnPhaseSpaceFrame frame) {
        frame.phaseSpacePanel().addMouseListener(new MouseController());
        framesMatrix_[frameRow][frameColumn] = frame;

    }

    public RPnPhaseSpaceFrame[][] getFramesMatrix() {
        return framesMatrix_;
    }

    public int getSubPhysicsNumber() {
        return subPhysicsNumber_;
    }

    private class MouseController extends MouseAdapter {

        @Override
        public void mouseEntered(MouseEvent event) {
            for (int i = 0; i < framesMatrix_.length; i++) {
                RPnPhaseSpaceFrame[] rPnPhaseSpaceFrames = framesMatrix_[i];
                for (int j = 0; j < rPnPhaseSpaceFrames.length; j++) {
                    RPnPhaseSpaceFrame rPnPhaseSpaceFrame = rPnPhaseSpaceFrames[j];
                    if (rPnPhaseSpaceFrame != null) {
                        if (rPnPhaseSpaceFrame.phaseSpacePanel() == event.getSource()) {
                            System.out.println("Subfisica: " + i + " painel " + j);


                        }
                    }

                }
            }


        }
    }
}
