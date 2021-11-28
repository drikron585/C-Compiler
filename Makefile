JAVA=java
JAVAC=javac
CLASSPATH=-cp java-cup-11b.jar:.
CUP=$(JAVA) $(CLASSPATH) java_cup.Main
JFLEX=jflex
#CLASSPATH=-cp /usr/share/java/cup.jar:.
#CUP=cup

all: CM.class

CM.class: absyn/*.java parser.java sym.java Lexer.java ShowTreeVisitor.java Scanner.java SemanticAnalyzer.java TableSymb.java CM.java TMgen.java

%.class: %.java
	$(JAVAC) $(CLASSPATH) $^

Lexer.java: c-.flex
	$(JFLEX) c-.flex

parser.java: c-.cup
	#$(CUP) -dump -expect 3 c-.cup
	$(CUP) -expect 3 c-.cup
clean:
	rm -f parser.java Lexer.java sym.java *.class absyn/*.class *~
