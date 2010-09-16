/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import rpn.component.BifurcationCurveGeom;
import rpn.component.BifurcationCurveGeomFactory;
import rpn.component.RpGeometry;
import rpn.parser.RPnDataModule;
import rpn.usecase.BifurcationRefineAgent;
import rpnumerics.methods.*;
import rpnumerics.methods.contour.support.CurveDomainManager;
import rpnumerics.methods.contour.support.DimensionDoenstMatch;
import rpnumerics.methods.contour.support.NoContourMethodDefined;

public class BifurcationCurveCalc implements RpCalculation {

    private BifurcationMethod bifurcationMethod_;
    private BifurcationParams params_;

    //
    // Constructors
    //
    public BifurcationCurveCalc() {
        params_ = new BifurcationParams();
        bifurcationMethod_ = new BifurcationContourMethod(params_);
    

    }

    public BifurcationCurveCalc(BifurcationParams params) {
        params_ = params;
        bifurcationMethod_ = new BifurcationContourMethod(params_);

    }

    public BifurcationCurveCalc(BifurcationContourMethod bifurcationMethod) {
        bifurcationMethod_ = bifurcationMethod;
        params_ = bifurcationMethod.getParams();
    }

    //
    // Accessors/Mutators
    //
    public int getFamilyIndex() {
        return params_.getFamilyIndex();
    }

    public RpSolution calc() {


        return bifurcationMethod_.curve();
    }

    public RpSolution recalc() {
        return calc();
    }

    public String getCalcMethodName() {
        throw new UnsupportedOperationException("Bifurcation");
    }


  


}
