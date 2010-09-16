RPGNU=yes
RPLOCATION=impa
RPGROUPHOME=~/rp
BUILDARCH=`uname -m`
RPN=..
include ${RPN}/make/defs
vpath %.cc generic myfunction


# The following line can be set by user
EXEC = lab
SRC = Lab.cc \
      RealVectorUse.cc \
      GenericFluxParams.cc \
      GenericFluxFunction.cc \
      GenericFluxCalc.cc \
      My.cc \
      MyFluxFunction.cc \
      MyFluxParams.cc \
      MyWaveState.cc

# Do not change the following lines
RPNUMSRC += 
WAVESRC += 
ForSRC += 

SRCDIR = ${RPN}
OBJDIR = ./lib
LIBDIR = $(OBJDIR)

RPNUMERICSDIR = $(SRCDIR)/rpnumerics
WAVEDIR       = $(SRCDIR)/wave
JNIDIR        = $(SRCDIR)/JNI
include $(RPNUMERICSDIR)/defs
include $(WAVEDIR)/defs
include $(JNIDIR)/defs

OBJ = $(SRC:%.cc=$(OBJDIR)/%.o)
DEP = $(SRC:%.cc=$(OBJDIR)/%.d)

RPNUMLIBNAME = librpnumerics.a
RPNUMLIBARC = $(LIBDIR)/$(RPNUMLIBNAME)
RPNUMLIBOBJ = $(RPNUMSRC:%.cc=$(OBJDIR)/%.o)
RPNUMLIBOBJ += $(ForSRC:%.F=$(OBJDIR)/%.o)
RPNUMDEPENDOBJ = $(RPNUMSRC:%.cc=$(OBJDIR)/%.d)
RPNUMDEPENDOBJ += $(ForSRC:%.F=$(OBJDIR)/%.d)

WAVELIBNAME = libwave.a
WAVELIBARC = $(LIBDIR)/$(WAVELIBNAME)
WAVELIBOBJ = $(WAVESRC:%.cc=$(OBJDIR)/%.o)
WAVEDEPENDOBJ = $(WAVESRC:%.cc=$(OBJDIR)/%.d)

SHARELIBFLAG += -fPIC
CPPFLAGS += $(SHARELIBFLAG) -Igeneric -Imyfunction
#LIBLAPACK = -L/usr/lib \

LIBLAPACK = -L/usr/lib/gcc/$(BUILDGCCTARGET)/$(BUILDGCCVER) \
	    -L/usr/lib/gcc/$(BUILDGCCTARGET)/$(BUILDG77VER) \
	    -lblas -ldl -lg2c -lgfortran -llapack 
LIBWAVE = -L $(LIBDIR) -lwave



lib: $(WAVELIBARC) $(RPNUMLIBARC)

test: lib $(EXEC)

$(WAVELIBARC): $(WAVELIBOBJ)
	ar -ruv $(WAVELIBARC)  $(WAVELIBOBJ)

$(RPNUMLIBARC): $(RPNUMLIBOBJ)
	ar -ruv $(RPNUMLIBARC)  $(RPNUMLIBOBJ)

$(EXEC):  $(WAVELIBARC) $(RPNUMLIBARC) $(OBJ)
	$(CCC) -o $(EXEC) $(OBJ) $(WAVELIBARC) $(RPNUMLIBARC)

pack: clean
	mkdir -p lab/lib lab/rpn
	sed -e "s/^RPN=\.\./RPN=rpn/g" makefile > lab/makefile
	cp -r *.cc *.h generic myfunction lab
	cp -r ../rpnumerics ../wave ../JNI ../make ../plugins lab/rpn
	tar zcf lab.tar.gz lab
	\rm -rf lab

z:
	$(CCC) -o z z.cc zfuncs.c

.PRECIOUS: $(LIBARC)

include ${RPN}/make/rules
ifneq ($(RPHOSTTYPE),mingw32)
-include $(DEPENDOBJ)
endif

CLEANSRC += $(LIBARC) $(EXEC)
