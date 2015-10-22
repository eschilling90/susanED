#include <stdio.h>
#include <sim.sh>

import "rtos";

behavior sub_behv(OS_API api_port, in int threadID)
{

  void main(void)
  {
    api_port.os_register(threadID);
    api_port.acquire_running_key(threadID);

    if (threadID == 0)
      api_port.os_timewait(0,10);

    if (threadID == 0)
      api_port.os_timewait(0,10);

    if (threadID == 0)
      api_port.os_timewait(0,10);

    printf("th%d is running\n", threadID);

    if (threadID == 1)
      api_port.os_timewait(1,15);

    api_port.os_terminate(threadID);
  }
};

behavior wapper(OS_API api_port)
{
  sub_behv b0(api_port, 0);
  sub_behv b1(api_port, 1);

  void main(void)
  {
    api_port.os_init();
    fsm{
	b0 : goto b1;
	b1 : {}
	}
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
