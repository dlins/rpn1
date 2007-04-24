package rpnumerics;

import wave.util.*;
import rpnumerics.physics.FluxFunction;


public class HugoniotParams{


    private  PhasePoint phasePoint_;


    public  HugoniotParams (PhasePoint p){

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



}
