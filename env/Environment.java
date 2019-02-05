/**
 * Author: Jake Wachs
 * CS 403, Programming Languages
 * The University of Alabama
 *
 * Environment Class
 */

// if extra time make nicer looking test methods (static private)

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
    public Lexeme getVal(Lexeme id)
    {
        Lexeme vars;
        Lexeme vals;

        while (env != null)
        {
//            System.out.println("loop");
            vars = (env.getCar()).getCar();
            vals = (env.getCar()).getCdr();

            while (vars != null)
            {
//                System.out.println("comparing " + id.getName() + " to " + vars.getCar().getName());
                if ( id.getName().equals(vars.getCar().getName()) )
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
    public boolean updateVal(Lexeme id, Lexeme newVal)
    {
        Lexeme vars;
        Lexeme vals;

        while (env != null)
        {
//            System.out.println("loop");
            vars = (env.getCar()).getCar();
            vals = (env.getCar()).getCdr();

            while (vars != null)
            {
//                System.out.println("loop2");
//                System.out.println("searching for: " + id.getName());
//                System.out.println("comparing to: " + vars.getCar().getName());
                if ( id.getName().equals(vars.getCar().getName()) )
                {
//                    System.out.println("good!");
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

/*
        System.out.println("Adding variable x with value 3");
        Lexeme id = new Lexeme(ID, "x");
        Lexeme val = new Lexeme(INTEGER, 3);
        env.insertEnv(id, val);

        System.out.println("The environment is: ...");
        System.out.println("Extending the environment with y:4 and z:\"hello\"");
        Lexeme vars = new Lexeme(ID, "y");
        Lexeme vals = new Lexeme(STRING, "hello");
        Lexeme extension = env.newScope(env, vars, vals);

        System.out.println("Updating value with ID x in original environment...");
        Lexeme searchID = new Lexeme(I, "x");
        Lexeme newVal = new Lexeme(V, 10);
        if ( env.updateVal(searchID, newVal) == false )
            System.out.println("ERROR: Update unsuccessful.");
        else
            System.out.println("Update successful!");


        System.out.println("The local environment is: ...");
        System.out.println("The environment is: ...");
        // ...
*/
        String var = "a";
        int val = 1;

        Lexeme id;
        Lexeme value;

        for (int i = 0; i < 10; i++)
        {
            System.out.println("Adding variable " + var + " with value: " + val);

            /* Set the value of the ID Lexeme */
            id = new Lexeme(I, var);

            /* Changing the value of var */
            char tmp = var.charAt(0);
            tmp++;
            var = String.valueOf(tmp);

            /* Set the value Lexeme and then increase for next set */
            value = new Lexeme(V, val++);

            /* Insert id, value pair to env */
            env.insertEnv(id, value);
        }

        /* Resetting var to the beginning */
        var = "a";
        Lexeme getVal;
        for (int i = 0; i < 10; i++)
        {
            getVal = env.getVal(new Lexeme(I, var));

            System.out.println("ID " + var + " has value: " + getVal.getInt());

            char tmp = var.charAt(0);
            tmp++;
            var = String.valueOf(tmp);
        }

        System.out.println("Updating variable a to have value 50");
        env.updateVal(new Lexeme(I, "a"), new Lexeme(V, 50));
        System.out.println("New val of Lexeme with ID a is: " + env.getVal(new Lexeme(I, "a")).getInt() );

        System.out.println("Updating variable c to have value 35");
        env.updateVal(new Lexeme(I, "c"), new Lexeme(V, 35));
        System.out.println("New val of Lexeme with ID c is: " + env.getVal(new Lexeme(I, "c")).getInt() );

        // need to test extend/newScope here...
    }
}
