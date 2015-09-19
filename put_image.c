#include "put_image.h"

void put_image(filename,in)
  char filename [100];
  char in[X_SIZE*Y_SIZE];
{
FILE  *fd;

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
  
  if (fwrite(in,X_SIZE*Y_SIZE,1,fd) != 1)
    printf("Can't write image %s (exit_error used to be here).\n",filename);
    //exit_error("Can't write image %s.\n",filename);

  fclose(fd);
}
