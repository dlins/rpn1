include make/inc
include make/env

# Executar o comando: 
#    setenv LD_LIBRARY_PATH "$LD_LIBRARY_PATH":/impa/home/f/panters/rpnContour/lib/linux_i686

LDLIB=-lblas -llapack -lgfortran -lfltk -lpng -lX11 -ldl
CCOPT=-fopenmp -g -p

#all:
##	g++ -o main $(INCLUDE) ../lib/linux_i686/*.o -lgomp -L ../lib/linux_i686 -lblas -llapack -lfltk main.cpp
#	g++ -o main $(INCLUDE) ../lib/$(RPHOSTTYPE)/*.o  $(LDLIB) $(CCOPT) main.cpp

##	g++ -o main $(INCLUDE) ../lib/linux_x86_64/*.o -L ../lib/linux_i686 -lblas -llapack -lgfortran -lfltk -lX11 -ldl -lpng -fopenmp main.cpp
##	g++ -o main $(INCLUDE) ../lib/linux_i686/*.o -L ../lib/linux_i686 -lblas -llapack -lfltk main.cpp

stoneliu: main_stone_liuwavecurve.cpp
	make -f make/makefile
	g++ $(INCLUDE) ../lib/$(RPHOSTTYPE)/*.o  $(LDLIB) $(CCOPT) main_stone_liuwavecurve.cpp -o stoneliu

tpcwliu: main_tpcw_liuwavecurve.cpp
	make -f make/makefile
	g++ $(INCLUDE) ../lib/$(RPHOSTTYPE)/*.o  $(LDLIB) $(CCOPT) main_tpcw_liuwavecurve.cpp -o tpcwliu

splineliu: main_splineflux_liuwavecurve.cpp
	rm -f splineliu
	g++ $(INCLUDE) ../lib/$(RPHOSTTYPE)/*.o  $(LDLIB) $(CCOPT) main_splineflux_liuwavecurve.cpp -o splineliu

hyper: main_hyperoctree.cpp
	rm -f hyper
	g++ $(INCLUDE) ../lib/$(RPHOSTTYPE)/*.o  $(LDLIB) $(CCOPT) main_hyperoctree.cpp -o hyper

circle: main_circle.cpp
	rm -f circle
	g++ $(INCLUDE) ../lib/$(RPHOSTTYPE)/*.o  $(LDLIB) $(CCOPT) main_circle.cpp -o circle

hugoniot_no_gui: main_hugoniot_no_gui.cpp
	rm -f hugoniot_no_gui
	g++ $(INCLUDE) ../lib/$(RPHOSTTYPE)/*.o  $(LDLIB) $(CCOPT) main_hugoniot_no_gui.cpp -o hugoniot_no_gui

secondary_bifurcation_no_gui: main_secondary_bifurcation_no_gui.cpp
	rm -f secondary_bifurcation_no_gui
	g++ $(INCLUDE) ../lib/$(RPHOSTTYPE)/*.o  $(LDLIB) $(CCOPT) main_secondary_bifurcation_no_gui.cpp -o secondary_bifurcation_no_gui

riemann: main_riemann.cpp
	rm -f riemann
	g++ $(INCLUDE) ../lib/$(RPHOSTTYPE)/*.o  $(LDLIB) $(CCOPT) main_riemann.cpp -o riemann

wavecurve_from_wavecurve: main_wavecurve_from_wavecurve.cpp
	rm -f wavecurve_from_wavecurve
	g++ $(INCLUDE) ../lib/$(RPHOSTTYPE)/*.o  $(LDLIB) $(CCOPT) main_wavecurve_from_wavecurve.cpp -o wavecurve_from_wavecurve

raytrace: main_raytrace.cpp
	rm -f raytrace
	g++ $(INCLUDE) ../lib/$(RPHOSTTYPE)/*.o  $(LDLIB) $(CCOPT) main_raytrace.cpp -o raytrace

plot: main_plot.cpp
	rm -f plot
	g++ $(INCLUDE) ../lib/$(RPHOSTTYPE)/*.o  $(LDLIB) $(CCOPT) main_plot.cpp -o plot

corey_doublecontact: main_corey_doublecontact.cpp
	rm -f corey_doublecontact
	g++ $(INCLUDE) ../lib/$(RPHOSTTYPE)/*.o  $(LDLIB) $(CCOPT) main_corey_doublecontact.cpp -o corey_doublecontact

corey_extensioncurve: main_corey_extensioncurve.cpp
	rm -f corey_extensioncurve
	g++ $(INCLUDE) ../lib/$(RPHOSTTYPE)/*.o  $(LDLIB) $(CCOPT) main_corey_extensioncurve.cpp -o corey_extensioncurve

corey_explicit_hugoniot: main_corey_explicit_hugoniot.cpp
	rm -f corey_explicit_hugoniot
	g++ $(INCLUDE) ../lib/$(RPHOSTTYPE)/*.o  $(LDLIB) $(CCOPT) main_corey_explicit_hugoniot.cpp -o corey_explicit_hugoniot

corey_wavecurve_speed: main_corey_wavecurve_speed.cpp
	rm -f corey_wavecurve_speed
	g++ $(INCLUDE) ../lib/$(RPHOSTTYPE)/*.o  $(LDLIB) $(CCOPT) main_corey_wavecurve_speed.cpp -o corey_wavecurve_speed

singlephase_boundary: main_singlephase_boundary.cpp
	rm -f singlephase_boundary
	g++ $(INCLUDE) ../lib/$(RPHOSTTYPE)/*.o  $(LDLIB) $(CCOPT) main_singlephase_boundary.cpp -o singlephase_boundary

subphysics: main_subphysics.cpp
	rm -f subphysics
	g++ $(INCLUDE) ../lib/$(RPHOSTTYPE)/*.o  $(LDLIB) $(CCOPT) main_subphysics.cpp -o subphysics

injection_to_side: main_injection_to_side.cpp
	rm -f injection_to_side
	g++ $(INCLUDE) ../lib/$(RPHOSTTYPE)/*.o  $(LDLIB) $(CCOPT) main_injection_to_side.cpp -o injection_to_side

nonconvex: main_nonconvex.cpp
	rm -f nonconvex
	g++ $(INCLUDE) ../lib/$(RPHOSTTYPE)/*.o  $(LDLIB) $(CCOPT) main_nonconvex.cpp -o nonconvex

god_hydro: main_god_hydro.cpp
	rm -f god_hydro
	g++ $(INCLUDE) ../lib/$(RPHOSTTYPE)/*.o  $(LDLIB) $(CCOPT) main_god_hydro.cpp -o god_hydro
	
test_jets: main_test_jets.cpp
	rm -f test_jets
	g++ $(INCLUDE) ../lib/$(RPHOSTTYPE)/*.o  $(LDLIB) $(CCOPT) main_test_jets.cpp -o test_jets

equilibria: main_equilibria.cpp
	rm -f equilibria
	g++ $(INCLUDE) ../lib/$(RPHOSTTYPE)/*.o  $(LDLIB) $(CCOPT) main_equilibria.cpp -o equilibria

test_jet1d: main_test_jet1d.cpp
	rm -f test_jet1d
	g++ $(INCLUDE) ../lib/$(RPHOSTTYPE)/*.o  $(LDLIB) $(CCOPT) main_test_jet1d.cpp -o test_jet1d

xml: main_xml.cpp
	rm -f xml
	g++ $(INCLUDE) ../lib/$(RPHOSTTYPE)/*.o  $(LDLIB) $(CCOPT) main_xml.cpp -o xml

follow: main_follow.cpp
	rm -f follow
	g++ $(INCLUDE) ../lib/$(RPHOSTTYPE)/*.o  $(LDLIB) $(CCOPT) main_follow.cpp -o follow

transitional: main_transitional.cpp
	rm -f transitional
	g++ $(INCLUDE) ../lib/$(RPHOSTTYPE)/*.o  $(LDLIB) $(CCOPT) main_transitional.cpp -o transitional

equations: main_equations.cpp
	rm -f equations
	g++ $(INCLUDE) ../lib/$(RPHOSTTYPE)/*.o  $(LDLIB) $(CCOPT) main_equations.cpp -o equations

coreyquadwavecurve: main_coreyquadwavecurve.cpp
	rm -f coreyquadwavecurve
	g++ $(INCLUDE) ../lib/$(RPHOSTTYPE)/*.o  $(LDLIB) $(CCOPT) main_coreyquadwavecurve.cpp -o coreyquadwavecurve

jettester: main_jettester.cpp
	rm -f jettester
	g++ $(INCLUDE) ../lib/$(RPHOSTTYPE)/*.o  $(LDLIB) $(CCOPT) main_jettester.cpp -o jettester

rec: main_rec.cpp
	rm -f rec
	g++ $(INCLUDE) ../lib/$(RPHOSTTYPE)/*.o  $(LDLIB) $(CCOPT) main_rec.cpp -o rec

helmut_test: main_helmut_test.cpp
	rm -f helmut_test
	g++ $(INCLUDE) ../lib/$(RPHOSTTYPE)/*.o  $(LDLIB) $(CCOPT) main_helmut_test.cpp -o helmut_test

clean:
	rm -f stoneliu riemann splineliu hyper circle hugoniot_no_gui secondary_bifurcation_no_gui wavecurve_from_wavecurve raytrace

