#include <iostream>
#include <fstream>
#include "StoneSubPhysics.h"
#include "GasVolatileDeadSubPhysics.h"
#include "TrivialTestSubPhysics.h"

#include <FL/Fl.H>
#include <FL/Fl_Double_Window.H>
#include <FL/Fl_Round_Button.H>
#include <FL/Fl_Choice.H>
#include <FL/Fl_Tile.H>
#include <FL/Fl_Float_Input.H>
#include <FL/Fl_Scroll.H>
#include "canvas.h"
#include "canvasmenuscroll.h"
#include "curve2d.h"
#include "segmentedcurve.h"
#include "MultiColoredCurve.h"
#include "DoubleMatrixDisplay.h"
#include "TestTools.h"

// TODO: Convert Helmut's problem to the new style
#include "FluxSinglePhaseVaporAdimensionalized_Params.h"
#include "FluxSinglePhaseVaporAdimensionalized.h"

#include "AccumulationSinglePhaseVaporAdimensionalized_Params.h"
#include "AccumulationSinglePhaseVaporAdimensionalized.h"

#include "Flux2Comp2PhasesAdimensionalized.h"
#include "Accum2Comp2PhasesAdimensionalized.h"

#include "FluxSinglePhaseLiquidAdimensionalized_Params.h"
#include "FluxSinglePhaseLiquidAdimensionalized.h"

#include "AccumulationSinglePhaseLiquidAdimensionalized_Params.h"
#include "AccumulationSinglePhaseLiquidAdimensionalized.h"

#include "Thermodynamics.h"
// TODO:

SubPhysics *subphysics;

Fl_Double_Window *winjac;
    Matrix<Canvas*> canvasjac;

Fl_Double_Window *winhes;
    Fl_Scroll *hesscroll;

const FluxFunction *flux;
const AccumulationFunction *accumulation;
GridValues *gridvalues;

void numerical_diff(const DoubleMatrix &analytical, 
                    const RealVector &delta, 
                    DoubleMatrix &num_x, DoubleMatrix &num_y,
                    RealVector &absolute_max_analytical,
                    RealVector &absolute_diff_numerical){
                
    num_x.resize(analytical.rows(), analytical.cols());
    num_y.resize(analytical.rows(), analytical.cols());
    
    absolute_max_analytical.resize(2);
    absolute_diff_numerical.resize(2);
    
    for (int j = 1; j < num_x.cols() - 1; j++){
        for (int i = 1; i < num_x.rows() - 1; i++){
            if (gridvalues->cell_type(i, j) == CELL_IS_INVALID || gridvalues->cell_type(i - 1, j) == CELL_IS_INVALID) continue;
            
            num_x(i, j) = (analytical(i + 1, j) - analytical(i - 1, j))/(2.0*delta(0));
        }
    }

    for (int j = 1; j < num_x.cols() - 1; j++){
        for (int i = 1; i < num_x.rows() - 1; i++){
            if (gridvalues->cell_type(i, j) == CELL_IS_INVALID || gridvalues->cell_type(i, j - 1) == CELL_IS_INVALID) continue;
            
            num_y(i, j) = (analytical(i, j + 1) - analytical(i, j - 1))/(2.0*delta(1));
        }
    }
    
    return;
}

void numdiff(const DoubleMatrix &analytical, const DoubleMatrix &diff_analytical, double delta, double var, GridValues *g, DoubleMatrix &diff_minus_analytical){
    DoubleMatrix numerical_diff(analytical.rows(), analytical.cols());
    
    if (var == 0){
        for (int i = 1; i < analytical.rows() - 1; i++){
            for (int j = 1; j < analytical.cols() - 1; j++){
                if (g->cell_type(i, j) == CELL_IS_INVALID || g->cell_type(i - 1, j) == CELL_IS_INVALID) continue;
                numerical_diff(i, j) = (analytical(i + 1, j) - analytical(i - 1, j))/(2.0*delta);
            }
        }
    }
    else {
        for (int i = 1; i < analytical.rows() - 1; i++){
            for (int j = 1; j < analytical.cols() - 1; j++){
                if (g->cell_type(i, j) == CELL_IS_INVALID || g->cell_type(i, j - 1) == CELL_IS_INVALID) continue;
                numerical_diff(i, j) = (analytical(i, j + 1) - analytical(i, j - 1))/(2.0*delta);
            }
        }
    }

//    std::cout << "analytical =\n" << analytical << std::endl;

//    std::cout << "numerical_diff =\n" << numerical_diff << std::endl;
//    
//    std::cout << "diff_analytical =\n" << diff_analytical << std::endl;

    diff_minus_analytical = (numerical_diff - diff_analytical).submatrix(1, analytical.rows() - 2, 1, analytical.cols() - 2);
    
    return;
}

void function_matrix(const RpFunction *rpf, GridValues *g, int component, DoubleMatrix &function_analytical){
    function_analytical.resize(g->grid.rows(), g->grid.cols());
    
    for (int i = 0; i < g->grid.rows(); i++){
        for (int j = 0; j < g->grid.cols(); j++){
            //if (g->cell_type(i, j) == CELL_IS_INVALID || g->cell_type(i - 1, j) == CELL_IS_INVALID) continue;
            JetMatrix jm(2);
            
            rpf->jet(g->grid(i, j), jm, 0);
            
            function_analytical(i, j) = jm.get(component);
        }
    }
    
    return;
}

void Jacobian_matrix(const RpFunction *rpf, GridValues *g, int component, int var, DoubleMatrix &Jacobian_analytical){
    Jacobian_analytical.resize(g->grid.rows(), g->grid.cols());
    
    for (int i = 0; i < g->grid.rows(); i++){
        for (int j = 0; j < g->grid.cols(); j++){
            //if (g->cell_type(i, j) == CELL_IS_INVALID || g->cell_type(i - 1, j) == CELL_IS_INVALID) continue;
            
            JetMatrix jm(2);
            
            rpf->jet(g->grid(i, j), jm, 1);
            
            Jacobian_analytical(i, j) = jm.get(component, var);
        }
    }
    
    return;
}

double Tref_water = 274.3775;
double T_typical_   = 304.63;

double Theta2T(double Theta){return Theta*T_typical_ + Tref_water;}

void matrices(DoubleMatrix &function_analytical, int var, DoubleMatrix &Jacobian_analytical){
    int rows = 100;
    int cols = 100;

    double Theta_min = 0.099309;
    double Theta_max = 0.576511;
    double T_min = Theta2T(Theta_min);
    double T_max = Theta2T(Theta_max);

    double delta_T = (T_max - T_min)/(double)(rows - 1);

    MolarDensity mdv(MOLAR_DENSITY_VAPOR,  100.900000e5);
    MolarDensity mdl(MOLAR_DENSITY_LIQUID, 100.900000e5);

    VLE_Flash_TPCW *flash = new VLE_Flash_TPCW(&mdl, &mdv);

    // Find min and max xc.
    double x_min = std::numeric_limits<double>::max();
    double x_max = -std::numeric_limits<double>::max();
    for (int i = 0; i < rows; i++){
        double T = T_min + delta_T*(double)i;

        JetMatrix xcj(1), ywj(1);
        flash->molarfractions_jet(T, xcj, ywj, 2);

        double x = xcj.get(0);
        if (x > x_max) x_max = x;
        if (x < x_min) x_min = x;
    }
    double delta_x = (x_max - x_min)/(double)(cols - 1);

    function_analytical.resize(rows, cols);
    Jacobian_analytical.resize(rows, cols);
    for (int i = 0; i < rows; i++){
        double T = T_min + delta_T*(double)i;
        for (int j = 0; j < cols; j++){
            double x = x_min + delta_x*(double)j;
                JetMatrix jet(2, 1);
                mdl.L_jet(x, T, 2, jet);
                function_analytical(i, j) = jet.get(0);

                Jacobian_analytical(i, j) = jet.get(0, var);
        }
    }    

    return;
}

void Jacobian_matrix(int var, DoubleMatrix &Jacobian_analytical){
}

void Jacobian(const RpFunction *rpf, GridValues *g){
    winjac = new Fl_Double_Window(10, 10, 800, 800, "Jacobians");
    winjac->end();
    
    std::ofstream out("Jacobian.txt", ios::app | ios::out);
    out << "Resolution: " << g->grid_resolution << std::endl;

    for (int component = 0; component < 2; component++){
        for (int var = 0; var < 2; var++){
            DoubleMatrix function_analytical, Jacobian_analytical;
            
            function_matrix(rpf, g, component,      function_analytical);
            Jacobian_matrix(rpf, g, component, var, Jacobian_analytical);
            
//            matrices(function_analytical, var, Jacobian_analytical);

            DoubleMatrix dma;
            
            numdiff(function_analytical, Jacobian_analytical, g->grid_resolution(var), var, g, dma);
            DoubleMatrix *diff_minus_analytical = new DoubleMatrix(dma);
                
            // Show.
            //
            int w = winjac->w()/2;
            int h = winjac->h()/2;
            
            Canvas *canvas = new Canvas(var*w, component*h, w, h);

            std::stringstream sy;
            sy << "Component = " << component << ", variable = " << var;
            canvas->ylabel(sy.str().c_str());

            std::stringstream sx;
            sx << "df in [" << diff_minus_analytical->min() << ", " << diff_minus_analytical->max() << "], f in [" << function_analytical.min() << ", " << function_analytical.max() << "]";
            canvas->xlabel(sx.str().c_str());
            
            out << "Component: " << component << ", var = " << var << "\n   " << sx.str() << std::endl;

            DoubleMatrixDisplay *d = new DoubleMatrixDisplay(diff_minus_analytical);
            canvas->add(d);
            canvas->nozoom();
            
            winjac->add(canvas);
        }
    }

    out.close();

    winjac->resizable(winjac);
    winjac->show();
    
    return;
}

void Hessian_matrix(const RpFunction *rpf, GridValues *g, int component, int var1, int var2, DoubleMatrix &analytical){
    //     double min_Hessian =  std::numeric_limits<double>::max();
    //     double max_Hessian = -std::numeric_limits<double>::max();
    //     
    
    analytical.resize(g->grid.rows(), g->grid.cols());

    for (int i = 0; i < g->grid.rows(); i++){
        for (int j = 0; j < g->grid.cols(); j++){
            if (g->cell_type(i, j) == CELL_IS_INVALID || g->cell_type(i - 1, j) == CELL_IS_INVALID) continue;
            
            JetMatrix jm(3);
            rpf->jet(g->grid(i, j), jm, 2);
            
            analytical(i, j) = jm.get(component, var1, var2);
        }
    }
    
    return;
}

void Hessian(const RpFunction *rpf, GridValues *g){
    winhes = new Fl_Double_Window(10, 10, 1000, 800, "Hessians");
        hesscroll = new Fl_Scroll(0, 0, winhes->w(), winhes->h());
    winhes->end();
    winhes->resizable(hesscroll);
    
    std::ofstream out("Hessian.txt", ios::app | ios::out);
    out << "Resolution: " << g->grid_resolution << std::endl;

    for (int component = 0; component < 2; component++){
        for (int var1 = 0; var1 < 2; var1++){
            for (int var2 = 0; var2 < 2; var2++){
                std::stringstream ss;
                ss << "Comp.: " << component << ", var1: " << var1 << ", var2: " << var2;
                out << ss.str() << std::endl;

                DoubleMatrix Jacobian_analytical, Hessian_analytical;
            
                Jacobian_matrix(rpf, g, component, var1, Jacobian_analytical);
                Hessian_matrix(rpf, g, component, var1, var2, Hessian_analytical);

                DoubleMatrix dma;
            
                numdiff(Jacobian_analytical, Hessian_analytical, g->grid_resolution(var2), var2, g, dma);
                DoubleMatrix *diff_minus_analytical = new DoubleMatrix(dma);
                
                // Show.
                //
                int w = winhes->w()/2;
                int h = winhes->h()/2;
            
                Canvas *canvas = new Canvas(var1*w, (component*2 + var2)*h, w, h);
    
                std::stringstream sy;
                sy << "Component = " << component << ", var1 = " << var1 << ", var2 = " << var2;
                canvas->ylabel(sy.str().c_str());

                std::stringstream sx;
                sx.precision(3);
                sx << "d2f in [" << diff_minus_analytical->min() << ", " << diff_minus_analytical->max() << "], df in [" << Jacobian_analytical.min() << ", " << Jacobian_analytical.max() << "]";
                canvas->xlabel(sx.str().c_str());
            
                out << sx.str() << std::endl << std::endl;

                DoubleMatrixDisplay *d = new DoubleMatrixDisplay(diff_minus_analytical);
                canvas->add(d);
                canvas->nozoom();
            
                hesscroll->add(canvas);
            }
        }
    }
    
    out.close();

    winhes->show();
    
    return;
}

void TPCW(){
         // Thermo
         double mc = 0.044;
         double mw = 0.018;
     
         MolarDensity mdv(MOLAR_DENSITY_VAPOR,  100.900000e5);
         MolarDensity mdl(MOLAR_DENSITY_LIQUID, 100.900000e5);
     
         VLE_Flash_TPCW *flash = new VLE_Flash_TPCW(&mdl, &mdv);
     
         Thermodynamics *tc = new Thermodynamics(mc, mw, "./c++/rpnumerics/physics/CompositionalPhysics/TPCW/hsigmaC_spline.txt");
         tc->set_flash(flash);
     
         // ************************* Physics ************************* //
     
         // TPCW
         double abs_perm = 3e-12; 
         double sin_beta = 0.0;
         double const_gravity = 9.8;
         bool has_gravity = false;
         bool has_horizontal = true;
     
         RealVector fpp(12);
         fpp.component(0) = abs_perm;
         fpp.component(1) = sin_beta;
         fpp.component(2) = (double)has_gravity;
         fpp.component(3) = (double)has_horizontal;
         
         fpp.component(4) = 0.0;
         fpp.component(5) = 0.0;
         fpp.component(6) = 2.0;
         fpp.component(7) = 2.0;
         fpp.component(8) = 0.38;
         fpp.component(9) = 304.63;
         fpp.component(10) = 998.2;
         fpp.component(11) = 4.22e-3;
     
         Flux2Comp2PhasesAdimensionalized_Params fp(fpp, tc);
         Flux2Comp2PhasesAdimensionalized *flux = new Flux2Comp2PhasesAdimensionalized(fp);
     
         double phi = 0.38;
     
         Accum2Comp2PhasesAdimensionalized_Params ap(tc, phi);
         Accum2Comp2PhasesAdimensionalized *accumulation = new Accum2Comp2PhasesAdimensionalized(ap);
     
         // ************************* Physics ************************* //
     
     
         double Theta_min = 0.099309;
         double Theta_max = 0.576511;
     
         RealVector pmin(3), pmax(3);
     
         pmin.component(0) = 0.0;
         pmin.component(1) = Theta_min;
         pmin.component(2) = 0.0;
     
         pmax.component(0) = 1.0;
         pmax.component(1) = Theta_max;
         pmax.component(2) = 2.0;
     
         std::vector<int> number_of_cells(2);
         number_of_cells[0] = number_of_cells[1] = 256;
     
         RectBoundary *boundary = new RectBoundary(pmin, pmax);
     
         // ************************* GridValues ************************* //
         GridValues *gridvalues = new GridValues(boundary, pmin, pmax, number_of_cells);
         // ************************* GridValues ************************* //
    


    gridvalues->fill_eigenpairs_on_grid(flux, accumulation);

    RpFunction *rpf;
    rpf = (RpFunction*)flux;
    rpf = (RpFunction*)accumulation;
    Jacobian(rpf, gridvalues);
    Hessian(rpf, gridvalues);

    return;
}

void call_subphysics(){
    //subphysics = new StoneSubPhysics;
    //subphysics = new TrivialTestSubPhysics;
    subphysics = new GasVolatileDeadSubPhysics;
    
    subphysics->gridvalues()->fill_eigenpairs_on_grid(subphysics->flux(), subphysics->accumulation());

    RpFunction *rpf;
    rpf = (RpFunction*)subphysics->flux();
    rpf = (RpFunction*)subphysics->accumulation();
    GridValues *gridvalues = subphysics->gridvalues();


    Jacobian(rpf, subphysics->gridvalues());
    Hessian(rpf, gridvalues);

    return;
}

int main(){
    TPCW();
    //call_subphysics();

    Fl::scheme("gtk+");    
    return Fl::run();
}

