/**
 * Author: Jake Wachs
 * CS 403, Programming Languages
 * The University of Alabama
 *
 * Lexical Analysis Module
 */

 // Is there a way to compile with more warning settings and do I need to do it?
 // Need to account for something like below that ends with '\n'. Do I need to escape newline char?
            // printf("hello world\n");
// should not have newline characters in the array. Maybe make it easy on myself and just get rid of '//' comments?
// does main() need to return an int?


import java.io.*;
import java.util.ArrayList;


class Scanner
{
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

    /**
     * Main method to be run for Scanner
     * @args The command line arguments
     */
    public static void main(String[] args) throws IOException
    {
        checkCmdArgs(args);

        File file = openFile(args[0]);

        Lexeme token;
        Lexer i = new Lexer();

        token = i.lex(file);
        while (token.getType() != "ENDofINPUT")
        {
            Lexeme.display(token);
            token = i.lex(file);
        }
    }
}
