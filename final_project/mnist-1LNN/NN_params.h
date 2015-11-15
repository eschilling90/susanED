/**
 * @file 1lnn.h
 * @brief Machine learning functionality for a 1-layer neural network
 * @author Matt Lind
 * @date July 2015
 */
#ifndef __1LNN_H__
#define __1LNN_H__

#include <stdio.h>

#define NUMBER_OF_INPUT_CELLS 784   ///< use 28*28 input cells (= number of pixels per MNIST image)
#define NUMBER_OF_OUTPUT_CELLS 10   ///< use 10 output cells to model 10 digits (0-9)

#define LEARNING_RATE 0.05          ///< Incremental increase for changing connection weights



typedef struct Cell Cell;
typedef struct Layer Layer;
typedef struct Vector Vector;




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

#endif	// __1LNN_H__
