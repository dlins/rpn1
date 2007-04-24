#include <math.h>
#include <stdlib.h>
#include "functionsFlux.h"
#include "mcd.h"

double *  flowNative (double * in,int sizeIn,int * sizeOut,double *phasePoint,double sigma,double *fParams){

  int i,dim,sizeFX,sizefx0;

  double *FX,*fx0,*xMinus;

  dim=sizeIn;

  *sizeOut=sizeIn;

  if (phasePoint==NULL)

    printf ("phasePoint eh nulo\n");

  if (fParams==NULL)

    printf ("fParams eh nulo\n");

  FX=f(in,dim,&sizeFX,fParams);

  fx0=f(phasePoint,dim,&sizefx0,fParams);

  for (i = 0; i < dim;i++){

    FX[i]-=fx0[i];

  }

  xMinus=(double *)calloc(dim,sizeof(double));

  if (xMinus==NULL){

    printf ("xMinus eh nullo em fluxNative\n");

    return;
  }


  for (i = 0; i < dim;i++){

    xMinus[i]=in[i];

  }


  for (i = 0; i < dim;i++){

    xMinus[i]-=phasePoint[i];

  }

  for (i = 0; i < dim;i++){

    xMinus[i]*=sigma;

  }

  for (i = 0; i < dim;i++){
    FX[i]-=xMinus[i];
  }

  if (fx0==NULL)

    printf ("fx0 eh nulo\n");

  if (xMinus==NULL)

    printf ("xMinus eh nulo\n");

  free(fx0);

  free(xMinus);

  //  showMemStats();

  return FX;

}

double * flowDerivNative (double * in,int sizeIn,int * sizeOut,double *phasePoint,double sigma,double *fParams){

  int i,j,ij,sizefluxDF;

  double *fluxDF,*identity;

  fluxDF=df(in,sizeIn,&sizefluxDF,fParams);

  *sizeOut=sizefluxDF;

  if (fluxDF==NULL){

    printf ("fluxDF eh nulo\n");

    return;
  }


  identity=(double *)calloc(*sizeOut,sizeof(double));

  if (identity==NULL){

    printf ("identity eh nulo em fluxDerivNative\n");

    return;
  }

  int I= pow ((double)sizefluxDF,(double)1/2);


  for (i = 0; i < I;i++){
    for (j = 0; j < I;j++){
      if (i==j)
	identity[i*I+j]=1;
      else
	identity[i*I+j]=0;

    }
  }


  for (i = 0; i < sizefluxDF;i++){

    identity[i]*=sigma;


  }


  for (i = 0; i < sizefluxDF;i++){

    fluxDF[i]-=identity[i];

  }


  free(identity);

  //  showMemStats();


  return fluxDF;
}


double * flowDeriv2Native (double * in,int sizeIn,int * sizeOut,double *phasePoint,double sigma,double *fParams){
  int sizeFlux;

  *sizeOut=sizeIn*sizeIn*sizeIn;
  //  showMemStats();
  return d2f(in,sizeIn,&sizeFlux,fParams);


}
