#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>

#include "constants.h"

import "c_queue";
import "c_handshake";

int decode_image(char frame[HEIGHT*WIDTH*3], char filename[]) {
  FILE *pFile;



  fprintf(stderr, "Opening %s.\n", filename);

  pFile = fopen(filename, "r");

  if(pFile == NULL) {
    fprintf(stderr, "Could not open %s.\n", filename);
    return -1;
  }

  fprintf(stderr, "Opened %s\n", filename);

  fseek(pFile, 15, SEEK_SET);
  fread(frame, sizeof(char), HEIGHT*WIDTH*3, pFile);
  fclose(pFile);
  
  return 0;
}

behavior Stimulus(i_sender out_queue, i_send start) {

  void main (void) {
    int frame_idx;
    char frame[HEIGHT*WIDTH*3];
    
    char filebase[] = "teststream/viptraffic";
    char filename[64];

    start.send();
    for(frame_idx=0; frame_idx < NUM_FRAMES; frame_idx++) {
      filename[0] = '\0';
      //fprintf(stderr, "Real: %s.ppm\n", filebase);
      sprintf(filename, "%s%d.ppm", filebase, frame_idx);
      if(decode_image(frame, filename) == 0) {
        out_queue.send(frame, sizeof(frame));
      } else {
        break;
      }
    }
    return;
  }
};
