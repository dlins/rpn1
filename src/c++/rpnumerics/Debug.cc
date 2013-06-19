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
    if ( dbl < 0 ) dbl = 0;
    if ( dbl > 5 ) dbl = 5;
    debug_level_ = dbl;
}

int Debug::get_debug_level() {
    return debug_level_;
}

void Debug::set_debug_level_parallel(int dlp) {
    if ( dlp < 0 ) dlp = 0;
    if ( dlp > 5 ) dlp = 5;
    debug_level_parallel_ = dlp;
}

int Debug::get_debug_level_parallel() {
    return debug_level_parallel_;
}

