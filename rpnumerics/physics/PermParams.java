/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpnumerics.physics;

public class PermParams {
    static public final double DEFAULT_LW = 0d;
    static public final double DEFAULT_LOW = 0d;
    static public final double DEFAULT_LG = 0d;
    static public final double DEFAULT_LOG = 0d;
    static public final double DEFAULT_CNW = 0d;
    static public final double DEFAULT_CNO = 0d;
    static public final double DEFAULT_CNG = 0d;
    static public final double DEFAULT_EPSL = 0d;
    private double lw_, low_, log_, lg_;
    private double cnw_, cno_, cng_;
    private double epsl_;

    public PermParams(double lw, double low, double lg, double log, double cnw, double cno, double cng, double epsl) {
        lw_ = lw; low_ = low;
        lg_ = lg; log_ = log;
        cnw_ = cnw; cno_ = cno; cng_ = cng;
        epsl_ = epsl;
    }

    public PermParams() { reset(); }

    public double lw() { return lw_; }

    public double low() { return low_; }

    public double lg() { return lg_; }

    public double log() { return log_; }

    public double cnw() { return cnw_; }

    public double cno() { return cno_; }

    public double cng() { return cng_; }

    public double epsl() { return epsl_; }

    public void reset() {
        lw_ = DEFAULT_LW;
        low_ = DEFAULT_LOW;
        lg_ = DEFAULT_LG;
        log_ = DEFAULT_LOG;
        cnw_ = DEFAULT_CNW;
        cno_ = DEFAULT_CNO;
        cng_ = DEFAULT_CNG;
        epsl_ = DEFAULT_EPSL;
    }
}
