
cSRC += 


ccSRC +=  \
	c++/Equations/Equations.cc \
	c++/rpnumerics/RarefactionCalc.cc \
	c++/rpnumerics/Envelope_Curve.cc \
	c++/rpnumerics/Newton_Improvement.cc \
	c++/rpnumerics/Hysteresis.cc \
	c++/rpnumerics/plugin/RPnPluginManager.cc \
	c++/rpnumerics/plugin/PluginService.cc \
	c++/rpnumerics/Extension_Curve.cc \
	c++/rpnumerics/AccumulationParams.cc \
	c++/rpnumerics/Hugoniot_Curve.cc \
	c++/rpnumerics/RpCalculation.cc \
	c++/rpnumerics/Double_Contact_Function.cc \
	c++/rpnumerics/RpFunction.cc \
	c++/rpnumerics/viscousprofile/StationaryPoint.cc \
	c++/rpnumerics/FluxFunction.cc \
	c++/rpnumerics/physics/Quad2SubPhysics/Quad2AccumulationFunction.cc \
	c++/rpnumerics/physics/Quad2SubPhysics/Quad2FluxFunction.cc \
	c++/rpnumerics/physics/Quad2SubPhysics/Quad2SubPhysics.cc \
	c++/rpnumerics/physics/Quad2SubPhysics/Quad2HugoniotFunction.cc \
	c++/rpnumerics/physics/Quad2SubPhysics/Quad2ExplicitHugoniotCurve.cc \
	c++/rpnumerics/physics/quad/Quad4AccumulationFunction.cc \
	c++/rpnumerics/physics/quad/Quad3FluxFunction.cc \
	c++/rpnumerics/physics/quad/Quad4FluxFunction.cc \
	c++/rpnumerics/physics/quad/Quad3FluxParams.cc \
	c++/rpnumerics/physics/quad/QuadWaveState.cc \
	c++/rpnumerics/physics/quad/Quad4FluxParams.cc \
	c++/rpnumerics/physics/quad/Quad3AccumulationFunction.cc \
	c++/rpnumerics/physics/SplineFlux/SplineFlux.cc \
	c++/rpnumerics/physics/Ion/IonAccumulation.cc \
	c++/rpnumerics/physics/Ion/IonPermeability.cc \
	c++/rpnumerics/physics/Ion/IonFlux.cc \
	c++/rpnumerics/physics/Ion/IonRatios.cc \
	c++/rpnumerics/physics/Ion/IonAdsorption.cc \
	c++/rpnumerics/physics/ThreePhaseFlowPhysics/Three_Phase_Flow_Explicit_Bifurcation_Curves.cc \
	c++/rpnumerics/physics/ThreePhaseFlowPhysics/Koval/KovalPermeability.cc \
	c++/rpnumerics/physics/ThreePhaseFlowPhysics/Koval/KovalFluxFunction.cc \
	c++/rpnumerics/physics/ThreePhaseFlowPhysics/Koval/KovalViscosity.cc \
	c++/rpnumerics/physics/ThreePhaseFlowPhysics/Koval/KovalSubPhysics.cc \
	c++/rpnumerics/physics/ThreePhaseFlowPhysics/StoneSubPhysics/StonePermeability.cc \
	c++/rpnumerics/physics/ThreePhaseFlowPhysics/StoneSubPhysics/StoneFluxFunction.cc \
	c++/rpnumerics/physics/ThreePhaseFlowPhysics/StoneSubPhysics/StoneParams.cc \
	c++/rpnumerics/physics/ThreePhaseFlowPhysics/StoneSubPhysics/Stone_Explicit_Bifurcation_Curves.cc \
	c++/rpnumerics/physics/ThreePhaseFlowPhysics/StoneSubPhysics/StonePermParams.cc \
	c++/rpnumerics/physics/ThreePhaseFlowPhysics/StoneSubPhysics/StoneSubPhysics.cc \
	c++/rpnumerics/physics/ThreePhaseFlowPhysics/StoneSubPhysics/StoneViscosity.cc \
	c++/rpnumerics/physics/ThreePhaseFlowPhysics/CoreyQuadSubPhysics/CoreyQuadTransitionalLine.cc \
	c++/rpnumerics/physics/ThreePhaseFlowPhysics/CoreyQuadSubPhysics/CoreyQuadViscosity.cc \
	c++/rpnumerics/physics/ThreePhaseFlowPhysics/CoreyQuadSubPhysics/CoreyQuadExplicitHugoniotCurve.cc \
	c++/rpnumerics/physics/ThreePhaseFlowPhysics/CoreyQuadSubPhysics/CoreyQuadWaveCurveFactory.cc \
	c++/rpnumerics/physics/ThreePhaseFlowPhysics/CoreyQuadSubPhysics/CoreyQuadSubPhysics.cc \
	c++/rpnumerics/physics/ThreePhaseFlowPhysics/CoreyQuadSubPhysics/CoreyQuadPermeability.cc \
	c++/rpnumerics/physics/ThreePhaseFlowPhysics/ThreePhaseFlowPermeability.cc \
	c++/rpnumerics/physics/ThreePhaseFlowPhysics/Foam/FoamPermeability.cc \
	c++/rpnumerics/physics/ThreePhaseFlowPhysics/Foam/FoamSubPhysics.cc \
	c++/rpnumerics/physics/ThreePhaseFlowPhysics/Foam/FoamViscosity.cc \
	c++/rpnumerics/physics/ThreePhaseFlowPhysics/Foam/FoamShockObserver.cc \
	c++/rpnumerics/physics/ThreePhaseFlowPhysics/Foam/FoamFluxFunction.cc \
	c++/rpnumerics/physics/ThreePhaseFlowPhysics/ThreePhaseFlowMobility.cc \
	c++/rpnumerics/physics/ThreePhaseFlowPhysics/Brooks_CoreySubPhysics/Brooks_CoreySubPhysics.cc \
	c++/rpnumerics/physics/ThreePhaseFlowPhysics/Brooks_CoreySubPhysics/Brooks_CoreyViscosity.cc \
	c++/rpnumerics/physics/ThreePhaseFlowPhysics/Brooks_CoreySubPhysics/Brooks_CoreyFluxFunction.cc \
	c++/rpnumerics/physics/ThreePhaseFlowPhysics/Brooks_CoreySubPhysics/Brooks_CoreyPermeability.cc \
	c++/rpnumerics/physics/ThreePhaseFlowPhysics/ThreePhaseFlowImplicitHugoniotCurve.cc \
	c++/rpnumerics/physics/ThreePhaseFlowPhysics/ThreePhaseFlowPermeabilityLevelCurve.cc \
	c++/rpnumerics/physics/ThreePhaseFlowPhysics/ThreePhaseFlowEquationFunctionLevelCurve.cc \
	c++/rpnumerics/physics/ThreePhaseFlowPhysics/Sorbie/SorbieFluxFunction.cc \
	c++/rpnumerics/physics/ThreePhaseFlowPhysics/Sorbie/SorbieSubPhysics.cc \
	c++/rpnumerics/physics/ThreePhaseFlowPhysics/Sorbie/SorbieViscosity.cc \
	c++/rpnumerics/physics/ThreePhaseFlowPhysics/Sorbie/SorbiePermeability.cc \
	c++/rpnumerics/physics/ThreePhaseFlowPhysics/ThreePhaseFlowFluxFunction.cc \
	c++/rpnumerics/physics/ThreePhaseFlowPhysics/ThreePhaseFlowSubPhysics.cc \
	c++/rpnumerics/physics/ThreePhaseFlowPhysics/ThreePhaseFlowHugoniotContinuation.cc \
	c++/rpnumerics/physics/ThreePhaseFlowPhysics/ThreePhaseFlowViscosity.cc \
	c++/rpnumerics/physics/ThreePhaseFlowPhysics/ThreePhaseFlowMobilityLevelCurve.cc \
	c++/rpnumerics/physics/ThreePhaseFlowPhysics/ThreePhaseFlowAccumulation.cc \
	c++/rpnumerics/physics/JD/JDEvaporationCompositeCurve.cc \
	c++/rpnumerics/physics/JD/JDAccumulationFunction.cc \
	c++/rpnumerics/physics/JD/JDSubPhysics.cc \
	c++/rpnumerics/physics/JD/JDEvap_Extension.cc \
	c++/rpnumerics/physics/JD/CoincidenceJD.cc \
	c++/rpnumerics/physics/JD/JDFluxFunction.cc \
	c++/rpnumerics/physics/SimpleFlux/SimpleFlux.cc \
	c++/rpnumerics/physics/Quad2C1SubPhysics/Quad2C1Equations.cc \
	c++/rpnumerics/physics/CompositionalPhysics/DeadVolatileVolatileGas/DeadVolatileVolatileGasHydrodynamics.cc \
	c++/rpnumerics/physics/CompositionalPhysics/DeadVolatileVolatileGas/DeadVolatileVolatileGasAccumulationFunction.cc \
	c++/rpnumerics/physics/CompositionalPhysics/DeadVolatileVolatileGas/DeadVolatileVolatileGasSubPhysics.cc \
	c++/rpnumerics/physics/CompositionalPhysics/DeadVolatileVolatileGas/DeadVolatileVolatileGasEvaporationExtension.cc \
	c++/rpnumerics/physics/CompositionalPhysics/DeadVolatileVolatileGas/DeadVolatileVolatileGasCompositeCurve.cc \
	c++/rpnumerics/physics/CompositionalPhysics/DeadVolatileVolatileGas/DeadVolatileVolatileGasThermodynamics.cc \
	c++/rpnumerics/physics/CompositionalPhysics/DeadVolatileVolatileGas/DeadVolatileVolatileGasFluxFunction.cc \
	c++/rpnumerics/physics/CompositionalPhysics/DeadVolatileVolatileGas/DeadVolatileVolatileGasCoincidence.cc \
	c++/rpnumerics/physics/CompositionalPhysics/ICDOW/ICDOWCoincidence.cc \
	c++/rpnumerics/physics/CompositionalPhysics/ICDOW/ICDOWSubPhysics.cc \
	c++/rpnumerics/physics/CompositionalPhysics/ICDOW/ICDOWHydrodynamics.cc \
	c++/rpnumerics/physics/CompositionalPhysics/ICDOW/ICDOWAccumulationFunction.cc \
	c++/rpnumerics/physics/CompositionalPhysics/ICDOW/ICDOWChemistry.cc \
	c++/rpnumerics/physics/CompositionalPhysics/ICDOW/ICDOWFluxFunction.cc \
	c++/rpnumerics/physics/CompositionalPhysics/Hugoniot_TP.cc \
	c++/rpnumerics/physics/CompositionalPhysics/TPCWPhysics/TPCW_SinglePhase/SinglePhaseBoundary.cc \
	c++/rpnumerics/physics/CompositionalPhysics/TPCWPhysics/TPCW_SinglePhase/Common/Thermodynamics.cc \
	c++/rpnumerics/physics/CompositionalPhysics/TPCWPhysics/TPCWSubPhysics/TPCWSubPhysics.cc \
	c++/rpnumerics/physics/Quad3SubPhysics/Quad3Equations.cc \
	c++/rpnumerics/PhasePoint.cc \
	c++/rpnumerics/EllipticExtension.cc \
	c++/rpnumerics/Double_Contact.cc \
	c++/rpnumerics/Secondary_Bifurcation.cc \
	c++/rpnumerics/HugoniotSegment.cc \
	c++/rpnumerics/WavePoint.cc \
	c++/rpnumerics/Hugoniot_Locus.cc \
	c++/rpnumerics/AccumulationFunction.cc \
	c++/rpnumerics/Elliptic_Boundary.cc \
	c++/rpnumerics/Inflection_Curve.cc \
	c++/rpnumerics/DefaultAccumulationFunction.cc \
	c++/rpnumerics/RpSolution.cc \
	c++/rpnumerics/Curve/Curve.cc \
	c++/rpnumerics/characteristics/curvegen.cc \
	c++/rpnumerics/characteristics/plotter.cc \
	c++/rpnumerics/characteristics/interpol.cc \
	c++/rpnumerics/DefaultAccumulationParams.cc \
	c++/rpnumerics/GridValues.cc \
	c++/rpnumerics/RPnCurve.cc \
	c++/rpnumerics/ShockFlowParams.cc \
	c++/rpnumerics/methods/ShockCurve/ShockCurve.cc \
	c++/rpnumerics/methods/ShockCurve/ClassifyShockCurve.cc \
	c++/rpnumerics/methods/ContactRegionBoundary/ContactRegionBoundary.cc \
	c++/rpnumerics/methods/EquationFunctionLevelCurve/EquationFunctionLevelCurve.cc \
	c++/rpnumerics/methods/CharacteristicPolynomialLevels/CharacteristicPolynomialLevels.cc \
	c++/rpnumerics/methods/Secondary_Bifurcations/Explicit_Bifurcation_Curves/Explicit_Bifurcation_Curves.cc \
	c++/rpnumerics/methods/BifurcationCurve/BifurcationCurve.cc \
	c++/rpnumerics/methods/RiemannSolver/RiemannProblem.cc \
	c++/rpnumerics/methods/CompositeCurve/CompositeCurve.cc \
	c++/rpnumerics/methods/WaveCurve/WaveCurveFactory.cc \
	c++/rpnumerics/methods/WaveCurve/WaveCurve.cc \
	c++/rpnumerics/methods/WaveCurve/ExplicitTransitionalWavecurve.cc \
	c++/rpnumerics/methods/ReferencePoint.cc \
	c++/rpnumerics/methods/HugoniotCurve/ImplicitHugoniotCurve.cc \
	c++/rpnumerics/methods/HugoniotCurve/HugoniotCurve.cc \
	c++/rpnumerics/methods/Bisection/Bisection.cc \
	c++/rpnumerics/methods/Extension/Implicit_Extension_Curve.cc \
	c++/rpnumerics/methods/Extension/Extension.cc \
	c++/rpnumerics/methods/TransitionalShock/TransitionalShock.cc \
	c++/rpnumerics/methods/Coincidence_Contour/Coincidence_Contour.cc \
	c++/rpnumerics/methods/Viscosity_Matrix/Viscosity_Matrix.cc \
	c++/rpnumerics/methods/Viscosity_Matrix/ViscosityJetMatrix.cc \
	c++/rpnumerics/methods/HugoniotContinuation/HugoniotContinuation_nDnD/HugoniotContinuation_nDnD.cc \
	c++/rpnumerics/methods/HugoniotContinuation/HugoniotContinuation2D2D/HugoniotContinuation2D2D.cc \
	c++/rpnumerics/methods/HugoniotContinuation/HugoniotContinuation3D2D/HugoniotContinuation3D2D.cc \
	c++/rpnumerics/methods/HugoniotContinuation/HugoniotContinuation.cc \
	c++/rpnumerics/methods/HugoniotContinuation/HugoniotContinuation_nD_nm1D/HugoniotContinuation_nD_nm1D.cc \
	c++/rpnumerics/methods/NewRarefactionCurve/NewRarefactionCurve.cc \
	c++/rpnumerics/methods/contour/ContourMethodPure.cc \
	c++/rpnumerics/methods/contour/GPU/Double_Contact_GPU.cc \
	c++/rpnumerics/methods/contour/GPU/Contour2x2_Method_GPU.cc \
	c++/rpnumerics/methods/contour/HyperCube.cc \
	c++/rpnumerics/methods/contour/Contour2x2_Method.cc \
	c++/rpnumerics/methods/contour/HugoniotFunctionClass.cc \
	c++/rpnumerics/methods/contour/ContourMethod.cc \
	c++/rpnumerics/methods/contour/Contour2p5_Method.cc \
	c++/rpnumerics/methods/RarefactionCurve/RarefactionCurve.cc \
	c++/rpnumerics/Implicit_Curve.cc \
	c++/rpnumerics/ParametricPlot/ParametricPlot.cc \
	c++/rpnumerics/WaveState.cc \
	c++/rpnumerics/FluxParams.cc \
	c++/wave/ode/LSODE/LSODE.cc \
	c++/wave/ode/EulerSolver/EulerSolver.cc \
	c++/wave/util/RealVector.cc \
	c++/wave/util/RealSegment.cc \
	c++/wave/util/except.cc \
	c++/wave/util/Boundary.cc \
	c++/wave/util/GaussLegendreIntegral/GaussLegendreIntegral.cc \
	c++/wave/util/Eigenproblem/Eigenproblem2x2.cc \
	c++/wave/util/Eigenproblem/Eigenpair.cc \
	c++/wave/util/Eigenproblem/Eigenproblem.cc \
	c++/wave/util/mathutil.cc \
	c++/wave/util/JetMatrix.cc \
	c++/wave/util/Line.cc \
	c++/wave/util/HessianMatrix.cc \
	c++/wave/util/bool.cc \
	c++/wave/util/IntArray.cc \
	c++/wave/util/RealMatrix2.cc \
	c++/wave/util/errorhandler.cc \
	c++/wave/util/RectBoundary.cc \
	c++/wave/util/JacobianMatrix.cc \
	c++/wave/util/Utilities.cc \
	c++/wave/util/IsoTriang2DBoundary.cc \
	c++/wave/util/eigen.cc \
	c++/wave/multid/Space.cc \
	c++/wave/multid/Multid.cc \
	c++/Model/Parameter.cc \
	c++/Model/SubPhysics.cc \
	c++/Model/Physics.cc \
	c++/Model/TrivialAccumulationFunction.cc \
	c++/JNI/JNISubInflectionCurveCalc.cc \
	c++/JNI/JNISecondaryBifurcation.cc \
	c++/JNI/JNITransitionalLine.cc \
	c++/JNI/JNIBuckleyLeverettiIInflectionCurveCalc.cc \
	c++/JNI/JNIWaveCurveCalc.cc \
	c++/JNI/JNIDiscriminantLevelCurveCalc.cc \
	c++/JNI/JNIRPnPluginManager.cc \
	c++/JNI/JNIShockCurveCalc.cc \
	c++/JNI/JNIPhysicalBondaryCalc.cc \
	c++/JNI/JNIInflectionCurveCalc.cc \
	c++/JNI/JNIStationaryPointCalc.cc \
	c++/JNI/JNIStateInformation.cc \
	c++/JNI/JNILevelCurveCalc.cc \
	c++/JNI/JNIRarefactionCurveCalc.cc \
	c++/JNI/JNIIntegralCurveCalc.cc \
	c++/JNI/JNIViscousProfileData.cc \
	c++/JNI/JNIEllipticBoundary.cc \
	c++/JNI/JNIRarefactionExtensionCalc.cc \
	c++/JNI/JNIOrbitCalc.cc \
	c++/JNI/JNICoincidenceCurveCalc.cc \
	c++/JNI/JNIHysteresis.cc \
	c++/JNI/JNIMultiPolygon.cc \
	c++/JNI/JNIManifoldOrbitCalc.cc \
	c++/JNI/JNIRiemannProfileCalc.cc \
	c++/JNI/JNIDoubleContact.cc \
	c++/JNI/JNISubInflectionExtensionCurveCalc.cc \
	c++/JNI/JNIRPnDesktopPlotter.cc \
	c++/JNI/JNICompositeCalc.cc \
	c++/JNI/JNICharacteristicsCurveCalc.cc \
	c++/JNI/JNIHugoniotCurveCalc.cc \
	c++/JNI/JNIEnvelopeCurve.cc \
	c++/JNI/JNIExtensionCurveCalc.cc \
	c++/JNI/JNICoincidenceExtensionCurve.cc \
	c++/JNI/JNIBoundaryExtensionCalc.cc \
	c++/JNI/JNIWaveCurveRRegionsCalc.cc \
	c++/JNI/JNIConnectionOrbitCalc.cc \
	c++/JNI/RpNumerics.cc \
	c++/JNI/JNIEllipticBoundaryExtension.cc \
	c++/JNI/JNIWaveCurveSpeedDiagram.cc \
	c++/JetTester/JetTester1D.cc \
	c++/JetTester/JetTester2D.cc \
	c++/JetTester/TestableJet.cc \
	c++/JetTester/JetTester.cc \
	c++/Observer_Subject/Observer.cc \
	c++/Observer_Subject/Subject.cc


cppSRC +=  \
	c++/rpnumerics/physics/corey/CoreyQuad_Params.cpp \
	c++/rpnumerics/physics/polydisperse/Polydisperse.cpp \
	c++/rpnumerics/physics/polydisperse/Polydisperse_Params.cpp \
	c++/rpnumerics/physics/ThreePhaseFlowPhysics/CoreyQuadSubPhysics/CoreyQuadFluxFunction.cpp \
	c++/rpnumerics/physics/CompositionalPhysics/TPCWPhysics/TPCW_SinglePhase/Flux2Comp2PhasesAdimensionalized_Params.cpp \
	c++/rpnumerics/physics/CompositionalPhysics/TPCWPhysics/TPCW_SinglePhase/Liquid/JetSinglePhaseLiquid.cpp \
	c++/rpnumerics/physics/CompositionalPhysics/TPCWPhysics/TPCW_SinglePhase/Liquid/FluxSinglePhaseLiquidAdimensionalized.cpp \
	c++/rpnumerics/physics/CompositionalPhysics/TPCWPhysics/TPCW_SinglePhase/Liquid/AccumulationSinglePhaseLiquidAdimensionalized.cpp \
	c++/rpnumerics/physics/CompositionalPhysics/TPCWPhysics/TPCW_SinglePhase/Liquid/FluxSinglePhaseLiquidAdimensionalized_Params.cpp \
	c++/rpnumerics/physics/CompositionalPhysics/TPCWPhysics/TPCW_SinglePhase/Vapor/FluxSinglePhaseVaporAdimensionalized.cpp \
	c++/rpnumerics/physics/CompositionalPhysics/TPCWPhysics/TPCW_SinglePhase/Vapor/FluxSinglePhaseVaporAdimensionalized_Params.cpp \
	c++/rpnumerics/physics/CompositionalPhysics/TPCWPhysics/TPCW_SinglePhase/Vapor/AccumulationSinglePhaseVaporAdimensionalized.cpp \
	c++/rpnumerics/physics/CompositionalPhysics/TPCWPhysics/TPCW_SinglePhase/Vapor/JetSinglePhaseVapor.cpp \
	c++/rpnumerics/physics/CompositionalPhysics/TPCWPhysics/TPCW_SinglePhase/Common/MolarDensity.cpp \
	c++/rpnumerics/physics/CompositionalPhysics/TPCWPhysics/TPCW_SinglePhase/Common/VLE_Flash_TPCW.cpp \
	c++/rpnumerics/physics/CompositionalPhysics/TPCWPhysics/TPCW_SinglePhase/Accum2Comp2PhasesAdimensionalized.cpp \
	c++/rpnumerics/physics/CompositionalPhysics/TPCWPhysics/TPCW_SinglePhase/Flux2Comp2PhasesAdimensionalized.cpp \
	c++/rpnumerics/methods/HugoniotCurve/ColorCurve.cpp \
	c++/rpnumerics/methods/Viscosity_Matrix/DoubleMatrix.cpp \
	c++/rpnumerics/Eigenvalue_Contour.cpp \
	c++/wave/util/BoxND.cpp \
	c++/wave/util/PointND.cpp \
	c++/wave/util/alglib/alglib/ratinterpolation.cpp \
	c++/wave/util/alglib/alglib/rcond.cpp \
	c++/wave/util/alglib/alglib/trfac.cpp \
	c++/wave/util/alglib/alglib/spdgevd.cpp \
	c++/wave/util/alglib/alglib/densesolver.cpp \
	c++/wave/util/alglib/alglib/kmeans.cpp \
	c++/wave/util/alglib/alglib/spline1d.cpp \
	c++/wave/util/alglib/alglib/ablas.cpp \
	c++/wave/util/alglib/alglib/hermite.cpp \
	c++/wave/util/alglib/alglib/bdss.cpp \
	c++/wave/util/alglib/alglib/conv.cpp \
	c++/wave/util/alglib/alglib/fht.cpp \
	c++/wave/util/alglib/alglib/nearunityunit.cpp \
	c++/wave/util/alglib/alglib/corr.cpp \
	c++/wave/util/alglib/alglib/minlm.cpp \
	c++/wave/util/alglib/alglib/matgen.cpp \
	c++/wave/util/alglib/alglib/creflections.cpp \
	c++/wave/util/alglib/alglib/correlationtests.cpp \
	c++/wave/util/alglib/alglib/linmin.cpp \
	c++/wave/util/alglib/alglib/ortfac.cpp \
	c++/wave/util/alglib/alglib/polint.cpp \
	c++/wave/util/alglib/alglib/trigintegrals.cpp \
	c++/wave/util/alglib/alglib/jacobianelliptic.cpp \
	c++/wave/util/alglib/alglib/descriptivestatistics.cpp \
	c++/wave/util/alglib/alglib/bdsvd.cpp \
	c++/wave/util/alglib/alglib/stest.cpp \
	c++/wave/util/alglib/alglib/fresnel.cpp \
	c++/wave/util/alglib/alglib/rotations.cpp \
	c++/wave/util/alglib/alglib/safesolve.cpp \
	c++/wave/util/alglib/alglib/bessel.cpp \
	c++/wave/util/alglib/alglib/mincg.cpp \
	c++/wave/util/alglib/alglib/gq.cpp \
	c++/wave/util/alglib/alglib/poissondistr.cpp \
	c++/wave/util/alglib/alglib/ibetaf.cpp \
	c++/wave/util/alglib/alglib/jarquebera.cpp \
	c++/wave/util/alglib/alglib/fft.cpp \
	c++/wave/util/alglib/alglib/gammafunc.cpp \
	c++/wave/util/alglib/alglib/estnorm.cpp \
	c++/wave/util/alglib/alglib/svd.cpp \
	c++/wave/util/alglib/alglib/psif.cpp \
	c++/wave/util/alglib/alglib/ap.cpp \
	c++/wave/util/alglib/alglib/reflections.cpp \
	c++/wave/util/alglib/alglib/inverseupdate.cpp \
	c++/wave/util/alglib/alglib/binomialdistr.cpp \
	c++/wave/util/alglib/alglib/pspline.cpp \
	c++/wave/util/alglib/alglib/mlptrain.cpp \
	c++/wave/util/alglib/alglib/schur.cpp \
	c++/wave/util/alglib/alglib/matinv.cpp \
	c++/wave/util/alglib/alglib/wsr.cpp \
	c++/wave/util/alglib/alglib/idwint.cpp \
	c++/wave/util/alglib/alglib/ssolve.cpp \
	c++/wave/util/alglib/alglib/elliptic.cpp \
	c++/wave/util/alglib/alglib/sdet.cpp \
	c++/wave/util/alglib/alglib/xblas.cpp \
	c++/wave/util/alglib/alglib/odesolver.cpp \
	c++/wave/util/alglib/alglib/mlpbase.cpp \
	c++/wave/util/alglib/alglib/fdistr.cpp \
	c++/wave/util/alglib/alglib/ftbase.cpp \
	c++/wave/util/alglib/alglib/autogk.cpp \
	c++/wave/util/alglib/alglib/ratint.cpp \
	c++/wave/util/alglib/alglib/linreg.cpp \
	c++/wave/util/alglib/alglib/sblas.cpp \
	c++/wave/util/alglib/alglib/chisquaredistr.cpp \
	c++/wave/util/alglib/alglib/airyf.cpp \
	c++/wave/util/alglib/alglib/pca.cpp \
	c++/wave/util/alglib/alglib/studentttests.cpp \
	c++/wave/util/alglib/alglib/minasa.cpp \
	c++/wave/util/alglib/alglib/chebyshev.cpp \
	c++/wave/util/alglib/alglib/apserv.cpp \
	c++/wave/util/alglib/alglib/legendre.cpp \
	c++/wave/util/alglib/alglib/tsort.cpp \
	c++/wave/util/alglib/alglib/hsschur.cpp \
	c++/wave/util/alglib/alglib/spline2d.cpp \
	c++/wave/util/alglib/alglib/minlbfgs.cpp \
	c++/wave/util/alglib/alglib/ialglib.cpp \
	c++/wave/util/alglib/alglib/mannwhitneyu.cpp \
	c++/wave/util/alglib/alglib/matdet.cpp \
	c++/wave/util/alglib/alglib/srcond.cpp \
	c++/wave/util/alglib/alglib/hblas.cpp \
	c++/wave/util/alglib/alglib/ldlt.cpp \
	c++/wave/util/alglib/alglib/laguerre.cpp \
	c++/wave/util/alglib/alglib/spline3.cpp \
	c++/wave/util/alglib/alglib/dawson.cpp \
	c++/wave/util/alglib/alglib/igammaf.cpp \
	c++/wave/util/alglib/alglib/evd.cpp \
	c++/wave/util/alglib/alglib/trlinsolve.cpp \
	c++/wave/util/alglib/alglib/logit.cpp \
	c++/wave/util/alglib/alglib/sinverse.cpp \
	c++/wave/util/alglib/alglib/betaf.cpp \
	c++/wave/util/alglib/alglib/lda.cpp \
	c++/wave/util/alglib/alglib/lsfit.cpp \
	c++/wave/util/alglib/alglib/hqrnd.cpp \
	c++/wave/util/alglib/alglib/variancetests.cpp \
	c++/wave/util/alglib/alglib/ablasf.cpp \
	c++/wave/util/alglib/alglib/mlpe.cpp \
	c++/wave/util/alglib/alglib/dforest.cpp \
	c++/wave/util/alglib/alglib/blas.cpp \
	c++/wave/util/alglib/alglib/normaldistr.cpp \
	c++/wave/util/alglib/alglib/studenttdistr.cpp \
	c++/wave/util/alglib/alglib/gkq.cpp \
	c++/wave/util/alglib/alglib/correlation.cpp \
	c++/wave/util/alglib/alglib/nearestneighbor.cpp \
	c++/wave/util/alglib/alglib/expintegrals.cpp


FSRC +=  \
	c++/wave/ode/LSODE/lsode2.F \
	c++/wave/ode/LSODE/lsode3.F \
	c++/wave/ode/LSODE/lsode.F


cuSRC += 
