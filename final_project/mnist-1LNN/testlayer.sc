// #include "testlayer.h"
#include "mnist.sh"

import "typed_queue";

/**
 * @details Tests a layer by looping through and testing its cells
 * Exactly the same as TrainLayer() but WITHOUT LEARNING.
 * @param l A pointer to the layer that is to be training
 */

behavior testLayer(i_layer_receiver layer_ch, i_MNIST_Image_test_receiver image, i_prediction_sender predict)
{
  void main(void)
  {
    int errCount = 0;
    int imgCount = 0;
    int i, j;
    double maxOut = 0;
    uint8_t maxInd[MNIST_MAX_TESTING_IMAGES];
    
    MNIST_Image img[MNIST_MAX_TESTING_IMAGES];
    MNIST_Label lbl[MNIST_MAX_TESTING_IMAGES];

    Layer l;

    Vector targetOutput;

    image.receive(&img);
    layer_ch.receive(&l);

    // Loop through all images in the file
    for (imgCount=0; imgCount<MNIST_MAX_TESTING_IMAGES; imgCount++){
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
        }
        
	maxInd[imgCount] = 0;
    	for (i=0; i<NUMBER_OF_OUTPUT_CELLS; i++){
            if (l.cell[i].output > maxOut){
            	maxOut = l.cell[i].output;
            	maxInd[imgCount] = i;
            }
    	}

        if (maxInd[imgCount]!=lbl[imgCount]) errCount++;
        
        // printf("\n      Prediction: %d   Actual: %d ",predictedNum, lbl);
        
        // displayProgress(imgCount, errCount, 5, 66);
        
    }
    printf("    Accuracy is %d%%\n",(imgCount-errCount)*100/imgCount);
    
    predict.send(maxInd);
  }
};
