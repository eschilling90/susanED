#include "constants.h"

import "rgb2gray";
import "mixtures_gaussian";

import "i_receive";
import "i_receiver";
import "i_sender";

import "c_queue";

behavior bgelim(i_receive start, i_receiver frameRGB, 
                i_sender frameBg, i_sender frameFg) {

  const unsigned long QUEUE_SIZE = HEIGHT*WIDTH*8*2;
  c_queue frameGS(QUEUE_SIZE);

  rgb2gray rgbg_c(start, frameRGB, frameGS);
  mixtures mixt(frameGS, frameBg, frameFg);

  void main(void) {
    par {
      rgbg_c;
      mixt;
    };
  }
};
