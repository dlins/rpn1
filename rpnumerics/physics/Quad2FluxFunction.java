/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpnumerics.physics;

import wave.util.HessianMatrix;
import wave.util.RealVector;
import wave.util.RealMatrix2;

public class Quad2FluxFunction implements FluxFunction
{
	private Quad2FluxParams params_;


	//
    // Constructors/Inner Classes
    //
    public Quad2FluxFunction(Quad2FluxParams params) {
        params_ = params;
    }
    public RealVector F(RealVector x) {

        RealVector res = new RealVector(2);

		double a1,b1,c1,d1,e1,a2,b2,c2,d2,e2,u,v,out1 = 0,out2 = 0;
	
		u=x.getElement(0);
		v=x.getElement(1);
	
		/*a1=params_.getParams().getElement(0);
		b1=params_.getParams().getElement(1);
		c1=params_.getParams().getElement(2);
		d1=params_.getParams().getElement(3);
		e1=params_.getParams().getElement(4);
		a2=params_.getParams().getElement(5);
		b2= params_.getParams().getElement(6);
		c2=params_.getParams().getElement(7);
		d2=params_.getParams().getElement(8);
		e2=params_.getParams().getElement(9);*/
		
		a1 = -1.0;
		b1 = 0.0;
		c1 = 1.0;
		d1 = 0.0;
		e1 = 0.0;
		
		a2 = 1.0;
		b2 = 1.0;
		c2 = 0.0;
		d2 = 0.0;
		e2 = 0.0;
	
		try {
			out1=0.5 * ( a1*Math.pow(u,(double)2) + 2.0*b1*u*v + c1*Math.pow(v,(double)2) ) + d1*u + e1*v;
			out2=0.5 * ( a2*Math.pow(u,(double)2) + 2.0*b2*u*v + c2*Math.pow(v,(double)2) ) + d2*u + e2*v;
		} catch (Exception e) {
			e.printStackTrace();
		}	
	
		res.setElement(0,out1);
		res.setElement(1,out2);

        return res;
    }

    public RealMatrix2 DF(RealVector x) {
        RealMatrix2 res = new RealMatrix2(2, 2);
        
        double a1,b1,c1,d1,e1,a2,b2,c2,d2,e2,u,v, out00,out01, out10, out11;
        
        u=x.getElement(0);
		v=x.getElement(1);	
		
		a1 = -1.0;
		b1 = 0.0;
		c1 = 1.0;
		d1 = 0.0;
		e1 = 0.0;
		
		a2 = 1.0;
		b2 = 1.0;
		c2 = 0.0;
		d2 = 0.0;
		e2 = 0.0;
    	
        out00= a1*u + b1*v + d1;    	
        out01= b1*u + c1*v + e1;    	
		out10= a2*u + b2*v + d2;		
		out11= b2*u + c2*v + e2;
		
		res.setElement(0, 0, out00);
		res.setElement(0, 1, out01);
		res.setElement(1, 0, out10);
		res.setElement(1, 1, out11);
        
        return res;
    }

    public HessianMatrix D2F(RealVector x) {

	double a1,b1,c1,d1,e1,a2,b2,c2,d2,e2,u,v,out[];

	out = new double[8];

	u=x.getElement(0);
	v=x.getElement(1);

	a1=params_.getParams().getElement(0);
	b1=params_.getParams().getElement(1);
	c1=params_.getParams().getElement(2);
	d1=params_.getParams().getElement(3);
	e1=params_.getParams().getElement(4);
	a2=params_.getParams().getElement(5);
	b2= params_.getParams().getElement(6);
	c2=params_.getParams().getElement(7);
	d2=params_.getParams().getElement(8);
	e2=params_.getParams().getElement(9);


	out[0] = a1;
	out[1] = b1;
	out[2] = b1;
	out[3] = c1;
	out[4] = a2;
	out[5] = b2;
	out[6] = b2;
	out[7] = c2;

	return new HessianMatrix (out);


    }

	public FluxParams fluxParams( )	{return params_;}

}
