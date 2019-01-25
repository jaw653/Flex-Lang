/**
 * Author: Jake Wachs
 * CS 403, Programming Languages
 * The University of Alabama
 *
 * Lexical Analysis Module
 */

 // Need to account for something like below that ends with '\n'. Do I need to escape newline char?
            // printf("hello world\n");
// change the while loop here to look for ENDofINPUT Lexeme, not what it currently is

import java.io.*;

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
        try
        {
            checkCmdArgs(args);

            File file = openFile(args[0]);
            PushbackInputStream stream = new PushbackInputStream(new FileInputStream(file));

            Lexeme token;
            Lexer i = new Lexer(stream);

            token = i.lex();
            while (stream.available() > 0)     // FIXME: might need to change this to "token.getType() != ENDofINPUT"
            {
                token.display();            // FIXME: need to implement this in Lexeme
                token = i.lex();
            }

            stream.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
