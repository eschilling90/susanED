#include <stdio.h>
#include "mnist.sh"

import "stimulus";
import "monitor";
import "mnist";
import "typed_queue";

behavior Main
{
  c_MNIST_Image_train_queue train_image(QUEUE_SIZE);
  c_MNIST_Label_train_queue train_label(QUEUE_SIZE);
  c_MNIST_Image_test_queue test_image(QUEUE_SIZE);
  c_MNIST_Label_test_queue test_label(QUEUE_SIZE);
  c_prediction_queue  prediction_ch(QUEUE_SIZE);

  stimulus sim(train_image, train_label, test_image);
  mnist neural_network(train_image, train_label, test_image, prediction_ch);
  monitor mon(prediction_ch);

  int main(void)
  {
    par
    {
      sim;
      neural_network;
      mon;
    }
  }
};
