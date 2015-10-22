#include <stdio.h>

#define THE_OTHER_THREAD(x) (1-(x))
#define MAX_THREAD_NUM 3

typedef struct os_thread
{
  int registered;	// an actiated thread
  int running;		// currently running
}os_thread;

typedef struct array_queue
{
  int valid[MAX_THREAD_NUM];
  int ID[MAX_THREAD_NUM];
}array_queue;

void enQueue(array_queue q, int threadID)
{
  int i;
  for(i = 0; i < MAX_THREAD_NUM; i++)
    if(q.valid[i] == 0)
    {
      q.valid[i] = 1;
      q.ID = threadID;
    }
}

int deQueue(array_queue q)
{
  int i;
  int tmp;

  tmp = q.ID[0];
  for(i = 1; i < MAX_THREAD_NUM; i++)
    if(q.valid[i] == 1)
    {
      q.valid[i-1] = q.valid[i];
      q.ID[i-1] = q.ID[i];
    }
  return tmp;
}

int is_empty(array_queue q)
{
  return !(q.valid[0]);
}


interface OS_API
{
  /*
   * @intro: Clear all the marks for all threads
   */
  void os_init();

  /*
   * @intro: register a thread, make it active, does not mean running
   */
  void os_register(int);

  /*
   * @intro: register a thread, make it active, does not mean running
   */
  void os_terminate(int);

  /*
   * @intro: try getting the key to run, blocked if fail
   */
  void acquire_running_key(int threadID);

  /*
   * @intro: wait for given time and switch to the other thread
   */
  void os_timewait(int threadID, int time);

  /*
   * @intro: wapper for par
   */
  void par_start();
  void par_end();
};

channel RTOS implements OS_API
{
  os_thread thread[3];

  array_queue ready_queue;

  event run_th0;
  event run_th1;
  event run_th2;

  void os_init()
  {
    thread[0].registered = 0;
    thread[0].running = 0;
    thread[1].registered = 0;
    thread[1].running = 0;
    thread[2].registered = 0;
    thread[2].running = 0;
  }

  void os_register(int threadID)
  {
    thread[threadID].registered = 1;
    enQueue(ready_queue, threadID);
printf("th%d registered!\n", threadID);
  }

  void os_terminate(int threadID)
  {
    thread[threadID].registered = 0;
    thread[threadID].running = 0;
    if (threadID == 0)
      notify th0_chunk_done;
    else if (threadID == 1)
      notify th1_chunk_done;
    else
      notify th2_chunk_done;
printf("th%d terminated!\n", threadID);
  }

  int is_thread_running()
  {
    int i;
    for(i = 0; i < 3; i++)
      if (thread[i].running == 1)
        return 1;
    return 0;
  }

  void acquire_running_key(int threadID)
  {
printf("th%d tries getting the key!\n", threadID);
    if (is_thread_running())	// the other thread is running
      if (tail_in_queue == 0)
        wait th0_chunk_done;
      else if (tail_in_queue == 1)
        wait th1_chunk_done;
      else
        wait th2_chunk_done;
    thread[threadID].running = 1;
    tail_in_queue = threadID;
printf("th%d got the key!\n", threadID);
printf("Now the tail in queue is th%d!\n", threadID);
  }

  void os_timewait(int threadID, int time)
  {
    waitfor(time);
    thread[threadID].running = 0;
printf("th%d: switch context!\n", threadID);
    if (threadID == 0 && (thread[1].registered || thread[2].registered))
    {
      notify th0_chunk_done;
      if (tail_in_queue == 1)
      {
        tail_in_queue = 0;
printf("Now the tail in queue is th%d!\n", threadID);
        wait th1_chunk_done;
      }
      else
      {
        tail_in_queue = 0;
printf("Now the tail in queue is th%d!\n", threadID);
        wait th2_chunk_done;
      }
    }
    else if (threadID == 1 && (thread[0].registered || thread[2].registered))
    {
      notify th1_chunk_done;
      if (tail_in_queue == 0)
      {
        tail_in_queue = 1;
printf("Now the tail in queue is th%d!\n", threadID);
        wait th0_chunk_done;
      }
      else
      {
        tail_in_queue = 1;
printf("Now the tail in queue is th%d!\n", threadID);
        wait th2_chunk_done;
      }
    }
    else if (threadID == 2 && (thread[0].registered || thread[1].registered))
    {
      notify th2_chunk_done;
      if (tail_in_queue == 0)
      {
        tail_in_queue = 2;
        wait th0_chunk_done;
      }
      else
      {
        tail_in_queue = 2;
        wait th1_chunk_done;
      }
    }
    thread[threadID].running = 1;
  }

  void par_start()
  {
    //os_init();
  }

  void par_end(){}

};
