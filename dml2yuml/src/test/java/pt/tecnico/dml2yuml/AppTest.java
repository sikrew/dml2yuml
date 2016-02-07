package pt.tecnico.dml2yuml;

import junit.framework.*; // Test, TestCase, TestSuite
import java.io.*;
import java.nio.file.*; // Paths & Files

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Test expected output
     */
    public void testApp() throws Exception
    {
	// input resource .dml file
	ClassLoader loader = getClass().getClassLoader();
	File file = new File(loader.getResource("phoneBook.dml").getFile());

	// output to a String
	StringWriter stringWriter = new StringWriter();
	PrintWriter writer = new PrintWriter(stringWriter);

	// generate output
	dml2yuml.writer(writer);
	dml2yuml.execute(new FileInputStream(file));

	// read expected output from resource .yuml file
	file = new File(loader.getResource("phoneBook.yuml").getFile());
	String dml = new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())));

        assertEquals( dml, stringWriter.toString() );
    }
}
