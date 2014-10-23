/*
* Instituto de Matematica Pura e Aplicada - IMPA
* Departamento de Dinamica dos Fluidos
*
*/

package rpnumerics;

import wave.util.RealVector;
/**
 *
 * @deprecated Will exists in native layer only
 */

public  class FluxParams {
    //
    // Members
    //
    private RealVector params_;
//    private RealVector initParams_;
//    private String physicsID_;
//    private int index_;
    //
    // Constructors
    //
    public FluxParams(FluxParams params) {
        this(params.getParams());

        System.out.println("Tamanho do flux params: " + params_.getSize());
        System.out.println("Usou o construtor FluxParams(FluxParams params)");
    }

    public FluxParams(RealVector params) {
        params_ = new RealVector(params);

//        System.out.println("Tamanho do flux params: " + params_.getSize());
//        System.out.println("Usou o construtor FluxParams(RealVector params)");
//        System.out.println("VOU IMPRIMIR OS PARAMS");
//        for (int i=0; i<params_.getSize();i++) {
//            System.out.println(params_.getElement(i));
//        }
//        System.out.println("PARAMS IMPRESSOS");


//        physicsID_ = physID.toString();
//        initParams_ = new RealVector(params);
//        index_=index;

          }

    //public FluxParams(RealVector params) {
    //    params_ = new RealVector(params);
    //}
 


    //
    // Accessors/Mutators
    //
    public double getElement(int index) { return params_.getElement(index); }

    public RealVector getParams() { return params_; }

    public synchronized void setParams(double[] params) { params_ = new RealVector(params); }

    public synchronized void setParams(RealVector params) { params_ = new RealVector(params); }

    public synchronized void setParam(int index, double value) { params_.setElement(index, value); }




    //
    // Methods
    //
    public boolean equals(Object object) {
        if ((object instanceof FluxParams) && ((FluxParams)object).getParams().equals(getParams()))
             
                return true;
        return false;
    }
    

    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append(params_.toString());
        return buf.toString();
    }
}
