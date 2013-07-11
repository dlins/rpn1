/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) Debug.h
 */

#ifndef _Debug_H
#define _Debug_H

/*
 * ---------------------------------------------------------------
 * Includes:
 */


/*
 * ---------------------------------------------------------------
 * Definitions:
 */


class Debug {
private:

    int static debug_level_;
    int static debug_level_parallel_;



public:



    void static set_debug_level(int);
    int static get_debug_level();

    void static set_debug_level_parallel(int);
    int static get_debug_level_parallel();


};

#endif //! _Debug_H
