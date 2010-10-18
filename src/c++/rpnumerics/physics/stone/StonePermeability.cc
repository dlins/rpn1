/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) StonePermeability.cc
 */

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "StonePermeability.h"
#include <iostream>

using namespace std;

/*
 * ---------------------------------------------------------------
 * Definitions:
 */


StonePermeability::StonePermeability(const StonePermParams & params) : params_(new StonePermParams(params)) {


}

StonePermeability::StonePermeability(const StonePermeability & copy) {
    params_ = new StonePermParams(copy.params());

}


StonePermeability::~StonePermeability() {
}
