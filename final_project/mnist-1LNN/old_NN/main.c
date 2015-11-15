/**
 * @file main.c
 *
 * @mainpage MNIST 1-Layer Neural Network
 *
 * @brief Main characteristics: Only 1 layer (= input layer), no hidden layer.  Feed-forward only.
 * No Sigmoid activation function. No back propagation.\n
 *
 * @details Learning is achieved simply by incrementally updating the connection weights based on the comparison
 * with the desired target output (supervised learning).\n
 *
 * Its performance (success rate) of 85% is far off the state-of-the-art techniques (surprise, surprise) 
 * but close the Yann Lecun's 88% when using only a linear classifier.
 *
 * @see [Simple 1-Layer Neural Network for MNIST Handwriting Recognition](http://mmlind.github.io/Simple_1-Layer_Neural_Network_for_MNIST_Handwriting_Recognition/)
 * @see http://yann.lecun.com/exdb/mnist/
 * @version [Github Project Page](http://github.com/mmlind/mnist-1lnn/)
 * @author [Matt Lind](http://mmlind.github.io)
 * @date July 2015
 *
 */
 
 
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>
#include <time.h>

//#include "screen.h"
#include "mnist-utils.h"
#include "mnist-stats.h"
#include "1lnn.h"

#include "trainlayer.h"
#include "testlayer.h"

/*
 * @intro: dump weight
 */
void dump_weight(Layer *l)
{
    char filename[50];
    FILE *dumpfile;

    for(int i = 0; i < NUMBER_OF_OUTPUT_CELLS; i++)
    {
      sprintf(filename, "dump/dumpfile_%02d", i);
      dumpfile = fopen(filename, "w");

      if(dumpfile == NULL)
      {
        printf("can't open %s\n", filename);
        exit(1);
      }

      for(int j = 0; j < NUMBER_OF_INPUT_CELLS; j++)
        fprintf(dumpfile, "%5.4f ", l->cell[j].weight);

      fclose(dumpfile);
    }
}


/**
 * @details Main function to run MNIST-1LNN
 */

int main(int argc, const char * argv[]) {
    
    // remember the time in order to calculate processing time at the end
    time_t startTime = time(NULL);
    
    // clear screen of terminal window
    // clearScreen();
    printf("    MNIST-1LNN: a simple 1-layer neural network processing the MNIST handwriting images\n");
    
    // initialize all connection weights to random values between 0 and 1
    Layer outputLayer;
    // initLayer(&outputLayer);
    memset(&outputLayer, 0, sizeof(outputLayer));
    outputLayer = trainLayer(outputLayer);
    
    // dump_weight(&outputLayer);
    
    testLayer(outputLayer);

    // locateCursor(38, 5);
    
    // Calculate and print the program's total execution time
    time_t endTime = time(NULL);
    double executionTime = difftime(endTime, startTime);
    printf("\n    DONE! Total execution time: %.1f sec\n\n",executionTime);
    
    return 0;
}


