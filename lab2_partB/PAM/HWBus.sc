//////////////////////////////////////////////////////////////////////
// File:   	HWBus.sc
//////////////////////////////////////////////////////////////////////

import "i_send";
import "i_receive";
import "full_rtos";

// Simple hardware bus

#define DATA_WIDTH	32u
#define ADDR_WIDTH	1u

#if DATA_WIDTH == 32u
# define DATA_BYTES 4u
#elif DATA_WIDTH == 16u
# define DATA_BYTES 2u
#elif DATA_WIDTH == 8u
# define DATA_BYTES 1u
#else
# error "Invalid data width"
#endif


/* ----- Physical layer, bus protocol ----- */

// Protocol primitives
interface IMasterHardwareBusProtocol
{
  void masterWrite(unsigned bit[ADDR_WIDTH-1:0] a, unsigned bit[DATA_WIDTH-1:0] d);
  void masterRead (unsigned bit[ADDR_WIDTH-1:0] a, unsigned bit[DATA_WIDTH-1:0] *d);
};

interface ISlaveHardwareBusProtocol
{
  void slaveWrite(unsigned bit[ADDR_WIDTH-1:0] a, unsigned bit[DATA_WIDTH-1:0] d);
  void slaveRead (unsigned bit[ADDR_WIDTH-1:0] a, unsigned bit[DATA_WIDTH-1:0] *d);
};

// Master protocol implementation
channel MasterHardwareBus(out signal unsigned bit[ADDR_WIDTH-1:0] A,
                              signal unsigned bit[DATA_WIDTH-1:0] D,
                          out signal unsigned bit[1]    ready,
                          in  signal unsigned bit[1]    ack)
  implements IMasterHardwareBusProtocol
{
  void masterWrite(unsigned bit[ADDR_WIDTH-1:0] a, unsigned bit[DATA_WIDTH-1:0] d)
  {
    do {
      t1: A = a;
          D = d;
          waitfor(5000);
      t2: ready = 1;
          while(!ack) wait(ack);
      t3: waitfor(10000);
      t4: ready = 0;
          while(ack) wait(ack);
    }
    timing {
      range(t1; t2; 5000; 15000);
      range(t3; t4; 10000; 25000);
    }
  }

  void masterRead (unsigned bit[ADDR_WIDTH-1:0] a, unsigned bit[DATA_WIDTH-1:0] *d)
  {
    do {
      t1: A = a;
          waitfor(5000);
      t2: ready = 1;
          while(!ack) wait(ack);
      t3: *d = D;
          waitfor(15000);
      t4: ready = 0;
          while(ack) wait(ack);
    }
    timing {
      range(t1; t2; 5000; 15000);
      range(t3; t4; 10000; 25000);
    }
//printf("master finished one-word reading\n");
  }

};


// Slave protocol implementation
channel SlaveHardwareBus(in  signal unsigned bit[ADDR_WIDTH-1:0] A,
                             signal unsigned bit[DATA_WIDTH-1:0] D,
                         in  signal unsigned bit[1]    ready,
                         out signal unsigned bit[1]    ack)
  implements ISlaveHardwareBusProtocol
{
  void slaveWrite(unsigned bit[ADDR_WIDTH-1:0] a, unsigned bit[DATA_WIDTH-1:0] d)
  {
    do {
      t1: while(!ready) wait(ready);
      t2: if(a != A) {
            waitfor(1000); // avoid hanging from t2 to t1
            goto t1;
          }
          else {
            D = d;
            waitfor(12000);
          }
      t3: ack = 1;
          while(ready) wait(ready);
      t4: waitfor(7000);
      t5: ack = 0;
    }
    timing {
      range(t2; t3; 10000; 20000);
      range(t4; t5; 5000; 15000);
    }
  }

  void slaveRead (unsigned bit[ADDR_WIDTH-1:0] a, unsigned bit[DATA_WIDTH-1:0] *d)
  {
    do {
      t1: while(!ready) wait(ready);
      t2: if(a != A) {
            waitfor(1000);  // avoid hanging from t2 to t1
	    goto t1;
	  }
          else {
            *d = D;
            waitfor(12000);
          }
      t3: ack = 1;
          while(ready) wait(ready);
      t4: waitfor(7000);
      t5: ack = 0;
    }
    timing {
      range(t2; t3; 10000; 20000);
      range(t4; t5; 5000; 15000);
    }
  }
};

/* -----  Physical layer, interrupt handling ----- */

/*
 * @intro: Add OS scheduling interface
 */
interface os_receive
{
  void receive(int threadID, OS_API_TOP os_port);
};

interface i_sync
{
  void send(void);
  void finish(void);
};

channel MasterHardwareSyncDetect(in signal unsigned bit[1] intr)
  implements os_receive		// modified by TT
{
  void receive(int threadID, OS_API_TOP os_port)
  {
    if(intr == 0)
    {
      os_port.pre_wait(threadID);
      wait(rising intr);
      os_port.post_wait(threadID);
    }
  }
};

channel SlaveHardwareSyncGenerate(out signal unsigned bit[1] intr)
  implements i_sync
{
  void send(void)
  {
    intr = 1;
  }

  void finish(void)
  {
    intr = 0;
  }
};


/* -----  Media access layer ----- */

interface IMasterHardwareBusLinkAccess
{
  void MasterRead(int addr, void *data, unsigned long len);
  void MasterWrite(int addr, const void* data, unsigned long len);
};
  
interface ISlaveHardwareBusLinkAccess
{
  void SlaveRead(int addr, void *data, unsigned long len);
  void SlaveWrite(int addr, const void* data, unsigned long len);
};

channel MasterHardwareBusLinkAccess(IMasterHardwareBusProtocol protocol)
  implements IMasterHardwareBusLinkAccess
{
  void MasterWrite(int addr, const void* data, unsigned long len)
  {    
    unsigned long i;
    unsigned char *p;
    unsigned bit[DATA_WIDTH-1:0] word = 0;
   
    for(p = (unsigned char*)data, i = 0; i < len; i++, p++)
    {
      word = (word<<8) + *p;
      
      if(!((i+1)%DATA_BYTES)) {
	protocol.masterWrite(addr, word);
	word = 0;
      }
    }
    
    if(i%DATA_BYTES) {
      word <<= 8 * (DATA_BYTES - (i%DATA_BYTES));
      protocol.masterWrite(addr, word);
    }    
  }
  
  void MasterRead(int addr, void* data, unsigned long len)
  {
    unsigned long i;
    unsigned char* p;
    unsigned bit[DATA_WIDTH-1:0] word;
   
    for(p = (unsigned char*)data, i = 0; i < len; i++, p++)
    {
      if(!(i%DATA_BYTES)) {
	protocol.masterRead(addr, &word);
      }

      *p = word[DATA_WIDTH-1:DATA_WIDTH-8];
      word = word << 8;      
    }
//printf("master finished mac reading\n");
  }
};

channel SlaveHardwareBusLinkAccess(ISlaveHardwareBusProtocol protocol)
  implements ISlaveHardwareBusLinkAccess
{
  void SlaveWrite(int addr, const void* data, unsigned long len)
  {    
    unsigned long i;
    unsigned char *p;
    unsigned bit[DATA_WIDTH-1:0] word = 0;
   
    for(p = (unsigned char*)data, i = 0; i < len; i++, p++)
    {
      word = (word<<8) + *p;
      
      if(!((i+1)%DATA_BYTES)) {
	protocol.slaveWrite(addr, word);
	word = 0;
      }
    }
    
    if(i%DATA_BYTES) {
      word <<= 8 * (DATA_BYTES - (i%DATA_BYTES));
      protocol.slaveWrite(addr, word);
    }    
  }
  
  void SlaveRead(int addr, void* data, unsigned long len)
  {
    unsigned long i;
    unsigned char* p;
    unsigned bit[DATA_WIDTH-1:0] word;
   
    for(p = (unsigned char*)data, i = 0; i < len; i++, p++)
    {
      if(!(i%DATA_BYTES)) {
	protocol.slaveRead(addr, &word);
      }

      *p = word[DATA_WIDTH-1:DATA_WIDTH-8];
      word = word << 8;      
    }
  }
};

/*
 * @intro: Interface for driver half channel
 */
interface dr_sender
{
  void send(int addr, const void *d, unsigned long l);
};

interface dr_receiver
{
  void receive(int addr, void *d, unsigned long l);
};

interface os_dr_sender
{
  void send(int addr, const void *d, unsigned long l, int threadID, OS_API_TOP os_port);
};

interface os_dr_receiver
{
  void receive(int addr, void *d, unsigned long l, int threadID, OS_API_TOP os_port);
};

/*
 * @intro: Driver half channel on master side
 */
channel MasterHardwareDriver(IMasterHardwareBusLinkAccess mac, os_receive intr) implements os_dr_sender, os_dr_receiver
{
  void receive(int addr, void *d, unsigned long l, int threadID, OS_API_TOP os_port)
  {
    intr.receive(threadID, os_port);
    mac.MasterRead(addr, d, l);
//printf("master finished entire reading\n");
  }

  void send(int addr, const void *d, unsigned long l, int threadID, OS_API_TOP os_port)
  {
    intr.receive(threadID, os_port);
    mac.MasterWrite(addr, d, l);
  }
};

/*
 * @intro: Driver half channel on the slave side
 */
channel SlaveHardwareDriver(ISlaveHardwareBusLinkAccess mac, i_sync intr) implements dr_sender, dr_receiver
{
  void receive(int addr, void *d, unsigned long l)
  {
    intr.send();
    mac.SlaveRead(addr, d, l);
    intr.finish();
  }

  void send(int addr, const void *d, unsigned long l)
  {
    intr.send();
    mac.SlaveWrite(addr, d, l);
    intr.finish();
  }
};

/* -----  Bus instantiation example ----- */

// Bus protocol interfaces
interface IMasterHardwareBus
{
  void MasterRead(unsigned bit[ADDR_WIDTH-1:0] addr, void *data, unsigned long len);
  void MasterWrite(unsigned bit[ADDR_WIDTH-1:0] addr, const void* data, unsigned long len);
  
  void MasterSyncReceive();
};
  
interface ISlaveHardwareBus
{
  void SlaveRead(unsigned bit[ADDR_WIDTH-1:0] addr, void *data, unsigned long len);
  void SlaveWrite(unsigned bit[ADDR_WIDTH-1:0] addr, const void* data, unsigned long len);
  
  void SlaveSyncSend();
};


// Bus protocol channel
channel HardwareBus()
  implements IMasterHardwareBus, ISlaveHardwareBus
{
  // wires
  signal unsigned bit[ADDR_WIDTH-1:0] A;
  signal unsigned bit[DATA_WIDTH-1:0] D;
  signal unsigned bit[1]    ready = 0;
  signal unsigned bit[1]    ack = 0;

  // interrupts
  signal unsigned bit[1]    int0 = 0;
  signal unsigned bit[1]    int1 = 0;

  MasterHardwareSyncDetect  MasterSync0(int0);
  SlaveHardwareSyncGenerate SlaveSync0(int0);

  MasterHardwareSyncDetect  MasterSync1(int1);
  SlaveHardwareSyncGenerate SlaveSync1(int1);
  
  MasterHardwareBus Master(A, D, ready, ack);
  SlaveHardwareBus  Slave(A, D, ready, ack);

  MasterHardwareBusLinkAccess MasterLink(Master);
  SlaveHardwareBusLinkAccess SlaveLink(Slave);

  
  void MasterRead(unsigned bit[ADDR_WIDTH-1:0] addr, void *data, unsigned long len) {
    MasterLink.MasterRead(addr, data, len);
  }
  
  void MasterWrite(unsigned bit[ADDR_WIDTH-1:0] addr, const void *data, unsigned long len) {
    MasterLink.MasterWrite(addr, data, len);
  }
  
  void SlaveRead(unsigned bit[ADDR_WIDTH-1:0] addr, void *data, unsigned long len) {
    SlaveLink.SlaveRead(addr, data, len);
  }
  
  void SlaveWrite(unsigned bit[ADDR_WIDTH-1:0] addr, const void *data, unsigned long len) {
    SlaveLink.SlaveWrite(addr, data, len);
  }

  void MasterSyncReceive() {
    //MasterSync0.receive();
  }
  
  void SlaveSyncSend() {
    SlaveSync0.send();
  }
};
