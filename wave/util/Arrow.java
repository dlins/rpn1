/*
*
* Instituto de Matematica Pura e Aplicada - IMPA
* Departamento de Dinamica dos Fluidos
*
*/

package wave.util;

import java.util.ArrayList;
import javax.swing.JComponent;
import java.awt.Graphics;

//     this routine draws an arrow starting at  positn,  with direction
//     direct. (these vectors are given in user coordinates).
//     the  length  and  size  are given in absolute window units.
//     if  length is negative, it will point to the opposite direction.
//     if  size is negative, it will have arrow head reversed.
//
//     direction coordinates are related to origin
public class Arrow extends JComponent {
    //
    // Constants
    //
    //
    // Members
    //
    private RealVector px_;
    private RealVector py_;
    private ArrayList head_;
    private ArrayList body_;

    //
    // Constructors
    //
    public Arrow(RealVector positn, RealVector direct, double size, double length) {
        RealVector nd = new RealVector(2);
        RealVector ne = new RealVector(2);
        double aux, fatorx, fatory;
        aux = 0d;
        fatorx = fatory = 1.;
        px_ = new RealVector(3);
        py_ = new RealVector(3);
        head_ = new ArrayList();
        body_ = new ArrayList();
        if (length >= 0)
            aux = 1. / Math.sqrt(Math.pow(direct.getElement(0), 2) + Math.pow(direct.getElement(1), 2));
        else
            aux = -1. / Math.sqrt(Math.pow(direct.getElement(0), 2) + Math.pow(direct.getElement(1), 2));
        nd.setElement(0, aux * direct.getElement(0));
        nd.setElement(1, aux * direct.getElement(1));
        ne.setElement(0, (nd.getElement(0) + nd.getElement(1)) * Math.abs(size));
        ne.setElement(1, (nd.getElement(0) - nd.getElement(1)) * Math.abs(size));
        px_.setElement(0, positn.getElement(0));
        py_.setElement(0, positn.getElement(1));
        if (length != 0.0) {
            px_.setElement(1, px_.getElement(0) + nd.getElement(0) * Math.abs(length) * fatorx);
            py_.setElement(1, py_.getElement(0) + nd.getElement(1) * Math.abs(length) * fatory);
            body_.add(new RealVector(
                new double[] { px_.getElement(0), py_.getElement(0) }));
            body_.add(new RealVector(
                new double[] { px_.getElement(1), py_.getElement(1) }));
        }
        if (size >= 0.0) {
            px_.setElement(0, px_.getElement(1) - ne.getElement(0) * fatorx);
            py_.setElement(0, py_.getElement(1) + ne.getElement(1) * fatory);
            px_.setElement(2, px_.getElement(1) - ne.getElement(1) * fatorx);
            py_.setElement(2, py_.getElement(1) - ne.getElement(0) * fatory);
        }
        else {
            px_.setElement(0, px_.getElement(1) + ne.getElement(1) * fatorx);
            py_.setElement(0, py_.getElement(1) + ne.getElement(0) * fatory);
            px_.setElement(2, px_.getElement(1) + ne.getElement(0) * fatorx);
            py_.setElement(2, py_.getElement(1) - ne.getElement(1) * fatory);
        }
        head_.add(new RealVector(
            new double[] { px_.getElement(0), py_.getElement(0) }));
        head_.add(new RealVector(
            new double[] { px_.getElement(1), py_.getElement(1) }));
        head_.add(new RealVector(
            new double[] { px_.getElement(2), py_.getElement(2) }));
    }

    //
    // Accessors
    //
    public ArrayList getHeadDefPoints() { return head_; }

    public ArrayList getBodyDefPoints() { return body_; }

    //
    // Methods
    //
    public void paintComponent(Graphics g) {
        double bodyAx = ((RealVector)getBodyDefPoints().get(0)).getElement(0);
        double bodyAy = ((RealVector)getBodyDefPoints().get(0)).getElement(1);
        double bodyBx = ((RealVector)getBodyDefPoints().get(1)).getElement(0);
        double bodyBy = ((RealVector)getBodyDefPoints().get(1)).getElement(1);
        double headAx = ((RealVector)getHeadDefPoints().get(0)).getElement(0);
        double headAy = ((RealVector)getHeadDefPoints().get(0)).getElement(1);
        double headBx = ((RealVector)getHeadDefPoints().get(1)).getElement(0);
        double headBy = ((RealVector)getHeadDefPoints().get(1)).getElement(1);
        double headCx = ((RealVector)getHeadDefPoints().get(2)).getElement(0);
        double headCy = ((RealVector)getHeadDefPoints().get(2)).getElement(1);
        g.drawLine(new Double(bodyAx).intValue(), new Double(bodyAy).intValue(), new Double(bodyBx).intValue(),
            new Double(bodyBy).intValue());
        g.drawLine(new Double(headAx).intValue(), new Double(headAy).intValue(), new Double(headBx).intValue(),
            new Double(headBy).intValue());
        g.drawLine(new Double(headBx).intValue(), new Double(headBy).intValue(), new Double(headCx).intValue(),
            new Double(headCy).intValue());
    }
}
