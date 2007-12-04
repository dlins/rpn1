include $(RPNHOME)/var/make/defs

C++SRC += 
RPNUMERICSDIR = $(RPNHOME)/src/rpnumerics
WAVEDIR       = $(RPNHOME)/src/wave
JNIDIR        = $(RPNHOME)/src/JNI
include $(RPNUMERICSDIR)/defs
include $(WAVEDIR)/defs
include $(JNIDIR)/defs 

LIBNAME = librpnumerics.so
OBJDIR = ../lib/$(RPHOSTTYPE)
LIBOBJ = $(C++SRC:%.cc=$(OBJDIR)/%.o)
DEPENDOBJ = $(C++SRC:%.cc=$(OBJDIR)/%.d)
#LIBARC = $(RPNHOME)/src/$(OBJDIR)/$(LIBNAME)
LIBARC = $(OBJDIR)/$(LIBNAME)
CLEANSRC += $(LIBARC)


all: $(LIBARC)

$(LIBARC): $(LIBOBJ)
	$(CC)  -shared -fPIC -Wl,-soname,$(LIBNAME) -o $(LIBARC) $(LIBOBJ)

# Static library target.
#$(LIBARC): $(LIBOBJ)
#	ar -ruv $(LIBARC) $(LIBOBJ)
#	$(RANLIB) $(LIBARC)


.PRECIOUS: $(LIBARC)

include $(RPNHOME)/var/make/rules
ifneq ($(RPHOSTTYPE),mingw32)
-include $(DEPENDOBJ)
endif
