/*
 * CSharpAnalyzerFile.java
 *
 * This work is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation; either version 2 of the License,
 * or (at your option) any later version.
 *
 * This work is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
 * USA
 *
 * As a special exception, the copyright holders of this library give
 * you permission to link this library with independent modules to
 * produce an executable, regardless of the license terms of these
 * independent modules, and to copy and distribute the resulting
 * executable under terms of your choice, provided that you also meet,
 * for each linked independent module, the terms and conditions of the
 * license of that module. An independent module is a module which is
 * not derived from or based on this library. If you modify this
 * library, you may extend this exception to your version of the
 * library, but you are not obligated to do so. If you do not wish to
 * do so, delete this exception statement from your version.
 *
 * Copyright (c) 2003 Per Cederberg. All rights reserved.
 */

package net.percederberg.grammatica.output;

import java.io.IOException;

import net.percederberg.grammatica.code.csharp.CSharpClass;
import net.percederberg.grammatica.code.csharp.CSharpComment;
import net.percederberg.grammatica.code.csharp.CSharpFile;
import net.percederberg.grammatica.code.csharp.CSharpMethod;
import net.percederberg.grammatica.code.csharp.CSharpNamespace;
import net.percederberg.grammatica.code.csharp.CSharpUsing;
import net.percederberg.grammatica.parser.ProductionPattern;
import net.percederberg.grammatica.parser.TokenPattern;

/**
 * The C# analyzer file generator. This class encapsulates all the
 * C# code necessary for creating a analyzer class file.
 *
 * @author   Per Cederberg, <per at percederberg dot net>
 * @version  1.0
 */
class CSharpAnalyzerFile {

    /**
     * The class comment.
     */
    private static final String TYPE_COMMENT =
        "<remarks>A class providing callback methods for the\n" +
        "parser.</remarks>";

    /**
     * The enter method comment.
     */
    private static final String ENTER_COMMENT =
        "<summary>Called when entering a parse tree node.</summary>\n\n" +
        "<param name='node'>the node being entered</param>\n\n" +
        "<exception cref='ParseException'>if the node analysis\n" +
        "discovered errors</exception>";

    /**
     * The exit method comment.
     */
    private static final String EXIT_COMMENT =
        "<summary>Called when exiting a parse tree node.</summary>\n\n" +
        "<param name='node'>the node being exited</param>\n\n" +
        "<returns>the node to add to the parse tree</returns>\n\n" +
        "<exception cref='ParseException'>if the node analysis\n" +
        "discovered errors</exception>";
   
    /**
     * The child method comment.
     */
    private static final String CHILD_COMMENT =
        "<summary>Called when adding a child to a parse tree\n" +
        "node.</summary>\n\n" +
        "<param name='node'>the parent node</param>\n" +
        "<param name='child'>the child node, or null</param>\n\n" +
        "<exception cref='ParseException'>if the node analysis\n" +
        "discovered errors</exception>";

    /**
     * The parser generator.
     */
    private CSharpParserGenerator gen;

    /**
     * The file to write.
     */
    private CSharpFile file;

    /**
     * The class.
     */
    private CSharpClass cls;

    /**
     * The enter method.
     */
    private CSharpMethod enter;
    
    /**
     * The exit method.
     */
    private CSharpMethod exit;
   
    /**
     * The child method.
     */
    private CSharpMethod child;

    /**
     * Creates a new analyzer file.
     * 
     * @param gen            the parser generator to use
     */
    public CSharpAnalyzerFile(CSharpParserGenerator gen) {
        String  name = gen.getBaseName() + "Analyzer";
        int     modifiers;

        this.gen = gen;
        this.file = new CSharpFile(gen.getBaseDir(), name);
        if (gen.getPublicAccess()) {
            modifiers = CSharpClass.PUBLIC + CSharpClass.ABSTRACT;
        } else {
            modifiers = CSharpClass.INTERNAL + CSharpClass.ABSTRACT;
        }
        this.cls = new CSharpClass(modifiers, name, "Analyzer");
        this.enter = new CSharpMethod(CSharpMethod.PUBLIC,
                                      "enter",
                                      "Node node",
                                      "void");
        this.exit = new CSharpMethod(CSharpMethod.PUBLIC,
                                     "exit",
                                     "Node node",
                                     "Node");
        this.child = new CSharpMethod(CSharpMethod.PUBLIC,
                                      "child",
                                      "Production node, Node child",
                                      "void");
        initializeCode();
    }
    
    /**
     * Initializes the source code objects.
     */    
    private void initializeCode() {
        String  str;

        // Add using
        file.addUsing(new CSharpUsing("PerCederberg.Grammatica.Parser"));

        // Add namespace
        if (gen.getNamespace() == null) {
            file.addClass(cls);
        } else {
            CSharpNamespace n = new CSharpNamespace(gen.getNamespace());
            n.addClass(cls);
            file.addNamespace(n);
        }

        // Add file comment
        str = file.toString() + "\n\n" + gen.getFileComment();
        file.addComment(new CSharpComment(CSharpComment.BLOCK, str));

        // Add type comment
        cls.addComment(new CSharpComment(TYPE_COMMENT));
        
        // Add enter method
        enter.addComment(new CSharpComment(ENTER_COMMENT));
        enter.addCode("switch (node.GetId()) {");
        cls.addMethod(enter);
        
        // Add exit method
        exit.addComment(new CSharpComment(EXIT_COMMENT));
        exit.addCode("switch (node.GetId()) {");
        cls.addMethod(exit);

        // Add child method
        child.addComment(new CSharpComment(CHILD_COMMENT));
        child.addCode("switch (node.GetId()) {");
        cls.addMethod(child);
    }

    /**
     * Adds the token analysis methods to this file.
     * 
     * @param pattern        the token pattern
     * @param constants      the constants file              
     */
    public void addToken(TokenPattern pattern, 
                         CSharpConstantsFile constants) {
        
        String  constant = constants.getConstant(pattern.getId());
        String  name;  

        if (!pattern.isIgnore()) {
            name = gen.getCodeStyle().getMixedCase(pattern.getName(), true);
            addEnterCase(constant, name, "Token");
            addEnterMethod(name, "Token");
            addExitCase(constant, name, "Token");
            addExitMethod(name, "Token");
        }
    }

    /**
     * Adds the production analysis methods to this file.
     * 
     * @param pattern        the production pattern
     * @param constants      the constants file              
     */
    public void addProduction(ProductionPattern pattern,
                              CSharpConstantsFile constants) {

        String   constant = constants.getConstant(pattern.getId());
        String   name;  
        Integer  id = new Integer(pattern.getId());

        if (!pattern.isSyntetic()) {
            name = gen.getCodeStyle().getMixedCase(pattern.getName(), 
                                                   true);
            addEnterCase(constant, name, "Production");
            addEnterMethod(name, "Production");
            addExitCase(constant, name, "Production");
            addExitMethod(name, "Production");
            addChildCase(constant, name);
            addChildMethod(name);
        }
    }

    /**
     * Adds an enter method switch case. 
     * 
     * @param constant       the node constant
     * @param name           the node name
     * @param type           the node type
     */
    private void addEnterCase(String constant, String name, String type) {
        enter.addCode("case (int) " + constant + ":");
        enter.addCode("    enter" + name + "((" + type + ") node);");
        enter.addCode("    break;");
    }

    /**
     * Adds an exit method switch case. 
     * 
     * @param constant       the node constant
     * @param name           the node name
     * @param type           the node type
     */
    private void addExitCase(String constant, String name, String type) {
        exit.addCode("case (int) " + constant + ":");
        exit.addCode("    return exit" + name + "((" + type + ") node);");
    }

    /**
     * Adds a child method switch case. 
     * 
     * @param constant       the node constant
     * @param name           the node name
     */
    private void addChildCase(String constant, String name) {
        child.addCode("case (int) " + constant + ":");
        child.addCode("    child" + name + "(node, child);");
        child.addCode("    break;");
    }

    /**
     * Adds an enter node method to this file.
     * 
     * @param name           the node name
     * @param type           the node type
     */
    private void addEnterMethod(String name, String type) {
        CSharpMethod  m;

        m = new CSharpMethod(CSharpMethod.PUBLIC,
                             "enter" + name,
                             type + " node",
                             "void");
        m.addComment(new CSharpComment(ENTER_COMMENT));
        cls.addMethod(m);
    }

    /**
     * Adds an exit node method to this file.
     * 
     * @param name           the node name
     * @param type           the node type
     */
    private void addExitMethod(String name, String type) {
        CSharpMethod  m;

        m = new CSharpMethod(CSharpMethod.PUBLIC,
                             "exit" + name,
                             type + " node",
                             "Node");
        m.addComment(new CSharpComment(EXIT_COMMENT));
        m.addCode("return node;");
        cls.addMethod(m);
    }

    /**
     * Adds an add child method to this file.
     * 
     * @param name           the node name
     */
    private void addChildMethod(String name) {
        CSharpMethod  m;

        m = new CSharpMethod(CSharpMethod.PUBLIC,
                             "child" + name,
                             "Production node, Node child",
                             "void");
        m.addComment(new CSharpComment(CHILD_COMMENT));
        m.addCode("node.AddChild(child);");
        cls.addMethod(m);
    }

    /**
     * Writes the file source code.
     * 
     * @throws IOException if the output file couldn't be created 
     *             correctly 
     */
    public void writeCode() throws IOException {
        enter.addCode("}");
        exit.addCode("}");
        exit.addCode("return node;");
        child.addCode("}");
        file.writeCode(gen.getCodeStyle());
    }
}