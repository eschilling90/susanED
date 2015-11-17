#include "mnist.sh"

import "typed_queue";
import "trainlayer";
import "testlayer";

behavior mnist(i_MNIST_Image_train_receiver train_image, i_MNIST_Label_train_receiver train_label, i_MNIST_Image_test_receiver test_image, i_prediction_sender predict)
{
  c_layer_queue layer_ch(QUEUE_SIZE);
  trainLayer train(layer_ch, train_image, train_label);
  testLayer test(layer_ch, test_image, predict);

  void main(void)
  {
    par{train; test;}
  }
};
//behavior mnist(i_MNIST_Image_train_receiver train_image, i_MNIST_Label_train_receiver train_label, i_MNIST_Image_test_receiver test_image, i_prediction_sender predict)
