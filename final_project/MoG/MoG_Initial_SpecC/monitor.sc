#include <stdio.h>
#include <stdlib.h>

#include "constants.h"
#include "mix_g.h"

import "c_queue";

void save_frame(unsigned char fr[WIDTH*HEIGHT], char base[], int idx) {
  FILE *pFile;
  char filename[32];
  int grayval, pix_idx;

  sprintf(filename, "proc/%s%03d.pgm", base, idx);
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

behavior Monitor(i_receiver frameBg, i_receiver frameFg) {

  int frame_idx;
  unsigned char frameB[WIDTH*HEIGHT];
  unsigned char frameF[WIDTH*HEIGHT];

  void main(void) {
    for(frame_idx=0; frame_idx < NUM_FRAMES; frame_idx++) {
      frameBg.receive(frameB, sizeof(frameB));
      fprintf(stderr, "Frame %03d.\n", frame_idx);
      save_frame(frameB, "bg", frame_idx);
      frameFg.receive(frameF, sizeof(frameF));
      save_frame(frameF, "fg", frame_idx);
    }
    return;
  }
};
