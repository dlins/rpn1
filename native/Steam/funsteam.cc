//#include<iostream.h>
#include<math.h>
#include "constantsn.h"



//---------------------------------------------------------------------------------
//---------------------------------------------------------------------------------
// Este contem todas as funcoes que serao utilizadas no sistema.
//---------------------------------------------------------------------------------
//---------------------------------------------------------------------------------
//----------------------------------------------------------------------------------
//----------------------------------------------------------------------------------
//Declaracao das funcoes:
// 1) vahw(T) = Entalpia por unidade de massa; funcao de T.
// 2) vahwlinha(T) = Derivada da entalpia por unidade de massa; Sera usada para encontrar
//                raizes pelo metodo de Newton.
// 3) Hr(T) = Entalpia da rocha. Linear como funcao de T.
// 4) Hw(T) = Entalpia do liquido, agua. 
// 5) Hwlinha(T) = Derivada da entalpia da agua. Sera usada para encontrar as raizes no metodo
//                 de Newton.
// 6) funcao(T,cons) = Funcao a composicao da conservacao de massa e de energia
//                     na regiao fria. Tera as raizes encontradas.
// 7) funcalinha(T) = Derivada de (6). Usada no metodo de Newton.
// 8) vahg(T) = Entalpia por unidade de massa; funcao de T.
// 9) vahglinha(T) = Derivada da entalpia por unidade de massa; Sera usada para encontrar
//                raizes pelo metodo de Newton.
// 10) rhog(T) = Densidade do gas como funcao da temperatura.
// 11) rhoglinha(T)= Derivada da densidade do gas; sera usada para encontrar as raizes do
//                  metodo de Newton.
// 12) Hr(T) = Entalpia da rocha. Linear como funcao de T.
// 13) Hg(T) = Entalpia do gas. 
// 14) Hglinha(T) = Derivada da entalpia do gas. Sera usada para encontrar as raizes no metodo
//                 de Newton.

//----------------------------------------------------------------------------------------
//---------------------------------------------------------------------------------------
//Usado tanto na regiao com nitrogenio quanto na regiao sem nitrogenio.
//---------------------------------------------------------------------------------------
//---------------------------------------------------------------------------------------


double vahw(double T) {
  return (CW*T)/pw;
}

double vahwlinha(double T){
  return CW/pw;
}

double Hr(double T){
  return Cr*(T-293);
}

double Hw(double T){
  return CW*(T-293);
}

double Hwlinha(double T){
  return CW;
}

double rhog(double T){
  return (pressaoatmosferica*massaH20)/(R*T);}


double krg(double sg){ 


  if ((sg<=(0.85))&(sg>=0.15)){
    return (0.95*pow( ((sg-0.15)/(0.7)), (double)2));
  }
  if (sg>0.85){
    return (double)1;
  }

  if (sg<0.15){
    return 0;

  }

}

double dkrg(double sg){ 
  if ((sg>=(0.15))&(sg<=0.85)){
    return (1.9*(-0.15+sg)/(0.49));
  }
  else{

    return 0;
    //    krg=0;
  }

  //  return dkrg;

}

double d2krg(double sg){ 
  if ((sg>=(0.15))&(sg<=0.85)){

    return (1.9/(0.49));

    //    krg=
  }
  else{
    return 0;  

    //  krg=0;
  }

  //  return dkrg;

}



double krw(double sg){ 
  if (sg<=0.85){
    return (0.5*pow( ((0.85-sg)/(0.7)), 2));
    //    krw=
  }
  else{
    //	krw=0;

    return 0;
  }

  //      return krw;
}

double dkrw(double sg){ 
  if (sg<=0.85){
    //      dkrw=


    return (-((0.85-sg)/(0.49)));
  }
  else{

    return 0;
    //	  dkrw=0;
  }

  //	return dkrw;

}


double d2krw(double sg){ 
  if (sg<=0.85){

    return ((1)/(0.49));

    //	dkrw=
  }
  else{

    return 0;
    //	dkrw=0;
  }

  //      return dkrw;
}


double vahg(double T){
  return  -22026900 +(365317+(-2258.37+(7.3742+(-0.0133437 +(0.0000126913-0.000000004968810*T)*T)*T)*T)*T)*T-CW*293/pw;
}
  
	
double vahglinha(double T){
  return  365317+(-2*2258.37+(3*7.3742+(-4*0.0133437 +(5*0.0000126913-6*0.000000004968810*T)*T)*T)*T)*T;
}

double rhoglinha(double T){
  return -(pressaoatmosferica*massaH20)/((R*T)*T);
}
double Hg(double T){
  return rhog(T)*(vahg(T));
}

double Hglinha(double T){
  return  rhog(T)*vahglinha(T)+rhoglinha(T)*vahg(T);
}


         
      

//--------------------------------------------------------
//-------------------------------------------------------
//Funcoes vindas do esquema com nitrogenio.
//-------------------------------------------------------
//------------------------------------------------------

double hw(double T) {
  return (CW*(T-293))/pw;
}

double psat(double T){
  double aux=(-175.776+(2.29272+(-0.0113953+((2.6278e-5)+((-2.73726e-8)+(1.13816e-11)*T)*T)*T)*T)*T);  
  return 1000*aux*aux;
}

double dpsatdT(double T){
  double aux1=-175.776+(2.29272+(-0.0113953+((2.6278e-5)+((-2.73726e-8)+(1.13816e-11)*T)*T)*T)*T)*T;  
  double aux2=(2.29272+(-2*0.0113953+(3*(2.6278e-5)+((-4*2.73726e-8)+(5*1.13816e-11)*T)*T)*T)*T);
  return 2000*aux1*aux2 ;
}

double d2psatdT(double T){
  double aux1=-175.776+(2.29272+(-0.0113953+((2.6278e-5)+((-2.73726e-8)+(1.13816e-11)*T)*T)*T)*T)*T;  
  double aux2=(2.29272+(-2*0.0113953+(3*(2.6278e-5)+((-4*2.73726e-8)+(5*1.13816e-11)*T)*T)*T)*T);
  double aux3= 2.29272+(-2*0.0113953+(6*(2.6278e-5)+((-12*2.73726e-8)+(20*1.13816e-11)*T)*T)*T);
  return (double)(2000*(aux1*aux3+aux2*aux2));
}


double mugn(double T){
  return ((1.8264e-5)*pow((T/Tb),(double)0.6));

  // return (double)1;
}

double dmugndT(double T){
  return (0.6)*1.8264e-5*pow(T,-0.4)*pow(1/Tb,0.6);
}

double dmugndT2(double T){
  return -(0.6)*(0.4)*1.8264e-5*pow(T,-1.4)*pow(1/Tb,0.6);
}

double muw(double T){
  return -0.0123274+(27.1038 +(-23527.5+(10142500+((-217342e+4)+(186935e+6)/T)/T)/T)/T)/T;}

double dmuwdT(double T){
  return -((27.1038 +(-2*23527.5+(3*10142500+((-4*217342e+4)+(5*186935e+6)/T)/T)/T)/T)/T)/T;}

double dmuwdT2(double T){
  return (((2*27.1038 +(-6*23527.5+(12*10142500+((-20*217342e+4)+(30*186935e+6)/T)/T)/T)/T)/T)/T)/T;}


double lambdawnitro(double sg, double T){
  return kapa*krw(sg)/muw(T);}

double dlambdawnitrodSg(double sg, double T){
  return kapa*dkrw(sg)/muw(T);}

double dlambdawnitrodT(double sg, double T){
 
  double mw=muw(T);

  return -(kapa*krw(sg)/mw)*(dmuwdT(T)/mw);}

double d2lambdawnitrodSgdT(double sg, double T){
 
  double mw=muw(T);

  return -(kapa*dkrw(sg)/mw)*(dmuwdT(T)/mw);}


double d2lambdawnitrodTdT(double sg, double T){
 
  double mw=muw(T);

  return (kapa*krw(sg)/mw)*( (2*dmuwdT(T)-dmuwdT2(T)*mw)/mw);}

double d2lambdawnitrodSgdSg(double sg, double T){
  return kapa*d2krw(sg)/muw(T);}



double lambdagnitro(double sg, double T){
  return (kapa*krg(sg)/mugn(T));
}


double dlambdagnitrodSg(double sg, double T){
  return kapa*dkrg(sg)/mugn(T);}

double dlambdagnitrodT(double sg, double T){
 
  double mg=mugn(T);

  return -(kapa*krg(sg)/mg)*(dmugndT(T)/mg);}

double d2lambdagnitrodSgdT(double sg, double T){
 
  double mg=mugn(T);

  return -(kapa*dkrg(sg)/mg)*(dmugndT(T)/mg);}


double d2lambdagnitrodTdT(double sg, double T){
 
  double mg=mugn(T);

  return (kapa*krg(sg)/mg)*( (2*dmugndT(T)-dmugndT2(T)*mg)/mg);}

double d2lambdagnitrodSgdSg(double sg, double T){
  return kapa*d2krg(sg)/mugn(T);}


double fgnitro(double sg, double T){  
  return (lambdagnitro(sg,T))/(lambdagnitro(sg,T)+lambdawnitro(sg,T));}

double dfgnitrodT(double sg, double T){ 
  double lg=lambdagnitro(sg,T);
  double lw=lambdawnitro(sg,T);
  double mw=muw(T);
  double dmw=dmuwdT(T);
  double mg=mugn(T);
  double dmg=dmugndT(T);


  return (lg*lw*(dmw/mw-dmg/mg))/(pow(lg+lw,2));}

       
double dfgnitrodSg(double sg, double T){
  double lg=lambdagnitro(sg,T);
  double lw=lambdawnitro(sg,T);
  double mw=muw(T);
  double mg=mugn(T);
  double dkg=dkrg(sg);
  double dkw=dkrw(sg);

  return ((dkg/mg)*lw-(dkw/mw)*lg)/(pow(lg+lw,2));
}


double d2fgnitrodSgdSg(double sg, double T){
  double lg=lambdagnitro(sg,T);
  double lw=lambdawnitro(sg,T);
  double lgss=d2lambdagnitrodSgdSg(sg,T);
  double lgs=dlambdagnitrodSg(sg,T);
  double lwss=d2lambdawnitrodSgdSg(sg,T);
  double lws=dlambdawnitrodSg(sg,T);




  return ( (lgss*lw-lwss*lg)*(lw+lg)-2*(lgs+lws)*(lgs*lw-lws*lg))/(pow(lg+lw,3));
}

double d2fgnitrodTdT(double sg, double T){
  double lg=lambdagnitro(sg,T);
  double lw=lambdawnitro(sg,T);
  double lgt=dlambdagnitrodT(sg,T);
  double lgtt=d2lambdagnitrodTdT(sg,T);
  double lwt=dlambdawnitrodT(sg,T);
  double lwtt=d2lambdawnitrodTdT(sg,T);


  return ( (lgtt*lw-lwtt*lg)*(lw+lg)-2*(lgt+lwt)*(lgt*lw-lwt*lg))/(pow(lg+lw,3));
}

double d2fgnitrodTdSg(double sg, double T){
  double lg=lambdagnitro(sg,T);
  double lw=lambdawnitro(sg,T);
  double lgst=d2lambdagnitrodSgdT(sg,T);
  double lgs=dlambdagnitrodSg(sg,T);
  double lgt=dlambdagnitrodT(sg,T);
  double lwst=d2lambdagnitrodSgdT(sg, T);
  double lws=dlambdawnitrodSg(sg,T);
  double lwt=dlambdawnitrodT(sg,T);


  return ( (lgst*lw+lgs*lwt-lwst*lg-lgs*lgt)*(lw+lg)-2*(lgt+lwt)*(lgs*lw-lws*lg))/(pow(lg+lw,3));
}
double fwnitro(double sg, double T){
  return 1-fgnitro(sg,T);
}

double rhogW(double T){
  return thetaW/T;
}

double drhogW(double T){
  return -thetaW/(T*T);
}

double d2rhogW(double T){
  return 2*thetaW/(T*T*T);
}
        
double rhogN(double T){
  return thetaN/T;
}

double drhogN(double T){
  return -thetaN/(T*T);
}



double d2rhogN(double T){
  return 2*thetaN/(T*T*T);
}

double rhogw(double T){
     
  return (psat(T)/T)*massaH20;}

double drhogw(double T){   
  return massaH20*(dpsatdT(T)/T-psat(T)/(T*T));
}

double d2rhogw(double T){
  return massaH20*(d2psatdT(T)/T-2*dpsatdT(T)/(T*T)+2*psat(T)/(T*T*T));
}


double rhogn(double T){
  return ((pressaoatmosferica-psat(T))/T)*massaN2;
}

double drhogn(double T){
  double aux1=T*T;
  return (-(pressaoatmosferica)/(aux1)-(dpsatdT(T)/T-psat(T)/(T*T)))*massaN2;
}

double d2rhogn(double T){
  double aux1=T*T;
  return (2*(pressaoatmosferica)/(aux1*T)-( d2psatdT(T)/T-2*dpsatdT(T)/(T*T)+2*psat(T)/(T*T*T) ) )*massaN2;
}


double vahgN(double T){
  double auxhgn=975+(0.0935+((-0.476e-7)*293)*293)*293;
  return (975+(0.0935+((-0.476e-7)*T)*T)*T)-auxhgn;}

double dvahgN(double T){
  return 975+(2*0.0935+(3*(-0.476e-7)*T)*T);
}

double d2vahgN(double T){
  return 2*0.0935+(6*(-0.476e-7)*T);
}

double vahgW(double T){
  return  -22026900 +(365317+(-2258.37+(7.3742+(-0.0133437 +(0.0000126913-0.000000004968810*T)*T)*T)*T)*T)*T-CW*293/pw;
}
double dvahgW(double T){
  return  365317+(-2*2258.37+(3*7.3742+(-4*0.0133437 +(5*0.0000126913-6*0.0000000049688*T)*T)*T)*T)*T;
}
double d2vahgW(double T){
  return  (-2*2258.37+(6*7.3742+(-12*0.0133437 +(20*0.0000126913-30*0.0000000049688*T)*T)*T)*T);
}

double Tempebulicao(double composition){
  double sigma=log((composition*pressaoatmosferica)/1000);
  return 280.034+sigma*(14.0856+sigma*(1.38075+sigma*(-0.101806+0.019017*sigma)));
}
//------------------------------------------------
//-----------------------------------------------
//Caso com nitrogenio regiao bifasica.
//----------------------------------------------
//--------------------------------------------------

double Hgnitrobi(double T){
  return rhogw(T)*vahg(T)+rhogn(T)*vahgN(T);
}

double dHgnitrobi(double T){
  return drhogw(T)*(vahg(T))+rhogw(T)*vahglinha(T)+drhogn(T)*vahgN(T)+rhogn(T)*dvahgN(T);
}

double d2Hgnitrobi(double T){
  return drhogw(T)*(vahg(T))+rhogw(T)*vahglinha(T)+drhogn(T)*vahgN(T)+rhogn(T)*dvahgN(T);
}


double compositionnitrobiw(double T){
  return psat(T)/pressaoatmosferica;
}
                                                




//------------------------------------------------------
//-----------------------------------------------------
//Regiao  com nitrogenio gasosa.
//-----------------------------------------------------
//-----------------------------------------------------

double HgWgas(double T){
  return rhogW(T)*vahgW(T);
}


double dHgWgas(double T){
  return drhogW(T)*vahgW(T)+rhogW(T)*dvahgW(T);
}

double HgNgas(double T){
  return rhogN(T)*vahgN(T);
}





double dHgNgas(double T){
  return drhogN(T)*vahgN(T)+dvahgN(T)*rhogN(T);
}


/*------------------------------------------
  Funcoes de acumulacao (G) para a regiao bifasica.
  -------------------------------------------*/

double G1bifasico(double Sg,double T){
  return pw*(1-Sg) + rhogw(T)*Sg;
}

double G2bifasico(double Sg, double T){
  return rhogn(T)*Sg;
}

double G3bifasico(double Sg, double T){
  return Hr(T)+Hw(T)*(1-Sg)+Hgnitrobi(T)*Sg;
}

/*----------------------------------------------
  Funcoes da derivada da acumulacao.
  -----------------------------------------------*/

/*Derivadas primeira em relacao a Sg*/

double dG1bifasicodSg(double Sg,double T){
  return  rhogw(T)-pw;
}

double dG2bifasicodSg(double Sg, double T){
  return rhogn(T);
}

double dG3bifasicodSg(double Sg, double T){
  return -Hw(T)+Hgnitrobi(T);

}
/*Derivadas primeira em relacao a T*/


double dG1bifasicodT(double Sg,double T){
  return  drhogw(T)*Sg;
}

double dG2bifasicodT(double Sg, double T){
  return drhogn(T)*Sg;
}

double dG3bifasicodT(double Sg, double T){
  return Cr+CW*(1-Sg)+dHgnitrobi(T)*Sg;
}

/*Derivadas segundas em relacao a Sg.*/


double dG1bifasicodSgdSg(double Sg,double T){
  return  0;
}

double dG2bifasicodSgdSg(double Sg, double T){
  return 0;
}

double dG3bifasicodSgdSg(double Sg, double T){
  return 0;
}

/*Derivadas segundas  em relacao a T*/


double dG1bifasicodTdT(double Sg,double T){
  return  d2rhogw(T)*Sg;
}

double dG2bifasicodTdT(double Sg, double T){
  return d2rhogn(T)*Sg;
}

double dG3bifasicodTdT(double Sg, double T){
  return d2Hgnitrobi(T)*Sg;
}


/*Derivadas segundas  em relacao a T and Sg*/


double dG1bifasicodTdSg(double Sg,double T){
  return  drhogw(T);
}

double dG2bifasicodTdSg(double Sg, double T){
  return drhogn(T);
}

double dG3bifasicodTdSg(double Sg, double T){
  return -CW+dHgnitrobi(T);
}

/*-------------------------------------------
  Funcoes de fluxo (F) para  a regiao bifasica.
  ---------------------------------------------*/

double F1bifasico(double Sg, double T){
  return pw*fwnitro(Sg,T)+rhogw(T)*fgnitro(Sg,T);
}

double F2bifasico(double Sg, double T){
  return rhogn(T)*fgnitro(Sg,T);
}

double F3bifasico(double Sg, double T){
  return Hw(T)*fwnitro(Sg,T)+Hgnitrobi(T)*fgnitro(Sg,T);
}

/*---------------------------------------------
  Funcoes da derivada do fluxo
  ----------------------------------------------*/

/*Derivadas primeira em relacao a Sg.*/

double dF1bifasicodSg(double Sg, double T){
  return (rhogw(T)-pw)*dfgnitrodSg( Sg,  T);
}

double dF2bifasicodSg(double Sg, double T){
  return rhogn(T)*dfgnitrodSg( Sg,  T);
} 

double dF3bifasicodSg(double Sg, double T){
  return (Hgnitrobi(T)-Hw(T))*dfgnitrodSg( Sg, T);
}

/*Derivadas primeira em relacao a T.*/

double dF1bifasicodT(double Sg, double T){
  return dfgnitrodT(Sg, T)*(rhogw(T)-pw)+fgnitro(Sg,T)*drhogw(T);
}

double dF2bifasicodT(double Sg, double T){
  return drhogn(T)*fgnitro(Sg,T)+dfgnitrodT(Sg,T)*rhogn(T);
}

double dF3bifasicodT(double Sg, double T){
  return (Hgnitrobi(T)-Hw(T))*dfgnitrodT(Sg,T)+(dHgnitrobi(T)-CW)*fgnitro(Sg,T)+CW;
}


/*Derivadas segunda em relacao a Sg.*/

double dF1bifasicodSgdSg(double Sg, double T){
  return (rhogw(T)-pw)*d2fgnitrodSgdSg( Sg,  T);
}

double dF2bifasicodSgdSg(double Sg, double T){
  return rhogn(T)*d2fgnitrodSgdSg( Sg,  T);
} 

double dF3bifasicodSgdSg(double Sg, double T){
  return (Hgnitrobi(T)-Hw(T))*d2fgnitrodSgdSg( Sg,  T);
}
/*Derivadas segundas em relacao a T.*/

double dF1bifasicodTdT(double Sg, double T){
  return d2fgnitrodTdT( Sg,  T)*(rhogw(T)-pw)+2*dfgnitrodT(Sg,T)*drhogw(T)+fgnitro(Sg,T)*d2rhogw(T);
}

double dF2bifasicodTdT(double Sg, double T){
  return d2rhogn(T)*fgnitro(Sg,T)+d2fgnitrodTdT(Sg,T)*rhogn(T)+ 2*drhogn(T)*dfgnitrodT(Sg,T);
}

double dF3bifasicodTdT(double Sg, double T){
  return (Hgnitrobi(T)-Hw(T))*d2fgnitrodTdT(Sg,T)+2*(dHgnitrobi(T)-CW)*dfgnitrodT(Sg,T)+d2Hgnitrobi(T)*fgnitro(Sg,T);
}

/*Derivadas segundas ordem  em relacao a T e Sg.*/

double dF1bifasicodTdSg(double Sg, double T){
  return d2fgnitrodTdSg(Sg,  T)*(rhogw(T)-pw)+dfgnitrodSg(Sg,T)*drhogw(T);
}

double dF2bifasicodTdSg(double Sg, double T){
  return drhogn(T)*dfgnitrodSg(Sg,T)+d2fgnitrodTdSg(Sg,T)*rhogn(T);
}

double dF3bifasicodTdSg(double Sg, double T){
  return (Hgnitrobi(T)-Hw(T))*d2fgnitrodTdSg(Sg,T)+(dHgnitrobi(T)-CW)*dfgnitrodSg(Sg,T);
}



 double Hugoniotimpl (double Tmenos, double Sgmenos, double Tmais, double Sgmais){

 double G1=G1bifasico(Sgmais,Tmais)-G1bifasico(Sgmenos,Tmenos);
 double G2=G2bifasico(Sgmais,Tmais)-G2bifasico(Sgmenos,Tmenos);
 double G3=G3bifasico(Sgmais,Tmais)-G3bifasico(Sgmenos,Tmenos);
 
 double F1menos=F1bifasico(Sgmenos, Tmenos);
 double F2menos=F2bifasico(Sgmenos, Tmenos);
 double F3menos=F3bifasico(Sgmenos, Tmenos);

 double F1mais=F1bifasico(Sgmais, Tmais);
 double F2mais=F2bifasico(Sgmais, Tmais);
 double F3mais=F3bifasico(Sgmais, Tmais);
 
return  G1*(F3mais*F2menos-F2mais*F3menos)+G2*(F1mais*F3menos-F1menos*F3mais)+G3*(F2mais*F1menos-F1mais*F2menos);  

}


double dHugoniotimpldT (double Tmenos, double Sgmenos, double Tmais, double Sgmais){

 double G1mais=G1bifasico(Sgmais,Tmais);
 double G1menos=G1bifasico(Sgmenos,Tmenos);
 double G2mais=G2bifasico(Sgmais,Tmais);
 double G2menos=G2bifasico(Sgmenos,Tmenos);
 double G3mais=G3bifasico(Sgmais,Tmais);
 double G3menos=G3bifasico(Sgmenos,Tmenos);
 
 double dG1dT=dG1bifasicodT(Sgmais,Tmais);
 double dG2dT=dG2bifasicodT(Sgmais,Tmais);
 double dG3dT=dG3bifasicodT(Sgmais,Tmais);

 double F1menos=F1bifasico(Sgmenos,Tmenos);
 double F2menos=F2bifasico(Sgmenos,Tmenos);
 double F3menos=F3bifasico(Sgmenos,Tmenos);

 double F1mais=F1bifasico(Sgmais,Tmais);
 double F2mais=F2bifasico(Sgmais,Tmais);
 double F3mais=F3bifasico(Sgmais,Tmais);

 double dF1dT=dF1bifasicodT(Sgmais,Tmais);
 double dF2dT=dF2bifasicodT(Sgmais,Tmais);
 double dF3dT=dF3bifasicodT(Sgmais,Tmais);

  
 
return  dG1dT*(F3mais*F2menos-F2mais*F3menos)+dG2dT*(F1mais*F3menos-F1menos*F3mais)+dG3dT*(F2mais*F1menos-F1mais*F2menos)+(G1mais-G1menos)*(dF3dT*F2menos-dF2dT*F3menos)+(G2mais-G2menos)*(dF1dT*F3menos-F1menos*dF3dT)+(G3mais-G3menos)*(dF2dT*F1menos-dF1dT*F2menos);  

}

double dHugoniotimpldSg  (double Tmenos, double Sgmenos, double Tmais, double Sgmais){

 double G1mais=G1bifasico(Sgmais,Tmais);
 double G1menos=G1bifasico(Sgmenos,Tmenos);
 double G2mais=G2bifasico(Sgmais,Tmais);
 double G2menos=G2bifasico(Sgmenos,Tmenos);
 double G3mais=G3bifasico(Sgmais,Tmais);
 double G3menos=G3bifasico(Sgmenos,Tmenos);
 
 double dG1dSg=dG1bifasicodSg(Sgmais,Tmais);
 double dG2dSg=dG2bifasicodSg(Sgmais,Tmais);
 double dG3dSg=dG3bifasicodSg(Sgmais,Tmais);

 double F1menos=F1bifasico(Sgmenos,Tmenos);
 double F2menos=F2bifasico(Sgmenos,Tmenos);
 double F3menos=F3bifasico(Sgmenos,Tmenos);

 double F1mais=F1bifasico(Sgmais,Tmais);
 double F2mais=F2bifasico(Sgmais,Tmais);
 double F3mais=F3bifasico(Sgmais,Tmais);

 double dF1dSg=dF1bifasicodSg(Sgmais,Tmais);
 double dF2dSg=dF2bifasicodSg(Sgmais,Tmais);
 double dF3dSg=dF3bifasicodSg(Sgmais,Tmais);

  
 
return  dG1dSg*(F3mais*F2menos-F2mais*F3menos)+dG2dSg*(F1mais*F3menos-F1menos*F3mais)+dG3dSg*(F2mais*F1menos-F1mais*F2menos)+(G1mais-G1menos)*(dF3dSg*F2menos-dF2dSg*F3menos)+(G2mais-G2menos)*(dF1dSg*F3menos-F1menos*dF3dSg)+(G3mais-G3menos)*(dF2dSg*F1menos-dF1dSg*F2menos);  

}
