#include <stdio.h>
#include <math.h>

//import "c_double_handshake";
import "c_queue";

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

behavior brt_lut_fsm(i_sender port_bp)
{
  setup_brightness_lut inst(port_bp);
  void main(void)
  {
    fsm{inst: goto inst;}
  }
};
