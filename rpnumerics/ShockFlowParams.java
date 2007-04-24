package rpnumerics;

import wave.util.*;
import rpnumerics.physics.FluxFunction;


public class ShockFlowParams{


    private  PhasePoint phasePoint_;

    private double sigma_;

    public  ShockFlowParams (PhasePoint p,double sigma){

	sigma_=sigma;
	phasePoint_=p;

    }


    public PhasePoint getPhasePoint(){

	return phasePoint_;
    }


    public void setPhasePoint(PhasePoint p){

	phasePoint_=p;

    }

    public RealVector getCoords (){

	return phasePoint_.getCoords();

    }


    public double getSigma(){

	return sigma_;
    }


    public void setSigma(double sigma){

	sigma_=sigma;
    }



}
