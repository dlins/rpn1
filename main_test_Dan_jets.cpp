#include <iostream>
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

Fl_Double_Window *win;
    Canvas *canvas_x, *canvas_y;

const FluxFunction *flux;
const AccumulationFunction *accumulation;
GridValues *gridvalues;

void numerical_diff(const DoubleMatrix &analytical, 
                    const RealVector &delta, 
                    DoubleMatrix &num_x, DoubleMatrix &num_y,
                    RealVector &absolute_max_analytical,
                    RealVector &absolute_diff_numerical){
                
    std::cout << "analytical.rows() = " << analytical.rows() << ", analytical.cols() = " << analytical.cols() << std::endl;
                        
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



void test_Jacobian(int component){
	DoubleMatrix analytical(gridvalues->F_on_grid.rows(), gridvalues->F_on_grid.cols());
    
    for (int i = 0; i < analytical.rows(); i++){
        for (int j = 0; j < analytical.cols(); j++){
            analytical(i, j) = gridvalues->F_on_grid(i, j)(component);
        }
    }
 
    DoubleMatrix num_x(analytical.rows(), analytical.cols()), num_y(analytical.rows(), analytical.cols());
    
    RealVector absolute_max_analytical, absolute_diff_numerical;
    
    numerical_diff(analytical, 
                   gridvalues->grid_resolution, 
                   num_x, num_y,
                   absolute_max_analytical,
                   absolute_diff_numerical);
    
    DoubleMatrix analytical_x(num_x.rows(), num_y.cols());
    for (int i = 0; i < num_x.rows(); i++){
        for (int j = 0; j < num_x.cols(); j++){
			if (gridvalues->cell_type(i, j) == CELL_IS_INVALID || gridvalues->cell_type(i - 1, j) == CELL_IS_INVALID) continue;
            analytical_x(i, j) = gridvalues->JF_on_grid(i, j)(component, 0);
        }
    }

    DoubleMatrix analytical_y(num_x.rows(), num_y.cols());
    for (int i = 0; i < num_x.rows(); i++){
        for (int j = 0; j < num_x.cols(); j++){
			if (gridvalues->cell_type(i, j) == CELL_IS_INVALID || gridvalues->cell_type(i, j - 1) == CELL_IS_INVALID) continue;
            analytical_y(i, j) = gridvalues->JF_on_grid(i, j)(component, 1);
        }
    }
    
    DoubleMatrix diff_x = (analytical_x - num_x).submatrix(1, num_x.rows() - 2, 1, num_x.cols() - 2);
    DoubleMatrix diff_y = (analytical_y - num_y).submatrix(1, num_y.rows() - 2, 1, num_y.cols() - 2);

    std::cout << "Max analytical_x: " << analytical_x.max() << ", Min analytical_x: " << analytical_x.min() << std::endl;
    std::cout << "Max diff x = " << diff_x.max() << ", Min diff x = " << diff_x.min() << std::endl;
    std::cout << "Max diff y = " << diff_y.max() << ", Min diff y = " << diff_y.min() << std::endl;
    
    win = new Fl_Double_Window(10, 10, 1000, 500, "Jacobian");
    {
        canvas_x = new Canvas(0, 0, win->w()/2, win->h());
        DoubleMatrix *mx = new DoubleMatrix(diff_x);
        DoubleMatrixDisplay *dx = new DoubleMatrixDisplay(mx);
        canvas_x->add(dx);
        canvas_x->nozoom();
        
        canvas_y = new Canvas(canvas_x->x() + canvas_x->w() + 1, 0, win->w()/2, win->h());
        DoubleMatrix *my = new DoubleMatrix(diff_y);
        DoubleMatrixDisplay *dy = new DoubleMatrixDisplay(my);
        canvas_y->add(dy);
        canvas_y->nozoom();
	}
    win->end();
    win->show();
    
    return;
}

void Hessian_matrix(const RPFunction *rpf, GridValues *g, int component, int var1, int var2, DoubleMatrix &analytical){
	//~ double min_Hessian =  std::numeric_limits<double>::max();
	//~ double max_Hessian = -std::numeric_limits<double>::max();
	//~ 
	
	analytical.resize(g->grid.rows(), g->grid.cols());
	
	for (int i = 0; i < g->grid.rows(); i++){
		for (int j = 0; j < g->grid.cols(); j++){
			if (g->cell_type(i, j) == CELL_IS_INVALID || g->cell_type(i - 1, j) == CELL_IS_INVALID) continue;
			
            JetMatrix jm(2);
            rpf->jet(gridvalues->grid(i, j), jm, 2);
            
         	analytical(i, j) = jm.get(component, var1, var2);
		}
	}
	
	return;
}



void wincb(Fl_Widget *w, void*){
    Fl_Double_Window *thiswindow = (Fl_Double_Window*)w;

    if (thiswindow == win) exit(0);

    return;
}

int main(){
    // Create the subphysics.
    //
    subphysics = new StoneSubPhysics;
    //~ subphysics = new TrivialTestSubPhysics;
    //subphysics = new GasVolatileDeadSubPhysics;
    
    gridvalues = subphysics->gridvalues();
    flux = subphysics->flux();
    accumulation = subphysics->accumulation();
    
    //~ // Thermo
    //~ double mc = 0.044;
    //~ double mw = 0.018;
//~ 
    //~ MolarDensity mdv(MOLAR_DENSITY_VAPOR,  100.900000e5);
    //~ MolarDensity mdl(MOLAR_DENSITY_LIQUID, 100.900000e5);
//~ 
    //~ VLE_Flash_TPCW *flash = new VLE_Flash_TPCW(&mdl, &mdv);
//~ 
    //~ Thermodynamics *tc = new Thermodynamics(mc, mw, "./c++/rpnumerics/physics/CompositionalPhysics/TPCW/hsigmaC_spline.txt");
    //~ tc->set_flash(flash);
//~ 
    //~ // ************************* Physics ************************* //
//~ 
    //~ // TPCW
    //~ double abs_perm = 3e-12; 
    //~ double sin_beta = 0.0;
    //~ double const_gravity = 9.8;
    //~ bool has_gravity = false;
    //~ bool has_horizontal = true;
//~ 
    //~ RealVector fpp(12);
    //~ fpp.component(0) = abs_perm;
    //~ fpp.component(1) = sin_beta;
    //~ fpp.component(2) = (double)has_gravity;
    //~ fpp.component(3) = (double)has_horizontal;
    //~ 
    //~ fpp.component(4) = 0.0;
    //~ fpp.component(5) = 0.0;
    //~ fpp.component(6) = 2.0;
    //~ fpp.component(7) = 2.0;
    //~ fpp.component(8) = 0.38;
    //~ fpp.component(9) = 304.63;
    //~ fpp.component(10) = 998.2;
    //~ fpp.component(11) = 4.22e-3;
//~ 
    //~ Flux2Comp2PhasesAdimensionalized_Params fp(fpp, tc);
    //~ Flux2Comp2PhasesAdimensionalized *flux = new Flux2Comp2PhasesAdimensionalized(fp);
//~ 
    //~ double phi = 0.38;
//~ 
    //~ Accum2Comp2PhasesAdimensionalized_Params ap(tc, phi);
    //~ Accum2Comp2PhasesAdimensionalized *accumulation = new Accum2Comp2PhasesAdimensionalized(ap);
//~ 
    //~ // ************************* Physics ************************* //
//~ 
//~ 
    //~ double Theta_min = 0.099309;
    //~ double Theta_max = 0.576511;
//~ 
    //~ RealVector pmin(3), pmax(3);
//~ 
    //~ pmin.component(0) = 0.0;
    //~ pmin.component(1) = Theta_min;
    //~ pmin.component(2) = 0.0;
//~ 
    //~ pmax.component(0) = 1.0;
    //~ pmax.component(1) = Theta_max;
    //~ pmax.component(2) = 2.0;
//~ 
    //~ std::vector<int> number_of_cells(2);
    //~ number_of_cells[0] = number_of_cells[1] = 128;
//~ 
    //~ RectBoundary *boundary = new RectBoundary(pmin, pmax);
//~ 
    //~ // ************************* GridValues ************************* //
    //~ GridValues *g = new GridValues(boundary, pmin, pmax, number_of_cells);
    //~ // ************************* GridValues ************************* //
    
    // Test the Jacobian.
    
    gridvalues->fill_eigenpairs_on_grid(flux, accumulation);
    
    int function_component = 1;
    test_Jacobian(function_component);

    // Test the Hessian.
    
    

    return Fl::run();
}

