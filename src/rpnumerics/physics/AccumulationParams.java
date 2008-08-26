/*
* Instituto de Matematica Pura e Aplicada - IMPA
* Departamento de Dinamica dos Fluidos
*
*/

package rpnumerics.physics;

import wave.util.RealVector;

public abstract class AccumulationParams {
    //
    // Members
    //
    private RealVector params_;
    private RealVector initParams_;
    private String physicsID_;

    //
    // Constructors
    //
    public AccumulationParams(AccumulationParams params) {
        this(params.getPhysicsID(), params.getParams());
    }

    public AccumulationParams(String physID, RealVector params) {
        params_ = new RealVector(params);
        physicsID_ = physID.toString();
        initParams_ = new RealVector(params);
    }

    //
    // Accessors/Mutators
    //
    public double getElement(int index) { return params_.getElement(index); }

    public RealVector getParams() { return params_; }

    public synchronized void setParams(double[] params) { params_ = new RealVector(params); }

    public synchronized void setParams(RealVector params) { params_ = new RealVector(params); }

    public synchronized void setParam(int index, double value) { params_.setElement(index, value); }

    public String getPhysicsID() {
        return physicsID_;
    }

    public abstract AccumulationParams defaultParams();

    //
    // Methods
    //
    public boolean equals(Object object) {
        if ((object instanceof AccumulationParams) && (((AccumulationParams)object).getParams().equals(getParams())) &&
            (((AccumulationParams)object).getPhysicsID().compareTo(getPhysicsID()) == 0))
                return true;
        return false;
    }

    public synchronized void reset() {
        setParams(initParams_);
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append(params_.toString());
        return buf.toString();
    }
}
