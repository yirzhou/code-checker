# Copyright (c) 2017 Capital One Financial Corporation All Rights Reserved.
#
# This software contains valuable trade secrets and proprietary information of
# Capital One and is protected by law. It may not be copied or distributed in
# any form or medium, disclosed to third parties, reverse engineered or used in
# any manner without prior written authorization from Capital One.

#Author: Ali Bhagat
#This script helps to build a simple stopwatch application using Python's time module.
import time

print('Press ENTER to begin, Press Ctrl + C to stop')
while True:
    try:
        input() # For ENTER. Use raw_input() if you are running python 2.x instead of input()
        starttime = time.time()
        print('Started')
    except KeyboardInterrupt:
        print('Stopped')
        endtime = time.time()
        print('Total Time:', round(endtime - starttime, 2),'secs')
        break

# Define again() function to ask user if they want to use the calculator again
def again():

    # Take input from user
    calc_again = input('''
                        Do you want to calculate again?
                        Please type Y for YES or N for NO.
                        ''')

    # If user types Y, run the calculate() function
    if calc_again == 'Y':
        calculate() # Inline comment about the code

    # If user types N, say good-bye to the user and end the program
    elif calc_again == 'N':
        print('See you later.')

# If user types another key, run the function again
    else:
        again()



def calculate():
    # TODO: Implement the calculate function

def input():
    # TODO: Implement this function
    return 'Dummy'

def realpath(path):
    '''
    Returns the true, canonical file system path equivalent to the given
    path.
    '''
    # TODO: There may be a more clever way to do this that also handles other,
    # less common file systems.
    return os.path.normpath(normcase(os.path.realpath(path)))

# '''
# end of block comment 

# This is a comment...

# TODO: This is a todo item

# Start of a block
# '''#''' still a comment (missed)
#still a part of the block

print " # "     # This is a comment and a line of code
print " # "         # This is a comment and a line of code

#single line
#but ended with a block

# '''
# end of block comment 
# This is a comment...
# TODO: This is a todo item
# Start of a block
# '''#''' still a comment (missed)
#still a part of the block
print " # " # This is a comment and a line of code
print " # " # This is a comment and a line of code
#single line
#but ended with a block

print '''
    # Nothing here is going to be commenting
    This is not a # comment
    this is also not a comment #
    '''

Total # of lines: 103
Total # of comment lines: 47
Total # of single line comments: 21
Total # of comment lines within block comments: 26
Total # of block line comments: 8
Total # of TODO's: 6
