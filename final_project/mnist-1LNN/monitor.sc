#include "mnist.sh"

import "typed_queue";

behavior monitor(i_prediction_receiver predict)
{
  void main(void)
  {
    uint8_t speed_limit[MNIST_MAX_TESTING_IMAGES];
    MNIST_Label lbl_tst[MNIST_MAX_TESTING_IMAGES];

    FILE *labelFile_test;
    size_t result;
    char dummy[50];

    int i;
    int errCount = 0;

    labelFile_test = fopen (MNIST_TESTING_SET_LABEL_FILE_NAME, "rb");
    if (labelFile_test == NULL) {
        printf("Abort! Could not find MNIST LABEL file: %s\n",MNIST_TESTING_SET_LABEL_FILE_NAME);
        exit(0);
    }

    fread(dummy, 4, 2, labelFile_test);

    // read image to buffer
    result = fread(lbl_tst, sizeof(MNIST_Label), MNIST_MAX_TESTING_IMAGES, labelFile_test);
    if (result!=1) {
        printf("\nError when reading LABEL file! Abort!\n");
        exit(1);
    }

    fclose(labelFile_test);

    predict.receive(&speed_limit);

    for(i = 0; i < MNIST_MAX_TESTING_IMAGES; i++)
	if (speed_limit[i] != lbl_tst[i])	// error
	    errCount++;

    printf("Accuracy rate is %d", (MNIST_MAX_TESTING_IMAGES-errCount)*100/MNIST_MAX_TESTING_IMAGES);
  }
};
