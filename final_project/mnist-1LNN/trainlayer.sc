//#include "trainlayer.h"
#include "mnist.sh"

import "c_queue"

/**
 * @details Trains a layer by looping through and training its cells
 * @param l A pointer to the layer that is to be training
 */

behavior trainLayer(i_layer_sender layer, i_MNIST_Image_train_receiver image, i_MNIST_Label_train_receiver label)
{
  void main(void)
  {
    int errCount = 0;
    int imgCount = 0;
    int i, j;
    double err;

    double maxOut = 0;
    int maxInd = 0;

    MNIST_Image img[MNIST_MAX_TRAINING_IMAGES];
    MNIST_Label lbl[MNIST_MAX_TRAINING_IMAGES];

    Layer l;

    Vector targetOutput;

    image.receive(img);
    label.receive(lbl);

    // Loop through all images in the file
    for (imgCount=0; imgCount<MNIST_MAX_TRAINING_IMAGES; imgCount++){
        // set target ouput of the number displayed in the current image (=label) to 1, all others to 0
	for (i=0; i<NUMBER_OF_OUTPUT_CELLS; i++){
            targetOutput.val[i] = (i==lbl[imgCount]) ? 1 : 0;
	}
        
        // loop through all output cells for the given image
        for (i=0; i < NUMBER_OF_OUTPUT_CELLS; i++){
    	    for (j=0; j<NUMBER_OF_INPUT_CELLS; j++){
        	l.cell[i].input[j] = img[imgCount].pixel[j] ? 1 : 0;
    	    }

    	    l.cell[i].output=0;
    	    for (j=0; j<NUMBER_OF_INPUT_CELLS; j++){
        	l.cell[i].output += l.cell[i].input[j] * l.cell[i].weight[j];
    	    }
    	    l.cell[i].output /= NUMBER_OF_INPUT_CELLS;             // normalize output (0-1)

    	    // learning (by updating the weights)
	    err = targetOutput.val[i] - l.cell[i].output;
    	    for (j=0; j<NUMBER_OF_INPUT_CELLS; j++){
        	l.cell[i].weight[j] += LEARNING_RATE * l.cell[i].input[j] * err;
    	    }
        }
        /*
    	for (i=0; i<NUMBER_OF_OUTPUT_CELLS; i++){

            if (l.cell[i].output > maxOut){
            	maxOut = l.cell[i].output;
            	maxInd = i;
            }
    	}*/
        // if (maxInd!=lbl[imgCount]) errCount++;
        
    }
    layer.send(l);
  }
};
