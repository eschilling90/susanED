#include "mnist.sh"

import "typed_queue";

behavior network(i_MNIST_Image_receiver image, i_cell_receiver neuton_queue, i_prediction_sender predict)
{
  void main(void)
  {
    static int is_layer_init = 0;
    int i, j;
    double maxOut = 0;
    uint8_t maxInd = 0;
    
    MNIST_Image img;

    Layer l;

    if (is_layer_init == 0)
    {
    	for(i = 0; i < NUMBER_OF_OUTPUT_CELLS; i++)
	    neuton_queue.receive(&l.cell[i]);
	is_layer_init = 1;
    }

    // loop through all output cells for the given image
    image.receive(&img);
    for (i=0; i < NUMBER_OF_OUTPUT_CELLS; i++){
        for (j=0; j<NUMBER_OF_INPUT_CELLS; j++){
            l.cell[i].input[j] = img.pixel[j] ? 1 : 0;
        }

        l.cell[i].output=0;

        for (j=0; j<NUMBER_OF_INPUT_CELLS; j++){
    	    l.cell[i].output += l.cell[i].input[j] * l.cell[i].weight[j];
        }

        l.cell[i].output /= NUMBER_OF_INPUT_CELLS;             // normalize output (0-1)
    }
    
    for (i=0; i<NUMBER_OF_OUTPUT_CELLS; i++){
        if (l.cell[i].output > maxOut){
            maxOut = l.cell[i].output;
            maxInd = i;
        }
    }

    predict.send(maxInd);
  }
};

behavior network_fsm(i_MNIST_Image_receiver image, i_cell_receiver neuton_queue, i_prediction_sender predict)
{
  network inst(image, neuton_queue, predict);

  void main(void)
  {
    fsm{inst: goto inst;}
  }
};
