#include <stdio.h>
#include "constants.h"

import "bgelim";
import "monitor";
import "stimulus";

import "c_handshake";
import "c_queue";
import "c_double_handshake";

behavior Testbench {
  c_handshake start;

  const unsigned long QUEUE_SIZE = HEIGHT*WIDTH*8*2;

  c_queue frameRGB(QUEUE_SIZE);
  c_queue frameBg(QUEUE_SIZE);
  c_queue frameFg(QUEUE_SIZE);

  Stimulus stim(frameRGB, start);
  bgelim bge(start, frameRGB, frameBg, frameFg);
  Monitor moni(frameBg, frameFg);

  void main(void) {
    par {
      stim;
      bge;
      moni;
    }
  }
};

behavior Main {

  Testbench tb;
  int main(void) {
    tb;
    return 0;
  }
};
