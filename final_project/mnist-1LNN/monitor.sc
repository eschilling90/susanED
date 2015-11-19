#include "mnist.sh"

import "typed_queue";

behavior monitor(i_prediction_receiver predict)
{
  void main(void)
  {
    uint8_t speed_limit;
    MNIST_Label lbl;

    FILE *labelFile;
    size_t result;
    char dummy[50];

    int i;
    int errCount = 0;

    labelFile = fopen (MNIST_TESTING_SET_LABEL_FILE_NAME, "rb");
    if (labelFile == NULL) {
        printf("Abort! Could not find MNIST LABEL file: %s\n",MNIST_TESTING_SET_LABEL_FILE_NAME);
        exit(0);
    }

    fread(dummy, 4, 2, labelFile);

    for(i = 0; i < MNIST_MAX_TESTING_IMAGES; i++)
    {
        // read image to buffer
        result = fread(&lbl, sizeof(MNIST_Label), 1, labelFile);
        if (result!=1) {
            printf("\nError when reading LABEL file! Abort!\n");
            exit(1);
        }

        predict.receive(&speed_limit);

	if (speed_limit != lbl)	// error
	    errCount++;
    }

    printf("    Accuracy rate is %%%d\n", (MNIST_MAX_TESTING_IMAGES-errCount)*100/MNIST_MAX_TESTING_IMAGES);

    fclose(labelFile);
    exit(0);
  }
};
