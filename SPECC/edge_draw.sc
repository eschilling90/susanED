#include <stdio.h>

#define X_SIZE 76
#define Y_SIZE 95
#define NUM_THREAD 4

#define QUEUE_SIZE 7220ul

import "c_queue";

behavior edge_draw_leaf(i_receiver port_mid2, i_receiver port_in2, i_sender port_in3, in int thread_id)
{
int   i;
// unsigned char mid[X_SIZE*Y_SIZE], in[X_SIZE*Y_SIZE];
unsigned char mid[76*95];
unsigned char in2[76*95];
unsigned char *midp, *inp; 
int j;
int start, end;

  void main(void)
  {
    port_in2.receive(in2, X_SIZE*Y_SIZE*sizeof(unsigned char));
    printf("in2 is received\n");
    port_mid2.receive(mid, X_SIZE*Y_SIZE*sizeof(unsigned char));
    printf("mid2 is received\n");
    printf("now executing thread:%d\n", thread_id);

    /* mark 3x3 white block around each edge point */
    midp = mid + thread_id * (X_SIZE * Y_SIZE / NUM_THREAD);
    //start = thread_id * (X_SIZE * Y_SIZE / NUM_THREAD);
    //end   = (thread_id + 1) * (X_SIZE * Y_SIZE / NUM_THREAD);
    for (i=0; i<X_SIZE * Y_SIZE / NUM_THREAD; i++)
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

  printf("start to send in3\n");
  inp = in2 + thread_id * (X_SIZE * Y_SIZE / NUM_THREAD);	// only send part of image that has been drawn
  port_in3.send(inp, X_SIZE*Y_SIZE/NUM_THREAD*sizeof(unsigned char));
  printf("in3 is sent\n");
  }
};

/*
 * @intro: distribute one image to four copies
 */
behavior distributor(i_sender port1, i_sender port2, i_sender port3, i_sender port4, i_receiver in_port)
{
  char buffer[X_SIZE * Y_SIZE];
  void main(void)
  {
    in_port.receive(buffer, X_SIZE * Y_SIZE * sizeof(char));
    port1.send(buffer, X_SIZE * Y_SIZE  * sizeof(char));
    port2.send(buffer, X_SIZE * Y_SIZE  * sizeof(char));
    port3.send(buffer, X_SIZE * Y_SIZE  * sizeof(char));
    port4.send(buffer, X_SIZE * Y_SIZE  * sizeof(char));
  }
};

/*
 * @intro: merge four subsections of image into one, order matters!
 */
behavior merger(i_receiver port1, i_receiver port2, i_receiver port3, i_receiver port4, i_sender out_port)
{
  char buffer[X_SIZE * Y_SIZE];
  void main(void)
  {
    port1.receive(buffer, X_SIZE * Y_SIZE / NUM_THREAD * sizeof(char));
    out_port.send(buffer, X_SIZE * Y_SIZE / NUM_THREAD * sizeof(char));
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
behavior edge_draw_fsm(i_receiver port_mid2, i_receiver port_in2, i_sender port_in3, in int thread_id)
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
behavior distributor_fsm(i_sender port1, i_sender port2, i_sender port3, i_sender port4, i_receiver in_port)
{
  distributor inst(port1, port2, port3, port4, in_port);
  void main(void)
  {
    fsm{ inst: goto inst;}
  }
};

behavior edge_draw(i_receiver port_mid2, i_receiver port_in2, i_sender port_in3)
{
  c_queue q1(QUEUE_SIZE), q2(QUEUE_SIZE), q3(QUEUE_SIZE), q4(QUEUE_SIZE);
  c_queue midq1(QUEUE_SIZE), midq2(QUEUE_SIZE), midq3(QUEUE_SIZE), midq4(QUEUE_SIZE);
  c_queue in2q1(QUEUE_SIZE), in2q2(QUEUE_SIZE), in2q3(QUEUE_SIZE), in2q4(QUEUE_SIZE);
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
