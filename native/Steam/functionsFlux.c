#include <math.h>
#include <stdlib.h>
#include "mcd.h"
#include "funsteam.cc"


double *  f(double * in,int sizeIn,int * sizeOut,double * param){

  
  double *res;
  
  *sizeOut=3;  
  
  res =(double *)calloc(sizeOut,sizeof(double));
  
  if (res==NULL){
    
    printf ("res is null\n");
    
    return;
  }
  
  res[0]= F1bifasico( in[0], in[1]);
  res[1]= F2bifasico( in[0], in[1]);
  res[2]= F3bifasico( in[0], in[1]);
  
  return res;

  
}
double * df(double * in,int sizeIn,int * sizeOut ,double * param){
  
  
  double *res;

  *sizeOut=6;

  res =(double *)calloc(sizeOut,sizeof(double));
  
  if (res==NULL){

    printf ("res is null\n");

    return;
  }

  res[0]= dF1bifasicodSg (in[0],in[1]);
  res[1]= dF2bifasicodSg  (in[0],in[1]);
  res[2]= dF3bifasicodSg  (in[0],in[1]);
  res[3]= dF1bifasicodT  (in[0],in[1]);
  res[4]= dF2bifasicodT  (in[0],in[1]);
  res[5]= dF3bifasicodT  (in[0],in[1]);
  

  // showMemStats();  

  return res;

}


double * d2f (double * in,int sizeIn,int * sizeOut,double *param){


  double *res;

  *sizeOut=9;

  res =(double *)calloc(sizeOut,sizeof(double));
  
  if (res==NULL){

    printf ("res is null\n");

    return;
  }

  res[0]= dF1bifasicodSgdSg(in[0],in[1]);
  res[1]= dF2bifasicodSgdSg(in[0],in[1]);
  res[2]= dF3bifasicodSgdSg(in[0],in[1]);

  res[3]= dF1bifasicodTdT(in[0],in[1]);
  res[4]= dF2bifasicodTdT(in[0],in[1]);
  res[5]= dF3bifasicodTdT(in[0],in[1]);

  res[6]= dF1bifasicodTdSg(in[0],in[1]);
  res[7]= dF2bifasicodTdSg(in[0],in[1]);
  res[8]= dF3bifasicodTdSg(in[0],in[1]);

  // showMemStats();  

  return res;
  

}
