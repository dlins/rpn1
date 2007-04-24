/*
* Instituto de Matematica Pura e Aplicada - IMPA
* Departamento de Dinamica dos Fluidos
*
*/

package rpnumerics.physics;

import wave.util.RealVector;

public abstract class FluxParams {
    //
    // Members
    //
    private RealVector params_;
    private RealVector initParams_;
    private String physicsID_;
    private int index_;
    //
    // Constructors
    //
    public FluxParams(FluxParams params) {
        this(params.getPhysicsID(), params.getParams(),params.getIndex());
    }

    public FluxParams(String physID, RealVector params,int index) {
        params_ = new RealVector(params);
        physicsID_ = physID.toString();
        initParams_ = new RealVector(params);
        index_=index;
    }

    //
    // Accessors/Mutators
    //
    public double getElement(int index) { return params_.getElement(index); }

    public RealVector getParams() { return params_; }

    public synchronized void setParams(double[] params) { params_ = new RealVector(params); }

    public synchronized void setParams(RealVector params) { params_ = new RealVector(params); }

    public synchronized void setParam(int index, double value) { params_.setElement(index, value); }

    public String getPhysicsID() {return physicsID_; }

    public int getIndex() {return index_;}

    public abstract FluxParams defaultParams();

    //
    // Methods
    //
    public boolean equals(Object object) {
        if ((object instanceof FluxParams) && (((FluxParams)object).getParams().equals(getParams())) &&
            (((FluxParams)object).getPhysicsID().compareTo(getPhysicsID()) == 0))
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
