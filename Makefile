build : Sauron.java Matrix.java FileParser.java
		javac *.java

run-p1: *.class
		java Sauron
run-p2: *.class
		java Matrix
clean:
		rm *.class
