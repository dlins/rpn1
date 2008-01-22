/**
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) RarefactionMethod.cc
 **/

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "RarefactionMethod.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */


RarefactionMethod::RarefactionMethod(const RarefactionFlow & flow):rarefactionFlow_((RarefactionFlow*)flow.clone()){}

RarefactionMethod::~RarefactionMethod(){ delete rarefactionFlow_;}



