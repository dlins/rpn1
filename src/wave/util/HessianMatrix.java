/*
* Instituto de Matematica Pura e Aplicada - IMPA
* Departamento de Dinamica dos Fluidos
*
*/

package wave.util;

public class HessianMatrix extends MultipleMatrix {
    //
    // Constants
    //
    //
    // Members
    //
    //
    // Constructors
    //
    private   double []  data_ ;
    private int dim_;

    public HessianMatrix(int n) {

      super(n);

      data_= new double[(int)(Math.pow((double)(n),(double)3))];
      dim_=n;//(int)(Math.pow((double)(n),(double)1/3));

    }


    public HessianMatrix(double data []){

	super((int)(Math.pow((double)(data.length),(double)1/3)));

//	int i,j,k,index;
        data_= new double[data.length];
        data_=data;
	dim_ = (int)(Math.pow((double)(data.length),(double)1/3));

//	for (i = 0 ;i < dim_;i++){
//
//	    for (j = 0 ;j < dim_ ; j++){
//
//		for (k=0 ; k < dim_ ; k++){
//                  index=((int)(Math.pow(dim_,2)*i))+dim_*j+k;
//                  data_[index]=data[index];
//		}
//	    }
//	}

    }

    //
    // Accessors/Mutators
    //


    public void setElement(int i,int j,int k,double value){

      int index=((int)(Math.pow(dim_,2)*i))+dim_*j+k;
	data_[index]=value;

    }


    public double getElement(int i,int j , int k){

       int index=((int)(Math.pow(dim_,2)*i))+dim_*j+k;
       return	data_[index];


    }




}
