package com.vhdlparser;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws Exception{
        if (args.length != 2 || args[0].equals("-h"))
            printHelp();
        else if (args[0].equals("-f"))
            runWalker(new File(args[1]));
        else if (args[0].equals("-F")) {
            File[] files = new File(args[1]).listFiles();
            for (File f : files)
                runWalker(f);
        }
        else
            printHelp();
    }

    static void runWalker (File file) throws IOException {
        if (!file.exists()) {
            System.out.println("File '" + file.toString() + "' does not exist.");
            return;
        }
        if (file.isDirectory()) {
            printHelp();
            return;
        }
        vhdlLexer lexer = new vhdlLexer(new ANTLRFileStream(file.toString()));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        vhdlParser parser = new vhdlParser(tokens);
        ParseTree tree = parser.design_file();
        ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk(new vhdlWalker(file.toString()), tree);
    }

    static void printHelp () {
        System.out.println("vhdlparser [-f-F] source");
        System.out.println("\t -h\t this help");
        System.out.println("\t -f\t vhdl file source");
        System.out.println("\t -F\t folder with vhdl file source");
    }
}
