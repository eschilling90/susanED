#include <stdio.h>

#define THE_OTHER_THREAD(x) (1-(x))

typedef struct os_thread
{
  int registered;	// an actiated thread
  int running;		// currently running
}os_thread;

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
  os_thread thread[2];

  event th0_chunk_done;
  event th1_chunk_done;

  void os_init()
  {
    thread[0].registered = 0;
    thread[0].running = 0;
    thread[1].registered = 0;
    thread[1].running = 0;
  }

  void os_register(int threadID)
  {
    thread[threadID].registered = 1;
//printf("th%d registered!\n", threadID);
  }

  void os_terminate(int threadID)
  {
    thread[threadID].registered = 0;
    thread[threadID].running = 0;
    if (threadID == 0)
      notify th0_chunk_done;
    else
      notify th1_chunk_done;
//printf("th%d terminated!\n", threadID);
  }

  void acquire_running_key(int threadID)
  {
    if (thread[THE_OTHER_THREAD(threadID)].running)	// the other thread is running
      if (threadID == 0)
        wait th1_chunk_done;
      else
        wait th0_chunk_done;
    thread[threadID].running = 1;
  }

  void os_timewait(int threadID, int time)
  {
    waitfor(time);
    thread[threadID].running = 0;
    if (threadID == 0 && thread[1].registered)
    {
      notify th0_chunk_done;
      wait th1_chunk_done;
    }
    else if (threadID == 1 && thread[0].registered)
    {
      notify th1_chunk_done;
      wait th0_chunk_done;
    }
    thread[threadID].running = 1;
  }

  void par_start()
  {
    os_init();
  }

  void par_end(){}

};
