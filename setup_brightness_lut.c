#include "setup_brightness_lut.h"

void setup_brightness_lut(bp)
  unsigned char bp[516];
{
int   k;
float temp;
int thresh=20;
int form=6;

  for(k=-256;k<257;k++)
  {
    temp=((float)k)/((float)thresh);
    temp=temp*temp;
    if (form==6)
      temp=temp*temp*temp;
    temp=100.0*exp(-temp);
    bp[k+258]= (unsigned char)temp;
  }
  
}
