#include <stdio.h>
#include "mnist.sh"

import "stimulus";
import "monitor";
import "mnist";
import "typed_queue";

behavior Main
{
  c_MNIST_Image_queue image_queue(QUEUE_SIZE);
  c_prediction_queue  prediction_queue(QUEUE_SIZE);

  stimulus sim(image_queue);
  mnist ann(image_queue, prediction_queue);
  monitor mon(prediction_queue);

  int main(void)
  {
    par
    {
      sim;
      ann;
      mon;
    }
    return 0;
  }
};
