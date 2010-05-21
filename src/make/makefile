include make/env
include make/defs

RPNUMSRC += 
WAVESRC += 
ForSRC += 

RPNHOME=.
LIBDIR = $(RPNHOME)/../lib/$(RPHOSTTYPE)
#OBJDIR = $(RPNHOME)/build/$(RPHOSTTYPE)
OBJDIR = $(LIBDIR)

RPNUMERICSDIR = $(RPNHOME)/rpnumerics
WAVEDIR       = $(RPNHOME)/wave
JNIDIR        = $(RPNHOME)/JNI
include $(RPNUMERICSDIR)/defs
include $(WAVEDIR)/defs
include $(JNIDIR)/defs 


RPNUMLIBARC = $(LIBDIR)/$(RPNUMLIBNAME)
RPNUMLIBOBJ = $(RPNUMSRC:%.cc=$(OBJDIR)/%.o)
RPNUMLIBOBJ += $(ForSRC:%.F=$(OBJDIR)/%.o)
RPNUMDEPENDOBJ = $(RPNUMSRC:%.cc=$(OBJDIR)/%.d)
RPNUMDEPENDOBJ += $(ForSRC:%.F=$(OBJDIR)/%.d)

WAVELIBARC = $(LIBDIR)/$(WAVELIBNAME)
WAVELIBOBJ = $(WAVESRC:%.cc=$(OBJDIR)/%.o)
WAVEDEPENDOBJ = $(WAVESRC:%.cc=$(OBJDIR)/%.d)


SHARELIBFLAG += -fPIC
CPPFLAGS += $(SHARELIBFLAG)
LIBWAVE = -L$(LIBDIR) -lwave

ifeq ($(RPHOSTTYPE),darwin_i386)
RPNUMLIBNAME = librpnumerics.dylib
WAVELIBNAME = libwave.dylib

CPPFLAGS += -I $(JAVA_INC)
LIBLAPACK = -L/Developer/SDKs/MacOSX10.5.sdk/System/Library/Frameworks/Accelerate.framework/Versions/A/Frameworks/vecLib.framework/Versions/A -lBLAS -lLAPACK \
	-L/usr/local/lib -lgfortran
else
#ifeq ($(RPHOSTTYPE),linux_*)
RPNUMLIBNAME = librpnumerics.so
WAVELIBNAME = libwave.so

CPPFLAGS += -I $(JAVA_INC) -I $(JAVA_INC)/linux
LIBLAPACK = -L/usr/lib \
	    -L/usr/lib/gcc/$(BUILDGCCTARGET)/$(BUILDGCCVER) \
	    -L/usr/lib/gcc/$(BUILDGCCTARGET)/$(BUILDG77VER) \
	    -L/usr/lib/gcc/i386-redhat-linux/4.1.1/ \
	    -L/usr/lib/gcc/i386-redhat-linux/4.1.2/ \
            -L/usr/lib/gcc/i386-redhat-linux/3.4.6/\
	    -lblas -ldl -llapack 
WAVELDFLAG = -Wl,-soname,$(WAVELIBNAME)
RPNUMLDFLAG = -Wl,-soname,$(RPNUMLIBNAME)
endif



all: $(WAVELIBARC) $(RPNUMLIBARC)

$(WAVELIBARC): $(WAVELIBOBJ)
	$(CCC) -shared $(SHARELIBFLAG) $(WAVELDFLAG) -o $(WAVELIBARC) $(WAVELIBOBJ) $(LIBLAPACK)

$(RPNUMLIBARC): $(RPNUMLIBOBJ)
	$(CCC) -shared $(SHARELIBFLAG) $(RPNUMLDFLAG) -o $(RPNUMLIBARC) $(RPNUMLIBOBJ) $(LIBWAVE) $(LIBLAPACK)


test:
	@echo $(JAVA_HOME)
	@echo $(RPHOSTTYPE)

.PRECIOUS: $(LIBARC)

include make/rules
ifneq ($(RPHOSTTYPE),mingw32)
-include $(DEPENDOBJ)
endif

CLEANSRC += $(LIBARC)
