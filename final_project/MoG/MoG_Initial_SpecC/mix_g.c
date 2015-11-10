/*
 * mix_g.c
 *
 * Code generation for function 'mix_g'
 *
 * C source code generated on: Fri Dec  2 14:32:32 2011
 *
 */


/* Include files */
#include "rt_nonfinite.h"
#include "mix_g.h"
#include "constants.h"

/* Type Definitions */

/* Named Constants */

/* Variable Declarations */

/* Variable Definitions */
static boolean_T method_not_empty;
static uint32_T state[625];

/* Function Declarations */
static boolean_T all(const real_T x_data[1], const int32_T x_size[2]);
static void b_abs(const real_T x[19200], real_T y[19200]);
static real_T b_rand(void);
static void b_rdivide(const real_T x[3], const real_T y[3], real_T z[3]);
static real_T eml_rand_mt19937ar_stateful(void);
static void rdivide(const real_T x[3], real_T y, real_T z[3]);
static void rgb2grayscale(const uint8_T X[57600], real_T I[19200]);
static real_T rt_powd_snf(real_T u0, real_T u1);
static real_T rt_roundd_snf(real_T u);
static real_T sum(const real_T x[3]);

/* Function Definitions */
static boolean_T all(const real_T x_data[1], const int32_T x_size[2])
{
  boolean_T y;
  int32_T ix;
  boolean_T exitg1;
  y = TRUE;
  ix = 1;
  exitg1 = FALSE;
  while ((exitg1 == 0U) && (ix <= 1)) {
    if (x_data[0] == 0.0) {
      y = FALSE;
      exitg1 = TRUE;
    } else {
      ix = 2;
    }
  }

  return y;
}

static void b_abs(const real_T x[19200], real_T y[19200])
{
  int32_T k;
  for (k = 0; k < 19200; k++) {
    y[k] = fabs(x[k]);
  }
}

static real_T b_rand(void)
{
  if (!method_not_empty) {
    method_not_empty = TRUE;
  }

  return eml_rand_mt19937ar_stateful();
}

static void b_rdivide(const real_T x[3], const real_T y[3], real_T z[3])
{
  int32_T i2;
  for (i2 = 0; i2 < 3; i2++) {
    z[i2] = x[i2] / y[i2];
  }
}

static real_T eml_rand_mt19937ar_stateful(void)
{
  real_T r;
  int32_T exitg1;
  uint32_T u[2];
  int32_T i;
  uint32_T b_r;
  int32_T kk;
  uint32_T y;
  boolean_T isvalid;
  boolean_T exitg2;

  /* <LEGAL>   This is a uniform (0,1) pseudorandom number generator based on: */
  /* <LEGAL> */
  /* <LEGAL>    A C-program for MT19937, with initialization improved 2002/1/26. */
  /* <LEGAL>    Coded by Takuji Nishimura and Makoto Matsumoto. */
  /* <LEGAL> */
  /* <LEGAL>    Copyright (C) 1997 - 2002, Makoto Matsumoto and Takuji Nishimura, */
  /* <LEGAL>    All rights reserved. */
  /* <LEGAL> */
  /* <LEGAL>    Redistribution and use in source and binary forms, with or without */
  /* <LEGAL>    modification, are permitted provided that the following conditions */
  /* <LEGAL>    are met: */
  /* <LEGAL> */
  /* <LEGAL>      1. Redistributions of source code must retain the above copyright */
  /* <LEGAL>         notice, this list of conditions and the following disclaimer. */
  /* <LEGAL> */
  /* <LEGAL>      2. Redistributions in binary form must reproduce the above copyright */
  /* <LEGAL>         notice, this list of conditions and the following disclaimer in the */
  /* <LEGAL>         documentation and/or other materials provided with the distribution. */
  /* <LEGAL> */
  /* <LEGAL>      3. The names of its contributors may not be used to endorse or promote */
  /* <LEGAL>         products derived from this software without specific prior written */
  /* <LEGAL>         permission. */
  /* <LEGAL> */
  /* <LEGAL>    THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS */
  /* <LEGAL>    "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT */
  /* <LEGAL>    LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR */
  /* <LEGAL>    A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR */
  /* <LEGAL>    CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, */
  /* <LEGAL>    EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, */
  /* <LEGAL>    PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR */
  /* <LEGAL>    PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF */
  /* <LEGAL>    LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING */
  /* <LEGAL>    NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS */
  /* <LEGAL>    SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. */
  do {
    exitg1 = 0;
    for (i = 0; i < 2; i++) {
      u[i] = 0U;
    }

    for (i = 0; i < 2; i++) {
      b_r = state[624] + 1U;
      if (b_r >= 625U) {
        for (kk = 0; kk < 227; kk++) {
          y = (state[kk] & 2147483648U) | (state[1 + kk] & 2147483647U);
          if ((int32_T)(y & 1U) == 0) {
            y >>= 1U;
          } else {
            y = y >> 1U ^ 2567483615U;
          }

          state[kk] = state[397 + kk] ^ y;
        }

        for (kk = 0; kk < 396; kk++) {
          y = (state[227 + kk] & 2147483648U) | (state[228 + kk] & 2147483647U);
          if ((int32_T)(y & 1U) == 0) {
            y >>= 1U;
          } else {
            y = y >> 1U ^ 2567483615U;
          }

          state[227 + kk] = state[kk] ^ y;
        }

        y = (state[623] & 2147483648U) | (state[0] & 2147483647U);
        if ((int32_T)(y & 1U) == 0) {
          y >>= 1U;
        } else {
          y = y >> 1U ^ 2567483615U;
        }

        state[623] = state[396] ^ y;
        b_r = 1U;
      }

      y = state[(int32_T)b_r - 1];
      state[624] = b_r;
      y ^= y >> 11U;
      y ^= y << 7U & 2636928640U;
      y ^= y << 15U & 4022730752U;
      y ^= y >> 18U;
      u[i] = y;
    }

    u[0] >>= 5U;
    u[1] >>= 6U;
    r = 1.1102230246251565E-16 * ((real_T)u[0] * 6.7108864E+7 + (real_T)u[1]);
    if (r == 0.0) {
      if ((state[624] >= 1U) && (state[624] < 625U)) {
        isvalid = TRUE;
      } else {
        isvalid = FALSE;
      }

      if (isvalid) {
        isvalid = FALSE;
        i = 1;
        exitg2 = FALSE;
        while ((exitg2 == 0U) && (i < 625)) {
          if (state[i - 1] == 0U) {
            i++;
          } else {
            isvalid = TRUE;
            exitg2 = TRUE;
          }
        }
      }

      if (!isvalid) {
        b_r = 5489U;
        state[0] = 5489U;
        for (i = 0; i < 623; i++) {
          b_r = ((b_r ^ b_r >> 30U) * 1812433253U + (uint32_T)i) + 1U;
          state[1 + i] = b_r;
        }

        state[624] = 624U;
      }
    } else {
      exitg1 = 1;
    }
  } while (exitg1 == 0U);

  return r;
}

static void rdivide(const real_T x[3], real_T y, real_T z[3])
{
  int32_T i1;
  for (i1 = 0; i1 < 3; i1++) {
    z[i1] = x[i1] / y;
  }
}

static void rgb2grayscale(const uint8_T X[57600], real_T I[19200])
{
  uint8_T b_X[57600];
  int32_T k;
  int32_T i0;
  memcpy(&b_X[0], &X[0], 57600U * sizeof(uint8_T));
  for (k = 0; k < 19200; k++) {
    i0 = (int32_T)rt_roundd_snf(((real_T)((int32_T)((uint32_T)b_X[19200 + k] *
      38470U + (uint32_T)b_X[k] * 19595U) + (int32_T)((uint32_T)b_X[38400 + k] *
      7471U)) + 32768.0) / 65536.0);
    if ((uint32_T)i0 > 255U) {
      i0 = 255;
    }

    I[k] = (real_T)i0;
  }
}

static real_T rt_powd_snf(real_T u0, real_T u1)
{
  real_T y;
  real_T d0;
  real_T d1;
  if (rtIsNaN(u0) || rtIsNaN(u1)) {
    y = rtNaN;
  } else {
    d0 = fabs(u0);
    d1 = fabs(u1);
    if (rtIsInf(u1)) {
      if (d0 == 1.0) {
        y = rtNaN;
      } else if (d0 > 1.0) {
        if (u1 > 0.0) {
          y = rtInf;
        } else {
          y = 0.0;
        }
      } else if (u1 > 0.0) {
        y = 0.0;
      } else {
        y = rtInf;
      }
    } else if (d1 == 0.0) {
      y = 1.0;
    } else if (d1 == 1.0) {
      if (u1 > 0.0) {
        y = u0;
      } else {
        y = 1.0 / u0;
      }
    } else if (u1 == 2.0) {
      y = u0 * u0;
    } else if ((u1 == 0.5) && (u0 >= 0.0)) {
      y = sqrt(u0);
    } else if ((u0 < 0.0) && (u1 > floor(u1))) {
      y = rtNaN;
    } else {
      y = pow(u0, u1);
    }
  }

  return y;
}

static real_T rt_roundd_snf(real_T u)
{
  real_T y;
  if (fabs(u) < 4.503599627370496E+15) {
    if (u >= 0.5) {
      y = floor(u + 0.5);
    } else if (u > -0.5) {
      y = u * 0.0;
    } else {
      y = ceil(u - 0.5);
    }
  } else {
    y = u;
  }

  return y;
}

static real_T sum(const real_T x[3])
{
  real_T y;
  int32_T k;
  y = x[0];
  for (k = 0; k < 2; k++) {
    y += x[k + 1];
  }

  return y;
}


void mix_g(real_T frame, real_T bg_bw[19200], real_T fg[19200])
{
  int32_T k;
  static real_T u_diff[57600];
  static real_T mean[57600];
  static real_T w[57600];
  static real_T sd[57600];
  int32_T i;
  int32_T j;
  real_T rank_temp_data_idx_0;
  int32_T n;
  static real_T fr_bw[19200];
  int32_T m;
  static real_T b_fr_bw[19200];
  int32_T match;
  real_T b_w[3];
  real_T c_w[3];
  real_T dv0[3];
  boolean_T exitg1;
  real_T rank_data[3];
  int8_T rank_ind[3];
  real_T b_rank_data[1];
  int32_T rank_size[2];
  real_T c_rank_data[1];
  int32_T b_rank_size[2];

  // SUGGESTED FIX
  uint8_T fr[HEIGHT*WIDTH*3];
  real_T bg_bw[HEIGHT*WIDTH];
  real_T fg[HEIGHT*WIDTH];
  // END SUGGESTED FIX
  
  uint8_T frame[HEIGHT*WIDTH*3];


  /*   */
  /*  -----------------------  frame size variables ----------------------- */
  for (k = 0; k < 57600; k++) {
    fr[k] = source[0].cdata[k];
  }

  /*  read in 1st frame as background frame */
  /*  For the viptraffic.avi */
  /*  numFrames= 120 */
  /*  width   = 160 */
  /*  height  = 120 */
  for (k = 0; k < 19200; k++) {
    fg[k] = 0.0;
    bg_bw[k] = 0.0;
  }

  /*  --------------------- mog variables ----------------------------------- */
  /*  number of gaussian components (typically 3-5) */
  /*  number of background components */
  /*  positive deviation threshold */
  /*  learning rate (between 0 and 1) (from paper 0.01) */
  /*  foreground threshold (0.25 or 0.75 in paper) */
  /*  initial standard deviation (for new components) var = 36 in paper */
  /*  initialize weights array */
  /*  pixel means */
  /*  pixel standard deviations */
  memset(&u_diff[0], 0, 57600U * sizeof(real_T));

  /*  difference of each pixel from mean */
  /*  initial p variable (used to update mean and sd) */
  /*  rank of components (w/sd) */
  /*  --------------------- initialize component means and weights ----------- */
  /*  8-bit resolution */
  /*  pixel range (# of possible values) */
  for (i = 0; i < 120; i++) {
    for (j = 0; j < 160; j++) {
      for (k = 0; k < 3; k++) {
        rank_temp_data_idx_0 = b_rand();
        mean[(i + 120 * j) + 19200 * k] = rank_temp_data_idx_0 * 255.0;

        /*  means random (0-255) */
        w[(i + 120 * j) + 19200 * k] = 0.33333333333333331;

        /*  weights uniformly dist */
        sd[(i + 120 * j) + 19200 * k] = 6.0;

        /*  initialize to sd_init */
      }
    }
  }

  /* --------------------- process frames ----------------------------------- */
  for (n = 0; n < 120; n++) {
    /* fr = double(source(n).cdata);    % read in frame */
    for (k = 0; k < 57600; k++) {
      fr[k] = source[n].cdata[k];
    }

    /*  read in frame with uint8 data type */
    //rgb2grayscale(source[n].cdata, fr_bw);

    int grayval, h;
    for(h=0; h<HEIGHT*WIDTH*3; h=h+3){
      grayval = (int) ((double)source[n].cdata[h]*0.21+
                       (double)source[n].cdata[h+1]*.71+
                       (double)source[n].cdata[h+3]*.07);
      fr_bw[h/3] = (uint8_T) grayval;
    }

    // TESTING GRAYSCALE FRAME
    if(n%15 == 0) {
      memcpy(&frame, source[n].cdata, sizeof(frame));
      save_rgb2gray(frame, n, "f");
      save_array_gray(fr_bw, WIDTH, HEIGHT, n, "frbw");
    }
    // END TESTING GRAYSCALE FRAME
    
    /*  calculate difference of pixel values from mean */
    for (m = 0; m < 3; m++) {
      for (k = 0; k < 160; k++) {
        for (match = 0; match < 120; match++) {
          b_fr_bw[match + 120 * k] = fr_bw[match + 120 * k] - mean[(match + 120 *
            k) + 19200 * m];
        }
      }

      b_abs(b_fr_bw, *(real_T (*)[19200])&u_diff[19200 * m]);
    }

    /*  update gaussian components for each pixel */
    for (i = 0; i < 120; i++) {
      for (j = 0; j < 160; j++) {
        match = 0;
        for (k = 0; k < 3; k++) {
          if (fabs(u_diff[(i + 120 * j) + 19200 * k]) <= 2.5 * sd[(i + 120 * j)
              + 19200 * k]) {
            /*  pixel matches component */
            match = 1;

            /*  variable to signal component match */
            /*  update weights, mean, sd, p */
            w[(i + 120 * j) + 19200 * k] = 0.99 * w[(i + 120 * j) + 19200 * k] +
              0.01;
            rank_temp_data_idx_0 = 0.01 / w[(i + 120 * j) + 19200 * k];
            mean[(i + 120 * j) + 19200 * k] = (1.0 - rank_temp_data_idx_0) *
              mean[(i + 120 * j) + 19200 * k] + rank_temp_data_idx_0 * fr_bw[i +
              120 * j];
            sd[(i + 120 * j) + 19200 * k] = sqrt((1.0 - rank_temp_data_idx_0) *
              rt_powd_snf(sd[(i + 120 * j) + 19200 * k], 2.0) +
              rank_temp_data_idx_0 * rt_powd_snf(fr_bw[i + 120 * j] - mean[(i +
              120 * j) + 19200 * k], 2.0));
          } else {
            /*  pixel doesn't match component */
            w[(i + 120 * j) + 19200 * k] *= 0.99;

            /*  weight slighly decreases */
          }

          b_w[k] = w[(i + 120 * j) + 19200 * k];
          c_w[k] = w[(i + 120 * j) + 19200 * k];
        }

        rdivide(c_w, sum(b_w), dv0);
        for (k = 0; k < 3; k++) {
          w[(i + 120 * j) + 19200 * k] = dv0[k];
        }

        bg_bw[i + 120 * j] = 0.0;
        for (k = 0; k < 3; k++) {
          bg_bw[i + 120 * j] += mean[(i + 120 * j) + 19200 * k] * w[(i + 120 * j)
            + 19200 * k];
        }

        /*  if no components match, create new component */
        if (match == 0) {
          match = 1;
          rank_temp_data_idx_0 = w[i + 120 * j];
          k = 0;
          if (rtIsNaN(w[i + 120 * j])) {
            m = 2;
            exitg1 = FALSE;
            while ((exitg1 == 0U) && (m < 4)) {
              match = m;
              if (!rtIsNaN(w[(i + 120 * j) + 19200 * (m - 1)])) {
                rank_temp_data_idx_0 = w[(i + 120 * j) + 19200 * (m - 1)];
                exitg1 = TRUE;
              } else {
                m++;
              }
            }
          }

          if (match < 3) {
            while (match + 1 < 4) {
              if (w[(i + 120 * j) + 19200 * match] < rank_temp_data_idx_0) {
                rank_temp_data_idx_0 = w[(i + 120 * j) + 19200 * match];
                k = match;
              }

              match++;
            }
          }

          mean[(i + 120 * j) + 19200 * k] = fr_bw[i + 120 * j];
          sd[(i + 120 * j) + 19200 * k] = 6.0;
        }

        for (k = 0; k < 3; k++) {
          b_w[k] = w[(i + 120 * j) + 19200 * k];
          c_w[k] = sd[(i + 120 * j) + 19200 * k];
        }

        b_rdivide(b_w, c_w, dv0);
        for (k = 0; k < 3; k++) {
          rank_data[k] = dv0[k];

          /*  calculate component rank */
          rank_ind[k] = (int8_T)(1 + k);
        }

        /*  sort rank values */
        for (k = 0; k < 2; k++) {
          for (m = 0; m <= k; m++) {
            rank_size[0] = 1;
            rank_size[1] = 1;
            b_rank_data[0] = rank_data[k + 1];
            b_rank_size[0] = 1;
            b_rank_size[1] = 1;
            c_rank_data[0] = rank_data[m];
            if ((int32_T)all(b_rank_data, rank_size) > (int32_T)all(c_rank_data,
                 b_rank_size)) {
              /*  swap max values */
              rank_temp_data_idx_0 = rank_data[m];
              rank_data[m] = rank_data[k + 1];
              rank_data[k + 1] = rank_temp_data_idx_0;

              /*  swap max index values */
              match = rank_ind[m];
              rank_ind[m] = rank_ind[1 + k];
              rank_ind[1 + k] = (int8_T)match;
            }
          }
        }

        /*  calculate foreground */
        match = 0;
        k = 0;
        fg[i + 120 * j] = 0.0;
        while ((match == 0) && (k + 1 <= 3)) {
          if (w[(i + 120 * j) + 19200 * (rank_ind[k] - 1)] >= 0.25) {
            if (fabs(u_diff[(i + 120 * j) + 19200 * (rank_ind[k] - 1)]) <= 2.5 *
                sd[(i + 120 * j) + 19200 * (rank_ind[k] - 1)]) {
              fg[i + 120 * j] = 0.0;
              match = 1;
            } else {
              fg[i + 120 * j] = fr_bw[i + 120 * j];
            }
          }

          k++;
        }
      }
    }


    if(n%15 == 0) {
      save_array_gray(fg, WIDTH, HEIGHT, n, "procfg");
      save_array_gray(bg_bw, WIDTH, HEIGHT, n, "procbg");
    }

    //save_array_gray(fg);
    //write_image_frame(fg);
  }


  /* movie2avi(Mov1,'mixture_of_gaussians_output','fps',30);           % save movie as avi  */
  /* movie2avi(Mov2,'mixture_of_gaussians_background','fps',30);           % save movie as avi  */
  /*  codegen -report mix_g.m -args {source} */
  /* codegen -d build01 -config coder.config('lib') -report mix_g.m -args {source} */
}

void mix_g_initialize(void)
{
  uint32_T r;
  int32_T mti;
  rt_InitInfAndNaN(8U);
  method_not_empty = FALSE;
  memset(&state[0], 0, 625U * sizeof(uint32_T));
  r = 5489U;
  state[0] = 5489U;
  for (mti = 0; mti < 623; mti++) {
    r = ((r ^ r >> 30U) * 1812433253U + (uint32_T)mti) + 1U;
    state[1 + mti] = r;
  }

  state[624] = 624U;
}

void mix_g_terminate(void)
{
  /* (no terminate code required) */
}

/* End of code generation (mix_g.c) */
