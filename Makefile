JAR_FILE= tds.jar
MANIFEST= manifest.mf

all: compile

compile:
	javac *.java -Xlint:deprecation

rm:
	rm *.class

jar:
	jar cvfm $(JAR_FILE) $(MANIFEST) *.class
