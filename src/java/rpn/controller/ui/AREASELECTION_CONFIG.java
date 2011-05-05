/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.controller.ui;

import java.awt.Point;
import rpn.usecase.AreaSelectionAgent;
import rpn.usecase.BifurcationRefineAgent;
import wave.multid.Coords2D;
import wave.multid.view.ViewingTransform;
import wave.util.RealVector;

public class AREASELECTION_CONFIG extends UI_ACTION_SELECTED {

    private int pointsSelected_;
    private ViewingTransform viewingTransf_;
    private int xResolution_;
    private int yResolution_;
    private RealVector areaTop_;
    private RealVector areaDown_;

    public AREASELECTION_CONFIG() {

        super(AreaSelectionAgent.instance());
        pointsSelected_ = 0;
        System.out.println("Construtor de AREASELECTION_CONFIG");

    }

    @Override
    public void userInputComplete(rpn.controller.ui.UIController ui,
            RealVector userInput) {

//        super.userInputComplete(ui, userInput);
        System.out.println(" Chamando userInputComplete de AREASELECTION_CONFIG");
//        if (pointsSelected_ == 0) {
//
//            ui.getFocusPanel().getCastedUI().getSelectionAreas().clear();
//            areaTop_ = new RealVector(userInput);
//            pointsSelected_++;
//
//        } else {
//            areaDown_ = new RealVector(userInput);
//
//            viewingTransf_ = ui.getFocusPanel().scene().getViewingTransform();
//
//            Point topLeft = toDCcoords(areaTop_);
//            Point downRight = toDCcoords(areaDown_);
//
//            double width = downRight.x - topLeft.x;
//            double heigh = downRight.y - topLeft.y;
//
//            Rectangle2D.Double rectangle = new Rectangle2D.Double(topLeft.x, topLeft.y, width, heigh);
//
//            BifurcationProfile.instance().addArea(new Area(xResolution_, yResolution_, topLeft.x, topLeft.y, downRight.x, downRight.y));
//
//
//            ui.getFocusPanel().getCastedUI().getSelectionAreas().add(rectangle);
//            ui.getFocusPanel().repaint();
//
//            RPnSelectedAreaDialog resolutionDialog = new RPnSelectedAreaDialog();
//            resolutionDialog.setVisible(true);
//
//            reset();

            //TESTE


//            ArrayList<rpnumerics.Area> testeArray = BifurcationProfile.instance().getSelectedAreas();
//
//            int i = 0;
//            for (rpnumerics.Area areaSelecionada : testeArray) {
//
//                System.out.println("Posicao " + i + areaSelecionada.getxUpLeft() + " " + areaSelecionada.getyUpLeft() + " " + areaSelecionada.getxDownRight() + " " + areaSelecionada.getyDownRight() + " " + areaSelecionada.getxResolution() + " " + areaSelecionada.getyResolution());
//
//                i++;
//
//            }
//
//            System.out.println("Tamanho do array: " + testeArray.size());
//
//
//
//            BifurcationRefineAgent.instance().getContainer().setEnabled(true);
//
//        }

    }

    public void reset() {
        UIController.instance().getFocusPanel().getCastedUI().pointMarkBuffer().clear();
        pointsSelected_ = 0;
        BifurcationRefineAgent.instance().getContainer().setEnabled(false);

    }

    private Point toDCcoords(RealVector input) {
        Coords2D wcCoords = new Coords2D(input.getElement(0), input.getElement(1));
        Coords2D dcCoords = new Coords2D();
        viewingTransf_.viewPlaneTransform(wcCoords, dcCoords);
        Point point = new Point((int) dcCoords.getX(), (int) dcCoords.getY());

        return point;

    }

    public void setxResolution_(int xResolution) {
        this.xResolution_ = xResolution;
    }

    public void setyResolution_(int yResolution) {
        this.yResolution_ = yResolution;
    }







}
