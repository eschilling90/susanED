#include "susan.sh"

import "c_uchar7220_queue";
import "c_int7220_queue";

import "detect_edges";
import "susan_thin";
import "edge_draw";

import "full_rtos";
import "HWBus";

     
behavior TASK_PE1(os_dr_receiver in_image, os_dr_sender out_image, OS_API_TOP os_port) 
{

    c_int7220_queue r(1ul);
    c_uchar7220_queue mid(1ul);
    c_uchar7220_queue mid_edge_draw(1ul);
    c_uchar7220_queue image_edge_draw(1ul);

    Edges edges(in_image, r, mid, image_edge_draw, os_port);
    ThinWapper thin(r, mid, mid_edge_draw, os_port);
    Draw draw(image_edge_draw, mid_edge_draw, out_image, os_port);
        
    void main(void)
    {
	os_port.os_init();
	os_port.par_start();
        par {
            edges;
            thin;
            draw;
        }      
	os_port.par_end();
    }
   
};   

behavior PE1(IMasterHardwareBusLinkAccess mac,
	     os_receive sync_read,
	     os_receive sync_write)
{
  RTOS_TOP rtos;

  MasterHardwareDriver driver_read(mac, sync_read);
  MasterHardwareDriver driver_write(mac, sync_write);

  TASK_PE1 susan(driver_read, driver_write, rtos);

  void main(void)
  {
    susan;
  }
};
