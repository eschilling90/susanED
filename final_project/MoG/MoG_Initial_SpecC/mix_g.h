/*
 * mix_g.h
 *
 * Code generation for function 'mix_g'
 *
 * C source code generated on: Fri Dec  2 14:32:32 2011
 *
 */

#ifndef __MIX_G_H__
#define __MIX_G_H__

typedef signed char int8_T;
typedef unsigned char uint8_T;
typedef short int16_T;
typedef unsigned short uint16_T;
typedef int int32_T;
typedef unsigned int uint32_T;
typedef long int64_T;
typedef unsigned long uint64_T;
typedef float real32_T;
typedef double real64_T;

/*===========================================================================* 
 * Generic type definitions: real_T, time_T, boolean_T, int_T, uint_T,       * 
 *                           ulong_T, char_T and byte_T.                     * 
 *===========================================================================*/

typedef double real_T;
typedef double time_T;
typedef unsigned char boolean_T;
typedef int int_T;
typedef unsigned uint_T;
typedef unsigned long ulong_T;
typedef char char_T;
typedef char_T byte_T;


/* Type Definitions */
typedef struct
{
  uint8_T cdata[57600];
} struct_T;

/* Function Declarations */
extern void mix_g(real_T fr_bw[19200], real_T bg_bw[19200], real_T fg[19200]);
extern void mix_g_initialize(void);
#endif
/* End of code generation (mix_g.h) */
