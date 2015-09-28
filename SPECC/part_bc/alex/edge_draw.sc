#include <stdio.h>

#define X_SIZE 76
#define Y_SIZE 95
#define NUM_THREAD 4

#define QUEUE_SIZE 7220ul
#define SIZE 1ul

import "c_queue";
import "c_image_queue";
import "c_image_thread_queue";

behavior edge_draw_leaf(i_image_receiver port_mid2, i_image_receiver port_in2, i_sender port_in3, in int thread_id)
{
int   i;
// unsigned char mid[X_SIZE*Y_SIZE], in[X_SIZE*Y_SIZE];
unsigned char mid[76*95];
unsigned char in2[76*95];
unsigned char *midp, *inp; 
int j;
int start, end;
int add_extra_top,add_extra_bot;

  void main(void)
  {
    
//    port_in2.receive(in2, X_SIZE*Y_SIZE*sizeof(unsigned char));
    port_in2.receive(&in2);
    printf("now executing edge_draw thread:%d\n", thread_id);
    printf("in2 is received in thread:%d\n",thread_id);
    //port_mid2.receive(mid, X_SIZE*Y_SIZE*sizeof(unsigned char));
    port_mid2.receive(&mid);
    printf("mid2 is received in thread:%d\n",thread_id);
    

    add_extra_top = -(X_SIZE);
    if(thread_id==0){add_extra_top=0;}
    /* mark 3x3 white block around each edge point */
    midp = mid + thread_id * (X_SIZE * Y_SIZE / NUM_THREAD) + add_extra_top;
    //start = thread_id * (X_SIZE * Y_SIZE / NUM_THREAD);
    //end   = (thread_id + 1) * (X_SIZE * Y_SIZE / NUM_THREAD);
    add_extra_bot = 2*X_SIZE;

    if(thread_id==3){add_extra_bot=0;}
    for (i=0; i<(X_SIZE * Y_SIZE / NUM_THREAD)+add_extra_bot; i++)
    {
      if (*midp<8) 
      {
        inp = in2 + (midp - mid) - X_SIZE - 1;
        *inp++=255; *inp++=255; *inp=255; inp+=X_SIZE-2;
        *inp++=255; *inp++;     *inp=255; inp+=X_SIZE-2;
        *inp++=255; *inp++=255; *inp=255;
      }
      midp++;
    }
  

  /* now mark 1 black pixel at each edge point */
  midp = mid + thread_id * (X_SIZE * Y_SIZE / NUM_THREAD);
  for (i=0; i<X_SIZE*Y_SIZE/NUM_THREAD; i++)
  {
    if (*midp<8) 
      *(in2 + (midp - mid)) = 0;
    midp++;
  }

  //printf("start to send in3 \n");
  //im not sure we can do this
  in2 = in2 + thread_id * (X_SIZE * Y_SIZE / NUM_THREAD);	// only send part of image that has been drawn
  port_in3.send(in2, X_SIZE*Y_SIZE/NUM_THREAD*sizeof(unsigned char));
  //port_in3.send(in2);
  printf("in3 is sent in edge_draw thread:%d\n",thread_id);
  }
};

/*
 * @intro: distribute one image to four copies
 */
behavior distributor(i_image_sender port1, i_image_sender port2, i_image_sender port3, i_image_sender port4, i_image_receiver in_port)
{
  unsigned char buffer[X_SIZE * Y_SIZE];
  void main(void)
  {
    //in_port.receive(buffer, X_SIZE * Y_SIZE * sizeof(char));
    in_port.receive(&buffer);
    //port1.send(buffer, X_SIZE * Y_SIZE  * sizeof(char));
    port1.send(buffer);
    port2.send(buffer);
    port3.send(buffer);
    port4.send(buffer);
  }
};

/*
 * @intro: merge four subsections of image into one, order matters!
 */
behavior merger(i_receiver port1, i_receiver port2, i_receiver port3, i_receiver port4,i_sender out_port)
{
  unsigned char buffer[X_SIZE * Y_SIZE];
  void main(void)
  {
    port1.receive(buffer, X_SIZE * Y_SIZE / NUM_THREAD * sizeof(char));
    //port1.receive(&buffer);
    out_port.send(buffer, X_SIZE * Y_SIZE / NUM_THREAD * sizeof(char));
    //out_port.send(buffer);
    /*port2.receive(&buffer);
    out_port.send(buffer);
    port3.receive(&buffer);
    out_port.send(buffer);
    port4.receive(&buffer);
    out_port.send(buffer);*/
    port2.receive(buffer, X_SIZE * Y_SIZE / NUM_THREAD * sizeof(char));
    out_port.send(buffer, X_SIZE * Y_SIZE / NUM_THREAD * sizeof(char));
    port3.receive(buffer, X_SIZE * Y_SIZE / NUM_THREAD * sizeof(char));
    out_port.send(buffer, X_SIZE * Y_SIZE / NUM_THREAD * sizeof(char)); 
    port4.receive(buffer, X_SIZE * Y_SIZE / NUM_THREAD * sizeof(char));
    out_port.send(buffer, X_SIZE * Y_SIZE / NUM_THREAD * sizeof(char));
    // if port1 did not receive, later ports will be blocked, order is guaranteed
  }
};

/*
 * @intro: create a endless loop
 */
behavior edge_draw_fsm(i_image_receiver port_mid2, i_image_receiver port_in2, i_sender port_in3, in int thread_id)
{
  edge_draw_leaf inst(port_mid2, port_in2, port_in3, thread_id);
  void main(void)
  {
    fsm{ inst: goto inst;}
  }
};

/*
 * @intro: create a endless loop
 */
behavior merger_fsm(i_receiver port1, i_receiver port2, i_receiver port3, i_receiver port4, i_sender out_port)
{
  merger inst(port1, port2, port3, port4, out_port);
  void main(void)
  {
    fsm{ inst: goto inst;}
  }
};

/*
 * @intro: create a endless loop
 */
behavior distributor_fsm(i_image_sender port1, i_image_sender port2, i_image_sender port3, i_image_sender port4, i_image_receiver in_port)
{
  distributor inst(port1, port2, port3, port4, in_port);
  void main(void)
  {
    fsm{ inst: goto inst;}
  }
};

behavior edge_draw(i_image_receiver port_mid2, i_image_receiver port_in2, i_sender port_in3)
{
  c_queue q1(QUEUE_SIZE), q2(QUEUE_SIZE), q3(QUEUE_SIZE), q4(QUEUE_SIZE);
  //c_image_queue q1(SIZE), q2(SIZE), q3(SIZE), q4(SIZE);
  //c_image_thread_queue q1(SIZE), q2(SIZE), q3(SIZE), q4(SIZE);
  //c_queue midq1(QUEUE_SIZE), midq2(QUEUE_SIZE), midq3(QUEUE_SIZE), midq4(QUEUE_SIZE);
  c_image_queue midq1(SIZE), midq2(SIZE), midq3(SIZE), midq4(SIZE);
  //c_queue in2q1(QUEUE_SIZE), in2q2(QUEUE_SIZE), in2q3(QUEUE_SIZE), in2q4(QUEUE_SIZE);
  c_image_queue in2q1((SIZE)), in2q2((SIZE)), in2q3((SIZE)), in2q4((SIZE));
  distributor_fsm mid_distribute(midq1, midq2, midq3, midq4, port_mid2);
  distributor_fsm in2_distribute(in2q1, in2q2, in2q3, in2q4, port_in2);
  edge_draw_fsm inst1(midq1, in2q1, q1, 0);
  edge_draw_fsm inst2(midq2, in2q2, q2, 1);
  edge_draw_fsm inst3(midq3, in2q3, q3, 2);
  edge_draw_fsm inst4(midq4, in2q4, q4, 3);
  merger_fsm merge(q1, q2, q3, q4, port_in3);

  void main(void)
  {
    par{mid_distribute; in2_distribute; inst1; inst2; inst3; inst4; merge;}
  }
};
