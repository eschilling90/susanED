#include "setup_brightness_lut.h"

void setup_brightness_lut(bp)
  unsigned char **bp;
{
int   k;
float temp;
int tmp_ary[516];
int thresh=20;
int form=6;

//  *bp=(unsigned char *)malloc(516);
  *bp=(unsigned char *) tmp_ary; //added by AJG
  *bp=*bp+258;

  for(k=-256;k<257;k++)
  {
    temp=((float)k)/((float)thresh);
    temp=temp*temp;
    if (form==6)
      temp=temp*temp*temp;
    temp=100.0*exp(-temp);
    *(*bp+k)= (unsigned char)temp;
  }
  
}
