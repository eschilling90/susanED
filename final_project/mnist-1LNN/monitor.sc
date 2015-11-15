#include "mnist.sh"

import "typed_queue";

behavior monitor(i_prediction_receiver prediction)
{
  void main(void)
  {
    int speed_limit;

    prediction.receive(&speed_limit);

    printf("speed limit is %d", speed_limit);
  }
};
