
  Convert .dml files into yuml (yuml.me) files.

  flat/ hand-compile source files and build .jar

  dml2yuml/ maven based tool

  dml2yuml-maven-plugin/ maven plugin to automate conversion

  example/ maven project with integrated automatic conversion in compilation

  Do 'mvn install' in dml2yuml to generate the tool, then do 'mvn install'
  in dml2yuml-maven-plugin/ to generate the plugin based in the tool.
  Then use the resulting plugin in a an example/ project with 'mvn compile'.

  (C) reis.santos(at)tecnico.ulisboa.pt 21oct2015
