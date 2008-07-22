include make/defs

RPNUMSRC += 
WAVESRC += 
ForSRC += 

SRCDIR=.
OBJDIR = $(SRCDIR)/build/$(RPHOSTTYPE)
LIBDIR = $(SRCDIR)/../lib/$(RPHOSTTYPE)

RPNUMERICSDIR = $(SRCDIR)/rpnumerics
WAVEDIR       = $(SRCDIR)/wave
JNIDIR        = $(SRCDIR)/JNI
include $(RPNUMERICSDIR)/defs
include $(WAVEDIR)/defs
include $(JNIDIR)/defs 


RPNUMLIBNAME = librpnumerics.so
RPNUMLIBARC = $(LIBDIR)/$(RPNUMLIBNAME)
RPNUMLIBOBJ = $(RPNUMSRC:%.cc=$(OBJDIR)/%.o)
RPNUMLIBOBJ += $(ForSRC:%.F=$(OBJDIR)/%.o)
RPNUMDEPENDOBJ = $(RPNUMSRC:%.cc=$(OBJDIR)/%.d)
RPNUMDEPENDOBJ += $(ForSRC:%.F=$(OBJDIR)/%.d)

WAVELIBNAME = libwave.so
WAVELIBARC = $(LIBDIR)/$(WAVELIBNAME)
WAVELIBOBJ = $(WAVESRC:%.cc=$(OBJDIR)/%.o)
WAVEDEPENDOBJ = $(WAVESRC:%.cc=$(OBJDIR)/%.d)

SHARELIBFLAG += -fPIC
CPPFLAGS += $(SHARELIBFLAG)
LIBLAPACK = -L/usr/lib \
	    -L/usr/lib/gcc/$(BUILDGCCTARGET)/$(BUILDGCCVER) \
	    -L/usr/lib/gcc/$(BUILDGCCTARGET)/$(BUILDG77VER) \
	    -lblas -ldl -lg2c -lgfortran -llapack 
LIBWAVE = -L $(LIBDIR) -lwave



all: $(WAVELIBARC) $(RPNUMLIBARC)

$(WAVELIBARC): $(WAVELIBOBJ)
	$(CCC) -shared $(SHARELIBFLAG) -Wl,-soname,$(WAVELIBNAME) -o $(WAVELIBARC) $(WAVELIBOBJ)

$(RPNUMLIBARC): $(RPNUMLIBOBJ)
	$(CCC) -shared $(SHARELIBFLAG) -Wl,-soname,$(RPNUMLIBNAME) -o $(RPNUMLIBARC) $(RPNUMLIBOBJ) $(LIBLAPACK) $(LIBWAVE)



.PRECIOUS: $(LIBARC)

include make/rules
ifneq ($(RPHOSTTYPE),mingw32)
-include $(DEPENDOBJ)
endif

CLEANSRC += $(LIBARC)
