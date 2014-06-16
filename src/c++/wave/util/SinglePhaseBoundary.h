#include "Boundary.h"

#ifndef _SinglePhaseBoundary_H
#define _SinglePhaseBoundary_H

#include "Extension_Curve.h"
#include "Envelope_Curve.h"

#include "VLE_Flash_TPCW.h"

#define DOMAIN_IS_VAPOR  0
#define DOMAIN_IS_LIQUID 1

#define DOMAIN_EDGE_LOWER_COMPOSITION 10
#define DOMAIN_EDGE_UPPER_COMPOSITION 20

// TODO: Explain what these names mean.
#define SINGLEPHASE_TOTAL_COMPOSITION_SIDE 30
#define SINGLEPHASE_ANY_OTHER_SIDE         40

class SinglePhaseBoundary : public Boundary {
    private:
    protected:
        RealVector bmin, bmax;

        // This is the value of the composition that is above zero and below the maximum value of the composition.
        // This value will be used by inside().
        //
        double top_min_composition; 

        VLE_Flash_TPCW *Flash;

        int domain_type;

        void init(VLE_Flash_TPCW *f, double Theta_min, double Theta_max, int domain_type_, double (*d)(double Theta));
        void copy_from(const SinglePhaseBoundary &orig);

        int edge_segments(int where_constant, int number_of_steps, std::vector<RealVector> &seg);
        


        static double default_dimensionalize_variable(double Theta);
        double (*dimensionalize_variable)(double Theta);

        int top_boundary_points;
    public:
        SinglePhaseBoundary(VLE_Flash_TPCW *f, double Theta_min, double Theta_max, int domain_type_);
        SinglePhaseBoundary(VLE_Flash_TPCW *f, double Theta_min, double Theta_max, int domain_type_, double (*d)(double Theta));
        SinglePhaseBoundary(const SinglePhaseBoundary &orig);
        SinglePhaseBoundary(const SinglePhaseBoundary *orig);

        ~SinglePhaseBoundary();

        bool inside(const RealVector &y) const;
        bool inside(const double *) const;

        //! Virtual constructor
        Boundary * clone()const;

        //! Minimums boundary values accessor
        const RealVector & minimums() const;

        //! Maximums boundary values accessor
        const RealVector & maximums() const;

        RealVector intersect(RealVector &y1, RealVector &y2) const;

        //! Returns the boundary type
        const char * boundaryType() const;

        int intersection(const RealVector &p, const RealVector &q, RealVector &r, int &) const;

        void physical_boundary(std::vector<RealVector> &);
    
        void extension_curve(const FluxFunction *f, const AccumulationFunction *a,
                             GridValues &gv,
                             int where_constant, int number_of_steps, bool singular,
                             int fam, int characteristic,
                             std::vector<RealVector> &c, std::vector<RealVector> &d);

        void extension_curve(const FluxFunction *df, const AccumulationFunction *da, // Over the domain
                             const FluxFunction *cf, const AccumulationFunction *ca, // Over the curve 
                             GridValues &gv,
                             int where_constant, int number_of_steps, bool singular,
                             int fam, int characteristic,
                             std::vector<RealVector> &c, std::vector<RealVector> &d);

        void envelope_curve(const FluxFunction *f, const AccumulationFunction *a,
                            GridValues &gv,
                            int where_constant, int number_of_steps, bool singular,
                            std::vector<RealVector> &c, std::vector<RealVector> &d);
        
        RealVector side_transverse_interior(const RealVector &p, int s) const;
        
        
        void envelope_curve(const FluxFunction *fl, const AccumulationFunction *al,const FluxFunction *fr, const AccumulationFunction *ar,
            GridValues &gr,
            int where_constant, int number_of_steps, bool singular,
            std::vector<RealVector> &c, std::vector<RealVector> &d) ;


        
        
        
        
        
        

        void set_number_of_top_boundary_points(int p){top_boundary_points = p; return;}

        //void set_dimensionalization(double (*f)(double Theta)){dimensionalize_variable = f; return;}
};

#endif