#include "put_image.h"

void put_image(filename,in,x_size,y_size)
  char filename [100];
  char *in;
  int  x_size,
       y_size;
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
  fprintf(fd,"%d %d\n",x_size,y_size);
  fprintf(fd,"255\n");
  
  if (fwrite(in,x_size*y_size,1,fd) != 1)
    printf("Can't write image %s (exit_error used to be here).\n",filename);
    //exit_error("Can't write image %s.\n",filename);

  fclose(fd);
}
