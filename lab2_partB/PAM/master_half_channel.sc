import "HWBus";

/*
 * @intro: a half channel on master side, includes all sub-channels
 * @type: PAM
 */
channel ch_half_master(out signal unsigned bit[ADDR_WIDTH-1:0] A,
			   signal unsigned bit[DATA_WIDTH-1:0] D,
		       out signal unsigned bit[1]    ready,
		       in  signal unsigned bit[1]    ack,
		       in  signal unsigned bit[1]    intr_read,
		       in  signal unsigned bit[1]    intr_write)
im
