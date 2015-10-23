#include "susan.sh"

import "c_uchar7220_queue";
import "rtos";
import "full_rtos";

behavior EdgeDrawThread_PartA(uchar image_buffer[7220], uchar mid[7220], in int thID, OS_API api_port) 
{

  void os_register(int threadID)
  {
    api_port.os_register(threadID);
  }

    void main(void) {
    
        int   i;
        uchar *inp, *midp;
        int drawing_mode;
        
        drawing_mode = DRAWING_MODE;
	
	api_port.os_register(thID);
	api_port.acquire_running_key(thID);

        if (drawing_mode==0)
        {
            /* mark 3x3 white block around each edge point */
            midp=mid + IMAGE_SIZE/PROCESSORS *thID;
            for (i=X_SIZE*Y_SIZE/PROCESSORS*thID; i<X_SIZE*Y_SIZE/PROCESSORS*(thID+1) + (thID+1==PROCESSORS && X_SIZE*Y_SIZE%PROCESSORS!=0 ?X_SIZE*Y_SIZE%PROCESSORS : 0); i++)
            {
                if (*midp<8) 
                {
                    inp = image_buffer + (midp - mid) - X_SIZE - 1;
                    *inp++=255; *inp++=255; *inp=255; inp+=X_SIZE-2;
                    *inp++=255; *inp++;     *inp=255; inp+=X_SIZE-2;
                    *inp++=255; *inp++=255; *inp=255;
                }
                midp++;
            }
        }
     //waitfor(12000000);
     api_port.os_timewait(thID,12000000);
     api_port.os_terminate(thID);
     }   
   
};    


behavior EdgeDrawThread_PartB(uchar image_buffer[7220], uchar mid[7220], in int thID, OS_API api_port) 
{

 

    void main(void) {
    
        int   i;
        uchar  *midp;
        int drawing_mode;
        
        drawing_mode = DRAWING_MODE;
	
	api_port.os_register(thID);
	api_port.acquire_running_key(thID);
     
        /* now mark 1 black pixel at each edge point */
        midp=mid+ IMAGE_SIZE/PROCESSORS *thID;
        //for (i=0; i<X_SIZE*Y_SIZE; i++)
        for (i=X_SIZE*Y_SIZE/PROCESSORS*thID; i<X_SIZE*Y_SIZE/PROCESSORS*(thID+1) + (thID+1==PROCESSORS && X_SIZE*Y_SIZE%PROCESSORS!=0 ?X_SIZE*Y_SIZE%PROCESSORS : 0); i++)
        {
            if (*midp<8) 
                *(image_buffer+ (midp - mid)) = 0;
            midp++;
        }
    //waitfor(12000000);
    api_port.os_timewait(thID,12000000);
    api_port.os_terminate(thID);
    }
    
};    

behavior EdgeDraw_ReadInput(i_uchar7220_receiver in_image, i_uchar7220_receiver in_mid, uchar image_buffer[IMAGE_SIZE], uchar mid[IMAGE_SIZE])
{
    void main(void) {
        in_image.receive(&image_buffer);
        in_mid.receive(&mid);
    }      
};

behavior EdgeDraw_WriteOutput(uchar image_buffer[IMAGE_SIZE],  i_uchar7220_sender out_image)
{
    void main(void) {
        out_image.send(image_buffer);
    }
};

behavior EdgeDraw_PartA(uchar image_buffer[7220], uchar mid[7220],OS_API api_port)
{

    EdgeDrawThread_PartA edge_draw_a_thread_0(image_buffer, mid, 0, api_port);
    EdgeDrawThread_PartA edge_draw_a_thread_1(image_buffer, mid, 1, api_port);
    
    void main(void) {
	api_port.par_start();
      //fsm {
	par {
            //edge_draw_a_thread_0 : goto edge_draw_a_thread_1;
            //edge_draw_a_thread_1 : {}
	    edge_draw_a_thread_0;
	    edge_draw_a_thread_1;
        }    
	api_port.par_end();
    }     
};

behavior EdgeDraw_PartB(uchar image_buffer[7220], uchar mid[7220], OS_API api_port)
{

    EdgeDrawThread_PartB edge_draw_b_thread_0(image_buffer, mid, 0, api_port);
    EdgeDrawThread_PartB edge_draw_b_thread_1(image_buffer, mid, 1, api_port);
    
    void main(void) {
      api_port.par_start();
      par {
            edge_draw_b_thread_0;
            edge_draw_b_thread_1;
        }    
      api_port.par_end();
    }     
};


behavior EdgeDraw(i_uchar7220_receiver in_image, i_uchar7220_receiver in_mid,  i_uchar7220_sender out_image, OS_API api_port)
{

    
    uchar image_buffer[IMAGE_SIZE];
    uchar mid[IMAGE_SIZE];         
    
    EdgeDraw_ReadInput edge_draw_read_input(in_image, in_mid, image_buffer, mid);
    EdgeDraw_WriteOutput edge_draw_write_output(image_buffer, out_image);
    EdgeDraw_PartA edge_draw_a(image_buffer, mid, api_port);
    EdgeDraw_PartB edge_draw_b(image_buffer, mid, api_port);

    
    void main(void) {
    
        fsm{
            edge_draw_read_input: goto edge_draw_a;
            edge_draw_a: goto edge_draw_b;
            edge_draw_b: goto edge_draw_write_output;
            edge_draw_write_output: {}
        }
    }     
    
};    

behavior Draw(i_uchar7220_receiver in_image, i_uchar7220_receiver in_mid,  i_uchar7220_sender out_image)
{

    RTOS draw_rtos;
    EdgeDraw edge_draw(in_image, in_mid,  out_image, draw_rtos);
    
    void main(void) {
        edge_draw.main();
    }
    
};



behavior Draw_wrapper(i_uchar7220_receiver in_image, i_uchar7220_receiver in_mid,  i_uchar7220_sender out_image, in int thID, OS_API_TOP api_port_top)
{

  void os_register(int threadID)
  {
    api_port_top.os_register(threadID);
  }   

    Draw draw(in_image, in_mid,  out_image);
    
    void main(void) {
	api_port_top.acquire_running_key(thID);
        draw;
	api_port_top.os_terminate(thID);
    }
    
};

