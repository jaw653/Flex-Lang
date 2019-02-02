/**
 * Author: Jake Wachs
 * CS 403, Programming Languages
 * The University of Alabama
 *
 * Environment Class
 */

package env;

import lex.Lexeme;
import lex.Types;
import lex.Lexer;

import java.io.File;
import java.io.FileInputStream;
import java.io.PushbackInputStream;
import java.io.IOException;

public class Environment implements Types
{
    Lexeme env;

    /**
     * Default Constructor, serves as the
     *  newEnvironment() function from class
     */
    public Environment()
    {
        System.out.println("Creating a new environment");
        env = cons(ENV, cons(TABLE, null, null), null);
    }

/********** Private Methods **********/
    /**
     * Construct method for binding together Lexemes
     * @type The type of the new Lexeme
     * @l The pointer to the left of the Lexeme
     * @r The pointer to the right of the Lexeme
     */                                                      //FIXME: edit these descriptions as functionality becomes clearer
    private Lexeme cons(String type, Lexeme l, Lexeme r)     //FIXME: what are the types of the params and what is the return type?
    {
        Lexeme ret = new Lexeme(type);
        ret.setCar(l);
        ret.setCdr(r);

        return ret;
    }

    /**
     * Checks to make sure there is a correct number of command line args
     * @args String array of command line args
     */
    private static void checkCmdArgs(String[] args)
    {
        if (args.length != 1)
        {
            System.out.println("Incorrect number of command line args");
            System.exit(-1);
        }
    }

    /**
     * Safe method for opening file
     * @filename The name of the file to be opened
     * @return The pointer to the opened file object
     */
    private static File openFile(String filename) throws IOException
    {
        File file = new File(filename);

        if ( !file.exists() )
        {
            System.out.println("File does not exist");
            System.exit(-1);
        }

        return file;
    }

/********** Public Methods **********/
    /**
     * Getter method for env Lexeme
     * @return The env Lexeme member
     */
    public Lexeme getEnv()
    {
        return env;
    }

    /**
     * Method for inserting id/val binding into table
     * @env The Lexeme from which to add on to
     * @id The ID of the Lexeme
     * @val The actual held value of the Lexeme identified by ID
     */
    public void insertEnv(Lexeme env, Lexeme id, Lexeme val)
    {
        Lexeme car = env.getCar();
        car.setCar( cons(I, id, car.getCar()) );
        car.setCdr( cons(V, val, car.getCdr()) );
    }

    /**
     * Gets the value of the variable with a given name
     * @env The environment to search
     * @id The name of the variable of whom's value is wanted
     * @return A Lexeme with the searched-for id
     */
    public Lexeme getVal(Lexeme env, Lexeme id)
    {
        Lexeme vars;
        Lexeme vals;

        while (env != null)
        {
            vars = (env.getCar()).getCar();
            vals = (env.getCar()).getCdr();

            while (vars != null)
            {
                if ( id.getName() == vars.getCar().getName() )
                    return vals.getCar();

                vars = vars.getCdr();
                vals = vals.getCdr();
            }

            env = env.getCdr();
        }

        System.out.println("Undefined variable error");
        System.exit(-1);

        return null;    // This was just added to surpress the no return error
    }

    /**
     * Sets the value of the variable with a given name
     * @env The environment to search
     * @id The name of the variable of whom's value is wanted
     * @newVal The new value of the variable mentioned above
     */
    public boolean updateVal(Lexeme env, Lexeme id, Lexeme newVal)
    {
        Lexeme vars;
        Lexeme vals;

        while (env != null)
        {
            vars = (env.getCar()).getCar();
            vals = (env.getCar()).getCdr();

            while (vars != null)
            {
                if ( id.getName() == vars.getCar().getName() )
                {
                    vals.setCar(newVal);
                    return true;
                }

                vars = vars.getCdr();
                vals = vals.getCdr();
            }

            env = env.getCdr();
        }

        // System.out.println("Undefined variable error");
        // System.exit(-1);

        return false;
    }

    /**
     * Adds a new scope to the list of environments
     * @env The old environment to which this scope is being added
     * @vars The list of variables of the new scope
     * @vals The corresponding values of the variables above
     * @return a pointer to the new environment scope
     */
    public Lexeme newScope(Lexeme env, Lexeme vars, Lexeme vals)        //FIXME: don't know if this should return a lexeme and/or if env, vars, and vals should be of type lexeme
    {
        return cons(ENV, cons(TABLE, vars, vals), env);
    }

    public static void main(String[] args) throws IOException
    {
        checkCmdArgs(args);

        /* Open file for reading character-by-character */
        File file = openFile(args[0]);
        PushbackInputStream stream = new PushbackInputStream(new FileInputStream(file));

        /* Init token and i to store current Lexeme serve as Lexer object, respectively */
        Lexeme token;
        Lexer i = new Lexer(stream);

        /* Init empty environment with only a TABLE */
        Environment myEnv = new Environment();

        /* Iterate over every Lexeme and add it to the environment */
        token = i.lex();
        while (token.getType() != ENDOFINPUT)
        {
            Lexeme id;
            Lexeme val;

            if (token.getType() == ID)
            {
                id = new Lexeme( token.getType(), token.getName() );

                token = i.lex();

                val = new Lexeme( token.getType(), token.getName() );

                myEnv.insertEnv(myEnv.getEnv(), id, val);
            }
            else
                token = i.lex();
        }

        stream.close();
    }
}
