#include <math.h>
#include <stdlib.h>
#include "functionsFlux.h"
#include "mcd.h"
#include <jni.h>

#include "physics.h"



//    double *  value  (double * in,int sizeIn,int * sizeOut,double *phasePoint,double *fParams){

//    double  phasePoint [2] = {0.14,0.07};


//    extern jdouble  *phasePoint;


//    extern jdouble * phasePoint ;

double fParams [2] = {1,1};

double *  value  (double * in,int sizeIn,int * sizeOut,double * pPoint){

  int i,dim,sizeDeltaF,sizeDeltaU,sizefMinus,sizeuMinus;

  double *deltaF,*deltaU,*uMinus,*fMinus,*result;




//printf ("Chamando value\n");


  dim=sizeIn;

  *sizeOut=dim-1;

//  uMinus=phasePoint;
double *phasePoint=pPoint;
uMinus=pPoint;

//  printf ("Valor de uMinus[0]: %f",uMinus[0]);

//  printf ("Valor de uMinus[1]: %f",uMinus[1]);



   deltaF= f(in,sizeIn,&sizeDeltaF,fParams);

  fMinus=f(uMinus,sizeIn,&sizefMinus,fParams);

  deltaU=(double *)calloc(dim,sizeof(double));

  if (deltaU==NULL){

    printf ("deltaU eh nulo em value\n");

    return;

  }

  for (i = 0; i < dim;i++){

    deltaU[i]= in[i];

  }


 for (i = 0; i < dim;i++){

   deltaU[i]= deltaU[i]-phasePoint[i];

  }

  for (i = 0; i < sizeDeltaF;i++){

    deltaF[i]= deltaF[i]-fMinus[i];
  }



   /*------------- Criando  o array de saida-------------- */

   result=(double *)calloc((dim-1),sizeof(double));

   if (result==NULL){

     printf ("result eh nulo em value\n");

     return;
   }


   for (i=0;i < dim -1;i++){


     result[i]=deltaF[i]*deltaU[dim-1]-deltaF[dim-1]*deltaU[i];


   }


   /*----------------------------------------------------*/


   free(deltaU);

   free(deltaF);

   free(fMinus);

   // showMemStats();

   return result;


}
// double * derive  (double * in,int sizeIn,int * sizeOut,double *phasePoint,double *fParams){

 double * derive  (double * in,int sizeIn,int * sizeOut,double *pPoint){

  int i,j,ij,dim,sizeDeltaF,sizeDeltaU,sizefMinus,sizeuMinus,sizeDF;

  double *deltaF,*deltaU,*uMinus,*fMinus,*DF,*DU,*result;

//  uMinus=phasePoint;

  uMinus=pPoint;

  dim = sizeIn;

  *sizeOut=dim*(dim-1);

  deltaU=(double *)calloc(dim,sizeof(double));

  if (deltaU==NULL){

    printf ("deltaU eh nulo em derive\n");

    return;
  }

  for (i = 0; i < dim;i++){

    deltaU[i]= in[i];

  }

  for (i = 0; i < dim;i++){

    deltaU[i]= deltaU[i]-uMinus[i];

  }

  deltaF= f(in,sizeIn,&sizeDeltaF,fParams);
  fMinus=f(uMinus,sizeIn,&sizefMinus,fParams);

  for (i = 0; i < dim;i++){

    deltaF[i]= deltaF[i]- fMinus[i];

  }

  DF=df(in,dim,&sizeDF,fParams);

  DU=(double *)calloc(dim*dim,sizeof(double));


  if (DU==NULL){printf ("DU eh nulo em derive !\n");return;}


  for (i=0;i < dim;i++){

    for (j=0; j < dim;j++){
      ij=i*dim+j;
      if (i==j)
	DU[ij]=1;
      else
	DU[ij]=0;
    }

  }

  result=(double *)calloc(*sizeOut,sizeof(double));

  if (result==NULL){

    printf ("result eh nulo em derive\n");

    return;

  }

  for (i=0;i < dim-1;i++){

    for (j=0;j < dim;j++){

      ij=i*dim+j;
      if (i==j)
	result[ij]=1;
      else
	result[ij]=0;

    }
  }

  for ( i = 0; i < (dim - 1); i++){
    for ( j = 0; j < dim; j++){

      ij=i*dim+j;

      result[ij]=(DF[ij]*deltaU[dim-1])+(deltaF[i]*DU[(dim-1)*dim+j])
	-(DF[(dim-1)*dim+j]*deltaU[i])-(deltaF[dim-1]*DU[ij]);

    }
  }

  free (DU);

  free(deltaU);

  free(deltaF);

  free (DF);

  free(fMinus);

  //  showMemStats();

  return result;


}

