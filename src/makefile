include $(RPNHOME)/var/make/defs

C++SRC += 
ForSRC += 

RPNUMERICSDIR = $(RPNHOME)/src/rpnumerics
WAVEDIR       = $(RPNHOME)/src/wave
JNIDIR        = $(RPNHOME)/src/JNI
include $(RPNUMERICSDIR)/defs
include $(WAVEDIR)/defs
include $(JNIDIR)/defs 

LIBNAME = librpnumerics.so
OBJDIR = ../lib/$(RPHOSTTYPE)
LIBOBJ = $(C++SRC:%.cc=$(OBJDIR)/%.o)
LIBOBJ += $(ForSRC:%.F=$(OBJDIR)/%.o)
DEPENDOBJ = $(C++SRC:%.cc=$(OBJDIR)/%.d)
DEPENDOBJ += $(ForSRC:%.F=$(OBJDIR)/%.d)
#LIBARC = $(RPNHOME)/src/$(OBJDIR)/$(LIBNAME)
JAVA_HOME=/usr/java/jdk1.5.0_07
LIBARC = $(OBJDIR)/$(LIBNAME)
SHARELIBFLAG += -fPIC
CPPFLAGS += $(SHARELIBFLAG) -I  /usr/lib/gcc/$(BUILDGCCVER)/3.4.6/libg2c.a
CLEANSRC += $(LIBARC)
#LIBLAPACK = /usr/lib/liblapack.a /usr/lib/libblas.a /usr/lib/gcc/$(GCCLIB)/$(BUILDGCCVER)/libgfortran.a /usr/lib/libdl.a /usr/lib/gcc/$(BUILDGCCVER)/3.4.6/libg2c.a
LIBLAPACK = /usr/lib/liblapack.a /usr/lib/libblas.a  /usr/lib/libdl.a /usr/lib/gcc/i386-redhat-linux/4.1.1/libgfortran.a /usr/lib/gcc/i386-redhat-linux/3.4.6/libg2c.a


all: $(LIBARC)

$(LIBARC): $(LIBOBJ)
	
	$(CCC)  -shared $(SHARELIBFLAG) -Wl,-soname,$(LIBNAME) -o $(LIBARC) $(LIBOBJ) $(LIBLAPACK)


t:
	@echo $(LIBARC)
	$(CCC)  -shared $(SHARELIBFLAG) -Wl,-soname,$(LIBNAME) -o $(LIBARC) $(LIBOBJ) $(LIBLAPACK)


# Static library target.
#$(LIBARC): $(LIBOBJ)
#	ar -ruv $(LIBARC) $(LIBOBJ)
#	$(RANLIB) $(LIBARC)


.PRECIOUS: $(LIBARC)

include $(RPNHOME)/var/make/rules
ifneq ($(RPHOSTTYPE),mingw32)
-include $(DEPENDOBJ)
endif
