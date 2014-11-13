
cSRC +=  \
	../../../../canvas/pnm.c


ccSRC +=  \
	../../../../canvas/DoubleMatrixDisplay.cc \
	../../../../canvas/TestTools.cc \
	../../../../Model/TrivialAccumulationFunction.cc \
	../../../../Model/Parameter.cc \
	../../../../Model/Physics.cc \
	../../../../Model/SubPhysics.cc \
	../../../../rpnumerics/viscousprofile/StationaryPoint.cc \
	../../../../rpnumerics/Extension_Curve.cc \
	../../../../rpnumerics/EllipticExtension.cc \
	../../../../rpnumerics/Secondary_Bifurcation.cc \
	../../../../rpnumerics/RarefactionCalc.cc \
	../../../../rpnumerics/ShockFlowParams.cc \
	../../../../rpnumerics/AccumulationFunction.cc \
	../../../../rpnumerics/Elliptic_Boundary.cc \
	../../../../rpnumerics/DefaultAccumulationFunction.cc \
	../../../../rpnumerics/Inflection_Curve.cc \
	../../../../rpnumerics/Hugoniot_Locus.cc \
	../../../../rpnumerics/WaveState.cc \
	../../../../rpnumerics/Shock.cc \
	../../../../rpnumerics/Double_Contact_Function.cc \
	../../../../rpnumerics/RPnCurve.cc \
	../../../../rpnumerics/WavePoint.cc \
	../../../../rpnumerics/AccumulationParams.cc \
	../../../../rpnumerics/methods/Coincidence_Contour/Coincidence_Contour.cc \
	../../../../rpnumerics/methods/WaveCurve/ExplicitTransitionalWavecurve.cc \
	../../../../rpnumerics/methods/WaveCurve/WaveCurveFactory.cc \
	../../../../rpnumerics/methods/WaveCurve/WaveCurve.cc \
	../../../../rpnumerics/methods/RiemannSolver/RiemannProblem.cc \
	../../../../rpnumerics/methods/EquationFunctionLevelCurve/EquationFunctionLevelCurve.cc \
	../../../../rpnumerics/methods/TransitionalShock/TransitionalShock.cc \
	../../../../rpnumerics/methods/HugoniotCurve/ImplicitHugoniotCurve.cc \
	../../../../rpnumerics/methods/HugoniotCurve/HugoniotCurve.cc \
	../../../../rpnumerics/methods/ReferencePoint.cc \
	../../../../rpnumerics/methods/Extension/Implicit_Extension_Curve.cc \
	../../../../rpnumerics/methods/Extension/Extension.cc \
	../../../../rpnumerics/methods/Viscosity_Matrix/ViscosityJetMatrix.cc \
	../../../../rpnumerics/methods/Viscosity_Matrix/Viscosity_Matrix.cc \
	../../../../rpnumerics/methods/BifurcationCurve/BifurcationCurve.cc \
	../../../../rpnumerics/methods/ShockCurve/ShockCurve.cc \
	../../../../rpnumerics/methods/ShockCurve/ClassifyShockCurve.cc \
	../../../../rpnumerics/methods/CompositeCurve/CompositeCurve.cc \
	../../../../rpnumerics/methods/Bisection/Bisection.cc \
	../../../../rpnumerics/methods/NewRarefactionCurve/NewRarefactionCurve.cc \
	../../../../rpnumerics/methods/contour/HugoniotFunctionClass.cc \
	../../../../rpnumerics/methods/contour/ContourMethod.cc \
	../../../../rpnumerics/methods/contour/HyperCube.cc \
	../../../../rpnumerics/methods/contour/Contour2p5_Method.cc \
	../../../../rpnumerics/methods/contour/Contour2x2_Method.cc \
	../../../../rpnumerics/methods/contour/GPU/Double_Contact_GPU.cc \
	../../../../rpnumerics/methods/contour/GPU/Contour2x2_Method_GPU.cc \
	../../../../rpnumerics/methods/RarefactionCurve/RarefactionCurve.cc \
	../../../../rpnumerics/methods/Secondary_Bifurcations/Explicit_Bifurcation_Curves/Explicit_Bifurcation_Curves.cc \
	../../../../rpnumerics/methods/CharacteristicPolynomialLevels/CharacteristicPolynomialLevels.cc \
	../../../../rpnumerics/methods/HugoniotContinuation/HugoniotContinuation_nDnD/HugoniotContinuation_nDnD.cc \
	../../../../rpnumerics/methods/HugoniotContinuation/HugoniotContinuation2D2D/HugoniotContinuation2D2D.cc \
	../../../../rpnumerics/methods/HugoniotContinuation/HugoniotContinuation_nD_nm1D/HugoniotContinuation_nD_nm1D.cc \
	../../../../rpnumerics/methods/HugoniotContinuation/HugoniotContinuation.cc \
	../../../../rpnumerics/methods/HugoniotContinuation/HugoniotContinuation3D2D/HugoniotContinuation3D2D.cc \
	../../../../rpnumerics/GridValues.cc \
	../../../../rpnumerics/ParametricPlot/ParametricPlot.cc \
	../../../../rpnumerics/FluxFunction.cc \
	../../../../rpnumerics/Hugoniot_Curve.cc \
	../../../../rpnumerics/RpSolution.cc \
	../../../../rpnumerics/Envelope_Curve.cc \
	../../../../rpnumerics/plugin/PluginService.cc \
	../../../../rpnumerics/plugin/RPnPluginManager.cc \
	../../../../rpnumerics/Newton_Improvement.cc \
	../../../../rpnumerics/characteristics/curvegen.cc \
	../../../../rpnumerics/characteristics/plotter.cc \
	../../../../rpnumerics/characteristics/interpol.cc \
	../../../../rpnumerics/Double_Contact.cc \
	../../../../rpnumerics/Hysteresis.cc \
	../../../../rpnumerics/RpCalculation.cc \
	../../../../rpnumerics/Rarefaction.cc \
	../../../../rpnumerics/FluxParams.cc \
	../../../../rpnumerics/Curve/Curve.cc \
	../../../../rpnumerics/PhasePoint.cc \
	../../../../rpnumerics/HugoniotSegment.cc \
	../../../../rpnumerics/RpFunction.cc \
	../../../../rpnumerics/DefaultAccumulationParams.cc \
	../../../../rpnumerics/physics/SimpleFlux/SimpleFlux.cc \
	../../../../rpnumerics/physics/JD/JDEvaporationCompositeCurve.cc \
	../../../../rpnumerics/physics/JD/JDFluxFunction.cc \
	../../../../rpnumerics/physics/JD/CoincidenceJD.cc \
	../../../../rpnumerics/physics/JD/JDSubPhysics.cc \
	../../../../rpnumerics/physics/JD/JDAccumulationFunction.cc \
	../../../../rpnumerics/physics/JD/JDEvap_Extension.cc \
	../../../../rpnumerics/physics/SplineFlux/SplineFlux.cc \
	../../../../rpnumerics/physics/CoreyQuad4Phase/ZeroImplicit.cc \
	../../../../rpnumerics/physics/CoreyQuad4Phase/CoreyQuad4PhaseHugoniotZeroImplicit.cc \
	../../../../rpnumerics/physics/CompositionalPhysics/TPCW/Double_Contact_TP.cc \
	../../../../rpnumerics/physics/CompositionalPhysics/TPCW/ShockContinuationMethod3D2D.cc \
	../../../../rpnumerics/physics/CompositionalPhysics/TPCW/ShockMethod.cc \
	../../../../rpnumerics/physics/CompositionalPhysics/Hugoniot_TP.cc \
	../../../../rpnumerics/physics/CompositionalPhysics/DeadVolatileVolatileGas/DeadVolatileVolatileGasAccumulationFunction.cc \
	../../../../rpnumerics/physics/CompositionalPhysics/DeadVolatileVolatileGas/DeadVolatileVolatileGasThermodynamics.cc \
	../../../../rpnumerics/physics/CompositionalPhysics/DeadVolatileVolatileGas/DeadVolatileVolatileGasCoincidence.cc \
	../../../../rpnumerics/physics/CompositionalPhysics/DeadVolatileVolatileGas/DeadVolatileVolatileGasSubPhysics.cc \
	../../../../rpnumerics/physics/CompositionalPhysics/DeadVolatileVolatileGas/DeadVolatileVolatileGasHydrodynamics.cc \
	../../../../rpnumerics/physics/CompositionalPhysics/DeadVolatileVolatileGas/DeadVolatileVolatileGasEvaporationExtension.cc \
	../../../../rpnumerics/physics/CompositionalPhysics/DeadVolatileVolatileGas/DeadVolatileVolatileGasCompositeCurve.cc \
	../../../../rpnumerics/physics/CompositionalPhysics/DeadVolatileVolatileGas/DeadVolatileVolatileGasFluxFunction.cc \
	../../../../rpnumerics/physics/CompositionalPhysics/ICDOW/ICDOWHydrodynamics.cc \
	../../../../rpnumerics/physics/CompositionalPhysics/ICDOW/ICDOWAccumulationFunction.cc \
	../../../../rpnumerics/physics/CompositionalPhysics/ICDOW/ICDOWFluxFunction.cc \
	../../../../rpnumerics/physics/CompositionalPhysics/ICDOW/ICDOWChemistry.cc \
	../../../../rpnumerics/physics/CompositionalPhysics/ICDOW/ICDOWSubPhysics.cc \
	../../../../rpnumerics/physics/Quad3SubPhysics/Quad3Equations.cc \
	../../../../rpnumerics/physics/SinglePhase/Thermodynamics.cc \
	../../../../rpnumerics/physics/Ion/IonAccumulation.cc \
	../../../../rpnumerics/physics/Ion/IonPermeability.cc \
	../../../../rpnumerics/physics/Ion/IonRatios.cc \
	../../../../rpnumerics/physics/Ion/IonAdsorption.cc \
	../../../../rpnumerics/physics/Ion/IonFlux.cc \
	../../../../rpnumerics/physics/ThreePhaseFlowPhysics/Foam/FoamPermeability.cc \
	../../../../rpnumerics/physics/ThreePhaseFlowPhysics/Foam/FoamFluxFunction.cc \
	../../../../rpnumerics/physics/ThreePhaseFlowPhysics/Foam/FoamViscosity.cc \
	../../../../rpnumerics/physics/ThreePhaseFlowPhysics/Foam/FoamSubPhysics.cc \
	../../../../rpnumerics/physics/ThreePhaseFlowPhysics/ThreePhaseFlowPermeability.cc \
	../../../../rpnumerics/physics/ThreePhaseFlowPhysics/StoneSubPhysics/StoneParams.cc \
	../../../../rpnumerics/physics/ThreePhaseFlowPhysics/StoneSubPhysics/StoneViscosity.cc \
	../../../../rpnumerics/physics/ThreePhaseFlowPhysics/StoneSubPhysics/Stone_Explicit_Bifurcation_Curves.cc \
	../../../../rpnumerics/physics/ThreePhaseFlowPhysics/StoneSubPhysics/StoneFluxFunction.cc \
	../../../../rpnumerics/physics/ThreePhaseFlowPhysics/StoneSubPhysics/StonePermeability.cc \
	../../../../rpnumerics/physics/ThreePhaseFlowPhysics/StoneSubPhysics/StoneSubPhysics.cc \
	../../../../rpnumerics/physics/ThreePhaseFlowPhysics/StoneSubPhysics/StonePermParams.cc \
	../../../../rpnumerics/physics/ThreePhaseFlowPhysics/Sorbie/SorbiePermeability.cc \
	../../../../rpnumerics/physics/ThreePhaseFlowPhysics/ThreePhaseFlowPermeabilityLevelCurve.cc \
	../../../../rpnumerics/physics/ThreePhaseFlowPhysics/ThreePhaseFlowSubPhysics.cc \
	../../../../rpnumerics/physics/ThreePhaseFlowPhysics/CoreyQuadSubPhysics/CoreyQuadSubPhysics.cc \
	../../../../rpnumerics/physics/ThreePhaseFlowPhysics/CoreyQuadSubPhysics/CoreyQuadViscosity.cc \
	../../../../rpnumerics/physics/ThreePhaseFlowPhysics/CoreyQuadSubPhysics/CoreyQuadPermeability.cc \
	../../../../rpnumerics/physics/ThreePhaseFlowPhysics/CoreyQuadSubPhysics/CoreyQuadWaveCurveFactory.cc \
	../../../../rpnumerics/physics/ThreePhaseFlowPhysics/CoreyQuadSubPhysics/CoreyQuadExplicitHugoniotCurve.cc \
	../../../../rpnumerics/physics/ThreePhaseFlowPhysics/CoreyQuadSubPhysics/CoreyQuadTransitionalLine.cc \
	../../../../rpnumerics/physics/ThreePhaseFlowPhysics/ThreePhaseFlowImplicitHugoniotCurve.cc \
	../../../../rpnumerics/physics/ThreePhaseFlowPhysics/ThreePhaseFlowAccumulation.cc \
	../../../../rpnumerics/physics/ThreePhaseFlowPhysics/Three_Phase_Flow_Explicit_Bifurcation_Curves.cc \
	../../../../rpnumerics/physics/ThreePhaseFlowPhysics/ThreePhaseFlowViscosity.cc \
	../../../../rpnumerics/physics/ThreePhaseFlowPhysics/ThreePhaseFlowFluxFunction.cc \
	../../../../rpnumerics/physics/ThreePhaseFlowPhysics/Brooks_CoreySubPhysics/Brooks_CoreyViscosity.cc \
	../../../../rpnumerics/physics/ThreePhaseFlowPhysics/Brooks_CoreySubPhysics/Brooks_CoreyPermeability.cc \
	../../../../rpnumerics/physics/ThreePhaseFlowPhysics/Brooks_CoreySubPhysics/Brooks_CoreyFluxFunction.cc \
	../../../../rpnumerics/physics/ThreePhaseFlowPhysics/Brooks_CoreySubPhysics/Brooks_CoreySubPhysics.cc \
	../../../../rpnumerics/physics/ThreePhaseFlowPhysics/ThreePhaseFlowEquationFunctionLevelCurve.cc \
	../../../../rpnumerics/physics/ThreePhaseFlowPhysics/Koval/KovalFluxFunction.cc \
	../../../../rpnumerics/physics/ThreePhaseFlowPhysics/Koval/KovalSubPhysics.cc \
	../../../../rpnumerics/physics/ThreePhaseFlowPhysics/Koval/KovalViscosity.cc \
	../../../../rpnumerics/physics/ThreePhaseFlowPhysics/Koval/KovalPermeability.cc \
	../../../../rpnumerics/physics/quad/Quad3AccumulationFunction.cc \
	../../../../rpnumerics/physics/quad/Quad3FluxParams.cc \
	../../../../rpnumerics/physics/quad/Quad4FluxFunction.cc \
	../../../../rpnumerics/physics/quad/Quad2HugoniotFunction.cc \
	../../../../rpnumerics/physics/quad/Quad4FluxParams.cc \
	../../../../rpnumerics/physics/quad/QuadWaveState.cc \
	../../../../rpnumerics/physics/quad/Quad4AccumulationFunction.cc \
	../../../../rpnumerics/physics/quad/Quad2FluxParams.cc \
	../../../../rpnumerics/physics/quad/Quad2AccumulationFunction.cc \
	../../../../rpnumerics/physics/quad/Quad2FluxFunction.cc \
	../../../../rpnumerics/physics/quad/Quad3FluxFunction.cc \
	../../../../rpnumerics/physics/Quad2C1SubPhysics/Quad2C1Equations.cc \
	../../../../rpnumerics/Implicit_Curve.cc \
	../../../../JetTester/TestableJet.cc \
	../../../../JetTester/JetTester2D.cc \
	../../../../JetTester/JetTester1D.cc \
	../../../../JetTester/JetTester.cc \
	../../../../wave/util/eigen.cc \
	../../../../wave/util/errorhandler.cc \
	../../../../wave/util/RectBoundary.cc \
	../../../../wave/util/Boundary.cc \
	../../../../wave/util/bool.cc \
	../../../../wave/util/except.cc \
	../../../../wave/util/HessianMatrix.cc \
	../../../../wave/util/IsoTriang2DBoundary.cc \
	../../../../wave/util/IntArray.cc \
	../../../../wave/util/RealSegment.cc \
	../../../../wave/util/JetMatrix.cc \
	../../../../wave/util/Line.cc \
	../../../../wave/util/mathutil.cc \
	../../../../wave/util/RealVector.cc \
	../../../../wave/util/JacobianMatrix.cc \
	../../../../wave/util/Utilities.cc \
	../../../../wave/util/RealMatrix2.cc \
	../../../../wave/multid/Space.cc \
	../../../../wave/multid/Multid.cc \
	../../../../wave/ode/EulerSolver/EulerSolver.cc \
	../../../../wave/ode/LSODE/LSODE.cc \
	../../../../Equations/Equations.cc


cppSRC +=  \
	../../../../canvas/Text.cpp \
	../../../../canvas/canvasmenupack.cpp \
	../../../../canvas/curve2d.cpp \
	../../../../canvas/GridValuesPlot.cpp \
	../../../../canvas/canvasmenu.cpp \
	../../../../canvas/marchingsquares.cpp \
	../../../../canvas/quiverplot.cpp \
	../../../../canvas/graphicobject.cpp \
	../../../../canvas/canvas.cpp \
	../../../../canvas/point2d.cpp \
	../../../../canvas/MultiColoredCurve.cpp \
	../../../../canvas/WaveCurvePlot.cpp \
	../../../../canvas/canvasmenuscroll.cpp \
	../../../../canvas/ArrowHead.cpp \
	../../../../canvas/segmentedcurve.cpp \
	../../../../canvas/Dump_Fl_Widget/Dump_Fl_Widget.cpp \
	../../../../rpnumerics/Eigenvalue_Contour.cpp \
	../../../../rpnumerics/methods/HugoniotCurve/ColorCurve.cpp \
	../../../../rpnumerics/methods/Viscosity_Matrix/DoubleMatrix.cpp \
	../../../../rpnumerics/CoincidenceTP/CoincidenceTP.cpp \
	../../../../rpnumerics/physics/CompositionalPhysics/TPCW/SubinflectionTP.cpp \
	../../../../rpnumerics/physics/CompositionalPhysics/TPCW/Flux2Comp2PhasesAdimensionalized.cpp \
	../../../../rpnumerics/physics/CompositionalPhysics/TPCW/Thermodynamics_SuperCO2_WaterAdimensionalized.cpp \
	../../../../rpnumerics/physics/CompositionalPhysics/TPCW/Accum2Comp2PhasesAdimensionalized.cpp \
	../../../../rpnumerics/physics/CompositionalPhysics/TPCW/Accum2Comp2PhasesAdimensionalized_Params.cpp \
	../../../../rpnumerics/physics/CompositionalPhysics/TPCW/TrivialAccumulation_Params.cpp \
	../../../../rpnumerics/physics/CompositionalPhysics/TPCW/TrivialAccumulation.cpp \
	../../../../rpnumerics/physics/CompositionalPhysics/TPCW/Flux2Comp2PhasesAdimensionalized_Params.cpp \
	../../../../rpnumerics/physics/polydisperse/Polydisperse_Params.cpp \
	../../../../rpnumerics/physics/polydisperse/Polydisperse.cpp \
	../../../../rpnumerics/physics/SinglePhase/JetSinglePhaseLiquid.cpp \
	../../../../rpnumerics/physics/SinglePhase/FluxSinglePhaseLiquidAdimensionalized.cpp \
	../../../../rpnumerics/physics/SinglePhase/FluxSinglePhaseVaporAdimensionalized.cpp \
	../../../../rpnumerics/physics/SinglePhase/AccumulationSinglePhaseVaporAdimensionalized_Params.cpp \
	../../../../rpnumerics/physics/SinglePhase/FluxSinglePhaseLiquidAdimensionalized_Params.cpp \
	../../../../rpnumerics/physics/SinglePhase/JetSinglePhaseVapor.cpp \
	../../../../rpnumerics/physics/SinglePhase/VLE_Flash_TPCW.cpp \
	../../../../rpnumerics/physics/SinglePhase/Thermodynamics_SuperCO2_Water_SinglePhases_Adimensionalized.cpp \
	../../../../rpnumerics/physics/SinglePhase/AccumulationSinglePhaseVaporAdimensionalized.cpp \
	../../../../rpnumerics/physics/SinglePhase/AccumulationSinglePhaseLiquidAdimensionalized_Params.cpp \
	../../../../rpnumerics/physics/SinglePhase/FluxSinglePhaseVaporAdimensionalized_Params.cpp \
	../../../../rpnumerics/physics/SinglePhase/AccumulationSinglePhaseLiquidAdimensionalized.cpp \
	../../../../rpnumerics/physics/SinglePhase/MolarDensity.cpp \
	../../../../rpnumerics/physics/ThreePhaseFlowPhysics/CoreyQuadSubPhysics/CoreyQuadFluxFunction.cpp \
	../../../../rpnumerics/physics/corey/CoreyQuad_Params.cpp \
	../../../../wave/util/alglib/alglib/safesolve.cpp \
	../../../../wave/util/alglib/alglib/idwint.cpp \
	../../../../wave/util/alglib/alglib/corr.cpp \
	../../../../wave/util/alglib/alglib/jarquebera.cpp \
	../../../../wave/util/alglib/alglib/elliptic.cpp \
	../../../../wave/util/alglib/alglib/apserv.cpp \
	../../../../wave/util/alglib/alglib/mincg.cpp \
	../../../../wave/util/alglib/alglib/dawson.cpp \
	../../../../wave/util/alglib/alglib/bdss.cpp \
	../../../../wave/util/alglib/alglib/fdistr.cpp \
	../../../../wave/util/alglib/alglib/chebyshev.cpp \
	../../../../wave/util/alglib/alglib/bdsvd.cpp \
	../../../../wave/util/alglib/alglib/studentttests.cpp \
	../../../../wave/util/alglib/alglib/airyf.cpp \
	../../../../wave/util/alglib/alglib/minlm.cpp \
	../../../../wave/util/alglib/alglib/ldlt.cpp \
	../../../../wave/util/alglib/alglib/sdet.cpp \
	../../../../wave/util/alglib/alglib/igammaf.cpp \
	../../../../wave/util/alglib/alglib/linreg.cpp \
	../../../../wave/util/alglib/alglib/rcond.cpp \
	../../../../wave/util/alglib/alglib/ablas.cpp \
	../../../../wave/util/alglib/alglib/ratint.cpp \
	../../../../wave/util/alglib/alglib/lda.cpp \
	../../../../wave/util/alglib/alglib/bessel.cpp \
	../../../../wave/util/alglib/alglib/ablasf.cpp \
	../../../../wave/util/alglib/alglib/ortfac.cpp \
	../../../../wave/util/alglib/alglib/matinv.cpp \
	../../../../wave/util/alglib/alglib/creflections.cpp \
	../../../../wave/util/alglib/alglib/nearunityunit.cpp \
	../../../../wave/util/alglib/alglib/normaldistr.cpp \
	../../../../wave/util/alglib/alglib/sblas.cpp \
	../../../../wave/util/alglib/alglib/ftbase.cpp \
	../../../../wave/util/alglib/alglib/correlationtests.cpp \
	../../../../wave/util/alglib/alglib/mlpe.cpp \
	../../../../wave/util/alglib/alglib/trfac.cpp \
	../../../../wave/util/alglib/alglib/pca.cpp \
	../../../../wave/util/alglib/alglib/stest.cpp \
	../../../../wave/util/alglib/alglib/studenttdistr.cpp \
	../../../../wave/util/alglib/alglib/kmeans.cpp \
	../../../../wave/util/alglib/alglib/gammafunc.cpp \
	../../../../wave/util/alglib/alglib/ialglib.cpp \
	../../../../wave/util/alglib/alglib/tsort.cpp \
	../../../../wave/util/alglib/alglib/hermite.cpp \
	../../../../wave/util/alglib/alglib/logit.cpp \
	../../../../wave/util/alglib/alglib/spline1d.cpp \
	../../../../wave/util/alglib/alglib/xblas.cpp \
	../../../../wave/util/alglib/alglib/binomialdistr.cpp \
	../../../../wave/util/alglib/alglib/gkq.cpp \
	../../../../wave/util/alglib/alglib/conv.cpp \
	../../../../wave/util/alglib/alglib/wsr.cpp \
	../../../../wave/util/alglib/alglib/fft.cpp \
	../../../../wave/util/alglib/alglib/fresnel.cpp \
	../../../../wave/util/alglib/alglib/ssolve.cpp \
	../../../../wave/util/alglib/alglib/dforest.cpp \
	../../../../wave/util/alglib/alglib/evd.cpp \
	../../../../wave/util/alglib/alglib/polint.cpp \
	../../../../wave/util/alglib/alglib/mannwhitneyu.cpp \
	../../../../wave/util/alglib/alglib/hqrnd.cpp \
	../../../../wave/util/alglib/alglib/inverseupdate.cpp \
	../../../../wave/util/alglib/alglib/pspline.cpp \
	../../../../wave/util/alglib/alglib/reflections.cpp \
	../../../../wave/util/alglib/alglib/jacobianelliptic.cpp \
	../../../../wave/util/alglib/alglib/trlinsolve.cpp \
	../../../../wave/util/alglib/alglib/matgen.cpp \
	../../../../wave/util/alglib/alglib/matdet.cpp \
	../../../../wave/util/alglib/alglib/chisquaredistr.cpp \
	../../../../wave/util/alglib/alglib/trigintegrals.cpp \
	../../../../wave/util/alglib/alglib/svd.cpp \
	../../../../wave/util/alglib/alglib/estnorm.cpp \
	../../../../wave/util/alglib/alglib/schur.cpp \
	../../../../wave/util/alglib/alglib/spline3.cpp \
	../../../../wave/util/alglib/alglib/ratinterpolation.cpp \
	../../../../wave/util/alglib/alglib/ap.cpp \
	../../../../wave/util/alglib/alglib/mlptrain.cpp \
	../../../../wave/util/alglib/alglib/spdgevd.cpp \
	../../../../wave/util/alglib/alglib/blas.cpp \
	../../../../wave/util/alglib/alglib/variancetests.cpp \
	../../../../wave/util/alglib/alglib/mlpbase.cpp \
	../../../../wave/util/alglib/alglib/srcond.cpp \
	../../../../wave/util/alglib/alglib/ibetaf.cpp \
	../../../../wave/util/alglib/alglib/laguerre.cpp \
	../../../../wave/util/alglib/alglib/fht.cpp \
	../../../../wave/util/alglib/alglib/psif.cpp \
	../../../../wave/util/alglib/alglib/gq.cpp \
	../../../../wave/util/alglib/alglib/sinverse.cpp \
	../../../../wave/util/alglib/alglib/betaf.cpp \
	../../../../wave/util/alglib/alglib/autogk.cpp \
	../../../../wave/util/alglib/alglib/minlbfgs.cpp \
	../../../../wave/util/alglib/alglib/expintegrals.cpp \
	../../../../wave/util/alglib/alglib/spline2d.cpp \
	../../../../wave/util/alglib/alglib/nearestneighbor.cpp \
	../../../../wave/util/alglib/alglib/poissondistr.cpp \
	../../../../wave/util/alglib/alglib/densesolver.cpp \
	../../../../wave/util/alglib/alglib/linmin.cpp \
	../../../../wave/util/alglib/alglib/correlation.cpp \
	../../../../wave/util/alglib/alglib/minasa.cpp \
	../../../../wave/util/alglib/alglib/legendre.cpp \
	../../../../wave/util/alglib/alglib/descriptivestatistics.cpp \
	../../../../wave/util/alglib/alglib/odesolver.cpp \
	../../../../wave/util/alglib/alglib/hblas.cpp \
	../../../../wave/util/alglib/alglib/hsschur.cpp \
	../../../../wave/util/alglib/alglib/rotations.cpp \
	../../../../wave/util/alglib/alglib/lsfit.cpp \
	../../../../wave/util/BoxND.cpp \
	../../../../wave/util/PointND.cpp


FSRC +=  \
	../../../../wave/ode/LSODE/lsode2.F \
	../../../../wave/ode/LSODE/lsode3.F \
	../../../../wave/ode/LSODE/lsode.F

