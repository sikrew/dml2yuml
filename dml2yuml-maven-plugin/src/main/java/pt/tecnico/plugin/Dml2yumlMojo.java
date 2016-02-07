package pt.tecnico.plugin;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;

import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;

import pt.tecnico.dml2yuml.dml2yuml;

/**
 *
 */
@Mojo(	name = "dml2yuml",
	defaultPhase = LifecyclePhase.PROCESS_SOURCES,
	requiresProject = true)
public class Dml2yumlMojo extends AbstractMojo {

    /**
     * The directory where the .dml files ({@code *.dml}) are located.
     */
    @Parameter(defaultValue = "${basedir}/src/main/dml")
    private File sourceDirectory;

    /**
     * Specify output directory where the .yuml files are generated.
     */
    @Parameter(defaultValue = "${project.build.directory}/")
    private File outputDirectory;

    /**
     * Print multiplicity in associations.
     */
    @Parameter(property = "dml2yuml.multiplicity", defaultValue = "true")
    protected boolean multiplicity;

    /**
     * Print role names in associations.
     */
    @Parameter(property = "dml2yuml.role", defaultValue = "true")
    protected boolean role;

    /**
     * Print commented attributes in classes.
     */
    @Parameter(property = "dml2yuml.attributes", defaultValue = "true")
    protected boolean attributes;


    public void execute() throws MojoExecutionException
    {
      try {
	if (!multiplicity) dml2yuml.omitMultiplicity();
	if (!role) dml2yuml.omitRole();
	if (!attributes) dml2yuml.omitAttributes();
	getLog().info( " dml2yuml:" );
	for (File f: sourceDirectory.listFiles()) {
	  String name = f.getName();
	  getLog().info( "    " + name );
	  if (name.endsWith(".dml")) {
	    String out = name.substring(0, name.length() - 4) + ".yuml";
	    /*
	    new PrintWriter(new File(outputDirectory, out), "UTF-8");
	    new FileInputStream(f);
	    */
	    dml2yuml.writer(new PrintWriter(new File(outputDirectory, out), "UTF-8"));
	    dml2yuml.execute(new FileInputStream(f));
	  }
	}
      } catch (Exception e) { getLog().warn(e); } // getLog().error(e);
    }
}
