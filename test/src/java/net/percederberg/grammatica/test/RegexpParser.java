/*
 * RegexpParser.java
 * 
 * THIS FILE HAS BEEN GENERATED AUTOMATICALLY. DO NOT EDIT!
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

package net.percederberg.grammatica.test;

import java.io.Reader;

import net.percederberg.grammatica.parser.Analyzer;
import net.percederberg.grammatica.parser.ParserCreationException;
import net.percederberg.grammatica.parser.ProductionPattern;
import net.percederberg.grammatica.parser.ProductionPatternAlternative;
import net.percederberg.grammatica.parser.RecursiveDescentParser;

/**
 * A token stream parser.
 * 
 * @author   Per Cederberg, <per at percederberg dot net>
 * @version  1.0
 */
class RegexpParser extends RecursiveDescentParser {

    /**
     * A generated production node identity constant.
     */
    private static final int SUBPRODUCTION_1 = 3001;

    /**
     * A generated production node identity constant.
     */
    private static final int SUBPRODUCTION_2 = 3002;

    /**
     * Creates a new parser.
     * 
     * @param in             the input stream to read from
     * 
     * @throws ParserCreationException if the parser couldn't be
     *             initialized correctly
     */
    public RegexpParser(Reader in) throws ParserCreationException {
        super(new RegexpTokenizer(in));
        createPatterns();
    }

    /**
     * Creates a new parser.
     * 
     * @param in             the input stream to read from
     * @param analyzer       the analyzer to use while parsing
     * 
     * @throws ParserCreationException if the parser couldn't be
     *             initialized correctly
     */
    public RegexpParser(Reader in, Analyzer analyzer)
        throws ParserCreationException {

        super(new RegexpTokenizer(in), analyzer);
        createPatterns();
    }

    /**
     * Initializes the parser by creating all the production patterns.
     * 
     * @throws ParserCreationException if the parser couldn't be
     *             initialized correctly
     */
    private void createPatterns() throws ParserCreationException {
        ProductionPattern             pattern;
        ProductionPatternAlternative  alt;

        pattern = new ProductionPattern(RegexpConstants.EXPR,
                                        "Expr");
        alt = new ProductionPatternAlternative();
        alt.addProduction(RegexpConstants.TERM, 1, 1);
        alt.addProduction(SUBPRODUCTION_1, 0, 1);
        pattern.addAlternative(alt);
        addPattern(pattern);

        pattern = new ProductionPattern(RegexpConstants.TERM,
                                        "Term");
        alt = new ProductionPatternAlternative();
        alt.addProduction(RegexpConstants.FACT, 1, -1);
        pattern.addAlternative(alt);
        addPattern(pattern);

        pattern = new ProductionPattern(RegexpConstants.FACT,
                                        "Fact");
        alt = new ProductionPatternAlternative();
        alt.addProduction(RegexpConstants.ATOM, 1, 1);
        alt.addProduction(RegexpConstants.ATOM_MODIFIER, 0, 1);
        pattern.addAlternative(alt);
        addPattern(pattern);

        pattern = new ProductionPattern(RegexpConstants.ATOM,
                                        "Atom");
        alt = new ProductionPatternAlternative();
        alt.addToken(RegexpConstants.CHAR, 1, 1);
        pattern.addAlternative(alt);
        alt = new ProductionPatternAlternative();
        alt.addToken(RegexpConstants.NUMBER, 1, 1);
        pattern.addAlternative(alt);
        alt = new ProductionPatternAlternative();
        alt.addToken(RegexpConstants.COMMA, 1, 1);
        pattern.addAlternative(alt);
        alt = new ProductionPatternAlternative();
        alt.addToken(RegexpConstants.DOT, 1, 1);
        pattern.addAlternative(alt);
        alt = new ProductionPatternAlternative();
        alt.addToken(RegexpConstants.LEFT_PAREN, 1, 1);
        alt.addProduction(RegexpConstants.EXPR, 1, 1);
        alt.addToken(RegexpConstants.RIGHT_PAREN, 1, 1);
        pattern.addAlternative(alt);
        alt = new ProductionPatternAlternative();
        alt.addToken(RegexpConstants.LEFT_BRACKET, 1, 1);
        alt.addProduction(RegexpConstants.CHARACTER_SET, 1, 1);
        alt.addToken(RegexpConstants.RIGHT_BRACKET, 1, 1);
        pattern.addAlternative(alt);
        addPattern(pattern);

        pattern = new ProductionPattern(RegexpConstants.ATOM_MODIFIER,
                                        "AtomModifier");
        alt = new ProductionPatternAlternative();
        alt.addToken(RegexpConstants.QUESTION, 1, 1);
        alt.addToken(RegexpConstants.QUESTION, 0, 1);
        pattern.addAlternative(alt);
        alt = new ProductionPatternAlternative();
        alt.addToken(RegexpConstants.ASTERISK, 1, 1);
        alt.addToken(RegexpConstants.QUESTION, 0, 1);
        pattern.addAlternative(alt);
        alt = new ProductionPatternAlternative();
        alt.addToken(RegexpConstants.PLUS, 1, 1);
        alt.addToken(RegexpConstants.QUESTION, 0, 1);
        pattern.addAlternative(alt);
        alt = new ProductionPatternAlternative();
        alt.addToken(RegexpConstants.LEFT_BRACE, 1, 1);
        alt.addToken(RegexpConstants.NUMBER, 1, 1);
        alt.addProduction(SUBPRODUCTION_2, 0, 1);
        alt.addToken(RegexpConstants.RIGHT_BRACE, 1, 1);
        alt.addToken(RegexpConstants.QUESTION, 0, 1);
        pattern.addAlternative(alt);
        addPattern(pattern);

        pattern = new ProductionPattern(RegexpConstants.CHARACTER_SET,
                                        "CharacterSet");
        alt = new ProductionPatternAlternative();
        alt.addProduction(RegexpConstants.CHARACTER, 1, -1);
        pattern.addAlternative(alt);
        addPattern(pattern);

        pattern = new ProductionPattern(RegexpConstants.CHARACTER,
                                        "Character");
        alt = new ProductionPatternAlternative();
        alt.addToken(RegexpConstants.CHAR, 1, 1);
        pattern.addAlternative(alt);
        alt = new ProductionPatternAlternative();
        alt.addToken(RegexpConstants.NUMBER, 1, 1);
        pattern.addAlternative(alt);
        alt = new ProductionPatternAlternative();
        alt.addToken(RegexpConstants.COMMA, 1, 1);
        pattern.addAlternative(alt);
        alt = new ProductionPatternAlternative();
        alt.addToken(RegexpConstants.DOT, 1, 1);
        pattern.addAlternative(alt);
        alt = new ProductionPatternAlternative();
        alt.addToken(RegexpConstants.VERTICAL_BAR, 1, 1);
        pattern.addAlternative(alt);
        alt = new ProductionPatternAlternative();
        alt.addToken(RegexpConstants.PLUS, 1, 1);
        pattern.addAlternative(alt);
        alt = new ProductionPatternAlternative();
        alt.addToken(RegexpConstants.ASTERISK, 1, 1);
        pattern.addAlternative(alt);
        alt = new ProductionPatternAlternative();
        alt.addToken(RegexpConstants.QUESTION, 1, 1);
        pattern.addAlternative(alt);
        alt = new ProductionPatternAlternative();
        alt.addToken(RegexpConstants.LEFT_BRACE, 1, 1);
        pattern.addAlternative(alt);
        alt = new ProductionPatternAlternative();
        alt.addToken(RegexpConstants.RIGHT_BRACE, 1, 1);
        pattern.addAlternative(alt);
        alt = new ProductionPatternAlternative();
        alt.addToken(RegexpConstants.LEFT_PAREN, 1, 1);
        pattern.addAlternative(alt);
        alt = new ProductionPatternAlternative();
        alt.addToken(RegexpConstants.RIGHT_PAREN, 1, 1);
        pattern.addAlternative(alt);
        alt = new ProductionPatternAlternative();
        alt.addToken(RegexpConstants.LEFT_BRACKET, 1, 1);
        pattern.addAlternative(alt);
        addPattern(pattern);

        pattern = new ProductionPattern(SUBPRODUCTION_1,
                                        "Subproduction1");
        pattern.setSyntetic(true);
        alt = new ProductionPatternAlternative();
        alt.addToken(RegexpConstants.VERTICAL_BAR, 1, 1);
        alt.addProduction(RegexpConstants.EXPR, 1, 1);
        pattern.addAlternative(alt);
        addPattern(pattern);

        pattern = new ProductionPattern(SUBPRODUCTION_2,
                                        "Subproduction2");
        pattern.setSyntetic(true);
        alt = new ProductionPatternAlternative();
        alt.addToken(RegexpConstants.COMMA, 1, 1);
        alt.addToken(RegexpConstants.NUMBER, 0, 1);
        pattern.addAlternative(alt);
        addPattern(pattern);
    }
}