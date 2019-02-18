/**
 * Author: Jake Wachs
 * CS 403, Programming Languages
 * The University of Alabama
 *
 * Evaluator class
 */


package eval;

import lex.*;
import parse.*;
import env.*;


public class Evaluator implements Types
{
	/**
	 * Default constructor
	 */
	public Evaluator()
	{
	}

/***** Private Methods *****/
	/**
	 * Construct method for binding together Lexemes
	 * @type The type of the new Lexeme
	 * @l The pointer to the left of the Lexeme
	 * @r The pointer to the right of the Lexeme
	 */
	private Lexeme cons(String type, Lexeme l, Lexeme r)
	{
		Lexeme ret = new Lexeme(type);
		ret.setCar(l);
		ret.setCdr(r);

		return ret;
	}

	/**
	 * Gets the value of the ID at tree root from the environment
	 * @tree Lexeme for which to search environment
	 * @env Environment for which to search for Lexeme id
	 * @return The value of the id at tree
	 */
	private Lexeme lookup(Lexeme tree, Environment env)
	{
		return env.getVal(tree);
	}

	/**
	 * Adds function variables to its scope
	 * @tree Root of FUNCDEF tree to use for environment insertion
	 * @env The environment into which the newly created tree will be inserted
	 */
	private void evalFuncDef(Lexeme tree, Environment env)
	{
		env.insertEnv(tree.getCar(), cons(CLOSURE, env, tree));
	}

	/**
	 * Main evaluator method
	 * @tree Root of the tree to be evaluated
	 * @env Corresponding environment to tree
	 * @return root of another Lexeme tree
	 */
	private Lexeme eval(Lexeme tree, Environment env)
	{
		switch (tree.getType())
		{
			case INTEGER:
				return tree;
			case STRING:
				return tree;
			case REAL:
				return tree;
			case ID:
				return lookup(tree, env);
			case FUNCDEF:
				return evalFuncDef(tree, env):
		}
	}


/***** Public Methods *****/
	public static void main(String[] args) throws IOException
	{
		checkCmdArgs(args);

		File file = openFile(args[0]);
		PushbackInputStream stream = new PushbackInputStream(new FileInputStream(file));

		Lexer i = new Lexer(stream);
		Lexeme curr = i.lex();

		Parser p = new Parser(curr, stream);


		Environment env = new Environment();
		Lexeme tree = p.program();
		eval(tree, env);


		stream.close();
	}
}


