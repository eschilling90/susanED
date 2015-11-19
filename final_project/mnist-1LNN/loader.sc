#include "mnist.sh"

import "typed_queue";

behavior loader(i_cell_sender neuton_queue)
{
  void main(void)
  {
    // open MNIST files
    FILE *weightFile;

    Cell neuton;

    char filename[100];
    size_t result = 0;
    int i, j;

    for(i = 0; i < NUMBER_OF_OUTPUT_CELLS; i++)
    {
	sprintf(filename, "dump/dumpfile_%02d", i);
        weightFile = fopen (filename, "rb");
        if (weightFile == NULL) {
            printf("Abort! Could not open dump file: %s\n", filename);
            exit(1);
        }

        for(j = 0; j < NUMBER_OF_INPUT_CELLS; j++)
        {
            result = fscanf(weightFile, "%lf", &neuton.weight[j]);
            if (result!=1) {
                printf("\nError when reading dump file! Abort!\n");
                exit(1);
            }
        }
        neuton_queue.send(neuton);
        fclose(weightFile);
    }
  }
};
