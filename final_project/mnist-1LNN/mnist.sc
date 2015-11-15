#include "mnist.sh"

import "typed_queue"

behavior mnist(i_MNIST_Image_train_sender train_image, i_MNIST_Label_train_sender train_label, i_MNIST_Image_test_sender test_image, i_MNIST_Label_test_sender test_label, i_prediction_sender prediction)
{
  c_layer_queue layer(QUEUE_SIZE);
  trainLayer train(layer, train_image, train_label);
  testLayer test(layer, test_image, test_label, prediction);

  void main(void)
  {
    par{train; test;}
  }
};
