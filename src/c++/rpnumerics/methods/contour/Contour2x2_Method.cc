#include "Contour2x2_Method.h"
#include "Debug.h"


#include <iostream>
#include <cstring>
#ifdef _OPENMP
#include <omp.h>
#endif

// Static variables are defined here:
//
bool Contour2x2_Method::is_first = true;

HyperCube Contour2x2_Method::hc;

const int Contour2x2_Method::hn = 4;
const int Contour2x2_Method::hm = 3;

// Estos numeros todavia no fueron determindados...
int Contour2x2_Method::nsface_;  // =  3; colocarlos en la primera salida de impresion y ver cuanto valen.
int Contour2x2_Method::nface_;   // =  5;TODO: pero no es importante.
// Hasta aqui. (Los dos ultimos no parecen ser importantes.)

const int Contour2x2_Method::dims_   = 125;
const int Contour2x2_Method::dime_   = 200;
const int Contour2x2_Method::dimf_   =  84;
const int Contour2x2_Method::ncvert_ =  16;
const int Contour2x2_Method::dncv    =  16; //TODO, ver si este no es el ncvert_
const int Contour2x2_Method::nsimp_  =  24;

const int Contour2x2_Method::numberOfCombinations = 5;

double Contour2x2_Method::ul0;
double Contour2x2_Method::vl0;
double Contour2x2_Method::ur0;
double Contour2x2_Method::vr0;

double Contour2x2_Method::dul;
double Contour2x2_Method::dvl;
double Contour2x2_Method::dur;
double Contour2x2_Method::dvr;

double Contour2x2_Method::dumax;
double Contour2x2_Method::dvmax;

/********** used by all threads *********/
Matrix<int>    Contour2x2_Method::fnbr_;
Matrix<double> Contour2x2_Method::cvert_;
Matrix<int>    Contour2x2_Method::facptr_;
Matrix<int>    Contour2x2_Method::face_;
int * Contour2x2_Method::index;
int * Contour2x2_Method::usevrt;

void Contour2x2_Method::allocate_arrays(void){
    if (is_first){
        // local variable
        Matrix<int> comb_(numberOfCombinations, hm + 1); // Was transposed: hccube.inc:7
        Matrix<int> perm_(hn, nsimp_); // Was NOT transposed: hccube.inc:5
        Matrix<int> bsvert_(hn + 1, hn); // Was transposed: hccube.inc:5
        // used by all threads
        cvert_.resize(ncvert_, hn); // Was transposed: hccube.inc:4
        // writes on comb
        nsface_ = hc.mkcomb(comb_.data(), hn + 1, hm + 1);
        fnbr_.resize(nsface_, nsface_);
        face_.resize(hm + 1, dimf_); // Was NOT transposed: hccube.inc:7 // Confirmed in: hcmarc.inc:4.
        facptr_.resize(nsimp_, nsface_);
        index = new int[4]; index[0] = 0; index[1] = 2; index[2] = 3; index[3] = 1; 
        usevrt = new int[dncv]; // Defined in: contour3.F.
        for (int i = 0; i < dncv; i++) usevrt[i] = 1;

        // writes on cvert, bsvert, perm
        hc.mkcube(cvert_.data(), bsvert_.data(), perm_.data(), ncvert_, nsimp_, hn);

        // writes on face, facptr, fnbr
        // storm_, storn_ are only used inside the function as local storage
        int * storm_ = new int[hm + 1];
        int * storn_ = new int[hn + 1];
        nface_ = hc.mkface(face_.data(), facptr_.data(), fnbr_.data(), dimf_, nsimp_, hn, hm, nsface_,
                           bsvert_.data(), comb_.data(), perm_.data(), &storn_[0], &storm_[0]);
        delete[] storm_;
        delete[] storn_;
        // Verify that nface_ is DNFACE.
        //exstfc = new int[nface_]; for (int i = 0; i < nface_; i++) exstfc[i] = 1;
        //sptr_  = new int[nface_];

        is_first = false;

        if ( Debug::get_debug_level() == 5 ) {
            printf("++++++++++++++++ Contour2x2_Method: REMEMBER TO INVOKE deallocate_arrays() AT QUIT-TIME!!!\n++++++++++++++++ DON\'T SAY I DIDN\'T WARN YOU!!!\n");
            printf("    After allocating arrays, nsface_ = %d, nface_ =  %d\n", nsface_, nface_);
        }
    }
}

void Contour2x2_Method::deallocate_arrays(void){
    if (!is_first){
        delete [] usevrt;
        delete [] index;
        is_first = true;
    }
    if ( Debug::get_debug_level() == 5 ) {
        printf("++++++++++++++++ Contour2x2_Method: arrays deallocated. ++++++++++++++++\n");
    }
}

typedef std::vector<RealVector> rvector;
struct context {
    //
    Matrix<int>    fnbr_;
    Matrix<double> cvert_;
    Matrix<int>    facptr_;
    Matrix<int>    face_;
    int *index;
    int *usevrt; // TODO, ver si es necesario, sino, matar [dncv]
    //
    Matrix<double> foncub;
    Matrix<int> solptr_;
    Matrix<double> cpp_sol;
    Matrix<int> cpp_edges_;
    Matrix<int> smpedg_;
    int * storm_; 
    int * exstfc;
    int * sptr_;
    double * u;
    double * g;
    double * stormd;
    rvector lvecs;
    rvector rvecs;
    //
    context( const Matrix<int>& fnbr_, const Matrix<double>& cvert_, 
            const Matrix<int>& facptr_, const Matrix<int>& face_,
            const int *index_, const int *usevrt_, int dncv,
            unsigned hm, unsigned ncvert, unsigned nsimp, unsigned nsface, unsigned hn, unsigned dims, unsigned dime, unsigned nface )
        : fnbr_(fnbr_), cvert_(cvert_), facptr_(facptr_), face_(face_), 
            foncub(hm, ncvert), solptr_(nsimp, nsface), cpp_sol(hn, dims), cpp_edges_(2, dime), smpedg_(nsimp,2)
    { 
        index = new int[4];
        memcpy( index, index_, 4*sizeof(int) ); 
        usevrt = new int[dncv];
        memcpy( usevrt, usevrt_, dncv*sizeof(int) ); 
        storm_ = new int[hm+1];
        exstfc = new int[nface]; 
        for (unsigned int i = 0; i < nface; i++) exstfc[i] = 1;
        sptr_ = new int[nface];
        u = new double[hn * (hn+1)];
        g = new double[hn * (hn+1)];
        stormd = new double[hn];
    }

    void reset() {
        unsigned size = lvecs.size();
        lvecs.clear();
        lvecs.reserve( size );
        size = rvecs.size();
        rvecs.clear();
        rvecs.reserve( size );
    }

    ~context ( ) {
        delete[] index;
        delete[] usevrt;
        delete[] storm_;
        delete[] exstfc;
        delete[] sptr_;
        delete[] u;
        delete[] g;
        delete[] stormd;
    }
};

void Contour2x2_Method::process_cell( context * cnt, ThreeImplicitFunctions *timpf, int il, int jl, int ir, int jr )
{
    if (filhcub4(timpf, ir, jr, cnt->index, cnt->foncub.data())) {
        int nsoln_  = -1;
        nsoln_ = hc.cpp_cubsol(cnt->solptr_.data(), cnt->cpp_sol, dims_, 
                               cnt->sptr_, nsoln_, cnt->foncub.data(), cnt->exstfc, 
                               cnt->face_.data(), cnt->facptr_.data(), dimf_, cnt->cvert_.data(), 
                               ncvert_, hn, hm, nsimp_, nsface_, nface_, cnt->u, 
                               cnt->g, cnt->stormd, cnt->storm_);
        // writes on cpp_edges, smpedg 
        // smpedg is used inside to hold temp data
        int nedges_ = -1 ;
        nedges_ = hc.cpp_mkedge(cnt->cpp_edges_, dime_, nedges_, cnt->smpedg_.data(), 
                                cnt->solptr_.data(), cnt->fnbr_.data(), nsimp_, nsface_);
       
        filedg4(cnt->cpp_sol, cnt->cpp_edges_, nedges_, il, jl, ir, jr, cnt->lvecs, cnt->rvecs);
    }
}


void Contour2x2_Method::curve2x2(ThreeImplicitFunctions *timpf,
                                 std::vector<RealVector> &left_vrs,   // on_domain
                                 std::vector<RealVector> &right_vrs){ // on_curve

    allocate_arrays();
     
    // Get the current data.
    //
    GridValues *gv_left  = timpf->grid_value_left();
    GridValues *gv_right = timpf->grid_value_right();

    ul0 = gv_left->grid(0,0).component(0);
    vl0 = gv_left->grid(0,0).component(1);
    ur0 = gv_right->grid(0,0).component(0);
    vr0 = gv_right->grid(0,0).component(1);

    dul = gv_left->grid_resolution.component(0);
    dvl = gv_left->grid_resolution.component(1);
    dur = gv_right->grid_resolution.component(0);
    dvr = gv_right->grid_resolution.component(1);

    dumax = 3.0 * max ( dur, dul ) ;//+ min ( dur, dul );
    dvmax = 3.0 * max ( dvr, dvl ) ;//+ min ( dvr, dvl );

    std::cerr << "Contour2x2_Method::curve2x2() " << std::endl;
//    timer tm;
//    tm.reset();
    double time = 0;
    unsigned int size = (gv_right->grid.rows()-1)*(gv_right->grid.cols()-1);
    std::vector< rvector * > rvecs( size );
    std::vector< rvector * > lvecs( size );
    int num_threads = omp_get_max_threads();
    std::cerr << "Number of threads: " << num_threads << std::endl;
    std::vector<context*> contexts(num_threads);
    for ( int i=0; i<num_threads; i++ ) {
        contexts[i] = new context( fnbr_, cvert_, facptr_, face_, index, usevrt, dncv, 
                hm, ncvert_, nsimp_, nsface_, hn, dims_, dime_, nface_ );
    }
    int ls = 0;
    for (int il = 0; il < gv_left->grid.rows() - 1; il++) {
        for (int jl = 0; jl < gv_left->grid.cols() - 1; jl++) {
            // Only for squares within the domain.
            if (gv_left->cell_type(il, jl) != CELL_IS_SQUARE) {
                continue;
            }
            //if ( !(gv_left->cell_is_real(il, jl)) ) continue;
            if(!timpf->prepare_cell(il, jl)) continue;
//            double t1 = tm.elapsed();
            #pragma omp parallel for schedule(dynamic)
            for (int ir = 0; ir < gv_right->grid.rows() - 1; ir++){
                unsigned int id = omp_get_thread_num();
                context * cnt = contexts[id];
                for (int jr = 0; jr < gv_right->grid.cols() - 1; jr++){
                    if (gv_right->cell_type(ir, jr) == CELL_IS_SQUARE){
                        if ( (timpf->is_singular()) && left_right_adjacency(il, jl, ir, jr)) continue;
                        process_cell( cnt, timpf, il, jl, ir, jr ); 
                    }
                }
            } // For ir
//            time += tm.elapsed() - t1;
        } // For jl
    } // For il
    for (int id = 0; id < num_threads; id++){
        context *cnt = contexts[id];
        rvector::iterator lend = left_vrs.end();
        rvector::iterator rend = right_vrs.end();
        ls += cnt->lvecs.size();
        left_vrs.insert( lend, cnt->lvecs.begin(), cnt->lvecs.end() );
        right_vrs.insert( rend, cnt->rvecs.begin(), cnt->rvecs.end() );
        //delete vectors!!!!
    }
    //delete contexts
    for ( int i=0; i<num_threads; i++ ) {
        delete contexts[i];
    }
//    std::cerr << "Elapsed Time: " << tm.elapsed() << "s" << std::endl;
//    std::cerr << "Working Time: " << time << "s" << std::endl;
}


bool Contour2x2_Method::filhcub4(ThreeImplicitFunctions *timpf,
                                 int ir, int jr, int *index, double *foncub){
    bool zero[3] = {false, false, false};

    double val[3];    // To be filled e.g. by Double_Contact::function_on_cell();
    double refval[3]; // To be filled e.g. by Double_Contact::function_on_cell();
    
    if (!timpf->function_on_cell(refval, ir, jr, 0, 0)) return false;

    for (int kl = 0; kl < 4; kl++){
        for (int kr = 0; kr < 4; kr++){
            if (!timpf->function_on_cell(val, ir, jr, kl, kr)) return false;

            for (int comp = 0; comp < 3; comp++){
                foncub[comp*ncvert_ + 4*index[kl] + index[kr]] = val[comp];
                // Modified by Morante on 21-06-2011 by advice from Castaneda.
                if (refval[comp]*val[comp] < 0.0) zero[comp] = true;
            }
        }
    }
    // DEBUG: Sufficient condition:
    //        Probably next line must be increased becaus index[2] = 3:
    //        if (!timpf->function_on_cell(val, ir, jr, 2, 2)) return false;
    if ( Debug::get_debug_level() == 5 ) {
        if ( (refval[0]*val[0] < 0.0) && (refval[1]*val[1] < 0.0) && (refval[2]*val[2] < 0.0) ) {
            cout << endl;
            cout << "***** Sufficient (" << ir << ", " << jr << "): " << refval[0] << " " << refval[1] << " " << refval[2] << " *****" << endl;
            cout << "                 (" << ir << ", " << jr << "): " << val[0]    << " " << val[1]    << " " << val[2] << endl;
        }
    }
    // END DEBUG
     
    if (!zero[0]) return false;
    if (!zero[1]) return false;
    if (!zero[2]) return false;

    return true;
}

void Contour2x2_Method::filedg4(Matrix<double> &sol_, Matrix<int> &edges_, int nedges_, 
                                int il, int jl, int ir, int jr,
                                std::vector<RealVector> &left_vrs, std::vector<RealVector> &right_vrs){

    double epsilon = 1e-10;

    // START_DEBUG (1/2) */
    int segmentos = 0;
    if ( Debug::get_debug_level() == 5 ) {
        if (nedges_ > 0) {
            cout << "For " << nedges_ << " nedges (" << il << ", " << jl << ", " << ir << ", " << jr << ") : " << endl;
            for(int i = 0; i < nedges_; i++){
                cout << edges_(0, i) << " " << edges_(1, i) << " :: ";
            }
            cout << endl;
        }
    }
    // } (1/2) The second part of DEBUG is not always necessary */

    // Store all pairs of edges that were found
    double temp[2]; temp[0] = 0.0; temp[1] = 0.0;
    RealVector p1_old(2, temp), p2_old(2, temp), p3_old(2, temp), p4_old(2, temp);
    RealVector p1(2), p2(2), p3(2), p4(2);
    for (int nedg = 0; nedg < nedges_; nedg++) {
        // LX1, LY1
        p1.component(0) = ul0 + dul * (il + sol_(0, edges_(0, nedg) ) );
        p1.component(1) = vl0 + dvl * (jl + sol_(1, edges_(0, nedg) ) );

        // LX2, LY2    
        p2.component(0) = ul0 + dul * (il + sol_(0, edges_(1, nedg) ) );
        p2.component(1) = vl0 + dvl * (jl + sol_(1, edges_(1, nedg) ) );
 
        // RX1, RY1
        p3.component(0) = ur0 + dur * (ir + sol_(2, edges_(0, nedg) ) );
        p3.component(1) = vr0 + dvr * (jr + sol_(3, edges_(0, nedg) ) );

        // RX2, RY2
        p4.component(0) = ur0 + dur * (ir + sol_(2, edges_(1, nedg) ) );
        p4.component(1) = vr0 + dvr * (jr + sol_(3, edges_(1, nedg) ) );


        /* TODO: These two "neglections" are GAMBIARRAS, HyperCube must be fixed!!! */
        // Neglect zero segments
        double norm1 = 0.0;
        double norm2 = 0.0;
        for (int comp = 0; comp < 2; comp++){
            norm1 += (p1.component(comp)-p2.component(comp))*(p1.component(comp)-p2.component(comp));
            norm2 += (p3.component(comp)-p4.component(comp))*(p3.component(comp)-p4.component(comp));
        }
        if ( (norm1 < epsilon) && (norm2 < epsilon) ) continue;

        // Neglect repetition
        norm1 = 0.0; norm2 = 0.0;
        for (int comp = 0; comp < 2; comp++){
            norm1 += (p1.component(comp)-p1_old.component(comp))*(p1.component(comp)-p1_old.component(comp))
                   + (p3.component(comp)-p3_old.component(comp))*(p3.component(comp)-p3_old.component(comp));
            norm2 += (p2.component(comp)-p2_old.component(comp))*(p2.component(comp)-p2_old.component(comp))
                   + (p4.component(comp)-p4_old.component(comp))*(p4.component(comp)-p4_old.component(comp));
        }
        if ( (norm1 < epsilon) && (norm2 < epsilon) ) continue;
        p1_old = p1; p2_old = p2; p3_old = p3; p4_old = p4;
        // END of GAMBIARRAS!!!

        left_vrs.push_back(p1);
        left_vrs.push_back(p2);

        right_vrs.push_back(p3);
        right_vrs.push_back(p4);

        // START_DEBUG (2/2) TODO: It needs the first part of the DEBUG */
        if ( Debug::get_debug_level() == 5 ) {
                printf("At points (%2d, %2d, %2d, %2d) [%2d/%2d--%2d]: p1 = %1.6f, %1.6f;  p2 = %1.6f, %1.6f\n",
                    il, jl, ir, jr, nedges_, edges_(0, nedg), edges_(1, nedg),
                    p1.component(0), p1.component(1), p2.component(0), p2.component(1));
                printf("                           [%2d/%2d--%2d]: p3 = %1.6f, %1.6f;  p4 = %1.6f, %1.6f\n",
                    nedges_, edges_(0, nedg), edges_(1, nedg),
                    p3.component(0), p3.component(1), p4.component(0), p4.component(1));
            segmentos++;
        }
        // (2/2)

    }

    if ( Debug::get_debug_level() == 5 ) {
        if(nedges_ > 0) cout << "Apos gambiarras, temos " << segmentos << "/" << nedges_ << " segmentos" << endl;
    }

    return;
}


bool Contour2x2_Method::left_right_adjacency(int il, int jl, int ir, int jr){
      return ( (fabs( (ur0+(ir+.5)*dur) - (ul0+(il+.5)*dul) ) <= dumax) &&
               (fabs( (vr0+(jr+.5)*dvr) - (vl0+(jl+.5)*dvl) ) <= dvmax) );
}
