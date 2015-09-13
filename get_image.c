

#include "get_image.h"


int getint(fd)
  FILE *fd;
{
  int c, i;
  char dummy[10000];

  c = getc(fd);
  while (1) /* find next integer */
  {
    if (c=='#')    /* if we're at a comment, read to end of line */
      fgets(dummy,9000,fd);
    if (c==EOF)
      //exit_error("Image %s not binary PGM.\n","is");
	printf("Image is not in binary PGM (exit_error used to be here)\n");
    if (c>='0' && c<='9')
      break;   /* found what we were looking for */
    c = getc(fd);
  }

  /* we're at the start of a number, continue until we hit a non-number */
  i = 0;
  while (1) {
    i = (i*10) + (c - '0');
    c = getc(fd);
    if (c==EOF) return (i);
    if (c<'0' || c>'9') break;
  }

  return (i);
}

void get_image(filename,in,x_size,y_size)
  char           filename[200];
  unsigned char  **in;
  int            x_size, y_size;
{
FILE  *fd;
char header [100];
int  tmp1,tmp2,tmp3;
int tmp_ary[x_size][y_size]; //Added by AJG



#ifdef FOPENB
  if ((fd=fopen(filename,"rb")) == NULL)
#else
  if ((fd=fopen(filename,"r")) == NULL)
#endif
    //exit_error("Can't input image %s.\n",filename);
    printf("Can't input image %s.\n (exit_error used to be here)\n",filename);
	

  /* {{{ read header */

  header[0]=fgetc(fd);
  header[1]=fgetc(fd);
  if(!(header[0]=='P' && header[1]=='5'))
    printf("Image %s does not have binary PGM header (exit_error used to be here)\n",filename);
    //exit_error("Image %s does not have binary PGM header.\n",filename);

  tmp1 = getint(fd); //x_size
  tmp2 = getint(fd); //y_size
  tmp3 = getint(fd); //maxval

/* }}} */

  //*in = (uchar *) malloc(x_size * y_size); //this was removed by AJG 
  *in = (unsigned char *) tmp_ary; //Added by AJG


  if (fread(*in,1,x_size * y_size,fd) == 0)
    printf("Image %s is the wrong size (exit_error used to be here)\n",filename);
    //exit_error("Image %s is wrong size.\n",filename);

  fclose(fd);
}
