#include <stdio.h>
#include <math.h>

import "c_double_handshake";

behavior setup_brightness_lut(i_sender port_bp)
{
int   k;
float temp;
int thresh=20;
unsigned char bp[516];

  void main(void)
  {
    printf("start setup lut\n");
    for(k=-256;k<257;k++)
    {
      temp=((float)k)/((float)thresh);
      temp=temp*temp;
      temp=temp*temp*temp;
      temp=100.0*exp(-temp);
      bp[k+258]= (unsigned char)temp;
    }
    port_bp.send(bp, 516*sizeof(unsigned char));
    printf("bp is sent\n");
  }
};
