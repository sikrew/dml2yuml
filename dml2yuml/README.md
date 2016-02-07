
  Convert .dml files into yuml (yuml.me) files.

  compile: mvn install

  run:  mvn exec:java -Dexec.args=src/test/resources/phoneBook.dml

  autonomous run:
    java -cp antlr-runtime-4.5.1.jar:target/dml2yuml-1.0-SNAPSHOT.jar pt.tecnico.dml2yuml.dml2yuml src/test/resources/phoneBook.dml
    java -jar target/dml2yuml-1.0-SNAPSHOT-jar-with-dependencies.jar src/test/resources/phoneBook.dml

  (C) reis.santos(at)tecnico.ulisboa.pt 21oct2015
