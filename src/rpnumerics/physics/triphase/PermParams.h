/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) PermParams.h
 */

#ifndef _PermParams_H
#define _PermParams_H

/*
 * ---------------------------------------------------------------
 * Includes:
 */


/*
 * ---------------------------------------------------------------
 * Definitions:
 */


class PermParams {
    
private:
    
    double lw_, low_, log_, lg_;
    double cnw_, cno_, cng_;
    double epsl_;
    
public:
    
    const static double DEFAULT_LW;
    const static double DEFAULT_LOW;
    const static double DEFAULT_LG;
    const static double DEFAULT_LOG;
    const static double DEFAULT_CNW;
    const static double DEFAULT_CNO;
    const static double DEFAULT_CNG;
    const static double DEFAULT_EPSL;
    
    
    PermParams(double lw, double low, double lg, double log, double cnw, double cno, double cng, double epsl);
    
    
    PermParams();
    
    double lw();
    
    double low();
    
    double lg();
    
    double log();
    
    double cnw();
    
    double cno();
    
    double cng();
    
    double epsl();
    
    void reset();
    
    
    
};

inline double PermParams::lw() { return lw_; }

inline double PermParams::low() { return low_; }

inline double PermParams::lg() { return lg_; }

inline double PermParams::log() { return log_; }

inline double PermParams::cnw() { return cnw_; }

inline double PermParams::cno() { return cno_; }

inline double PermParams::cng() { return cng_; }

inline double PermParams::epsl() { return epsl_; }



#endif //! _PermParams_H
