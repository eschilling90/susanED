#include <c_typed_queue.sh>   /* make the template available */
#include "mnist.sh"

typedef struct MNIST_Image  MNIST_Image_train[MNIST_MAX_TRAINING_IMAGES];

DEFINE_I_TYPED_TRANCEIVER(MNIST_Image_train, MNIST_Image_train)
DEFINE_I_TYPED_SENDER(MNIST_Image_train, MNIST_Image_train)
DEFINE_I_TYPED_RECEIVER(MNIST_Image_train, MNIST_Image_train)
DEFINE_C_TYPED_QUEUE(MNIST_Image_train, MNIST_Image_train)

typedef uint8_t MNIST_Label_train[MNIST_MAX_TRAINING_IMAGES];

DEFINE_I_TYPED_TRANCEIVER(MNIST_Label_train, MNIST_Label_train)
DEFINE_I_TYPED_SENDER(MNIST_Label_train, MNIST_Label_train)
DEFINE_I_TYPED_RECEIVER(MNIST_Label_train, MNIST_Label_train)
DEFINE_C_TYPED_QUEUE(MNIST_Label_train, MNIST_Label_train)

typedef struct MNIST_Image  MNIST_Image_test[MNIST_MAX_TESTING_IMAGES];

DEFINE_I_TYPED_TRANCEIVER(MNIST_Image_test, MNIST_Image_test)
DEFINE_I_TYPED_SENDER(MNIST_Image_test, MNIST_Image_test)
DEFINE_I_TYPED_RECEIVER(MNIST_Image_test, MNIST_Image_test)
DEFINE_C_TYPED_QUEUE(MNIST_Image_test, MNIST_Image_test)

typedef uint8_t MNIST_Label_test[MNIST_MAX_TESTING_IMAGES];

DEFINE_I_TYPED_TRANCEIVER(MNIST_Label_test, MNIST_Label_test)
DEFINE_I_TYPED_SENDER(MNIST_Label_test, MNIST_Label_test)
DEFINE_I_TYPED_RECEIVER(MNIST_Label_test, MNIST_Label_test)
DEFINE_C_TYPED_QUEUE(MNIST_Label_test, MNIST_Label_test)

typedef uint8_t prediction[MNIST_MAX_TESTING_IMAGES];

DEFINE_I_TYPED_TRANCEIVER(prediction, prediction)
DEFINE_I_TYPED_SENDER(prediction, prediction)
DEFINE_I_TYPED_RECEIVER(prediction, prediction)
DEFINE_C_TYPED_QUEUE(prediction, prediction)

typedef struct Layer layer;

DEFINE_I_TYPED_TRANCEIVER(layer, layer)
DEFINE_I_TYPED_SENDER(layer, layer)
DEFINE_I_TYPED_RECEIVER(layer, layer)
DEFINE_C_TYPED_QUEUE(layer, layer)
