/*
*   GenericChecker Object
*   Written by: Yiren Zhou
*
*   Supported Languages: C, C++, Java, JavaScript, Swift, etc.
*
*   The purpose of the program is to scan a code file and return:
*   1. File length (comments included)
*   2. Number of comments
*   3. Number of single-line comments
*   4. Number of comment blocks
*   5. Number of comment lines from comment blocks
*   6. Number of TODOs
*
*/

import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import org.apache.commons.lang3.*;

public class GenericChecker {

    // Initializing all variables needed;
    private int countLines;
    private int countTotalComments;
    private int countSingleComments;
    private int countMultiComments;
    private int countBlockComments;
    private int countTodos = 0;
    // Signal to detect if we are currently reading a part of a comment block
    private boolean commentStart;

    // Constructor
    public GenericChecker() {
        this.countLines = 0;
        this.countTotalComments = 0;
        this.countSingleComments = 0;
        this.countMultiComments = 0;
        this.countBlockComments = 0;
        this.countTodos = 0;
        this.commentStart = false;
    }

    // The core function that does the file checking
    public void CheckFile(String filename) {
        // The following code counts the # lines and store them into a list
        try {
            // "allLines" stores all strings line by line in a list
            List<String> allLines = Files.readAllLines(Paths.get(filename));
            
			for (String line : allLines) {
                /*
                    Counting Single-line Comments:
                    A single-line comment is started with "//",
                */
                { 
                    if(this.isSingleComment(line) && !this.commentStart) {
                        this.countSingleComments++;
                        this.countTotalComments++;
                    }
                }

                /*
                    Counting total lines of comments
                    It will start with a "/*" somewhere, and ends with its counterpart somewhere else,
                    but the opening and closing tags should not be on the same line, 
                    which is defined as a single-line comment
                */ 
                {
                    if (this.startBlockComment(line) && !this.commentStart) 
                        this.commentStart = true;
                    if (this.commentStart)
                        this.countTotalComments++;
                    // If the line has a "*/" when the comment has started, it should be closed
                    // It could be the same line as well, which forms a very short block comment
                    if (this.endBlockComment(line, this.commentStart)) {
                        this.countBlockComments++;
                        this.commentStart = false;
                    }    
                }
                
                /*
                    Counting TODOs: 
                        1. It has to be a comment.
                        2. Check if there is ant "TODO" between double quotes
                */
                {
                    if (this.isTODO(line))
                        this.countTodos++;
                }   
            }

            // Printing results
            this.countLines = allLines.size();
            this.countMultiComments = this.countTotalComments - this.countSingleComments;

            System.out.println("Total # of lines: " + this.countLines);
            System.out.println("Total # of comment lines: " + this.countTotalComments);
            System.out.println("Total # of single line comments: " + this.countSingleComments);
            System.out.println("Total # of comment lines within block comments: " + this.countMultiComments);
            System.out.println("Total # of block line comments: " + this.countBlockComments);
            System.out.println("Total # of TODO's: " + this.countTodos);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }


    //  Function to return the number of occurrences of an element in an array of strings
    public int numberOfOccurrences(String[] arr, String element) {
        int count = 0;
        for(int i = 0; i < arr.length; ++i) {
            if (arr[i].contains(element)) count++;
        }
        return count;
    }

    // Function to determine if this is a single-line comment
    // It will detect whether a "//" is in any double quote
    public boolean isSingleComment(String line) {
        if(line.contains("//") && StringUtils.substringsBetween(line, "\"", "\"") != null) {
            if(this.numberOfOccurrences(StringUtils.substringsBetween(line, "\"", "\""), "//") < StringUtils.countMatches(line, "//")) {
                return true;
            } else 
                return false;
        } else if (line.contains("//") && StringUtils.substringsBetween(line, "\"", "\"") == null){
            return true; 
        } else if (!line.contains("//"))
            return false;

        return false;
    }

    public boolean isTODO(String line) {
        if (this.isSingleComment(line) && line.contains("TODO")) {
            // If the comment doesn't contain any double quote, it is a TODO
            // Else, count the occurrences of TODOs
            if (StringUtils.substringsBetween(line, "\"", "\"") == null)
                return true;
            else if (this.numberOfOccurrences(StringUtils.substringsBetween(line, "\"", "\""), "TODO") < StringUtils.countMatches(line, "TODO")) {
                return true;
            } 
        } 
        return false;
    }

    /*
        Returns true if it is the start of a comment block
    */
    public boolean startBlockComment(String line) {
        if(line.contains("/*") && StringUtils.substringsBetween(line, "\"", "\"") != null) {
            if(this.numberOfOccurrences(StringUtils.substringsBetween(line, "\"", "\""), "/*") < StringUtils.countMatches(line, "/*")) {
                return true;
            } else 
                return false;
        } else if (line.contains("/*") && StringUtils.substringsBetween(line, "\"", "\"") == null){
            return true; 
        } else if (!line.contains("/*"))
            return false;

        return false;
    }

    public boolean endBlockComment(String line, boolean started) {
        if (line.contains("*/") && started)
            return true;

        return false;
    }
}