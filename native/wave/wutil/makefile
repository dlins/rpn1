include $(WAVEDIR)/make/defs

WUTILDIR = .

#DEBUGFLAG =
DEBUGFLAG = -g3
#DEBUGFLAG = -O3 -ffast-math

CPPFLAGS = $(WUTILINC)
LDLIBS = $(WUTILLIBS) $(SYSLIBS)
CLEANSRC += $(TESTEXE)
TARSRC += tlabel.in tlabel.out

C++SRC = bool.cc trapfpe.cc errorhandler.cc except.cc ADTADV.cc \
	mathutil.cc polyroot.cc eigen.cc \
	IntArray.cc Vector.cc \
	Vector1.cc Vector1Array.cc BlockTri1.cc \
	Vector2.cc Vector2Array.cc BlockTri2.cc \
	Vector3.cc Vector3Array.cc BlockTri3.cc \
	label.cc Extractable.cc Insertable.cc Inserter.cc
LIBOBJ = $(C++SRC:%.cc=%.o)
LIBARC = $(OBJDIR)/libwutil.a
TESTSRC = ttrapfpe.cc tVector1.cc tVector2.cc tVector3.cc tVector4.cc \
	tBlockTri2.cc tBlockTri2per.cc tBlockTri2low.cc \
	tADTADV.cc tbool.cc texcept1.cc texcept2.cc texcept3.cc \
	tlabel.cc tpolyroot.cc
TESTEXE = $(TESTSRC:%.cc=$(OBJDIR)/%)
DEPENDOBJ = $(C++SRC:%.cc=$(OBJDIR)/%.d) \
	$(TESTSRC:%.cc=$(OBJDIR)/%.d) $(OBJDIR)/Vector4.d

all: $(LIBARC)

ifneq ($(RPHOSTTYPE),mingw32)
$(LIBARC): $(LIBARC)($(LIBOBJ))
else
$(LIBARC): $(LIBOBJ)
	ar -ruv $(LIBARC) $(LIBOBJ)
endif
	$(RANLIB) $(LIBARC)

.PRECIOUS: $(LIBARC)

test: $(TESTEXE)
$(TESTEXE): $(WUTILLIB)
$(OBJDIR)/tVector4: $(OBJDIR)/Vector4.o $(OBJDIR)/tVector4.o
	$(LINK.cc) -o $@ $(OBJDIR)/tVector4.o $(OBJDIR)/Vector4.o $(WUTILLIBS)

include $(WAVEDIR)/make/rules
ifneq ($(RPHOSTTYPE),mingw32)
-include $(DEPENDOBJ)
endif
