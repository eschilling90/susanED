#include "susan.sh"

import "rtos";




behavior SetupBrightnessLutThread(uchar bp[516], in int thID, OS_API api_port)
{

       
    void main(void) {
        int   k;
        float temp;
        int thresh, form;
        
        thresh = BT;
        form = 6;

	api_port.os_register(thID);
	api_port.acquire_running_key(thID);

        //for(k=-256;k<257;k++)
       for(k=(-256)+512/PROCESSORS*thID; k<(-256)+512/PROCESSORS*thID+512/PROCESSORS+1; k++){
            temp=((float)k)/((float)thresh);
            temp=temp*temp;
            if (form==6)
                temp=temp*temp*temp;
            temp=100.0*exp(-temp);
            bp[(k+258)] = (uchar) temp;
        }
    api_port.os_timewait(thID, 2700);
    api_port.os_terminate(thID);
    }

};
 
behavior SetupBrightnessLut(uchar bp[516], OS_API api_port)
{
       
    SetupBrightnessLutThread setup_brightness_thread_0(bp, 0, api_port);
    SetupBrightnessLutThread setup_brightness_thread_1(bp, 1, api_port);
       
    void main(void) {
        api_port.par_start();
        par {
            setup_brightness_thread_0;
            setup_brightness_thread_1;
        }
	api_port.par_end();
    }

};

