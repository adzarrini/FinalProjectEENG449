# Makefile variables, compiler, virtual machine, flags, and FILE variable for parameter
JC = javac
JVM = java
JFLAGS = -g
FILE = 

# Clear default targets for building90-
.SUFFIXES: .java .class

#(dependency suffix target suffix) rule for builidng
.java.class:
	$(JC) $(JFLAGS) $*.java

# classes for our java program
CLASSES = \
	FFT.java

# File containing the main method
MAIN = FFT

# default make target entry, our classes in this example
default: classes

# Target replacement within a macro to change .java to .class files
classes: $(CLASSES:.java=.class)

# target for running the program
run: $(MAIN).class
	$(JVM) $(MAIN)

# clean up extra .class files
clean:
	$(RM) *.class
