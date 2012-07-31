/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rpn.usecase;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JToggleButton;
import rpn.RPnPhaseSpaceAbstraction;
import rpn.component.RpGeometry;
import rpn.component.util.GeometryGraph;
import rpn.component.util.GeometryGraphND;
import rpn.controller.ui.UIController;
import rpn.controller.ui.UserInputTable;
import rpnumerics.DoubleContactCurve;
import rpnumerics.SecondaryBifurcationCurve;
import rpnumerics.HugoniotCurve;
import rpnumerics.HugoniotSegment;
import rpnumerics.RPnCurve;
import rpnumerics.SegmentedCurve;
import wave.util.RealVector;
import rpn.controller.ui.CLASSIFIERAGENT_CONFIG;

/**
 *
 * @author moreira
 */
public class ClassifierAgent extends RpModelPlotAgent {

    static public final String DESC_TEXT = "Classify";
    static private ClassifierAgent instance_ = null;
    private JToggleButton button_;

    //** para classificar os segmentos (Leandro)
    static public List tipo = new ArrayList();
    //** Para a posição da string em coordenadas físicas
    static public List xStr = new ArrayList();
    static public List yStr = new ArrayList();
    //** Para ponta da seta da string na curva em coordenadas físicas
    static public List xSeta = new ArrayList();
    static public List ySeta = new ArrayList();


    //** Para indicar as curvas com classificadores
    static public List indCurvaCla = new ArrayList();       // vai ser comum para remocao e ocultacao
    static public List paraRemoverGeomCla = new ArrayList();
    static public List paraRemoverIndCla = new ArrayList(); // este é usado efetivamente para remocao
    static public List paraOcultarIndCla = new ArrayList(); // este é usado efetivamente para ocultacao
    static public List strView = new ArrayList();           // este é usado efetivamente para ocultacao


    private ClassifierAgent() {
        super(DESC_TEXT, null ,new JToggleButton());

        button_ = new JToggleButton(this);
        button_.setToolTipText(DESC_TEXT);

////        button_.setToolTipText("<html>After selecting this option, you<br>"              //*** Leandro
////                                   + "should take two clicks on the panel:<br>"
////                                   + "the first click determine the point<br>"
////                                   + "of interest in a curve; the second<br>"
////                                   + "click determine the location of the label.</html>");

        button_.setFont(rpn.RPnConfigReader.MODELPLOT_BUTTON_FONT);
        setEnabled(true);
    }

    @Override
    public void actionPerformed(ActionEvent event) {

        UIController.instance().setState(new CLASSIFIERAGENT_CONFIG());

    }

    public static ClassifierAgent instance() {
        if (instance_ == null) {
            instance_ = new ClassifierAgent();
        }
        return instance_;
    }

    @Override
    public JToggleButton getContainer() {
        return button_;
    }


    @Override
    public void execute() {

        VelocityAgent.listaEquil.clear();

        UserInputTable userInputList = UIController.instance().globalInputTable();
        RealVector newValue = userInputList.values();

        //RpGeometry geom = RPnPhaseSpaceAbstraction.findClosestGeometry(newValue);
        RpGeometry geom = phaseSpace_.findClosestGeometry(newValue);
        RPnCurve curve = (RPnCurve)(geom.geomFactory().geomSource());

        if ((GeometryGraph.count % 2) == 0) {

            GeometryGraphND.pMarca = curve.findClosestPoint(newValue);

            if (curve instanceof DoubleContactCurve  ||  curve instanceof SecondaryBifurcationCurve) {
                GeometryGraphND.pMarcaDC = GeometryGraphND.secondPointDC(curve);
            }
            else GeometryGraphND.pMarcaDC = GeometryGraphND.pMarca;

        }

        else if ((GeometryGraph.count % 2) == 1) {
            for (int i = 0; i < newValue.getSize(); i++) {
                GeometryGraphND.cornerStr.setElement(i, newValue.getElement(i));
                GeometryGraphND.cornerRet.setElement(i, 0);
            }

            //*** Botao CLASSIFY para HUGONIOT CURVE
            if (curve instanceof HugoniotCurve) {

                HugoniotSegment segment = (HugoniotSegment)(((SegmentedCurve)curve).segments()).get(curve.findClosestSegment(GeometryGraphND.pMarca));
                tipo.add(segment.getType());

                xStr.add(GeometryGraphND.cornerStr.getElement(1));
                yStr.add(GeometryGraphND.cornerStr.getElement(0));

                //--------------------------------------------------------------
                if (RPnPhaseSpaceAbstraction.namePhaseSpace.equals("Phase Space"))
                    strView.add(1);
                if (RPnPhaseSpaceAbstraction.namePhaseSpace.equals("RightPhase Space"))
                    strView.add(2);
                if (RPnPhaseSpaceAbstraction.namePhaseSpace.equals("LeftPhase Space"))
                    strView.add(3);
                //--------------------------------------------------------------

                xSeta.add(GeometryGraphND.pMarca.getElement(1));
                ySeta.add(GeometryGraphND.pMarca.getElement(0));

                indCurvaCla.add(RPnPhaseSpaceAbstraction.closestCurve);

                GeometryGraph.count++;
                return;

            } //***

        }
        GeometryGraph.count++;
    }

    @Override
    public void unexecute() {
        throw new UnsupportedOperationException("Not supported yet.");
    }


    public static void clearClassifiers() {
        GeometryGraphND.clearpMarca();
        strView.clear();
        indCurvaCla.clear();
        tipo.clear();
        xSeta.clear();
        xStr.clear();
        ySeta.clear();
        yStr.clear();
    }

    public static void clearClassifiers(List paraRemover) {
        for (int i = 0; i < paraRemover.size(); i++) {
            int index = (Integer)paraRemover.get(i);
            xSeta.remove(index);
            xStr.remove(index);
            ySeta.remove(index);
            yStr.remove(index);

            xSeta.add(index, 0.);
            xStr.add(index, 0.);
            ySeta.add(index, 0.);
            yStr.add(index, 0.);
        }
    }

    public static void hideClassifiers(List paraOcultar) {
        for (int i = 0; i < paraOcultar.size(); i++) {
            int index = (Integer)paraOcultar.get(i);
            strView.set(index, -1);
        }
    }

    public static void viewClassifiers(List paraOcultar) {
        for (int i = 0; i < paraOcultar.size(); i++) {
            int index = (Integer)paraOcultar.get(i);
            strView.set(index, 1);
        }
    }

    @Override
    public RpGeometry createRpGeometry(RealVector[] coords) {
        throw new UnsupportedOperationException("Not supported yet.");
    }



}
