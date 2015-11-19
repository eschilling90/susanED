#include "mnist.sh"

import "loader";
import "network";
import "typed_queue";

behavior mnist(i_MNIST_Image_receiver image, i_prediction_sender predict)
{
  c_cell_queue cell_queue(QUEUE_SIZE);

  loader ldr(cell_queue);
  network_fsm ntwk(image, cell_queue, predict);

  void main(void)
  {
    par{ldr; ntwk;}
  }
};
