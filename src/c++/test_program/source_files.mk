
cSRC +=  \
	canvas/pnm.c


ccSRC +=  \
	../Model/SubPhysics.cc \
	../Model/TrivialAccumulationFunction.cc \
	../Model/Parameter.cc \
	../Model/CheckParameter.cc \
	../Model/Physics.cc \
	../Observer_Subject/Subject.cc \
	../Observer_Subject/Observer.cc \
	../JetTester/JetTester1D.cc \
	../JetTester/JetTester.cc \
	../JetTester/JetTester2D.cc \
	../JetTester/TestableJet.cc \
	../rpnumerics/viscousprofile/StationaryPoint.cc \
	../rpnumerics/RpFunction.cc \
	../rpnumerics/Hugoniot_Locus.cc \
	../rpnumerics/ShockFlowParams.cc \
	../rpnumerics/FluxFunction.cc \
	../rpnumerics/PhasePoint.cc \
	../rpnumerics/RarefactionCalc.cc \
	../rpnumerics/Envelope_Curve.cc \
	../rpnumerics/Elliptic_Boundary.cc \
	../rpnumerics/Hugoniot_Curve.cc \
	../rpnumerics/EllipticExtension.cc \
	../rpnumerics/WavePoint.cc \
	../rpnumerics/Inflection_Curve.cc \
	../rpnumerics/Implicit_Curve.cc \
	../rpnumerics/DefaultAccumulationFunction.cc \
	../rpnumerics/Curve/Curve.cc \
	../rpnumerics/Newton_Improvement.cc \
	../rpnumerics/AccumulationFunction.cc \
	../rpnumerics/plugin/PluginService.cc \
	../rpnumerics/plugin/RPnPluginManager.cc \
	../rpnumerics/RpCalculation.cc \
	../rpnumerics/RPnCurve.cc \
	../rpnumerics/methods/TransitionalShock/TransitionalShock.cc \
	../rpnumerics/methods/Extension/Extension.cc \
	../rpnumerics/methods/Extension/Implicit_Extension_Curve.cc \
	../rpnumerics/methods/EquationFunctionLevelCurve/EquationFunctionLevelCurve.cc \
	../rpnumerics/methods/contour/Contour2p5_Method.cc \
	../rpnumerics/methods/contour/ContourMethodPure.cc \
	../rpnumerics/methods/contour/Contour2x2_Method.cc \
	../rpnumerics/methods/contour/ContourMethod.cc \
	../rpnumerics/methods/contour/HugoniotFunctionClass.cc \
	../rpnumerics/methods/contour/GPU/Contour2x2_Method_GPU.cc \
	../rpnumerics/methods/contour/GPU/Double_Contact_GPU.cc \
	../rpnumerics/methods/contour/HyperCube.cc \
	../rpnumerics/methods/NewRarefactionCurve/NewRarefactionCurve.cc \
	../rpnumerics/methods/ReferencePoint.cc \
	../rpnumerics/methods/RiemannSolver/RiemannProblem.cc \
	../rpnumerics/methods/ContactRegionBoundary/ContactRegionBoundary.cc \
	../rpnumerics/methods/RarefactionCurve/RarefactionCurve.cc \
	../rpnumerics/methods/Bisection/Bisection.cc \
	../rpnumerics/methods/Coincidence_Contour/Coincidence_Contour.cc \
	../rpnumerics/methods/ShockCurve/ShockCurve.cc \
	../rpnumerics/methods/ShockCurve/ClassifyShockCurve.cc \
	../rpnumerics/methods/BifurcationCurve/BifurcationCurve.cc \
	../rpnumerics/methods/HugoniotContinuation/HugoniotContinuation_nDnD/HugoniotContinuation_nDnD.cc \
	../rpnumerics/methods/HugoniotContinuation/HugoniotContinuation_nD_nm1D/HugoniotContinuation_nD_nm1D.cc \
	../rpnumerics/methods/HugoniotContinuation/HugoniotContinuation2D2D/HugoniotContinuation2D2D.cc \
	../rpnumerics/methods/HugoniotContinuation/HugoniotContinuation.cc \
	../rpnumerics/methods/HugoniotContinuation/HugoniotContinuation3D2D/HugoniotContinuation3D2D.cc \
	../rpnumerics/methods/Secondary_Bifurcations/Explicit_Bifurcation_Curves/Explicit_Bifurcation_Curves.cc \
	../rpnumerics/methods/Viscosity_Matrix/Viscosity_Matrix.cc \
	../rpnumerics/methods/Viscosity_Matrix/ViscosityJetMatrix.cc \
	../rpnumerics/methods/HugoniotODE/HugoniotODE.cc \
	../rpnumerics/methods/HugoniotCurve/HugoniotCurve.cc \
	../rpnumerics/methods/HugoniotCurve/ImplicitHugoniotCurve.cc \
	../rpnumerics/methods/CompositeCurve/CompositeCurve.cc \
	../rpnumerics/methods/WaveCurve/WaveCurve.cc \
	../rpnumerics/methods/WaveCurve/ExplicitTransitionalWavecurve.cc \
	../rpnumerics/methods/WaveCurve/WaveCurveFactory.cc \
	../rpnumerics/methods/CharacteristicPolynomialLevels/CharacteristicPolynomialLevels.cc \
	../rpnumerics/Double_Contact_Function.cc \
	../rpnumerics/FluxParams.cc \
	../rpnumerics/AccumulationParams.cc \
	../rpnumerics/Secondary_Bifurcation.cc \
	../rpnumerics/RpSolution.cc \
	../rpnumerics/characteristics/plotter.cc \
	../rpnumerics/characteristics/interpol.cc \
	../rpnumerics/characteristics/curvegen.cc \
	../rpnumerics/HugoniotSegment.cc \
	../rpnumerics/ParametricPlot/ParametricPlot.cc \
	../rpnumerics/Double_Contact.cc \
	../rpnumerics/WaveState.cc \
	../rpnumerics/Hysteresis.cc \
	../rpnumerics/DefaultAccumulationParams.cc \
	../rpnumerics/GridValues.cc \
	../rpnumerics/physics/quad/Quad4AccumulationFunction.cc \
	../rpnumerics/physics/quad/QuadWaveState.cc \
	../rpnumerics/physics/quad/Quad4FluxFunction.cc \
	../rpnumerics/physics/quad/Quad3FluxFunction.cc \
	../rpnumerics/physics/quad/Quad3AccumulationFunction.cc \
	../rpnumerics/physics/quad/Quad3FluxParams.cc \
	../rpnumerics/physics/quad/Quad4FluxParams.cc \
	../rpnumerics/physics/Quad2C1SubPhysics/Quad2C1Equations.cc \
	../rpnumerics/physics/Quad3SubPhysics/Quad3Equations.cc \
	../rpnumerics/physics/SplineFlux/SplineFlux.cc \
	../rpnumerics/physics/Quad2SubPhysics/Quad2HugoniotFunction.cc \
	../rpnumerics/physics/Quad2SubPhysics/Quad2ExplicitHugoniotCurve.cc \
	../rpnumerics/physics/Quad2SubPhysics/Quad2FluxFunction.cc \
	../rpnumerics/physics/Quad2SubPhysics/Quad2AccumulationFunction.cc \
	../rpnumerics/physics/Quad2SubPhysics/Quad2SubPhysics.cc \
	../rpnumerics/physics/Ion/IonAdsorption.cc \
	../rpnumerics/physics/Ion/IonAccumulation.cc \
	../rpnumerics/physics/Ion/IonPermeability.cc \
	../rpnumerics/physics/Ion/IonFlux.cc \
	../rpnumerics/physics/Ion/IonRatios.cc \
	../rpnumerics/physics/CompositionalPhysics/TPCWPhysics/TPCW_SinglePhase/SinglePhaseBoundary.cc \
	../rpnumerics/physics/CompositionalPhysics/TPCWPhysics/TPCW_SinglePhase/Common/Thermodynamics.cc \
	../rpnumerics/physics/CompositionalPhysics/TPCWPhysics/TPCWSubPhysics/TPCWSubPhysics.cc \
	../rpnumerics/physics/CompositionalPhysics/DeadVolatileVolatileGas/DeadVolatileVolatileGasAccumulationFunction.cc \
	../rpnumerics/physics/CompositionalPhysics/DeadVolatileVolatileGas/DeadVolatileVolatileGasSubPhysics.cc \
	../rpnumerics/physics/CompositionalPhysics/DeadVolatileVolatileGas/DeadVolatileVolatileGasCompositeCurve.cc \
	../rpnumerics/physics/CompositionalPhysics/DeadVolatileVolatileGas/DeadVolatileVolatileGasEvaporationExtension.cc \
	../rpnumerics/physics/CompositionalPhysics/DeadVolatileVolatileGas/DeadVolatileVolatileGasCoincidence.cc \
	../rpnumerics/physics/CompositionalPhysics/DeadVolatileVolatileGas/DeadVolatileVolatileGasThermodynamics.cc \
	../rpnumerics/physics/CompositionalPhysics/DeadVolatileVolatileGas/DeadVolatileVolatileGasHydrodynamics.cc \
	../rpnumerics/physics/CompositionalPhysics/DeadVolatileVolatileGas/DeadVolatileVolatileGasFluxFunction.cc \
	../rpnumerics/physics/CompositionalPhysics/ICDOW/ICDOWFluxFunction.cc \
	../rpnumerics/physics/CompositionalPhysics/ICDOW/ICDOWHydrodynamics.cc \
	../rpnumerics/physics/CompositionalPhysics/ICDOW/ICDOWSubPhysics.cc \
	../rpnumerics/physics/CompositionalPhysics/ICDOW/ICDOWAccumulationFunction.cc \
	../rpnumerics/physics/CompositionalPhysics/ICDOW/ICDOWCoincidence.cc \
	../rpnumerics/physics/CompositionalPhysics/ICDOW/ICDOWChemistry.cc \
	../rpnumerics/physics/CompositionalPhysics/Hugoniot_TP.cc \
	../rpnumerics/physics/SimpleFlux/SimpleFlux.cc \
	../rpnumerics/physics/JD/JDEvaporationCompositeCurve.cc \
	../rpnumerics/physics/JD/JDEvap_Extension.cc \
	../rpnumerics/physics/JD/JDSubPhysics.cc \
	../rpnumerics/physics/JD/CoincidenceJD.cc \
	../rpnumerics/physics/JD/JDFluxFunction.cc \
	../rpnumerics/physics/JD/JDAccumulationFunction.cc \
	../rpnumerics/physics/ThreePhaseFlowPhysics/ThreePhaseFlowMobilityLevelCurve.cc \
	../rpnumerics/physics/ThreePhaseFlowPhysics/ThreePhaseFlowViscosity.cc \
	../rpnumerics/physics/ThreePhaseFlowPhysics/ThreePhaseFlowSubPhysics.cc \
	../rpnumerics/physics/ThreePhaseFlowPhysics/Brooks_CoreySubPhysics/Brooks_CoreyPermeability.cc \
	../rpnumerics/physics/ThreePhaseFlowPhysics/Brooks_CoreySubPhysics/Brooks_CoreyViscosity.cc \
	../rpnumerics/physics/ThreePhaseFlowPhysics/Brooks_CoreySubPhysics/Brooks_CoreyFluxFunction.cc \
	../rpnumerics/physics/ThreePhaseFlowPhysics/Brooks_CoreySubPhysics/Brooks_CoreySubPhysics.cc \
	../rpnumerics/physics/ThreePhaseFlowPhysics/ThreePhaseFlowEquationFunctionLevelCurve.cc \
	../rpnumerics/physics/ThreePhaseFlowPhysics/ThreePhaseFlowAccumulation.cc \
	../rpnumerics/physics/ThreePhaseFlowPhysics/ThreePhaseFlowPermeability.cc \
	../rpnumerics/physics/ThreePhaseFlowPhysics/ThreePhaseFlowPermeabilityLevelCurve.cc \
	../rpnumerics/physics/ThreePhaseFlowPhysics/ThreePhaseFlowHugoniotContinuation.cc \
	../rpnumerics/physics/ThreePhaseFlowPhysics/ThreePhaseFlowImplicitHugoniotCurve.cc \
	../rpnumerics/physics/ThreePhaseFlowPhysics/StoneSubPhysics/StonePermeability.cc \
	../rpnumerics/physics/ThreePhaseFlowPhysics/StoneSubPhysics/StoneSubPhysics.cc \
	../rpnumerics/physics/ThreePhaseFlowPhysics/StoneSubPhysics/StonePermParams.cc \
	../rpnumerics/physics/ThreePhaseFlowPhysics/StoneSubPhysics/StoneViscosity.cc \
	../rpnumerics/physics/ThreePhaseFlowPhysics/StoneSubPhysics/StoneParams.cc \
	../rpnumerics/physics/ThreePhaseFlowPhysics/StoneSubPhysics/StoneFluxFunction.cc \
	../rpnumerics/physics/ThreePhaseFlowPhysics/StoneSubPhysics/Stone_Explicit_Bifurcation_Curves.cc \
	../rpnumerics/physics/ThreePhaseFlowPhysics/Foam/FoamSubPhysics.cc \
	../rpnumerics/physics/ThreePhaseFlowPhysics/Foam/FoamShockObserver.cc \
	../rpnumerics/physics/ThreePhaseFlowPhysics/Foam/FoamViscosity.cc \
	../rpnumerics/physics/ThreePhaseFlowPhysics/Foam/FoamPermeability.cc \
	../rpnumerics/physics/ThreePhaseFlowPhysics/Foam/FoamFluxFunction.cc \
	../rpnumerics/physics/ThreePhaseFlowPhysics/CoreyQuadSubPhysics/CoreyQuadPermeability.cc \
	../rpnumerics/physics/ThreePhaseFlowPhysics/CoreyQuadSubPhysics/CoreyQuadTransitionalLine.cc \
	../rpnumerics/physics/ThreePhaseFlowPhysics/CoreyQuadSubPhysics/CoreyQuadExplicitHugoniotCurve.cc \
	../rpnumerics/physics/ThreePhaseFlowPhysics/CoreyQuadSubPhysics/CoreyQuadViscosity.cc \
	../rpnumerics/physics/ThreePhaseFlowPhysics/CoreyQuadSubPhysics/CoreyQuadWaveCurveFactory.cc \
	../rpnumerics/physics/ThreePhaseFlowPhysics/CoreyQuadSubPhysics/CoreyQuadSubPhysics.cc \
	../rpnumerics/physics/ThreePhaseFlowPhysics/Three_Phase_Flow_Explicit_Bifurcation_Curves.cc \
	../rpnumerics/physics/ThreePhaseFlowPhysics/Sorbie/SorbieFluxFunction.cc \
	../rpnumerics/physics/ThreePhaseFlowPhysics/Sorbie/SorbieViscosity.cc \
	../rpnumerics/physics/ThreePhaseFlowPhysics/Sorbie/SorbieSubPhysics.cc \
	../rpnumerics/physics/ThreePhaseFlowPhysics/Sorbie/SorbiePermeability.cc \
	../rpnumerics/physics/ThreePhaseFlowPhysics/Koval/KovalPermeability.cc \
	../rpnumerics/physics/ThreePhaseFlowPhysics/Koval/KovalViscosity.cc \
	../rpnumerics/physics/ThreePhaseFlowPhysics/Koval/KovalFluxFunction.cc \
	../rpnumerics/physics/ThreePhaseFlowPhysics/Koval/KovalSubPhysics.cc \
	../rpnumerics/physics/ThreePhaseFlowPhysics/ThreePhaseFlowMobility.cc \
	../rpnumerics/physics/ThreePhaseFlowPhysics/ThreePhaseFlowFluxFunction.cc \
	../rpnumerics/Extension_Curve.cc \
	../wave/multid/Multid.cc \
	../wave/multid/Space.cc \
	../wave/util/Eigenproblem/Eigenproblem2x2.cc \
	../wave/util/Eigenproblem/Eigenproblem.cc \
	../wave/util/Eigenproblem/Eigenpair.cc \
	../wave/util/mathutil.cc \
	../wave/util/RealSegment.cc \
	../wave/util/RealVector.cc \
	../wave/util/IsoTriang2DBoundary.cc \
	../wave/util/errorhandler.cc \
	../wave/util/eigen.cc \
	../wave/util/Line.cc \
	../wave/util/bool.cc \
	../wave/util/Boundary.cc \
	../wave/util/RealMatrix2.cc \
	../wave/util/GaussLegendreIntegral/GaussLegendreIntegral.cc \
	../wave/util/except.cc \
	../wave/util/JetMatrix.cc \
	../wave/util/IntArray.cc \
	../wave/util/RectBoundary.cc \
	../wave/util/HessianMatrix.cc \
	../wave/util/JacobianMatrix.cc \
	../wave/util/Utilities.cc \
	../wave/ode/LSODE/LSODE.cc \
	../wave/ode/EulerSolver/EulerSolver.cc \
	../Equations/Equations.cc \
	canvas/DoubleMatrixDisplay.cc \
	canvas/TestTools.cc


cppSRC +=  \
	../rpnumerics/Eigenvalue_Contour.cpp \
	../rpnumerics/methods/Viscosity_Matrix/DoubleMatrix.cpp \
	../rpnumerics/methods/HugoniotCurve/ColorCurve.cpp \
	../rpnumerics/physics/corey/CoreyQuad_Params.cpp \
	../rpnumerics/physics/polydisperse/Polydisperse.cpp \
	../rpnumerics/physics/polydisperse/Polydisperse_Params.cpp \
	../rpnumerics/physics/CompositionalPhysics/TPCWPhysics/TPCW_SinglePhase/Liquid/JetSinglePhaseLiquid.cpp \
	../rpnumerics/physics/CompositionalPhysics/TPCWPhysics/TPCW_SinglePhase/Liquid/FluxSinglePhaseLiquidAdimensionalized.cpp \
	../rpnumerics/physics/CompositionalPhysics/TPCWPhysics/TPCW_SinglePhase/Liquid/FluxSinglePhaseLiquidAdimensionalized_Params.cpp \
	../rpnumerics/physics/CompositionalPhysics/TPCWPhysics/TPCW_SinglePhase/Liquid/AccumulationSinglePhaseLiquidAdimensionalized.cpp \
	../rpnumerics/physics/CompositionalPhysics/TPCWPhysics/TPCW_SinglePhase/Flux2Comp2PhasesAdimensionalized_Params.cpp \
	../rpnumerics/physics/CompositionalPhysics/TPCWPhysics/TPCW_SinglePhase/Accum2Comp2PhasesAdimensionalized.cpp \
	../rpnumerics/physics/CompositionalPhysics/TPCWPhysics/TPCW_SinglePhase/Common/VLE_Flash_TPCW.cpp \
	../rpnumerics/physics/CompositionalPhysics/TPCWPhysics/TPCW_SinglePhase/Common/MolarDensity.cpp \
	../rpnumerics/physics/CompositionalPhysics/TPCWPhysics/TPCW_SinglePhase/Flux2Comp2PhasesAdimensionalized.cpp \
	../rpnumerics/physics/CompositionalPhysics/TPCWPhysics/TPCW_SinglePhase/Vapor/JetSinglePhaseVapor.cpp \
	../rpnumerics/physics/CompositionalPhysics/TPCWPhysics/TPCW_SinglePhase/Vapor/FluxSinglePhaseVaporAdimensionalized_Params.cpp \
	../rpnumerics/physics/CompositionalPhysics/TPCWPhysics/TPCW_SinglePhase/Vapor/AccumulationSinglePhaseVaporAdimensionalized.cpp \
	../rpnumerics/physics/CompositionalPhysics/TPCWPhysics/TPCW_SinglePhase/Vapor/FluxSinglePhaseVaporAdimensionalized.cpp \
	../rpnumerics/physics/ThreePhaseFlowPhysics/CoreyQuadSubPhysics/CoreyQuadFluxFunction.cpp \
	../wave/util/PointND.cpp \
	../wave/util/alglib/alglib/correlation.cpp \
	../wave/util/alglib/alglib/blas.cpp \
	../wave/util/alglib/alglib/mannwhitneyu.cpp \
	../wave/util/alglib/alglib/spdgevd.cpp \
	../wave/util/alglib/alglib/jarquebera.cpp \
	../wave/util/alglib/alglib/creflections.cpp \
	../wave/util/alglib/alglib/elliptic.cpp \
	../wave/util/alglib/alglib/idwint.cpp \
	../wave/util/alglib/alglib/ibetaf.cpp \
	../wave/util/alglib/alglib/normaldistr.cpp \
	../wave/util/alglib/alglib/ortfac.cpp \
	../wave/util/alglib/alglib/polint.cpp \
	../wave/util/alglib/alglib/stest.cpp \
	../wave/util/alglib/alglib/logit.cpp \
	../wave/util/alglib/alglib/legendre.cpp \
	../wave/util/alglib/alglib/mlpbase.cpp \
	../wave/util/alglib/alglib/apserv.cpp \
	../wave/util/alglib/alglib/svd.cpp \
	../wave/util/alglib/alglib/ldlt.cpp \
	../wave/util/alglib/alglib/trfac.cpp \
	../wave/util/alglib/alglib/studentttests.cpp \
	../wave/util/alglib/alglib/gkq.cpp \
	../wave/util/alglib/alglib/evd.cpp \
	../wave/util/alglib/alglib/ratinterpolation.cpp \
	../wave/util/alglib/alglib/psif.cpp \
	../wave/util/alglib/alglib/gammafunc.cpp \
	../wave/util/alglib/alglib/spline2d.cpp \
	../wave/util/alglib/alglib/poissondistr.cpp \
	../wave/util/alglib/alglib/variancetests.cpp \
	../wave/util/alglib/alglib/matdet.cpp \
	../wave/util/alglib/alglib/descriptivestatistics.cpp \
	../wave/util/alglib/alglib/kmeans.cpp \
	../wave/util/alglib/alglib/dawson.cpp \
	../wave/util/alglib/alglib/corr.cpp \
	../wave/util/alglib/alglib/rotations.cpp \
	../wave/util/alglib/alglib/trlinsolve.cpp \
	../wave/util/alglib/alglib/xblas.cpp \
	../wave/util/alglib/alglib/linmin.cpp \
	../wave/util/alglib/alglib/spline1d.cpp \
	../wave/util/alglib/alglib/fresnel.cpp \
	../wave/util/alglib/alglib/wsr.cpp \
	../wave/util/alglib/alglib/tsort.cpp \
	../wave/util/alglib/alglib/matinv.cpp \
	../wave/util/alglib/alglib/densesolver.cpp \
	../wave/util/alglib/alglib/expintegrals.cpp \
	../wave/util/alglib/alglib/fdistr.cpp \
	../wave/util/alglib/alglib/bdsvd.cpp \
	../wave/util/alglib/alglib/gq.cpp \
	../wave/util/alglib/alglib/laguerre.cpp \
	../wave/util/alglib/alglib/lda.cpp \
	../wave/util/alglib/alglib/odesolver.cpp \
	../wave/util/alglib/alglib/binomialdistr.cpp \
	../wave/util/alglib/alglib/lsfit.cpp \
	../wave/util/alglib/alglib/trigintegrals.cpp \
	../wave/util/alglib/alglib/mlptrain.cpp \
	../wave/util/alglib/alglib/reflections.cpp \
	../wave/util/alglib/alglib/pca.cpp \
	../wave/util/alglib/alglib/ssolve.cpp \
	../wave/util/alglib/alglib/ablasf.cpp \
	../wave/util/alglib/alglib/hsschur.cpp \
	../wave/util/alglib/alglib/srcond.cpp \
	../wave/util/alglib/alglib/igammaf.cpp \
	../wave/util/alglib/alglib/fht.cpp \
	../wave/util/alglib/alglib/pspline.cpp \
	../wave/util/alglib/alglib/mlpe.cpp \
	../wave/util/alglib/alglib/betaf.cpp \
	../wave/util/alglib/alglib/mincg.cpp \
	../wave/util/alglib/alglib/sblas.cpp \
	../wave/util/alglib/alglib/bessel.cpp \
	../wave/util/alglib/alglib/chebyshev.cpp \
	../wave/util/alglib/alglib/hqrnd.cpp \
	../wave/util/alglib/alglib/bdss.cpp \
	../wave/util/alglib/alglib/hblas.cpp \
	../wave/util/alglib/alglib/linreg.cpp \
	../wave/util/alglib/alglib/nearunityunit.cpp \
	../wave/util/alglib/alglib/jacobianelliptic.cpp \
	../wave/util/alglib/alglib/correlationtests.cpp \
	../wave/util/alglib/alglib/ap.cpp \
	../wave/util/alglib/alglib/minlbfgs.cpp \
	../wave/util/alglib/alglib/conv.cpp \
	../wave/util/alglib/alglib/studenttdistr.cpp \
	../wave/util/alglib/alglib/minasa.cpp \
	../wave/util/alglib/alglib/ftbase.cpp \
	../wave/util/alglib/alglib/dforest.cpp \
	../wave/util/alglib/alglib/spline3.cpp \
	../wave/util/alglib/alglib/matgen.cpp \
	../wave/util/alglib/alglib/fft.cpp \
	../wave/util/alglib/alglib/chisquaredistr.cpp \
	../wave/util/alglib/alglib/minlm.cpp \
	../wave/util/alglib/alglib/airyf.cpp \
	../wave/util/alglib/alglib/safesolve.cpp \
	../wave/util/alglib/alglib/ialglib.cpp \
	../wave/util/alglib/alglib/ratint.cpp \
	../wave/util/alglib/alglib/nearestneighbor.cpp \
	../wave/util/alglib/alglib/ablas.cpp \
	../wave/util/alglib/alglib/inverseupdate.cpp \
	../wave/util/alglib/alglib/schur.cpp \
	../wave/util/alglib/alglib/sdet.cpp \
	../wave/util/alglib/alglib/estnorm.cpp \
	../wave/util/alglib/alglib/rcond.cpp \
	../wave/util/alglib/alglib/autogk.cpp \
	../wave/util/alglib/alglib/sinverse.cpp \
	../wave/util/alglib/alglib/hermite.cpp \
	../wave/util/BoxND.cpp \
	canvas/canvas.cpp \
	canvas/point2d.cpp \
	canvas/canvasmenuscroll.cpp \
	canvas/quiverplot.cpp \
	canvas/Text.cpp \
	canvas/segmentedcurve.cpp \
	canvas/marchingsquares.cpp \
	canvas/GridValuesPlot.cpp \
	canvas/Dump_Fl_Widget/Dump_Fl_Widget.cpp \
	canvas/curve2d.cpp \
	canvas/graphicobject.cpp \
	canvas/canvasmenu.cpp \
	canvas/canvasmenupack.cpp \
	canvas/WaveCurvePlot.cpp \
	canvas/ArrowHead.cpp \
	canvas/MultiColoredCurve.cpp


FSRC +=  \
	../wave/ode/LSODE/lsode.F \
	../wave/ode/LSODE/lsode3.F \
	../wave/ode/LSODE/lsode2.F

