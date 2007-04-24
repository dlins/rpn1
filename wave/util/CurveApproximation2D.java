/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package wave.util;

import java.util.*;
import java.awt.geom.*;



public class CurveApproximation2D {

  private static final int ESQ = 0;
  private static final int DIR = 1;
  private static final int INF = 2;
  private static final int SUP = 3;
  private static final int DIAG = 4;

  private  int mh, mv;      // Numero de divisoes
  private double dx, dy;    // Variacao em cada divisao
  private double xmin, xmax, ymin, ymax;

  private double theta;     // Angulo entre a diagonal e a borda superior
  private double seno, cosseno;  // seno e cosseno do angulo anterior
  private double x, y;

  private int maxIterNewton; //Numero maximo de iteracoes do metodo de newton
  private double precNewton;    //Precisao da raiz encontrada pelo metodo de newton

  ApproxFunction ap;


  /**
  * Construtor.
  *      Recebe como parametro as variaveis necessarias
  *      para a execucao da aproximacao
  * @param curveInterface Interface que define a funcao a ser
  * aproximada, assim como suas derivadas parciais
  * @param xinicial Limite inferior no eixo x
  * @param xfinal Limite superior no eixo x
  * @param yinicial Limite inferior no eixo y
  * @param yfinal Limite superior no eixo y
  * @param numDivisoesx Determina o numero de divisoes no eixo x
  * @param numDivisoesy Determina o numero de divisoes no eixo y
  *
  */
  public CurveApproximation2D(ApproxFunction curveInterface, double xinicial, double xfinal,
                              double yinicial, double yfinal, int numDivisoesx, int numDivisoesy){

    maxIterNewton = 100;
    precNewton = 0.0001d;

    ap = curveInterface;


    if(numDivisoesx>0){
      mh = numDivisoesx;
    }

    if(numDivisoesy>0){
      mv = numDivisoesy;
    }

    xmin = xinicial;
    xmax = xfinal;
    ymin = yinicial;
    ymax = yfinal;

  }
  /**
   * Modifica valor inicializado no construtor
   * @param num Deve ser um inteiro maior que zero
   */
   public void setMaxIterNewton(int num){
    if(num>0)
      maxIterNewton = num;
   }

  /**
   * Modifica valor inicializado no construtor
   * @param num Deve ser um inteiro maior que zero
   */
   public void setNumDivisoesHorizontais(int num){
    if(num>0)
      mh = num;
  }

  /**
   * Modifica valor inicializado no construtor
   * @param num Deve ser um inteiro maior que zero
   */
  public void setNumDivisoesVerticais(int num){
    if(num>0)
      mv = num;
  }

  /**
  * Modifica valor inicializado no construtor
  * @param num Limite inferior no eixo x
  */
  public void setXmin(double num){
      xmin = num;
  }

  /**
  * Modifica valor inicializado no construtor
  * @param num Limite superior no eixo x
  */
  public void setXmax(double num){
      xmax = num;
  }

  /**
  * Modifica valor inicializado no construtor
  * @param num Limite inferior no eixo y
  */
   public void setYmin(double num){
      ymin = num;
  }

  /**
  * Modifica valor inicializado no construtor
  * @param num Limite superior no eixo y
  */
  public void setYmax(double num){
      ymax = num;
  }

  /**
  *  Funcao responsavel pela aproximacao triangular.
  *       Percorre o espaco definido pelas variaveis
  *       xmin, xmax, ymin, ymax
  *@return Vetor contendo os segmentos que
  *constituem a aproximacao da curva. Obs.: Os
  *segmentos nao estao ordenados.
  *
  */
  public Vector  doApproximation(){

    int i,j,k,ind,indAnt,size;
    double a,b,c,d;
    double alpha, delta, diag;
    double [][] result = new double[2][mh+1];

    Vector ptTriSup = new Vector();
    Vector ptTriInf = new Vector();
    Vector segmentos = new Vector();
    RealVector2 pEsq = null;

    dx = (xmax-xmin)/mh;
    dy = (ymax-ymin)/mv;

    theta = Math.atan(dy/dx);
    seno = Math.sin(theta);
    cosseno = Math.cos(theta);
    diag = dy / seno;

    x = xmin; // TODO watch for rounding problems...
    for(j=0;j<mh+1;j++){
        result[0][j] =  ap.function(x,ymin);
        x += dx;
    }

    //Percorre Todos os quadrados
    for(i=0;i<mv;i++){
      y = ymin + i*dy;

      indAnt = i%2;
      ind = (i+1) % 2;
      result[ind][0] = ap.function(xmin,y+dy);

      for(j=0;j<mh;j++){
        x = xmin + j*dx;

        result[ind][j+1] = ap.function(x+dx,y+dy);

        //Saida da funcao nas quatro extremidades
        a = result[indAnt][j];    //Ponto Inferior Esquerdo
        b = result[indAnt][j+1];  //Ponto Inferior Direito
        c = result[ind][j+1];     //Ponto Superior Direito
        d = result[ind][j];       //Ponto Superior Esquerdo

        if(j==0){ //Se estiver no primeiro quadrado da linha
          //Calcula intersecao na borda esquerda
          alpha = (a/(a-d));
          if((alpha <= 1.0d) && (alpha >= 0.0d)){
            delta = alpha * dy;
            ptTriInf.add(new RealVector2(x,y+delta));
          }
        }
        else{//Se nao estiver na primeira coluna
          //Calcula intersecao na borda esquerda
          //adicionando a borda direita do quadrado anterior
          if(pEsq != null){
            ptTriInf.add(new RealVector2(pEsq));
            pEsq = null;
          }
        }

        //Calcula intersecao na borda inferior
        alpha = (a/(a-b));
        if((alpha <= 1.0d) && (alpha >= 0.0d)){
          delta = alpha * dx;
          ptTriInf.add(new RealVector2(x+delta,y));
        }

        //Calcula intersecao na diagonal
        alpha = (d/(d-b));
        if((alpha <= 1.0d) && (alpha >= 0.0d)){
          delta = alpha * diag;
          ptTriInf.add(new RealVector2(x+delta*cosseno,y+dy-delta*seno));
          ptTriSup.add(new RealVector2(x+delta*cosseno,y+dy-delta*seno));
        }

        //Checa se existe um segmento a ser inserido
        size = ptTriInf.size();
        if(size >= 2){
          for(k=0;k<size-1;k++){
            RealVector2 p1 = (RealVector2) ptTriInf.elementAt(k);
            RealVector2 p2 = (RealVector2) ptTriInf.elementAt(k+1);
            segmentos.addElement(new RealSegment(p1,p2));
          }
        }
        ptTriInf.removeAllElements();

        //Calcula intersecao na borda direita
        alpha = (b/(b-c));
        if((alpha <= 1.0d) && (alpha >= 0.0d)){
          delta = alpha * dy;
          ptTriSup.add(new RealVector2(x+dx,y+delta));
          pEsq = new RealVector2(x+dx,y+delta);
        }
        else{
          pEsq = null;
        }

        //Calcula intersecao na borda superior
        alpha = (d/(d-c));
        if((alpha <= 1.0d) && (alpha >= 0.0d)){
          delta = alpha * dx;
          ptTriSup.add(new RealVector2(x+delta,y+dy));
        }

        //Checa se existe um segmento a ser inserido
        size = ptTriSup.size();
        if(size >= 2){
          for(k=0;k<size-1;k++){
            RealVector2 p1 = (RealVector2) ptTriSup.elementAt(k);
            RealVector2 p2 = (RealVector2) ptTriSup.elementAt(k+1);
            segmentos.addElement(new RealSegment(p1,p2));
          }
        }
        ptTriSup.removeAllElements();

      }
    }
    return segmentos;
  }


  /**
  *  Funcao responsavel pela aproximacao triangular.
  *       Percorre o espaco definido pelas variaveis
  *       xmin, xmax, ymin, ymax ate a diagonal que
  *       liga os pontos (xmax,ymin) (xmin,ymax)
  *@return Vetor contendo os segmentos que
  *constituem a aproximacao da curva. Obs.: Os
  *segmentos nao estao ordenados.
  */
  public Vector  doTriangularApproximation(){

    int i,j,k,ind,indAnt,size;
    double a,b,c,d;
    double alpha, delta, diag;
    double [][] result = new double[2][mh+1];

    Vector ptTriSup = new Vector();
    Vector ptTriInf = new Vector();
    Vector segmentos = new Vector();
    RealVector2 pEsq = null;

    dx = (xmax-xmin)/mh;
    dy = (ymax-ymin)/mv;

    theta = Math.atan(dy/dx);
    seno = Math.sin(theta);
    cosseno = Math.cos(theta);
    diag = dy / seno;

    x = xmin;
    for(j=0;j<mh+1;j++){
        result[0][j] =  ap.function(x,ymin);
        x += dx;
    }

    //Percorre Todos os quadrados
    for(i=0;i<mv;i++){
      y = ymin + i*dy;

      indAnt = i%2;
      ind = (i+1) % 2;
      result[ind][0] = ap.function(xmin,y+dy);

      for(j=0;j<(mh-i);j++){
        x = xmin + j*dx;

        result[ind][j+1] = ap.function(x+dx,y+dy);

        //Saida da funcao nas quatro extremidades
        a = result[indAnt][j];    //Ponto Inferior Esquerdo
        b = result[indAnt][j+1];  //Ponto Inferior Direito
        c = result[ind][j+1];     //Ponto Superior Direito
        d = result[ind][j];       //Ponto Superior Esquerdo

        if(j==0){ //Se estiver no primeiro quadrado da linha
          //Calcula intersecao na borda esquerda
          alpha = (a/(a-d));
          if((alpha <= 1.0d) && (alpha >= 0.0d)){
            delta = alpha * dy;
            ptTriInf.add(new RealVector2(x,y+delta));
          }
        }
        else{//Se nao estiver na primeira coluna
          //Calcula intersecao na borda esquerda
          //adicionando a borda direita do quadrado anterior
          if(pEsq != null){
            ptTriInf.add(new RealVector2(pEsq));
            pEsq = null;
          }
        }

        //Calcula intersecao na borda inferior
        alpha = (a/(a-b));
        if((alpha <= 1.0d) && (alpha >= 0.0d)){
          delta = alpha * dx;
          ptTriInf.add(new RealVector2(x+delta,y));
        }

        //Calcula intersecao na diagonal
        alpha = (d/(d-b));
        if((alpha <= 1.0d) && (alpha >= 0.0d)){
          delta = alpha * diag;
          ptTriInf.add(new RealVector2(x+delta*cosseno,y+dy-delta*seno));
          ptTriSup.add(new RealVector2(x+delta*cosseno,y+dy-delta*seno));
        }

        //Checa se existe um segmento a ser inserido
        size = ptTriInf.size();
        if(size >= 2){
          for(k=0;k<size-1;k++){
            RealVector2 p1 = (RealVector2) ptTriInf.elementAt(k);
            RealVector2 p2 = (RealVector2) ptTriInf.elementAt(k+1);
            segmentos.addElement(new RealSegment(p1,p2));
          }
        }
        ptTriInf.removeAllElements();

        if(j!=(mh-i-1)){
          //Calcula intersecao na borda direita
          alpha = (b/(b-c));
          if((alpha <= 1.0d) && (alpha >= 0.0d)){
            delta = alpha * dy;
            ptTriSup.add(new RealVector2(x+dx,y+delta));
            pEsq = new RealVector2(x+dx,y+delta);
          }
          else{
            pEsq = null;
          }


          //Calcula intersecao na borda superior
          alpha = (d/(d-c));
          if((alpha <= 1.0d) && (alpha >= 0.0d)){
            delta = alpha * dx;
            ptTriSup.add(new RealVector2(x+delta,y+dy));
          }
        }

        //Checa se existe um segmento a ser inserido
        size = ptTriSup.size();
        if(size >= 2){
          for(k=0;k<size-1;k++){
            RealVector2 p1 = (RealVector2) ptTriSup.elementAt(k);
            RealVector2 p2 = (RealVector2) ptTriSup.elementAt(k+1);
            segmentos.addElement(new RealSegment(p1,p2));
          }
        }
        ptTriSup.removeAllElements();

      }
    }
    return segmentos;
  }

  /**
  *  Executa o mesmo processo que a funcao DoApproximation, com
  *  a diferenca de que os pontos obtidos sao refinados com o
  *  uso do metodo de newton.
  *       Percorre o espaco definido pelas variaveis
  *       xmin, xmax, ymin, ymax. Cada ponto encontrado
  *       e refinado com o uso do metodo de newton.
  *@return Vetor contendo os segmentos que
  *constituem a aproximacao da curva. Obs.: Os
  *segmentos nao estao ordenados.
  *
  */
  public Vector  doRefinedApproximation(){

    int i,j,k,ind,indAnt,size;
    double a,b,c,d;
    double alpha, delta, diag;
    double aux;
    double [][] result = new double[2][mh+1];

    Vector ptTriSup = new Vector();
    Vector ptTriInf = new Vector();
    Vector segmentos = new Vector();
    RealVector2 pEsq = null;

    dx = (xmax-xmin)/mh;
    dy = (ymax-ymin)/mv;

    theta = Math.atan(dy/dx);
    seno = Math.sin(theta);
    cosseno = Math.cos(theta);
    diag = dy / seno;

    x = xmin;
    for(j=0;j<mh+1;j++){
        result[0][j] =  ap.function(x,ymin);
        x += dx;
    }

    //Percorre Todos os quadrados
    for(i=0;i<mv;i++){
      y = ymin + i*dy;

      indAnt = i%2;
      ind = (i+1) % 2;
      result[ind][0] = ap.function(xmin,y+dy);

      for(j=0;j<mh;j++){
        x = xmin + j*dx;

        result[ind][j+1] = ap.function(x+dx,y+dy);

        //Saida da funcao nas quatro extremidades
        a = result[indAnt][j];    //Ponto Inferior Esquerdo
        b = result[indAnt][j+1];  //Ponto Inferior Direito
        c = result[ind][j+1];     //Ponto Superior Direito
        d = result[ind][j];       //Ponto Superior Esquerdo

        if(j==0){ //Se estiver no primeiro quadrado da linha
          //Calcula intersecao na borda esquerda
          alpha = (a/(a-d));
          if((alpha <= 1.0d) && (alpha >= 0.0d)){
            delta = alpha * dy;

            //System.out.println("Ponto Interpolado:");
            //System.out.println(new RealVector2(x,y+delta));
            try{
              aux = newton(ESQ,y,y+dy,y+delta);
              //System.out.println("Ponto Newton:");
              //System.out.println(paux3);
              ptTriInf.add(new RealVector2(x,aux));
            }
            catch(Exception ex){
              ptTriInf.add(new RealVector2(x,y+delta));
            }
          }
        }
        else{//Se nao estiver na primeira coluna
          //Calcula intersecao na borda esquerda
          //adicionando a borda direita do quadrado anterior
          if(pEsq != null){
            ptTriInf.add(new RealVector2(pEsq));
            pEsq = null;
          }
        }

        //Calcula intersecao na borda inferior
        alpha = (a/(a-b));
        if((alpha <= 1.0d) && (alpha >= 0.0d)){
          delta = alpha * dx;

          //System.out.println("Ponto Interpolado:");
          //System.out.println(new RealVector2(x+delta,y));
          try{
            aux = newton(INF,x,x+dx,x+delta);
            //System.out.println("Ponto Newton:");
            //System.out.println(new RealVector2(aux,y));
            ptTriInf.add(new RealVector2(aux,y));
          }
          catch(Exception ex){
            ptTriInf.add(new RealVector2(x+delta,y));
          }
        }

        //Calcula intersecao na diagonal
        alpha = (d/(d-b));
        if((alpha <= 1.0d) && (alpha >= 0.0d)){
          delta = alpha * diag;

          //System.out.println("Ponto Interpolado:");
          //System.out.println(new RealVector2(x+delta*cosseno,y+dy-delta*seno));
          try{
            aux = newton(DIAG,x,x+dx,x+delta*cosseno);
            //System.out.println("Ponto Newton:");
            //System.out.println(new RealVector2(aux,(((x+dx)-aux)*dy/dx)+y));
            ptTriInf.add(new RealVector2(aux,(((x+dx)-aux)*dy/dx)+y));
            ptTriSup.add(new RealVector2(aux,(((x+dx)-aux)*dy/dx)+y));
          }
          catch(Exception ex){
            ptTriInf.add(new RealVector2(x+delta*cosseno,y+dy-delta*seno));
            ptTriSup.add(new RealVector2(x+delta*cosseno,y+dy-delta*seno));
          }
        }

        //Checa se existe um segmento a ser inserido
        size = ptTriInf.size();
        if(size >= 2){
          for(k=0;k<size-1;k++){
            RealVector2 p1 = (RealVector2) ptTriInf.elementAt(k);
            RealVector2 p2 = (RealVector2) ptTriInf.elementAt(k+1);
            segmentos.addElement(new RealSegment(p1,p2));
          }
        }
        ptTriInf.removeAllElements();

        //Calcula intersecao na borda direita
        alpha = (b/(b-c));
        if((alpha <= 1.0d) && (alpha >= 0.0d)){
          delta = alpha * dy;

          //System.out.println("Ponto Interpolado:");
          //System.out.println(new RealVector2(x+dx,y+delta));
          try{
            aux = newton(DIR,y,y+dy,y+delta);
            //System.out.println("Ponto Newton:");
            //System.out.println(new RealVector2(x+dx,aux));
            ptTriSup.add(new RealVector2(x+dx,aux));
            pEsq = new RealVector2(x+dx,aux);
          }
          catch(Exception ex){
            ptTriSup.add(new RealVector2(x+dx,y+delta));
            pEsq = new RealVector2(x+dx,y+delta);
          }
        }
        else{
          pEsq = null;
        }

        //Calcula intersecao na borda superior
        alpha = (d/(d-c));
        if((alpha <= 1.0d) && (alpha >= 0.0d)){
          delta = alpha * dx;

          //System.out.println("Ponto Interpolado:");
          //System.out.println(new RealVector2(x+delta,y+dy));
          try{
            aux = newton(SUP,x,x+dx,x+delta);
            //System.out.println("Ponto Newton:");
            //System.out.println(paux3);
            ptTriSup.add(new RealVector2(aux,y+dy));
          }
          catch(Exception ex){
            ptTriSup.add(new RealVector2(x+delta,y+dy));
          }
        }

        //Checa se existe um segmento a ser inserido
        size = ptTriSup.size();
        if(size >= 2){
          for(k=0;k<size-1;k++){
            RealVector2 p1 = (RealVector2) ptTriSup.elementAt(k);
            RealVector2 p2 = (RealVector2) ptTriSup.elementAt(k+1);
            segmentos.addElement(new RealSegment(p1,p2));
          }
        }
        ptTriSup.removeAllElements();

      }
    }
    return segmentos;
  }

  /*
   *  Calcula a derivada da funcao utilizando as
   *  derivadas parciais definidas na interface AproxFunction
   *
   */
   private double  derivada(int tipo, double p) {

     switch(tipo){
       case DIR: return ap.dy(x+dx,p);
       case ESQ: return ap.dy(x,p);

       case SUP: return ap.dx(p,y+dy);
       case INF: return ap.dx(p,y);

       case DIAG: return (ap.dx(p,(((x+dx)-p)*dy/dx)+y) - (dy/dx)*ap.dy(p,(((x+dx)-p)*dy/dx)+y));
     }

    return 0.0;  //Valor invalido

   }

   /*
   *  Calcula a derivada da funcao utilizando as
   *  derivadas parciais definidas na interface AproxFunction
   *
   */
   private double funcNewton(int tipo, double p) {

     switch(tipo){
       case DIR: return ap.function(x+dx,p);
       case ESQ: return ap.function(x,p);

       case SUP: return ap.function(p,y+dy);
       case INF: return ap.function(p,y);

       case DIAG: return ap.function(p,(((x+dx)-p)*dy/dx)+y);
     }

    return 0.0;  //Valor invalido

   }

  /*
   *  Executa o metodo de newton para refinar o valor obtido
   *  atraves da interpolacao triangular
   *
   *  Obs.: A variavel rtn e utilizada como chute inicial
   *
   */
   private double newton(int tipo, double x1, double x2, double rtn) throws Exception {

     RealVector2 pt;

     int j;
     double df, dx, f;

     for (j=1;j<=maxIterNewton;j++) {

      f = funcNewton(tipo,rtn);

       /*if(f == 0.0){
         System.out.println("Ponto ja e uma solucao !");
         throw (new Exception());   //Ponto ja e uma solucao
       }*/

       df = derivada(tipo,rtn);

       dx = f/df;
       rtn -= dx;

       if((x1-rtn)*(rtn-x2) < 0.0)
         throw (new Exception());   //Valor fora do intervalo determinado

       if (Math.abs(dx) < precNewton){
         return rtn; //Convergence
       }
     }

     //Lanca excecao pois o numero maximo de iteracoes foi atingido
     throw (new Exception());

   }

   public ApproxFunction approxFunction() {return ap;}

 /*float rtnewt(void (*funcd)(float, float *, float *), float x1, float x2,float xacc){
 void nrerror(char error_text[]);
  int j;
  float df,dx,f,rtn;

  rtn=0.5*(x1+x2); //Initial guess.

  for (j=1;j<=JMAX;j++) {
    (*funcd)(rtn,&f,&df);
    dx=f/df;
    rtn -= dx;

    if ((x1-rtn)*(rtn-x2) < 0.0)
      nrerror("Jumped out of brackets in rtnewt");

    if (fabs(dx) < xacc) return rtn; //Convergence.
  }

  nrerror("Maximum number of iterations exceeded in rtnewt");
  return 0.0; //Never get here.
}
*/

}
