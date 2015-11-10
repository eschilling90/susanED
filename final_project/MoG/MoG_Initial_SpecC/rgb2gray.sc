#include <stdio.h>

//#include "rt_nonfinite.h"
#include "constants.h"
//#include "imgproc.sh"
#include "mix_g.h"

import "i_receive";
import "i_receiver";
import "i_sender";

void rgb_to_gray(unsigned char frgb[HEIGHT*WIDTH*3], real_T frgray[HEIGHT*WIDTH]) {
  int grayval, pix;

  for(pix=0; pix<HEIGHT*WIDTH*3; pix += 3) {
    grayval = (int) ((double)frgb[pix]*0.21+(double)frgb[pix+1]*.71+
                     (double)frgb[pix+2]*.07);
    frgray[pix/3] = grayval;
  }
}

void rgbg(unsigned char frgb[HEIGHT*WIDTH*3], unsigned char frgray[HEIGHT*WIDTH]) {
  int grayval, pix;

  for(pix=0; pix<HEIGHT*WIDTH*3; pix += 3) {
    grayval = (int) ((double)frgb[pix]*0.21+(double)frgb[pix+1]*.71+
                     (double)frgb[pix+2]*.07);
    frgray[pix/3] = (char) grayval;
  }
}


void save_frame_t(unsigned char fr[WIDTH*HEIGHT], char base[], int idx) {
//void save_frame_t(real_T fr[WIDTH*HEIGHT], char base[], int idx) {
  FILE *pFile;
  char filename[32];
  int grayval, pix_idx;

  sprintf(filename, "%s%d.pgm", base, idx);
  pFile = fopen(filename, "wb");
  if(pFile == NULL) {
    fprintf(stderr, "Could not open file %s. Terminating...\n", filename);
  }

  fprintf(pFile, "P5 %d %d 255 ", WIDTH, HEIGHT);
  for(pix_idx=0; pix_idx < HEIGHT*WIDTH; pix_idx++) {
    grayval = (int) fr[pix_idx];
    putc(grayval, pFile);
  }
  fclose(pFile);
}


behavior rgb2gray(i_receive start, i_receiver rgbFrame, i_sender gsFrame) {

  void main(void) {
    unsigned char frgb[HEIGHT*WIDTH*3];
    int frame_idx = 0;

    unsigned char frg[HEIGHT*WIDTH];

    start.receive();
    fprintf(stderr, "Received start signal.\n");

    for(frame_idx=0; frame_idx < NUM_FRAMES; frame_idx++) {
      fprintf(stderr, "Converting frame %d to grayscale.\n", frame_idx);
      rgbFrame.receive(&frgb, sizeof(frgb));
      fprintf(stderr, "Received a frame.\n");
      rgbg(frgb, frg);
      fprintf(stderr, "Converted frame.\n");
      gsFrame.send(&frg, sizeof(frg));
      fprintf(stderr, "Sent grayscale frame.\n");
    }
    return;
  }
};
