
#include "get_image.h"


void get_image(filename,in)
  char           filename[200];
  unsigned char  in[X_SIZE*Y_SIZE];
{
FILE  *fd;
char header [100],dummy[1000];
/*int tmp_ary[X_SIZE][Y_SIZE];*/ //Added by AJG. then commented by TT



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

  fgets(dummy,1000,fd); //used these to get rid of header items from pgm file and replace getint()
  fgets(dummy,1000,fd);
  fgets(dummy,1000,fd);
  fgets(dummy,1000,fd);

/* }}} */

  //*in = (uchar *) malloc(X_SIZE * Y_SIZE); //this was removed by AJG 
  //*in = (unsigned char *) tmp_ary; //Added by AJG


  if (fread(in,1,X_SIZE * Y_SIZE,fd) == 0)
    printf("Image %s is the wrong size (exit_error used to be here)\n",filename);
    //exit_error("Image %s is wrong size.\n",filename);

  fclose(fd);
}
// Status API Training Shop Blog About Pricing
// © 2015 GitHub, Inc. Terms Privacy Security Contact Help
