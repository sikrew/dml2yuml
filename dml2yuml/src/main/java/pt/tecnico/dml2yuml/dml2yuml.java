package pt.tecnico.dml2yuml;

/* Convert .dml file into yuml (yuml.me) format
 *
 * compile: antlr4 dml.g4; javac -cp antlr-4.5-complete.jar:. *.java
 * run: java -cp antlr-4.5-complete.jar:. dml2yuml ex.dml
 * reis.santos(at)tecnico.ulisboa.pt (C)21oct2015
 */

import java.io.*;
import java.util.*;
import org.antlr.v4.runtime.*;

public class dml2yuml {
    private static boolean multiplicity = true;
    private static boolean role = true;
    private static boolean atrib = true;
    private static PrintWriter out;
    private static final String USAGE = "USAGE: dml2yuml [-a] [-m] [-r] file.dml [out.yuml]";

    public static void main(String[] args) throws Exception {
	int argc = 0;
	if (args.length == 0) {
	    System.err.println(USAGE + "\n"
	    	+ "\t-a: omit attributes in classes\n"
	    	+ "\t-m: omit multiplicity in associations\n"
	    	+ "\t-r: omit role names in associations");
	    return;
	}
	if (args.length > argc && args[argc].equals("-a")) { atrib = false; argc++; }
	if (args.length > argc && args[argc].equals("-m")) { multiplicity = false; argc++; }
	if (args.length > argc && args[argc].equals("-r")) { role = false; argc++; }
	if (args.length <= argc) {
	    System.err.println(USAGE + "\n\tfile.dml: missing.");
	    return;
	}
	if (args.length > argc+1)
    	    out = new PrintWriter(args[argc+1], "UTF-8");
	else
	    out = new PrintWriter(System.out);
	InputStream in = new FileInputStream(args[argc]);
	execute(in);
    }
    public static void execute(InputStream in) throws Exception {
        dmlLexer l = new dmlLexer(new ANTLRInputStream(in));
        dmlParser p = new dmlParser(new CommonTokenStream(l));
        p.addErrorListener(new BaseErrorListener() {
            @Override
            public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
                throw new IllegalStateException("failed to parse at line " + line + " due to " + msg, e);
            }
        });

        p.addParseListener(new dmlBaseListener() {

            @Override
            public void exitClassDefinition(dmlParser.ClassDefinitionContext ctx) {
		String cl = ctx.entityTypeId().getText();

		if (ctx.superClassClause().getText().length() > 0)
		    out.println("["+ctx.superClassClause().entityTypeId().getText()+"]^-["+cl+"]");

                out.print("[" + cl);
		List<dmlParser.ClassSlotContext> slots = ctx.classBlock().classSlot();
		if (atrib) {
		  if (slots.size() > 0) out.print("|");
		  for (dmlParser.ClassSlotContext c: slots)
		      out.print(" " + c.typeSpec().identifier().getText()
			  + " " + c.classSlotInternal().ID().getText() + ";");
		}
                out.println("]");

                // System.err.println("RULE: "+ctx.getText());
            }

            @Override
            public void exitRelationDefinition(dmlParser.RelationDefinitionContext ctx) {
		dmlParser.EntityTypeIdContext
			e0 = ctx.rolesAndSlots().entityTypeId(0),
			e1 = ctx.rolesAndSlots().entityTypeId(1);
		dmlParser.RoleContext
			r0 = ctx.rolesAndSlots().role(0),
			r1 = ctx.rolesAndSlots().role(1);
		String rel, n, n0 = "", n1 = "", c0, c1, m0 = "", m1 = "";

                n = ctx.identifier().getText();
		if (r0.roleName().ID() != null)
		  n0 = r0.roleName().ID().getText();
		if (multiplicity && r0.roleOptions().roleOption(0) != null)
		m0 = r0.roleOptions().roleOption(0).multiplicityRange().getText();
		c0 = e0.getText();
		if (!role) n0 = "";
		rel = "[" + c0 + "]" + m0 + " " + n0 + " - ";

		if (r1 != null) {
		  if (r1.roleName().ID() != null)
		    n1 = r1.roleName().ID().getText();
		  if (multiplicity && r1.roleOptions().roleOption(0) != null)
		  m1 = r1.roleOptions().roleOption(0).multiplicityRange().getText();
		  c1 = e1.getText();
		  if (!role) n1 = "";
		  rel += n1 + " " + m1 + "[" + c1 + "] // " + n;
		} else rel = null; // must have two classes!!!
                if (rel != null) out.println(rel);
                // System.err.println("LIST: "+ctx.getText());
            }
        });
        p.dml();
	out.close();
    }
    public static boolean printMultiplicity() { return multiplicity; }
    public static boolean printRole() { return role; }
    public static boolean printAttributes() { return atrib; }
    public static void omitMultiplicity() { multiplicity = false; }
    public static void omitRole() { role = false; }
    public static void omitAttributes() { atrib = false; }
    public static void writer(PrintWriter wrt) { out = wrt; }
}
