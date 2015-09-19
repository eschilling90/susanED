#include "edge_draw.h"

void edge_draw(in,mid)
  unsigned char in[X_SIZE*Y_SIZE];
  unsigned char mid[X_SIZE*Y_SIZE];
{
int   i;
unsigned char *inp, *midp;

  
  
    /* mark 3x3 white block around each edge point */
    midp=mid;
    for (i=0; i<X_SIZE*Y_SIZE; i++)
    {
      if (*midp<8) 
      {
        inp = in + (midp - mid) - X_SIZE - 1;
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
      *(in + (midp - mid)) = 0;
    midp++;
  }
}

