
import "i_send";
import "os_receive";
import "full_rtos";


channel os_double_handshake_signal implements i_send, os_receive
{

    event         req,
                  ack;
    bool          v = false,
                  w = false;

    void receive(int threadID, OS_API_TOP os_port)
    {
        if (!v)
        {
            w = true;
	    os_port.pre_wait(threadID);
            wait req;
	    os_port.post_wait(threadID);
            w = false;
        }
        v = false;
        notify ack;
        wait ack;
    }

    void send(void)
    {
        v = true;
        if (w)
        {
            notify req;
        }
        wait ack;
    }
};



