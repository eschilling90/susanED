#include "mnist.sh"

import "typed_queue";

behavior stimulus(i_MNIST_Image_sender image)
{
  void main(void)
  {
    // open MNIST files
    FILE *imageFile;

    MNIST_Image img;

    char dummy[100];
    size_t result = 0;
    int i;

    imageFile = fopen (MNIST_TESTING_SET_IMAGE_FILE_NAME, "rb");
    if (imageFile == NULL) {
        printf("Abort! Could not fine MNIST IMAGE file: %s\n",MNIST_TESTING_SET_IMAGE_FILE_NAME);
        exit(1);
    }

    fread(dummy, 4, 4, imageFile);

    for(i = 0; i < MNIST_MAX_TESTING_IMAGES; i++)
    {
    	// read image to buffer
    	result = fread(&img, sizeof(MNIST_Image), 1, imageFile);
    	if (result!=1) {
            printf("\nError when reading IMAGE file! Abort!\n");
            exit(1);
    	}
    	image.send(img);
    }
    fclose(imageFile);
  }
};
