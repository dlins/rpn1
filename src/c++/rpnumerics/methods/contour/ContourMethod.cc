/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) HugoniotContourMethod.cc
 */

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "ContourMethod.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */

// Static variables are defined here:
//
bool ContourMethod::is_first = true;

HyperCube ContourMethod::hc;

int ContourMethod::hn = 2;
int ContourMethod::hm = 1;

int ContourMethod::nsface_ = 3;
int ContourMethod::nface_ = 5;
int ContourMethod::nsoln_ = -1;
int ContourMethod::nedges_;

int ContourMethod::dims_ = 6; // Estava 50, Fortran diz 6 (inicialmente 4)
int ContourMethod::dime_ = 6; // Estava 60, Fortran diz 6 (inicialmente 2)
int ContourMethod::dimf_ = 5;
int ContourMethod::ncvert_ = 4;
int ContourMethod::nsimp_ = 2;

int ContourMethod::numberOfCombinations = 3;

int * ContourMethod::storn_;
int * ContourMethod::storm_;

double * ContourMethod::cvert_;
double * ContourMethod::vert;
int * ContourMethod::bsvert_;
int * ContourMethod::perm_;
int * ContourMethod::comb_;
int * ContourMethod::fnbr_;
int * ContourMethod::facptr_;
int * ContourMethod::face_;
double * ContourMethod::sol_;
int * ContourMethod::solptr_;
int * ContourMethod::edges_;
int * ContourMethod::smpedg_;
int * ContourMethod::exstfc;
int * ContourMethod::sptr_;

int ContourMethod::tsimp = 1;
int ContourMethod::tface = 3;

void ContourMethod::allocate_arrays(void){
    if (is_first){
        storn_ = new int[hn + 1];
        storm_ = new int[hm + 1];

        cvert_ = new double[ncvert_*hn];
        vert = new double[ncvert_*hn];
        bsvert_ = new int[(hn + 1)*hn];
        perm_ = new int[hn*nsimp_];
        comb_ = new int[numberOfCombinations*(hm + 1)];

        nsface_ = hc.mkcomb(comb_, hn + 1, hm + 1);
        fnbr_ = new int[nsface_*nsface_];

        //inicializing another arrays, it were globally defined in java
        facptr_ = new int[nsimp_*nsface_];
        face_ = new int[(hm + 1)*dimf_];

        hc.mkcube(cvert_, bsvert_, perm_, ncvert_, nsimp_, hn);
        nface_ = hc.mkface(face_, facptr_, fnbr_, dimf_, nsimp_, hn, hm, nsface_,
                           bsvert_, comb_, perm_, storn_, storm_);

        sol_ = new double[hn*dims_];
        solptr_ = new int[nsimp_*nsface_];
        edges_ = new int[2*dime_];
        smpedg_ = new int[nsimp_*2];

        exstfc = new int[nface_]; for (int i = 0; i < nface_; i++) exstfc[i] = 1;
        sptr_ = new int[nface_];

        is_first = false;

        printf("++++++++++++++++ ContourMethod: REMEMBER TO INVOKE deallocate_arrays() AT QUIT-TIME!!!\n++++++++++++++++ DON\'T SAY I DIDN\'T WARN YOU!!!\n");
    }

    return;
}

void ContourMethod::deallocate_arrays(void){
    if (!is_first){
        delete [] sptr_;
        delete [] exstfc;

        delete [] smpedg_;
        delete [] edges_;
        delete [] solptr_;
        delete [] sol_;

        delete [] face_;
        delete [] facptr_;

        delete [] fnbr_;

        delete [] comb_;
        delete [] perm_;
        delete [] bsvert_;
        delete [] vert;
        delete [] cvert_;

        delete [] storm_;
        delete [] storn_;

        is_first = true;
    }

    return;
}

//ContourMethod::ContourMethod(HugoniotFunctionClass *h){
//    hugoniot = h;
//}

//ContourMethod::~ContourMethod(){
//}

int ContourMethod::contour2d(ImplicitFunction *impf, std::vector<RealVector> &vrs) {

    GridValues *gv = impf->grid_value();

//TODO: int sn must be eliminated (because it is == vrs.size()).
// TODO: Get rid of seglim & fdummy, ifirst (use ctor instead), and rect will become Domain*.
    printf("BEGINS: vect2d()\n");

//    deallocate_arrays();
    allocate_arrays();

    vrs.clear();

    int zero;
    int nedg;

    int ssimp, sface;

    double refval;

    // The curve is tested in small squares with values on foncub
    double foncub[hm][ncvert_];


    /* Recall that the order of vertex index on the rectangles is given by hcube and it is not trivial,
       the order is:
                       3-vertex +---+ 2-vertex
                                |\  |
                                | \ |
                                |  \|
                       1-vertex +---+ 0-vertex
    */

    // loop over the rectangles
    for (int i = 0; i < gv->grid.rows() - 1; i++) {
        for (int j = 0; j < gv->grid.cols() - 1; j++) {
            vert[0*hn + 0] = gv->grid(i + 1, j + 0).component(0); // u + du;
            vert[1*hn + 0] = gv->grid(i + 0, j + 0).component(0); // u;
            vert[2*hn + 0] = gv->grid(i + 1, j + 1).component(0); // u + du;
            vert[3*hn + 0] = gv->grid(i + 0, j + 1).component(0); // u;

            vert[0*hn + 1] = gv->grid(i + 1, j + 0).component(1); // v;
            vert[1*hn + 1] = gv->grid(i + 0, j + 0).component(1); // v;
            vert[2*hn + 1] = gv->grid(i + 1, j + 1).component(1); // v + dv;
            vert[3*hn + 1] = gv->grid(i + 0, j + 1).component(1); // v + dv;

            if (gv->cell_type(i, j) != CELL_IS_INVALID) {
                    if (gv->cell_type(i, j) == CELL_IS_SQUARE){
                        ssimp = nsimp_;
                        sface = nface_;
                    } 
                    else if (gv->cell_type(i, j) == CELL_IS_TRIANGLE) {
                        ssimp = tsimp;
                        sface = tface;
                    }
                    else continue;

                    // The posibility of a zero on a cell is given by this...
                    zero = 0; // isso vai substituir o usso de uma variavel logica (false) TODO : --> colocar como booleano (zero --> root)

                    // foncub is filled here
                    int status = impf->function_on_square(&foncub[0][0], i, j);
                    
                    if (status != 0) {
                        refval = foncub[0][0];
                        for (int l = 1; l < 4; l++) {
                            if (l == 2 && gv->cell_type(i, j) == CELL_IS_TRIANGLE) {
                                } else {
                                    if (refval * foncub[0][l] < 0.0) zero = 1;
                                }
                            }
                        }

                    if (zero != 0) {
                        //  solve for the current cube
                        double u[hn][hm + 1]; //TODO Remove
                        double g[hm][hm+1];
                        double stormd[hm];

                        nsoln_ = hc.cubsol(solptr_, sol_, dims_, sptr_, nsoln_, &foncub[0][0],
                                           exstfc, face_, facptr_, dimf_, vert,
                                           ncvert_, hn, hm, ssimp, nsface_, sface, &u[0][0], &g[0][0],
                                           stormd, storm_);

                        // IMPROVE THE SOLUTION USING A ZERO-FINDER.
                        if (impf->improvable()) {
                            for (int ii = 0; ii < sface; ii++){

                                if (sptr_[ii] == -1) continue;
                                int sp = sptr_[ii];

                                RealVector p0newton(2), p1newton(2), p_init_newton(2), p_improved_newton(2);

                                p0newton.component(0) = vert[face_[0*dimf_ + ii]*hn + 0]; // p1(1) = vert(1,face(1,i)+1)
                                p0newton.component(1) = vert[face_[0*dimf_ + ii]*hn + 1]; // p1(2) = vert(2,face(1,i)+1)

                                p1newton.component(0) = vert[face_[1*dimf_ + ii]*hn + 0]; // p2(1) = vert(1,face(2,i)+1)
                                p1newton.component(1) = vert[face_[1*dimf_ + ii]*hn + 1]; // p2(2) = vert(2,face(2,i)+1)

                                // To initialize:
                                //
                                for (int jj = 0; jj < 2; jj++) p_init_newton.component(jj) = sol_[jj*dims_ + sp];

                                Newton_Improvement *newton_improver = new Newton_Improvement(impf);
                                int newton_info = newton_improver->newton(p0newton, p1newton, p_init_newton, p_improved_newton);

                                sol_[0*dims_ + sp] = p_improved_newton.component(0); // sol(1,sp) = v(1)
                                sol_[1*dims_ + sp] = p_improved_newton.component(1); // sol(2,sp) = v(2)

                            }
                        }

                        //MAKE THE LIST OF EDGE POINTERS
                        int N_EDGES = hc.mkedge(edges_, dime_, nedges_, smpedg_, solptr_,
                                            fnbr_, ssimp, nsface_); // TODO: Em Fortran esta nsimp_

                        nedges_ = N_EDGES;

                        //STORE ALL PAIRS OF EDGES THAT WERE FOUND
                        if (nedges_ > 0) {
                           if (edges_[1*dime_ + 1] == 3 && nedges_ == 2) {
                                   // Store the segments by points...
                                   //
                                   RealVector ptemp(2);

                                   // first, on edge comprising vertices 0, 1
                                   ptemp.component(0) = sol_[0*dims_ + edges_[0*dime_ + 0]];
                                   ptemp.component(1) = sol_[1*dims_ + edges_[0*dime_ + 0]];
                                   vrs.push_back(ptemp);

                                   // second, on edge comprising vertices 2, 3
                                   ptemp.component(0) = sol_[0*dims_ + edges_[1*dime_ + 1]];
                                   ptemp.component(1) = sol_[1*dims_ + edges_[1*dime_ + 1]];
                                   vrs.push_back(ptemp);

                                   // third, on edge comprising vertices 1, 3
                                   ptemp.component(0) = sol_[0*dims_ + edges_[1*dime_ + 0]];
                                   ptemp.component(1) = sol_[1*dims_ + edges_[1*dime_ + 0]];
                                   vrs.push_back(ptemp);

                                   // fourth, on edge comprising vertices 0, 2
                                   ptemp.component(0) = sol_[0*dims_ + edges_[0*dime_ + 1]];
                                   ptemp.component(1) = sol_[1*dims_ + edges_[0*dime_ + 1]];
                                   vrs.push_back(ptemp);
                           } else {
                               for (nedg = 0; nedg < nedges_; nedg++) {

                                   // Store the segments
                                   RealVector p1(2), p2(2);
                                   p1.component(0) = sol_[0*dims_ + edges_[0*dime_ + nedg ]];
                                   p1.component(1) = sol_[1*dims_ + edges_[0*dime_ + nedg ]];

                                   // TODO: Pablo comentou a seguinte linha em 11 Janeiro 2012
                                   //       if (edges_[1*dime_ + nedg ] < 0) return 0;
                                   p2.component(0) = sol_[0*dims_ + edges_[1*dime_ + nedg ]];
                                   p2.component(1) = sol_[1*dims_ + edges_[1*dime_ + nedg ]];

                                   vrs.push_back(p1);
                                   vrs.push_back(p2);
                                }
                            }
                        }
                    }
                //}
            }
        }
    }
    printf("ENDS:   vect2d()\n\n");
    return 0;
}
