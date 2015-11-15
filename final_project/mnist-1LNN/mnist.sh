#ifndef __MNIST_SH__
#define __MNIST_SH__

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>
#include <time.h>
#include <stdint.h>

// #include "mnist-utils.h"
// #include "mnist-stats.h"
#include "NN_params.h"

#define MNIST_TRAINING_SET_IMAGE_FILE_NAME "data/train-images-idx3-ubyte"
#define MNIST_TRAINING_SET_LABEL_FILE_NAME "data/train-labels-idx1-ubyte"
#define MNIST_TESTING_SET_IMAGE_FILE_NAME "data/t10k-images-idx3-ubyte"
#define MNIST_TESTING_SET_LABEL_FILE_NAME "data/t10k-labels-idx1-ubyte"

#define MNIST_MAX_TRAINING_IMAGES 60000
#define MNIST_MAX_TESTING_IMAGES 10000
#define MNIST_IMG_WIDTH 28
#define MNIST_IMG_HEIGHT 28
#define MNIST_IMG_SIZE 784

#define QUEUE_SIZE 5ul

typedef struct MNIST_ImageFileHeader MNIST_ImageFileHeader;
typedef struct MNIST_LabelFileHeader MNIST_LabelFileHeader;

typedef struct MNIST_Image MNIST_Image;
typedef uint8_t MNIST_Label;

/**
 * @brief Data block defining a MNIST image
 * @see http://yann.lecun.com/exdb/mnist/ for details
 */
struct MNIST_Image{
    uint8_t pixel[28*28];
};

/**
 * @brief Data block defining a MNIST image file header
 * @attention The fields in this structure are not used.
 * What matters is their byte size to move the file pointer
 * to the first image.
 * @see http://yann.lecun.com/exdb/mnist/ for details
 */
struct MNIST_ImageFileHeader{
    uint32_t magicNumber;
    uint32_t maxImages;
    uint32_t imgWidth;
    uint32_t imgHeight;
};

/**
 * @brief Data block defining a MNIST label file header
 * @attention The fields in this structure are not used.
 * What matters is their byte size to move the file pointer
 * to the first label.
 * @see http://yann.lecun.com/exdb/mnist/ for details
 */
struct MNIST_LabelFileHeader{
    uint32_t magicNumber;
    uint32_t maxImages;
};

/**
 * @details Reverse byte order in 32bit numbers
 * MNIST files contain all numbers in reversed byte order,
 * and hence must be reversed before using
 */
/*
uint32_t flipBytes(uint32_t n){

    uint32_t b0,b1,b2,b3;

    b0 = (n & 0x000000ff) <<  24u;
    b1 = (n & 0x0000ff00) <<   8u;
    b2 = (n & 0x00ff0000) >>   8u;
    b3 = (n & 0xff000000) >>  24u;

    return (b0 | b1 | b2 | b3);

}*/

#endif	// __MNIST_SH__
