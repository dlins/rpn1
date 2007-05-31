#ifndef TRIPHASEFLUXFUNCTION
#define TRIPHASEFLUXFUNCTION

double kw(double, double, double, double[]);
double kogden(double, double, double, double[]);
double kowden(double, double, double, double[]);
double ko(double, double, double, double[]);
double kG(double, double, double, double[]);
void F(double[], double[], double[], double[]);
double dkwdsw(double, double, double, double[]);
double dkogdendsg(double, double, double, double[]);
double dkowdendsw(double, double, double, double[]);
double dkodsw(double, double, double, double[]);
double dkgdsw(double, double, double, double[]);
double dkwdso(double, double, double, double[]);
double dkodso(double, double, double, double[]);
double dkgdso(double, double, double, double[]);
void DF(double[][], double[], double[], double[]);
void D2F(double[][][], double[]);
void balance(double[][], double[], double[], double[]);
double dpcowdsw(double, double[]);
double dpcogdsw(double, double[]);
double dpcogdso(double, double[]);
void capil_jacobian(double[][], double[], double[]);
void viscosity(double[][], double[], double[], double[], double[], double[]);

#endif
