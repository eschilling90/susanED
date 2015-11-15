# For final project
One-layer neural network written in C. It should be able to convert into SpecC easily. It is also possible to be used for rectangle detection.

# NOW
All are converted to SpecC, but cannot compile

# Some limits:
	1. Numbers(rectangles) need to be roughly the same size.
	2. Numbers(rectangles) need to be roughly at the same position.
	3. Probably background-noise-tolerant.

# mnist-1lnn
A simple 1-layer neural network to recognize handwritten single digit numbers from the MNIST image files.


### Compile and run source code

The repository comes with a pre-configured `makefile`. You can compile the source simply by typing

```
$ make
```

in the project directory. The binary will be created inside the `/bin` folder and can be executed via

```
$ ./bin/mnist-1lnn
```

### Documentation

The  `/doc` folder contains a doxygen configuration file. 
When you run it with doxygen it will create updated [HTML documentation](https://rawgit.com/mmlind/mnist-1lnn/master/doc/html/index.html) in the `/doc/html` folder.

### Screenshots

The `/screenshots` folder contains screenshots of running the program on my 2010 MacBook Pro.


### MNIST Database

The `/data` folder contains the original MNIST database files.

