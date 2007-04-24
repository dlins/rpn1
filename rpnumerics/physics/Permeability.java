/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpnumerics.physics;

import wave.util.RealVector;

public class Permeability {
    private double denkw_, denkg_, denkow_, denkog_;
    private PermParams params_;

    public Permeability() { this(new PermParams()); }

    public Permeability(PermParams params) {
        params_ = params;
        denkw_ = (params_.lw() + (1. - params_.lw()) * (1. - params_.cnw())) * (1. - params_.cnw());
        denkg_ = (params_.lg() + (1. - params_.lg()) * (1. - params_.cng())) * (1. - params_.cng());
        denkow_ = (params_.low() + (1. - params_.low()) * (1. - params_.cno())) * (1. - params_.cno());
        denkog_ = (params_.log() + (1. - params_.log()) * (1. - params_.cno())) * (1. - params_.cno());
    }

    public PermParams params() { return params_; }

    public double kw(double sw, double so, double sg) {
        double swcnw = sw - params_.cnw();
        if (swcnw <= 0.)
            return 0.;
        return (params_.lw() + (1. - params_.lw()) * swcnw) * swcnw / denkw_;
    }

    // computes kow divided by den = 1. - sw + params_.cnw()
    public double kowden(double sw, double so, double sg) {
        double sow = 1. - sw - params_.cno();
        if (sow <= 0.0)
            return 0.0;
        return (params_.low() + (1. - params_.low()) * sow) / denkow_;
    }

    // computes kog divided by den = 1. - sg + params_.cng()
    public double kogden(double sw, double so, double sg) {
        double sog = 1. - sg - params_.cno();
        if (sog <= 0.0)
            return 0.0;
        return (params_.log() + (1. - params_.log()) * sog) / denkog_;
    }

    public double ko(double sw, double so, double sg) {
        double socno = so - params_.cno();
        if (socno <= 0.)
            return 0.;
        return (params_.epsl() * (1. - params_.cno()) * kogden(sw, so, sg) * kowden(sw, so, sg) +
            (1. - params_.epsl()) * socno) * socno;
    }

    public double kg(double sw, double so, double sg) {
        double sgcng = sg - params_.cng();
        if (sgcng <= 0.)
            return 0.;
        return (params_.lg() + (1. - params_.lg()) * sgcng) * sgcng / denkg_;
    }

    public double dkwdso(double sw, double so, double sg) {
        return 0.;
    }

    public double dkwdsw(double sw, double so, double sg) {
        double swcnw = sw - params_.cnw();
        if (swcnw <= 0.)
            return 0.;
        return (params_.lw() + 2. * (1. - params_.lw()) * swcnw) / denkw_;
    }

    // computes the derivative of kow divided by den = 1. - sw + params_.cnw() relative to sw
    public double dkowdendsw(double sw, double so, double sg) {
        double sow = 1. - sw - params_.cno();
        if (sow <= 0.0)
            return 0.0;
        return (-1. + params_.low()) / denkow_;
    }

    public double dkogdendsg(double sw, double so, double sg) {
        double sog = 1. - sw - params_.cno();
        if (sog <= 0.0)
            return 0.0;
        return (-1. + params_.log()) / denkog_;
    }

    public double dkodsw(double sw, double so, double sg) {
        double socno = so - params_.cno();
        if (socno <= 0.)
            return 0.;
        return (-dkogdendsg(sw, so, sg) * kowden(sw, so, sg) + kogden(sw, so, sg) * dkowdendsw(sw, so, sg)) * socno *
            (1. - params_.cno()) * params_.epsl();
    }

    public double dkodso(double sw, double so, double sg) {
        double socno = so - params_.cno();
        if (socno <= 0.)
            return 0.;
        return (-dkogdendsg(sw, so, sg) * socno + kogden(sw, so, sg)) * kowden(sw, so, sg) * (1. - params_.cno()) *
            params_.epsl() + 2. * (1. - params_.epsl()) * socno;
    }

    public double dkgdsw(double sw, double so, double sg) {
        double sgcng = sg - params_.cng();
        if (sgcng <= 0.)
            return 0.;
        return -(params_.lg() + 2. * (1. - params_.lg()) * sgcng) / denkg_;
    }

    public double dkgdso(double sw, double so, double sg) {
        double sgcng = sg - params_.cng();
        if (sgcng <= 0.)
            return 0.;
        return -(params_.lg() + 2. * (1. - params_.lg()) * sgcng) / denkg_;
    }

    public double dkgdsg(double sw, double so, double sg) {
        double sgcng = sg - params_.cng();
        if (sgcng <= 0.)
            return 0.;
        return (params_.lg() + 2. * (1. - params_.lg()) * sgcng) / denkg_;
    }

    public double dkwdww(double sw, double so, double sg) {
        if (sw <= params_.cnw())
            return 0.;
        return 2. * (1. - params_.lw()) / denkw_;
    }

    public double dkwdwo(double sw, double so, double sg) {
        return 0.;
    }

    public double dkwdoo(double sw, double so, double sg) {
        return 0.;
    }

    // computes the second derivative of kow divided by
    // den = 1. - sw + params_.cnw() relative to sw
    public double dkowdendww(double sw, double so, double sg) {
        return 0.0;
    }

    // computes the second derivative of kog divided by
    // den = 1. - sg + params_.cng() relative to sg
    public double dkogdendgg(double sw, double so, double sg) {
        return 0.0;
    }

    // computes the second derivative of ko relative to sw
    public double dkodww(double sw, double so, double sg) {
        double socno = so - params_.cno();
        if (socno <= 0.)
            return 0.;
        return (dkogdendgg(sw, so, sg) * kowden(sw, so, sg) - 2. * dkogdendsg(sw, so, sg) * dkowdendsw(sw, so, sg) +
            kogden(sw, so, sg) * dkowdendww(sw, so, sg)) * socno * (1. - params_.cno()) * params_.epsl();
    }

    // computes the mixed second derivative of ko relative to sw and so
    public double dkodwo(double sw, double so, double sg) {
        double socno = so - params_.cno();
        if (socno <= 0.)
            return 0.;
        double dkowdendsw_ = dkowdendsw(sw, so, sg);
        double kowden_ = kowden(sw, so, sg);
        double dkogdendsg_ = dkogdendsg(sw, so, sg);
        return (-dkogdendsg_ * kowden_ + kogden(sw, so, sg) * dkowdendsw_) + socno *
            (dkogdendgg(sw, so, sg) * kowden_ - dkogdendsg_ * dkowdendsw_) * (1. - params_.cno()) * params_.epsl();
    }

    // computes the second derivative of ko relative to so
    public double dkodoo(double sw, double so, double sg) {
        double socno = so - params_.cno();
        if (socno <= 0.)
            return 0.;
        return (-2. * dkogdendsg(sw, so, sg) + socno * dkogdendgg(sw, so, sg)) * kowden(sw, so, sg) * (1. - params_.cno()) *
            params_.epsl() + 2. * (1. - params_.epsl());
    }

    public double dkgdww(double sw, double so, double sg) {
        if (sg <= params_.cng())
            return 0.;
        return 2. * (1. - params_.lg()) / denkg_;
    }

    public double dkgdwo(double sw, double so, double sg) {
        if (sg <= params_.cng())
            return 0.;
        return 2. * (1. - params_.lg()) / denkg_;
    }

    public double dkgdoo(double sw, double so, double sg) {
        if (sg <= params_.cng())
            return 0.;
        return 2. * (1. - params_.lg()) / denkg_;
    }
}
