#include <stdio.h>
#include <fcntl.h>

#define X_SIZE 76
#define Y_SIZE 95

import "c_handshake";
import "c_double_handshake";

behavior stimulus(i_send port_start, i_sender port_in1)
{
  unsigned char in1[X_SIZE*Y_SIZE];
  char filename[200];
  //char *filename;

  void main(void)
  {
    //port_filename.receive(filename, 200*sizeof(char));
    FILE  *fd;
    //char *fd;
    char header [100],dummy[1000];
   // filename = "input_small.pgm";
/*int tmp_ary[X_SIZE][Y_SIZE];*/ //Added by AJG. then commented by TT



#ifdef FOPENB
    if ((fd=fopen("input_small.pgm","rb")) == NULL)
   //if ((fd=open("input_small.pgm","rb")) == NULL)
#else
    if ((fd=fopen("input_small.pgm","r")) == NULL)
    //if ((fd=open("input_small.pgm","r")) == NULL)
#endif
    //exit_error("Can't input image %s.\n",filename);
      printf("Can't input image %s.\n (exit_error used to be here)\n",filename);
	

  /* {{{ read header */

   header[0]=fgetc(fd);
    header[1]=fgetc(fd);
    if(!(header[0]=='P' && header[1]=='5'))
      printf("Image %s does not have binary PGM header (exit_error used to be here)\n",filename);
    //exit_error("Image %s does not have binary PGM header.\n",filename); 
    //gets(fd); //this gets rid of P5 (hopefully)

    fgets(dummy,1000,fd); //used these to get rid of header items from pgm file and replace getint()
    fgets(dummy,1000,fd);
    fgets(dummy,1000,fd);
    fgets(dummy,1000,fd);

//    gets(fd); //used these to get rid of header items from pgm file and replace getint()
 //   gets(fd);
 //   gets(fd);
 //   gets(fd);

/* }}} */

  //*in = (uchar *) malloc(X_SIZE * Y_SIZE); //this was removed by AJG 
  //*in = (unsigned char *) tmp_ary; //Added by AJG


    if (fread(in1,1,X_SIZE * Y_SIZE,fd) == 0)
//    if (read(in1,1,X_SIZE * Y_SIZE,fd) == 0)
      printf("Image %s is the wrong size (exit_error used to be here)\n",filename);
    //exit_error("Image %s is wrong size.\n",filename);

    fclose(fd);
 //   close(fd);
    port_start.send();
    printf("start signal is sent\n");
    port_in1.send(in1, X_SIZE*Y_SIZE*sizeof(unsigned char));
    printf("in1 is sent\n");
    //port_start.send("start");
  }
};
