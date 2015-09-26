Change log:
-------------------------------------------------------------------------------------
9/24/15 | AJG | * Image will be sent x amount of times --> exit(0) moved from
        |     |   monitor to stimulus.
        |     | * Waitfor(1000) added before for() loop in stimulus which sends
        |     |   image, waitfor(50) added after each image sent
        |     | * Time checked when: a) start signal sent in stimulus
        |     |                      b) last byte of image received in monitor
-------------------------------------------------------------------------------------
9/25/15 | AJG | * Created read_image and write_image behaviors
        |     | * Edited susan to remove waiting for start signal-> now in read_image
        |     | * Created design block to connect susan,read_image,write_image
        |     | * Channels in top level connecting to design replaced with dh
-------------------------------------------------------------------------------------



TODO
-------------------------------------------------------------------------------------
1) Print total time to send image (start signal to last byte received) in monitor
2) Figure out how to get read/write_image to compile
