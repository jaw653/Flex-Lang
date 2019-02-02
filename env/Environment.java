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
    public void insertEnv(Lexeme id, Lexeme val)
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
    public Lexeme newScope(Environment e, Lexeme vars, Lexeme vals)        //FIXME: don't know if this should return a lexeme and/or if env, vars, and vals should be of type lexeme
    {
        return cons( ENV, cons(TABLE, vars, vals), e.getEnv() );
    }

    public static void main(String[] args)
    {
        /* Init empty environment with only a TABLE */
        Environment env = new Environment();

        System.out.println("The environment is: ...");                  //FIXME: should this be ... or should something take the ellipses place?

        System.out.println("Adding variable x with value 3");
        Lexeme id = new Lexeme(ID, "x");
        Lexeme val = new Lexeme(INTEGER, 3);
        env.insertEnv(id, val);

        System.out.println("The environment is: ...");
        System.out.println("Extending the environment with y:4 and z:\"hello\"");
        Lexeme vars = new Lexeme(ID, "y");
        Lexeme vals = new Lexeme(STRING, "hello");
        Lexeme extension = env.newScope(env, vars, vals);


        System.out.println("The local environment is: ...");
        System.out.println("The environment is: ...");
        // ...
    }
}
