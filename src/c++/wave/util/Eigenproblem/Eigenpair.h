#ifndef _EIGENPAIR_
#define _EIGENPAIR_

#include "RealVector.h"

// TODO: Add something to estate the existence of algebraic and geometric multiplicity.
//
class Eigenvalue {
    private:
    protected:
        void copy(const Eigenvalue &orig){
            real      = orig.real;
            imaginary = orig.imaginary;
            is_real   = orig.is_real;

            return;
        }

    public:
        double real, imaginary;
        bool is_real;

        Eigenvalue(){}
        Eigenvalue(const Eigenvalue &orig){copy(orig);}
        Eigenvalue(const Eigenvalue *orig){copy(*orig);}

        virtual ~Eigenvalue(){}

        Eigenvalue operator=(const Eigenvalue &orig){
            if (this != &orig) copy(orig);

            return *this;
        }

        friend bool operator<(const Eigenvalue &a, const Eigenvalue &b){
            return a.real < b.real;
        }
};

class Eigenvector { // Possibly this means that a ComplexVector is needed, etc.
    private:
    protected:
        void copy(const Eigenvector &orig){
            real      = orig.real;
            imaginary = orig.imaginary;
            is_real   = orig.is_real;

            return;
        }

    public:
        RealVector real, imaginary;
        bool is_real;

        Eigenvector(){}
        Eigenvector(const Eigenvector &orig){copy(orig);}
        Eigenvector(const Eigenvector *orig){copy(*orig);}

        virtual ~Eigenvector(){}

        Eigenvector operator=(const Eigenvector &orig){
            if (this != &orig) copy(orig);

            return *this;
        }

        void normalize(){
            double norm2 = 0.0;
            for (int i = 0; i < real.size(); i++){
                norm2 += real(i)*real(i) + imaginary(i)*imaginary(i);
            }
            double invnorm = 1.0/sqrt(norm2);

            real      *= invnorm;
            imaginary *= invnorm;

            return;
        }
};

class Eigenpair {
    private:
    protected:
        void copy(const Eigenpair &orig);
    public:
        Eigenvalue  eigenvalue;
        Eigenvector right_eigenvector, left_eigenvector;
        bool is_real;

        Eigenpair();
        Eigenpair(int n);
        Eigenpair(const Eigenpair &orig);
        Eigenpair(const Eigenpair *orig);

        virtual ~Eigenpair();

        Eigenpair operator=(const Eigenpair &orig);

        friend bool operator<(const Eigenpair &a, const Eigenpair &b){
            return a.eigenvalue.real < b.eigenvalue.real;
        }
};

#endif // _EIGENPAIR_

