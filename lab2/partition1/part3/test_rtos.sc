#include <stdio.h>
#include <sim.sh>

import "full_rtos";

interface OS_REG
{
  void os_register(int threadID);
};

behavior sub_behv(OS_API api_port, in int threadID) implements OS_REG
{
  void os_register(int threadID)
  {
    api_port.os_register(threadID);
  }

  void main(void)
  {
    api_port.acquire_running_key(threadID);

    if (threadID == 0)
      api_port.os_timewait(0, 10);

    if (threadID == 0)
      api_port.os_timewait(0, 10);

    if (threadID == 0)
      api_port.os_timewait(0, 10);

    if (threadID == 2)
      api_port.os_timewait(2, 25);

    printf("th%d is running\n", threadID);

    if (threadID == 1)
      api_port.os_timewait(1, 15);

    api_port.os_terminate(threadID);
  }
};

behavior wapper(OS_API api_port)
{
  sub_behv b0(api_port, 0);
  sub_behv b1(api_port, 1);
  sub_behv b2(api_port, 2);

  void main(void)
  {
    api_port.os_init();
    b0.os_register(0);
    b1.os_register(1);
    b2.os_register(2);
    api_port.par_start();
    par{b0;b1;b2;}
    api_port.par_end();
  }
};

behavior Main
{
  RTOS os;
  wapper w(os);
  sim_time_string buf;

  int main(void)
  {
    w;
    printf("Simulation Time: %4s\n", time2str(buf, now()));
  }
};
