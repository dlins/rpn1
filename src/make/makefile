include make/defs

C++SRC += 
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


LIBNAME = librpnumerics.so
LIBARC = $(LIBDIR)/$(LIBNAME)
LIBOBJ = $(C++SRC:%.cc=$(OBJDIR)/%.o)
LIBOBJ += $(ForSRC:%.F=$(OBJDIR)/%.o)
DEPENDOBJ = $(C++SRC:%.cc=$(OBJDIR)/%.d)
DEPENDOBJ += $(ForSRC:%.F=$(OBJDIR)/%.d)

SHARELIBFLAG += -fPIC
CPPFLAGS += $(SHARELIBFLAG)
LIBLAPACK = -L/usr/lib \
	    -L/usr/lib/gcc/$(BUILDGCCTARGET)/$(BUILDGCCVER) \
	    -L/usr/lib/gcc/$(BUILDGCCTARGET)/$(BUILDG77VER) \
	    -lblas -ldl -lg2c -lgfortran -llapack


all: $(LIBARC)

$(LIBARC): $(LIBOBJ)
	$(CCC) -shared $(SHARELIBFLAG) -Wl,-soname,$(LIBNAME) -o $(LIBARC) $(LIBOBJ) $(LIBLAPACK)


.PRECIOUS: $(LIBARC)

include make/rules
ifneq ($(RPHOSTTYPE),mingw32)
-include $(DEPENDOBJ)
endif

CLEANSRC += $(LIBARC)
