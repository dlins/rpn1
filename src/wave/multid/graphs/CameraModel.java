/*
*
* Instituto de Matematica Pura e Aplicada - IMPA
* Departamento de Dinamica dos Fluidos
*
*/

package wave.multid.graphs;

import wave.util.RealMatrix2;
import wave.util.RealVector;

public class CameraModel {
    //
    // Members
    //
    private RealMatrix2 transfMatrix_;
    private RealVector viewReference_;
    private RealVector viewUp_;
    private RealVector vpNormal_;
    private double vpDistance_;

    //
    // Constructors
    //
    public CameraModel(double[] viewReference, double[] viewUp, double viewPlaneDistance, ViewPlane viewPlane) {
        transfMatrix_ = new RealMatrix2(4, 4);
        //setViewPlaneNormal(viewPlane.normal());
        setViewDistance(viewPlaneDistance);
        setViewUp(viewUp);
        setViewReferencePoint(viewReference);
        makeTransfMatrix();
    }

    //
    // Accessors/Mutators
    //
    public void setViewPlaneNormal(double[] vpn) {
        vpNormal_ = new RealVector(vpn);
        vpNormal_.setElement(3, 1);
    }

    public void setViewDistance(double distance) {
        vpDistance_ = distance;
    }

    public void setViewUp(double[] viewUp) {
        viewUp_ = new RealVector(viewUp);
        viewUp_.setElement(3, 1);
    }

    public void setViewReferencePoint(double[] vrp) {
        viewReference_ = new RealVector(vrp);
        viewReference_.setElement(3, 1);
    }

    public RealVector getViewReferencePoint() { return viewReference_; }

    public RealVector getViewUp() { return viewUp_; }

    public RealVector getViewPlaneNormal() { return vpNormal_; }

    public double getViewPlaneDistance() { return vpDistance_; }

    public RealMatrix2 getTransfMatrix() { return transfMatrix_; }

    //
    // Methods
    //
    public void makeTransfMatrix() {
        // translation to make view plane center the origin
        RealMatrix2 T = new RealMatrix2(4, 4);
        T.setElement(3, 0, -(viewReference_.getElement(0) + vpNormal_.getElement(0) * vpDistance_));
        T.setElement(3, 1, -(viewReference_.getElement(1) + vpNormal_.getElement(1) * vpDistance_));
        T.setElement(3, 2, -(viewReference_.getElement(2) + vpNormal_.getElement(2) * vpDistance_));
        // rotation to make Z parallel to view plane normal
        double V = Math.sqrt(Math.pow(vpNormal_.getElement(1), 2) + Math.pow(vpNormal_.getElement(2), 2));
        RealMatrix2 Rx = new RealMatrix2(4, 4);
        Rx.setElement(1, 1, -vpNormal_.getElement(2) / V);
        Rx.setElement(1, 2, -vpNormal_.getElement(1) / V);
        Rx.setElement(2, 1, vpNormal_.getElement(1) / V);
        Rx.setElement(2, 2, -vpNormal_.getElement(2) / V);
        transfMatrix_.mul(Rx);
        RealMatrix2 Ry = new RealMatrix2(4, 4);
        Ry.setElement(0, 0, V);
        Ry.setElement(0, 2, -vpNormal_.getElement(0));
        Ry.setElement(2, 0, vpNormal_.getElement(0));
        Ry.setElement(2, 2, V);
        transfMatrix_.mul(Ry);
        // transform view up into view plane coords
        RealVector UPVPCoords = new RealVector(viewUp_);
        UPVPCoords.mul(transfMatrix_, UPVPCoords);
        // rotation on Z to make view up vertical
        double RUP = Math.sqrt(Math.pow(UPVPCoords.getElement(0), 2) + Math.pow(UPVPCoords.getElement(1), 2));
        RealMatrix2 Rz = new RealMatrix2(4, 4);
        Rz.setElement(0, 0, UPVPCoords.getElement(1) / RUP);
        Rz.setElement(0, 1, UPVPCoords.getElement(0) / RUP);
        Rz.setElement(1, 0, -UPVPCoords.getElement(0) / RUP);
        Rz.setElement(1, 1, UPVPCoords.getElement(1) / RUP);
        transfMatrix_.mul(Rz);
        transfMatrix_.mul(T);
    }
}
