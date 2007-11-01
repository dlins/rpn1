/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) Multid.h
 */

#ifndef _Multid_H
#define _Multid_H

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "Space.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */

class Multid {

public:
	static const Space PLANE;
	static const Space SPACE;

};

const Space Multid::PLANE = Space("PLANE", 2);
const Space Multid::SPACE = Space("SPACE", 3);

#endif //! _Multid_H
