#include "mnist.sh"

import "typed_queue";

behavior stimulus(i_MNIST_Image_train_sender train_image, i_MNIST_Label_train_sender train_label, i_MNIST_Image_test_sender test_image)
{
  void main(void)
  {
    // open MNIST files
    FILE *imageFile_train, *labelFile_train, *imageFile_test;

    MNIST_Image img_trn[MNIST_MAX_TRAINING_IMAGES];
    MNIST_Label lbl_trn[MNIST_MAX_TRAINING_IMAGES];
    MNIST_Image img_tst[MNIST_MAX_TESTING_IMAGES];

    char dummy[100];
    size_t result;

    imageFile_train = fopen (MNIST_TRAINING_SET_IMAGE_FILE_NAME, "rb");
    if (imageFile_train == NULL) {
        printf("Abort! Could not fine MNIST IMAGE file: %s\n",MNIST_TRAINING_SET_IMAGE_FILE_NAME);
        exit(1);
    }

    fread(dummy, 4, 4, imageFile_train);

    // read image to buffer
    result = fread(img_trn, sizeof(MNIST_Image), MNIST_MAX_TRAINING_IMAGES, imageFile_train);
    if (result!=1) {
        printf("\nError when reading IMAGE file! Abort!\n");
        exit(1);
    }

    fclose(imageFile_train);

    labelFile_train = fopen (MNIST_TRAINING_SET_LABEL_FILE_NAME, "rb");
    if (labelFile_train == NULL) {
        printf("Abort! Could not find MNIST LABEL file: %s\n",MNIST_TRAINING_SET_LABEL_FILE_NAME);
        exit(0);
    }

    fread(dummy, 4, 2, labelFile_train);

    // read image to buffer
    result = fread(lbl_trn, sizeof(MNIST_Label), MNIST_MAX_TRAINING_IMAGES, labelFile_train);
    if (result!=1) {
        printf("\nError when reading LABEL file! Abort!\n");
        exit(1);
    }

    fclose(labelFile_train);

    imageFile_test = fopen (MNIST_TESTING_SET_IMAGE_FILE_NAME, "rb");
    if (imageFile_test == NULL) {
        printf("Abort! Could not fine MNIST IMAGE file: %s\n",MNIST_TESTING_SET_IMAGE_FILE_NAME);
        exit(1);
    }

    fread(dummy, 4, 4, imageFile_test);

    // read image to buffer
    result = fread(img_tst, sizeof(MNIST_Image), MNIST_MAX_TESTING_IMAGES, imageFile_test);
    if (result!=1) {
        printf("\nError when reading IMAGE file! Abort!\n");
        exit(1);
    }

    fclose(imageFile_test);

    train_image.send(img_trn);
    train_label.send(lbl_trn);
    test_image.send(img_tst);
  }
};
