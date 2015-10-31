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

behavior PE1(out signal unsigned bit[ADDR_WIDTH-1:0] A,
	         signal unsigned bit[DATA_WIDTH-1:0] D,
	     out signal unsigned bit[1]    ready,
	     in  signal unsigned bit[1]    ack,
	     in  signal unsigned bit[1]	   intr_read,
	     in  signal unsigned bit[1]    intr_write)
{
  RTOS_TOP rtos;

  MasterHardwareBus protocol(A, D, ready, ack);
  MasterHardwareSyncDetect sync_read(intr_read);
  MasterHardwareSyncDetect sync_write(intr_write);
  MasterHardwareBusLinkAccess mac(protocol);
  MasterHardwareDriver driver_read(mac, sync_read);
  MasterHardwareDriver driver_write(mac, sync_write);

  TASK_PE1 susan(driver_read, driver_write, rtos);

  void main(void)
  {
    susan;
  }
};
