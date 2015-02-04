#include "ContactRegionBoundary.h"
#include "SubPhysics.h"

ContactRegionBoundary::ContactRegionBoundary(SubPhysics *s): ImplicitFunction(){
    subphysics = s;

    gv = subphysics->gridvalues();
}

ContactRegionBoundary::~ContactRegionBoundary(){
}

int ContactRegionBoundary::function_on_square(double *foncub, int i, int j) {
    int is_square = gv->cell_type(i, j);
    double f_aux[4];

    for (int l = 0; l < 2; l++) {
        for (int k = 0; k < 2; k++) {
            f_aux[l * 2 + k] = subphysics->inside_contact_region(gv->grid(i + l, j + k), family) ? -1.0 : 1.0;
        }
    }

    foncub[1] = f_aux[0]; // Was: foncub[0][1]
    foncub[0] = f_aux[2]; // Was: foncub[0][0]
    foncub[3] = f_aux[1]; // Was: foncub[0][2]

    // Only useful if the cell is a square.
    //
    if (is_square == CELL_IS_SQUARE) foncub[2] = f_aux[3]; // Was: foncub[0][2]

    return 1;
}

void ContactRegionBoundary::curve(int fam, std::vector<RealVector> &curve){
    gv = subphysics->gridvalues();


    curve.clear();

    family = fam;

    std::vector< std::deque <RealVector> > tempcurves;
    std::vector <bool> is_circular;

    int method = SEGMENTATION_METHOD;
    int info = ContourMethod::contour2d(this, curve, tempcurves, is_circular, method);


    return;
}

