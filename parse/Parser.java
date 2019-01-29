/**
 * Author: Jake Wachs
 * CS 403, Programming Languages
 * The University of Alabama
 *
 * Parser Class
 */
package parse;

import java.io.*;

import lex.*;


public class Parser
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
     * Main method to be run for Parser
     * @args The command line arguments
     */
    public static void main(String[] args) throws IOException
    {
        checkCmdArgs(args);

        File file = openFile(args[0]);
        PushbackInputStream stream = new PushbackInputStream(new FileInputStream(file));

        Lexer i = new Lexer(stream);
        Lexeme curr = i.lex();

        Recognizer r = new Recognizer(curr, stream);

        r.program();

        stream.close();
    }
}
