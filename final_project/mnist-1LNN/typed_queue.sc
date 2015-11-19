#include <c_typed_queue.sh>   /* make the template available */
#include "mnist.sh"

typedef struct MNIST_Image MNIST_Image;

DEFINE_I_TYPED_TRANCEIVER(MNIST_Image, MNIST_Image)
DEFINE_I_TYPED_SENDER(MNIST_Image, MNIST_Image)
DEFINE_I_TYPED_RECEIVER(MNIST_Image, MNIST_Image)
DEFINE_C_TYPED_QUEUE(MNIST_Image, MNIST_Image)

typedef struct Cell cell;

DEFINE_I_TYPED_TRANCEIVER(cell, cell)
DEFINE_I_TYPED_SENDER(cell, cell)
DEFINE_I_TYPED_RECEIVER(cell, cell)
DEFINE_C_TYPED_QUEUE(cell, cell)

typedef uint8_t MNIST_Label;

DEFINE_I_TYPED_TRANCEIVER(MNIST_Label, MNIST_Label)
DEFINE_I_TYPED_SENDER(MNIST_Label, MNIST_Label)
DEFINE_I_TYPED_RECEIVER(MNIST_Label, MNIST_Label)
DEFINE_C_TYPED_QUEUE(MNIST_Label, MNIST_Label)

typedef uint8_t prediction;

DEFINE_I_TYPED_TRANCEIVER(prediction, prediction)
DEFINE_I_TYPED_SENDER(prediction, prediction)
DEFINE_I_TYPED_RECEIVER(prediction, prediction)
DEFINE_C_TYPED_QUEUE(prediction, prediction)
