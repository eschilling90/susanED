#include "susan.sh"

import "c_uchar7220_queue";

import "full_rtos";
import "HWBus";

behavior EdgeDrawThread_PartA(uchar image_buffer[7220], uchar mid[7220], in int thID, OS_API_TOP os_port)
{

    void main(void) {
    
        int   i;
        uchar *inp, *midp;
        int drawing_mode;
        
        drawing_mode = DRAWING_MODE;
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
     os_port.os_timewait(2, 12000000);
     }   
   
};    


behavior EdgeDrawThread_PartB(uchar image_buffer[7220], uchar mid[7220], in int thID, OS_API_TOP os_port)
{

    void main(void) {
    
        int   i;
        uchar  *midp;
        int drawing_mode;
        
        drawing_mode = DRAWING_MODE;
     
        /* now mark 1 black pixel at each edge point */
        midp=mid+ IMAGE_SIZE/PROCESSORS *thID;
        //for (i=0; i<X_SIZE*Y_SIZE; i++)
        for (i=X_SIZE*Y_SIZE/PROCESSORS*thID; i<X_SIZE*Y_SIZE/PROCESSORS*(thID+1) + (thID+1==PROCESSORS && X_SIZE*Y_SIZE%PROCESSORS!=0 ?X_SIZE*Y_SIZE%PROCESSORS : 0); i++)
        {
            if (*midp<8) 
                *(image_buffer+ (midp - mid)) = 0;
            midp++;
        }
    os_port.os_timewait(2, 12000000);
    }
    
};    

behavior EdgeDraw_ReadInput(i_uchar7220_receiver in_image, i_uchar7220_receiver in_mid, uchar image_buffer[IMAGE_SIZE], uchar mid[IMAGE_SIZE], OS_API_TOP os_port)
{
    void main(void) {
        in_image.receive(&image_buffer, 2, os_port);
        in_mid.receive(&mid, 2, os_port);
    }      
};

behavior EdgeDraw_WriteOutput(uchar image_buffer[IMAGE_SIZE],  os_dr_sender out_image, OS_API_TOP os_port)
{
    void main(void) {
        out_image.send(OUTPUT_ADDR, image_buffer, sizeof(image_buffer), 2, os_port);
    }
};

behavior EdgeDraw_PartA(uchar image_buffer[7220], uchar mid[7220], OS_API_TOP os_port)
{

    EdgeDrawThread_PartA edge_draw_a_thread_0(image_buffer, mid, 0, os_port);
    EdgeDrawThread_PartA edge_draw_a_thread_1(image_buffer, mid, 1, os_port);
    
    void main(void) {
      fsm {
            edge_draw_a_thread_0: goto edge_draw_a_thread_1;
            edge_draw_a_thread_1: {}
        }    
    }     
};

behavior EdgeDraw_PartB(uchar image_buffer[7220], uchar mid[7220], OS_API_TOP os_port)
{

    EdgeDrawThread_PartB edge_draw_b_thread_0(image_buffer, mid, 0, os_port);
    EdgeDrawThread_PartB edge_draw_b_thread_1(image_buffer, mid, 1, os_port);
    
    void main(void) {
      fsm {
            edge_draw_b_thread_0: goto edge_draw_b_thread_1;
            edge_draw_b_thread_1: {}
        }    
    }     
};


behavior EdgeDraw(i_uchar7220_receiver in_image, i_uchar7220_receiver in_mid,  os_dr_sender out_image, OS_API_TOP os_port)
{

    
    uchar image_buffer[IMAGE_SIZE];
    uchar mid[IMAGE_SIZE];         
    
    EdgeDraw_ReadInput edge_draw_read_input(in_image, in_mid, image_buffer, mid, os_port);
    EdgeDraw_WriteOutput edge_draw_write_output(image_buffer, out_image, os_port);
    EdgeDraw_PartA edge_draw_a(image_buffer, mid, os_port);
    EdgeDraw_PartB edge_draw_b(image_buffer, mid, os_port);

    
    void main(void) {
	os_port.os_register(2);	// fixed threadID, ID of EdgeDraw is 2
	os_port.acquire_running_key(2);
        fsm{
            edge_draw_read_input: goto edge_draw_a;
            edge_draw_a: goto edge_draw_b;
            edge_draw_b: goto edge_draw_write_output;
            edge_draw_write_output: {}
        }
	os_port.os_terminate(2);
    }     
    
};    

behavior Draw(i_uchar7220_receiver in_image, i_uchar7220_receiver in_mid,  os_dr_sender out_image, OS_API_TOP os_port)
{

    EdgeDraw edge_draw(in_image, in_mid,  out_image, os_port);
    
    void main(void) {
        fsm {
            edge_draw: {goto edge_draw;}
        }
    }
    
};


