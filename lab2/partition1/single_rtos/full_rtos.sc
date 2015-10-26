#include <stdio.h>

#define THE_OTHER_THREAD(x) (1-(x))
#define MAX_THREAD_NUM 3

#define THREAD_NOTIFY(x) switch (x) {\
                           case 0: notify run_th0; break;\
                           case 1: notify run_th1; break;\
                           case 2: notify run_th2; break;\
			   default: break;\
			 }

#define THREAD_WAIT(x)   switch (x) {\
                           case 0: wait run_th0; break;\
                           case 1: wait run_th1; break;\
                           case 2: wait run_th2; break;\
			   default: break;\
			 }

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

void initQueue(array_queue* q)
{
  int i;      
  for(i = 0; i < MAX_THREAD_NUM; i++)
  {         
      q->valid[i] = 0;
      q->ID[i] = 0;
  }
}


void enQueue(array_queue* q, int threadID)
{
  int i;
  for(i = 0; i < MAX_THREAD_NUM; i++)
    if(q->valid[i] == 0)
    {
      q->valid[i] = 1;
      q->ID[i] = threadID;
      break;
    }
}

int deQueue(array_queue* q)
{
  int i;
  int tmp;

  if(q->valid[0] == 0)	//empty
    tmp = 3;
  else
    tmp = q->ID[0];

  for(i = 1; i < MAX_THREAD_NUM; i++)
  {
    q->valid[i-1] = q->valid[i];
    q->ID[i-1] = q->ID[i];
  }
  q->valid[2] = 0;
  q->ID[2] = 0;
  return tmp;
}

int is_empty(array_queue* q)
{
  return !(q->valid[0]);
}

int peek_head(array_queue* q)
{
  if (is_empty(q))
    return -1;
  else
    return q->ID[0];
}

void dbg_print_queue(array_queue* q)
{
  int i;
  for(i = 0; i < MAX_THREAD_NUM; i++)
    printf("No.%d: valid = %d, threadID = %d\n", i, q->valid[i], q->ID[i]);
}

interface OS_REG
{
  void os_register(int threadID);
};

interface OS_API_TOP
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

  /*
   * @intro: wapper for wait
   */
  void pre_wait(int);
  void post_wait(int);
};

channel RTOS_TOP implements OS_API_TOP
{
  os_thread thread[3];

  array_queue ready_queue;

  event run_th0;
  event run_th1;
  event run_th2;

  void os_init()
  {
//printf("init os\n");
    thread[0].registered = 0;
    thread[0].running = 0;
    thread[1].registered = 0;
    thread[1].running = 0;
    thread[2].registered = 0;
    thread[2].running = 0;

    initQueue(&ready_queue);
  }

  void os_register(int threadID)
  {
    thread[threadID].registered = 1;
//printf("th%d registered!\n", threadID);
    enQueue(&ready_queue, threadID);
  }

  void os_terminate(int threadID)
  {
    int next_thread;

    waitfor(1);
    thread[threadID].registered = 0;
    thread[threadID].running = 0;
    next_thread = peek_head(&ready_queue);    
    THREAD_NOTIFY(next_thread)
//printf("th%d terminated", threadID);
//if (next_thread == -1)	// no thread
//printf("!\n");
//else
//printf(", switch to th%d\n", next_thread);
  }

  int is_other_running()
  {
    int i;
    for(i = 0; i < 3; i++)
      if (thread[i].running == 1)
        return 1;
    return 0;
  }

  void acquire_running_key(int threadID)
  {
//printf("th%d tries getting the key!\n", threadID);
    if (is_other_running() || (threadID != peek_head(&ready_queue)))
    {
      THREAD_WAIT(threadID)
      deQueue(&ready_queue);
    }
    else
      deQueue(&ready_queue);
    thread[threadID].running = 1;
//printf("th%d got the key!\n", threadID);
//dbg_print_queue(&ready_queue);
  }

  void os_timewait(int threadID, int time)
  {
    int next_thread;

    waitfor(time);
    thread[threadID].running = 0;
//printf("th%d: switch context, ", threadID);
    enQueue(&ready_queue, threadID);
    //next_thread = deQueue(&ready_queue);
    next_thread = peek_head(&ready_queue);
//printf("next thread is th%d!\n", next_thread);
//dbg_print_queue(&ready_queue);
    THREAD_NOTIFY(next_thread);
    THREAD_WAIT(threadID);
    deQueue(&ready_queue);
    thread[threadID].running = 1;
  }

  void par_start()
  {
    // pre-register threads which are going to run in par{} block
    thread[0].registered = 1;
    thread[1].registered = 1;
    thread[2].registered = 1;
  }

  void par_end()
  {
    // logout all threads in par{} block
    thread[0].registered = 0;
    thread[1].registered = 0;
    thread[2].registered = 0;
  }

  void pre_wait(int threadID)
  {
    int next_thread;

    thread[threadID].running = 0;
//printf("th%d: wait for comm, switch context, ", threadID);
    next_thread = peek_head(&ready_queue);
//printf("next thread is th%d!\n", next_thread);
    THREAD_NOTIFY(next_thread);
  }

  void post_wait(int threadID)
  {
    enQueue(&ready_queue, threadID);
//printf("th%d: ready again!\n", threadID);
    THREAD_WAIT(threadID);
    deQueue(&ready_queue);
    thread[threadID].running = 1;
  }
};
