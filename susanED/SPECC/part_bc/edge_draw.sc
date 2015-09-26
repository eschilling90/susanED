#include <stdio.h>

#define X_SIZE 76
#define Y_SIZE 95

//import "c_double_handshake";
import "c_queue";

behavior edge_draw(i_receiver port_mid2, i_receiver port_in2, i_sender port_in3)
{
int   i;
// unsigned char mid[X_SIZE*Y_SIZE], in[X_SIZE*Y_SIZE];
unsigned char mid[76*95];
unsigned char in2[76*95];
unsigned char *midp, *inp; 
int j;

// for debug
  FILE *fd;
  char filename[] = "array_in3";

  void main(void)
  {
    port_in2.receive(in2, X_SIZE*Y_SIZE*sizeof(unsigned char));
    printf("in2 is received\n");
    port_mid2.receive(mid, X_SIZE*Y_SIZE*sizeof(unsigned char));
    printf("mid2 is received\n");
#ifdef FOPENB
    if ((fd=fopen(filename,"wb")) == NULL)
#else
    if ((fd=fopen(filename,"w")) == NULL)
#endif
    printf("Can't output image %s (exit_error used to be here).\n",filename);


    /* mark 3x3 white block around each edge point */
    midp = mid;
    for (i=0; i<X_SIZE*Y_SIZE; i++)
    {
      if (*midp<8) 
      {
        inp = in2 + (midp - mid) - X_SIZE - 1;
        *inp++=255; *inp++=255; *inp=255; inp+=X_SIZE-2;
        *inp++=255; *inp++;     *inp=255; inp+=X_SIZE-2;
        *inp++=255; *inp++=255; *inp=255;
      }
      midp++;
    }
  

  /* now mark 1 black pixel at each edge point */
  midp=mid;
  for (i=0; i<X_SIZE*Y_SIZE; i++)
  {
    if (*midp<8) 
      *(in2 + (midp - mid)) = 0;
    midp++;
  }

  for(j=0;j<X_SIZE*Y_SIZE;j++)
  {
	fprintf(fd, "%c", in2[j]);
	fprintf(fd, "\n");
  }  

  fclose(fd);

  printf("start to send in3\n");
  port_in3.send(in2, X_SIZE*Y_SIZE*sizeof(unsigned char));
  printf("in3 is sent\n");
  }
};

