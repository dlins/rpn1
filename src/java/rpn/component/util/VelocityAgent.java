/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rpn.component.util;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JToggleButton;
import rpn.RPnPhaseSpaceAbstraction;
import rpn.component.RpGeometry;
import rpn.controller.ui.UIController;
import rpn.controller.ui.UserInputTable;
import rpn.usecase.RpModelActionAgent;
import rpnumerics.HugoniotCurve;
import rpnumerics.HugoniotSegment;
import rpnumerics.Orbit;
import rpnumerics.OrbitPoint;
import rpnumerics.RPnCurve;
import rpnumerics.SegmentedCurve;
import wave.util.RealVector;

/**
 *
 * @author moreira
 */
public class VelocityAgent extends RpModelActionAgent {

    static public final String DESC_TEXT = "Velocity";
    static private VelocityAgent instance_ = null;
    private JToggleButton button_;

    static public List vel = new ArrayList();
    //** Para a posição da velocidade em coordenadas físicas
    static public List xVel = new ArrayList();
    static public List yVel = new ArrayList();
    //** Para ponta da seta da velocidade na curva em coordenadas físicas
    static public List xSetaVel = new ArrayList();
    static public List ySetaVel = new ArrayList();
    
    //** Para indicar as curvas com velocidades
    static public List indCurvaVel = new ArrayList();
    static public List paraRemoverGeomVel = new ArrayList();
    static public List paraRemoverIndVel = new ArrayList();
    static public List paraOcultarIndVel = new ArrayList();
    static public List velView = new ArrayList();

    public static List<RealVector> listaEquil = new ArrayList();  //substituto do ControlClick.listaEquil


    private VelocityAgent() {
        super(DESC_TEXT, null);

        button_ = new JToggleButton(this);
        button_.setToolTipText(DESC_TEXT);

        button_.setFont(rpn.RPnConfigReader.MODELPLOT_BUTTON_FONT);
        setEnabled(true);
    }

    @Override
    public void actionPerformed(ActionEvent event) {

       UIController.instance().setState(new VELOCITYAGENT_CONFIG());

    }

    public static VelocityAgent instance() {
        if (instance_ == null) {
            instance_ = new VelocityAgent();
        }
        return instance_;
    }

    public JToggleButton getContainer() {
        return button_;
    }


    @Override
    public void execute() {

        UserInputTable userInputList = UIController.instance().globalInputTable();
        RealVector newValue = userInputList.values();


        RpGeometry geom = RPnPhaseSpaceAbstraction.findClosestGeometry(newValue);
        RPnCurve curve = (RPnCurve)(geom.geomFactory().geomSource());

        if ((GeometryGraph.count % 2) == 0) {

            GeometryGraphND.pMarca = curve.findClosestPoint(newValue);

            if (curve instanceof HugoniotCurve)
                listaEquil = ((HugoniotCurve)(curve)).equilPoints(GeometryGraphND.pMarca);

        }

        else if ((GeometryGraph.count % 2) == 1) {
            for (int i = 0; i < newValue.getSize(); i++) {
                GeometryGraphND.cornerStr.setElement(i, newValue.getElement(i));
                GeometryGraphND.cornerRet.setElement(i, 0);
            }

            if (curve instanceof Orbit) {
                OrbitPoint point = (OrbitPoint) ((Orbit) curve).getPoints()[curve.findClosestSegment(GeometryGraphND.pMarca)];
                vel.add(point.getLambda());
            }
            else if (curve instanceof HugoniotCurve) {
                vel.add(((HugoniotCurve)curve).velocity(GeometryGraphND.pMarca));
            }

                xVel.add(GeometryGraphND.cornerStr.getElement(1));
                yVel.add(GeometryGraphND.cornerStr.getElement(0));

                //--------------------------------------------------------------
                if (RPnPhaseSpaceAbstraction.namePhaseSpace.equals("Phase Space"))      velView.add(1);
                if (RPnPhaseSpaceAbstraction.namePhaseSpace.equals("RightPhase Space")) velView.add(2);
                if (RPnPhaseSpaceAbstraction.namePhaseSpace.equals("LeftPhase Space"))  velView.add(3);
                //--------------------------------------------------------------

                xSetaVel.add(GeometryGraphND.pMarca.getElement(1));
                ySetaVel.add(GeometryGraphND.pMarca.getElement(0));

                indCurvaVel.add(RPnPhaseSpaceAbstraction.closestCurve);

                GeometryGraph.count++;
                return;

        }
        GeometryGraph.count++;

    }

    @Override
    public void unexecute() {
        throw new UnsupportedOperationException("Not supported yet.");
    }


    public static void clearVelocities() {
        GeometryGraphND.clearpMarca();
        velView.clear();
        indCurvaVel.clear();
        vel.clear();
        xSetaVel.clear();
        xVel.clear();
        ySetaVel.clear();
        yVel.clear();
    }


    public static void hideVelocities(List paraOcultar) {
        for (int i = 0; i < paraOcultar.size(); i++) {
            int index = (Integer)paraOcultar.get(i);
            velView.set(index, -1);
        }
    }

    public static void viewVelocities(List paraOcultar) {
        for (int i = 0; i < paraOcultar.size(); i++) {
            int index = (Integer)paraOcultar.get(i);
            velView.set(index, 1);
        }
    }

    public static void clearVelocities(List paraRemover) {
        for (int i = 0; i < paraRemover.size(); i++) {
            int index = (Integer)paraRemover.get(i);
            xSetaVel.remove(index);
            xVel.remove(index);
            ySetaVel.remove(index);
            yVel.remove(index);

            xSetaVel.add(index, 0.);
            xVel.add(index, 0.);
            ySetaVel.add(index, 0.);
            yVel.add(index, 0.);
        }

    }

    


}
