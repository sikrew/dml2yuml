
  Convert .dml files into yuml (yuml.me) files.


  compile: antlr4 dml.g4; javac -cp antlr-4.5-complete.jar:. *.java
  run: java -cp antlr-4.5-complete.jar:. dml2yuml ex.dml

  assemble: jar -cfm dml2yuml.jar MANIFEST:MF *.class
  run: java -jar dml2yuml.jar ex.dml

  (C) reis.santos(at)tecnico.ulisboa.pt 21oct2015
