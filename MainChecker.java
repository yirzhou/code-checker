/*
*   Code-Checker Project
*   Written by: Yiren Zhou
*   
*   The purpose of the program is to scan a file of code and return:
*   1. File length (comments included)
*   2. Number of comments
*   3. Number of single-line comments
*   4. Number of comment blocks
*   5. Number of comment lines from comment blocks
*   6. Number of TODOs
*
*   It takes a file that could be written in Python or other popular languages
*   such as C++, Java, Javascript, and Swift, which all share the same commenting style.
*
*   Credit to: Apache Software Foundation
*   This program utilizes "org.apache.commons" package from Apache Software Foundation
*/

import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import org.apache.commons.lang3.*;

public class MainChecker {
    public static void main(String[] args) {
        if(args.length == 0) {
            System.err.println("Please provide a filename.");
        } else {
            String filename = args[0];
            String[] tokens = filename.split("\\.(?=[^\\.]+$)");
            if(tokens[1].equals("py")) {
                PythonChecker pythonChecker = new PythonChecker();
                pythonChecker.CheckFile(filename);
            } else {
                GenericChecker genericChecker = new GenericChecker();
                genericChecker.CheckFile(filename);
            }
        }

        /*
            The following commented code is for debugging purpose
        */
        
        // String filename = args[0];
        // String filename = "testfiles/input3.py";
        // String[] tokens = filename.split("\\.(?=[^\\.]+$)");
        // if(tokens[1].equals("py")) {
        //     PythonChecker pythonChecker = new PythonChecker();
        //     pythonChecker.CheckFile(filename);
        // } else {
        //     GenericChecker genericChecker = new GenericChecker();
        //     genericChecker.CheckFile(filename);
        // }
    }
}