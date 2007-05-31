#include <math.h>
#include <stdlib.h>
#include "mcd.h"


 /*    Matrizes usadas nos parametros de fluxo */

  double A [2]  = { 0, 0 };

  double  B [] [2]   = { { 0, 0.1 }, { -0.1, 0 }};

  double  C [] [2] [2]  =  { { { -1, 0 }, { 0, 1 }}, { { 0, 1 }, { 1, 0 }}};

  /* ------------------------------------------- */

/* Definicao do espaco */

int spaceDim=2;

double *  f(double * in,int sizeIn,int * sizeOut,double * param){

  *sizeOut=spaceDim;

  int i,j,k;

  double *userOut,*q,*res;

  res =(double *)calloc(spaceDim,sizeof(double));

  q =(double *)calloc(spaceDim,sizeof(double));

  if ((q==NULL)||(res==NULL)){

    printf ("q ou res sao nulos em f\n");

    return;
  }

  for ( i = 0; i < spaceDim; i++)
    q[i] = in[i];
  for ( i = 0; i < spaceDim; i++) {
    res[i] = A[i];
    for ( j = 0; j < spaceDim; j++) {
      res[i] = res[i] + B[i] [j] * q[j];
      for ( k = 0; k < spaceDim; k++)
	res[i] = res[i] + 0.5 * C[i] [j] [k] * q[j] * q[k];
    }
  }

  free(q);

  return res;

}
double * df(double * in,int sizeIn,int * sizeOut ,double * param){


  int i,j,k;

  *sizeOut=(spaceDim*spaceDim);

  double *userOut,*q,*res;

  res =(double *)calloc(spaceDim*spaceDim,sizeof(double));

  q =(double *)calloc(spaceDim,sizeof(double));

  if ((q==NULL)||(res==NULL)){

    printf ("q ou res sao nulos em f\n");

    return;
  }

  for ( i = 0; i < spaceDim; i++){
    q[i] = in[i];
  }

  for ( i = 0; i < spaceDim; i++)
    for ( j = 0; j < spaceDim; j++) {
      res[i*spaceDim+j] = B[i] [j];
      for ( k = 0; k < spaceDim; k++)
	res[i*spaceDim+j] = res[i*spaceDim+j] + C[i] [j] [k] * q[k];
    }


  free(q);

  // showMemStats();

  return res;
}


double * d2f (double * in,int sizeIn,int * sizeOut,double *param){

  int i,j,k,index;

  double *res;

  res =(double *)calloc(spaceDim*spaceDim*spaceDim,sizeof(double));

  *sizeOut=(spaceDim*spaceDim*spaceDim);

  for ( i = 0; i < spaceDim; i++){
    for ( j = 0; j < spaceDim; j++){
      for ( k = 0; k < spaceDim; k++){

	index= (int)(((pow((double)spaceDim,(double)2))*i)+spaceDim*j+k);

	res[index]=param[spaceDim + spaceDim * spaceDim + i * spaceDim * spaceDim + j * spaceDim + k];

      }
    }
  }

  // showMemStats();

  return res;


}
