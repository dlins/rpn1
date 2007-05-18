#include <trapfpe.h>

/*
 * From http://www.fortran-2000.com/ArnaudRecipes/CompilerTricks.html#Glibc_FP
 */

#if defined(linux) && defined(__GNUC__) \
	&& ((__GNUC__ == 2 && __GNUC_MINOR__ >= 96) || __GNUC__ >= 3)

#define _GNU_SOURCE 1
#include <fenv.h>

void
trapfpe(void)
{
	/* Enable some exceptions.  At startup all exceptions are masked. */
	feenableexcept(FE_INVALID|FE_DIVBYZERO|FE_OVERFLOW);
}

#elif defined(linux) && defined(__GNUC__) \
	&& (__GNUC__ == 2 && __GNUC_MINOR__ >= 91)

#include <fpu_control.h>

void
trapfpe(void)
{
	fpu_control_t cw =
	_FPU_DEFAULT & ~(_FPU_MASK_IM | _FPU_MASK_ZM | _FPU_MASK_OM);
	_FPU_SETCW(cw);
	/* On x86, this expands to: */
	/* unsigned int cw = 0x037f & ~(0x01 | 0x04 | 0x08); */
	/* __asm__ ("fldcw %0" : : "m" (*&cw));              */
}

#else

void
trapfpe(void)
{
}

#endif
