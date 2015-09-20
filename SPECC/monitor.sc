#include <stdio.h>

#define X_SIZE 76
#define Y_SIZE 95

#define OUT_FILE "out.pgm"

import "c_queue";

behavior Monitor(i_receiver port_in3)
{
  FILE  *fd;
  char filename [] = OUT_FILE;
  unsigned char in3[X_SIZE*Y_SIZE];

  void main(void)
  {
    port_in3.receive(in3, X_SIZE*Y_SIZE*sizeof(unsigned char));

#ifdef FOPENB
    if ((fd=fopen(filename,"wb")) == NULL) 
#else
    if ((fd=fopen(filename,"w")) == NULL) 
#endif
    printf("Can't output image %s (exit_error used to be here).\n",filename);
    //exit_error("Can't output image%s.\n",filename);

    fprintf(fd,"P5\n");
    fprintf(fd,"%d %d\n",X_SIZE,Y_SIZE);
    fprintf(fd,"255\n");
  
    if (fwrite(in3,X_SIZE*Y_SIZE,1,fd) != 1)
      printf("Can't write image %s (exit_error used to be here).\n",filename);
    //exit_error("Can't write image %s.\n",filename);

    fclose(fd);
  }
};
