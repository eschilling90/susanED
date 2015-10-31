#include "susan.sh"

import "full_rtos";

behavior SetupBrightnessLutThread(uchar bp[516], in int thID, OS_API_TOP os_port)
{
       
    void main(void) {
        int   k;
        float temp;
        int thresh, form;
        
        thresh = BT;
        form = 6;

        //for(k=-256;k<257;k++)
       for(k=(-256)+512/PROCESSORS*thID; k<(-256)+512/PROCESSORS*thID+512/PROCESSORS+1; k++){
            temp=((float)k)/((float)thresh);
            temp=temp*temp;
            if (form==6)
                temp=temp*temp*temp;
            temp=100.0*exp(-temp);
            bp[(k+258)] = (uchar) temp;
        }
    os_port.os_timewait(0, 2700);
    }

};
 
behavior SetupBrightnessLut(uchar bp[516], OS_API_TOP os_port)
{
       
    SetupBrightnessLutThread setup_brightness_thread_0(bp, 0, os_port);
    SetupBrightnessLutThread setup_brightness_thread_1(bp, 1, os_port);
       
    void main(void) {
        fsm {
            setup_brightness_thread_0: goto setup_brightness_thread_1;
            setup_brightness_thread_1: {}
        }
    }

};

