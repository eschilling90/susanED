#ifndef __MNIST_SH__
#define __MNIST_SH__

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>
#include <time.h>
#include <stdint.h>

#define MNIST_TRAINING_SET_IMAGE_FILE_NAME "data/train-images-idx3-ubyte"
#define MNIST_TRAINING_SET_LABEL_FILE_NAME "data/train-labels-idx1-ubyte"
#define MNIST_TESTING_SET_IMAGE_FILE_NAME "data/t10k-images-idx3-ubyte"
#define MNIST_TESTING_SET_LABEL_FILE_NAME "data/t10k-labels-idx1-ubyte"

#define MNIST_MAX_TESTING_IMAGES 1000
#define MNIST_IMG_WIDTH 28
#define MNIST_IMG_HEIGHT 28
#define MNIST_IMG_SIZE 784

#define NUMBER_OF_INPUT_CELLS 784   ///< use 28*28 input cells (= number of pixels per MNIST image)
#define NUMBER_OF_OUTPUT_CELLS 10   ///< use 10 output cells to model 10 digits (0-9)

#define LEARNING_RATE 0.05          ///< Incremental increase for changing connection weights

#define QUEUE_SIZE 20ul

typedef struct MNIST_Image MNIST_Image;
typedef uint8_t MNIST_Label;

typedef struct Cell Cell;
typedef struct Layer Layer;
typedef struct Vector Vector;

/**
 * @brief Data block defining a MNIST image
 * @see http://yann.lecun.com/exdb/mnist/ for details
 */
struct MNIST_Image{
    uint8_t pixel[28*28];
};

/**
 * @brief Core unit of the neural network (neuron and synapses)
 */

struct Cell{
    double input [NUMBER_OF_INPUT_CELLS];
    double weight[NUMBER_OF_INPUT_CELLS];
    double output;
    double bias;
};

/**
 * @brief The single (output) layer of this network (a layer is number cells)
 */

struct Layer{
    Cell cell[NUMBER_OF_OUTPUT_CELLS];
};

/**
 * @brief Data structure containing defined number of integer values (the output vector contains values for 0-9)
 */

struct Vector{
    int val[NUMBER_OF_OUTPUT_CELLS];
};

#endif	// __MNIST_SH__
