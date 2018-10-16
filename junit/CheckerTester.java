import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

public class CheckerTester {
	
   PythonCheckerTest pythonCheckerTest = new PythonCheckerTest();
   GenericCheckerTest genericCheckerTest = new GenericCheckerTest();

   @Test
   public void testInput() {

        /* The following tests are for PythonChecker */
        System.out.println("Start testing PythonChecker!");
        // Test 1: input3.py
        System.out.println("Testing input3.py...");
        int[] output_1 = {61, 19, 9, 10, 3, 3};
        assertArrayEquals(output_1, pythonCheckerTest.CheckFile("../testfiles/input3.py"));

        // Test 2: edge.py
        System.out.println("Testing edge.py...");
        int[] output_2 = {37, 24, 6, 18, 6, 2};
        assertArrayEquals(output_2, pythonCheckerTest.CheckFile("../testfiles/edge.py"));

        // Test 3: edge2.py
        System.out.println("Testing edge2.py...");
        int[] output_3 = {10, 3, 1, 2, 1, 1};
        assertArrayEquals(output_3, pythonCheckerTest.CheckFile("../testfiles/edge2.py"));

        // Test 4: print " # this is not a comment" is not a comment
        System.out.println("Testing: print \" # this is not a comment \"");
        String non_comment_1 = "print \" # this is not a comment\"";
        assertEquals(false, pythonCheckerTest.isCommentDoubleQuote(non_comment_1));

        // Test 5: "# this is a comment"
        System.out.println("Testing: # a single-line comment");
        String single_comment = "# a single-line comment";
        assertEquals(true, pythonCheckerTest.isCommentDoubleQuote(single_comment));

        // Test 6: ' # not a comment tho'
        System.out.println("Testing: ' # not a comment tho'");
        String non_comment_2 = "' # not a comment tho'";
        assertEquals(false, pythonCheckerTest.isCommentSingleQuote(non_comment_2));

        // Test 7: "# TODO: Not a todo"
        System.out.println("Testing: \" # TODO: Not a todo\"");
        String non_TODO = "\"# TODO: Not a todo\"";
        assertEquals(false, pythonCheckerTest.isTODO(non_TODO));

        // Test 8: print(a) # TODO: this is a TODO
        System.out.println("Testing: print(a) # TODO: this is a TODO");
        String todo = "print(a) # TODO: this is a TODO";
        assertEquals(true, pythonCheckerTest.isTODO(todo));

        // Test 9: Test numOfOccurrences()
        System.out.println("Testing: numOfOccurrences() with #");
        String[] arr = {" # ", "awesome", "# two hashtags #", ""};
        assertEquals(3, pythonCheckerTest.numberOfOccurrences(arr, "#"));

        // Test 10: edge3.py
        System.out.println("Testing edge2.py...");
        int[] output_4 = {3, 3, 0, 3, 1, 0};
        assertArrayEquals(output_4, pythonCheckerTest.CheckFile("../testfiles/edge3.py"));



        /* The following tests are for GenericChecker */
        System.out.println("Start testing GenericChecker!");
        // Test 1: input1.java
        System.out.println("Testing input1.java...");
        int[] generic_output_1 = {60, 28, 6, 22, 2, 1};
        assertArrayEquals(generic_output_1, genericCheckerTest.CheckFile("../testfiles/input1.java"));

        // Test 2: input2.ts
        System.out.println("Testing input2.ts...");
        int[] generic_output_2 = {40, 23, 5, 18, 4, 1};
        assertArrayEquals(generic_output_2, genericCheckerTest.CheckFile("../testfiles/input2.ts"));

        // Test 3: edge.cpp
        System.out.println("Testing edge1.cpp...");
        int[] generic_output_3 = {29, 16, 6, 10, 5, 1};
        assertArrayEquals(generic_output_3, genericCheckerTest.CheckFile("../testfiles/edge1.cpp"));

        // Test 4: print " // this is not a comment" is not a comment
        System.out.println("Testing: print \" // this is not a comment \"");
        String generic_non_comment_1 = "print \" // this is not a comment\"";
        assertEquals(false, genericCheckerTest.isSingleComment(generic_non_comment_1));

        // Test 5: "// this is a comment"
        System.out.println("Testing: # a single-line comment");
        String generic_single_comment = "// a single-line comment";
        assertEquals(true, genericCheckerTest.isSingleComment(generic_single_comment));

        // Test 6:  "end of comment block */" with commentStart == true
        System.out.println("Testing:  /* end of comment block with commentStart == true");
        String generic_end_block = " end of comment block */";
        assertEquals(true, genericCheckerTest.endBlockComment(generic_end_block, true));

        // Test 7: "end of comment block */" with commentStart == false
        System.out.println("Testing:  /* end of comment block with commentStart == false");
        assertEquals(false, genericCheckerTest.endBlockComment(generic_end_block, false));

        // Test 8: printf("awesome"); // TODO: this is a TODO
        System.out.println("Testing: printf(\"awesome\"); // TODO: this is a TODO");
        String generic_todo = "printf(\"awesome\"); // TODO: this is a TODO";
        assertEquals(true, genericCheckerTest.isTODO(generic_todo));

        // Test 9: Test numOfOccurrences()
        System.out.println("Testing: numOfOccurrences()");
        String[] generic_arr = {" // ", "awesome", "// two hashtags //", "// awesome"};
        assertEquals(4, genericCheckerTest.numberOfOccurrences(generic_arr, "//"));

        // Test 10: // This is not strictly a "TODO"
        System.out.println("Testing: This is not strictly a \"TODO\"");
        String generic_non_todo = "This is not strictly a \"TODO\"";
        assertEquals(false, genericCheckerTest.isTODO(generic_non_todo));


   }
}