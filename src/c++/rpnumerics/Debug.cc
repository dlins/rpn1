/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) Debug.cc
 */

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "Debug.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */



int Debug::debug_level_ = 0;
int Debug::debug_level_parallel_ = 0;

void Debug::set_debug_level(int dbl) {
    debug_level_ = dbl;
}

int Debug::get_debug_level() {
    return debug_level_;
}

void Debug::set_debug_level_parallel(int dlp) {
    debug_level_parallel_ = dlp;
}

int Debug::get_debug_level_parallel() {
    return debug_level_parallel_;
}

