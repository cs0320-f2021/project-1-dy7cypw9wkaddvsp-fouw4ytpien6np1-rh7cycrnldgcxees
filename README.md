# README
To build use:
`mvn package`

To run use:
`./run`

To start the server use:
`./run --gui [--port=<port>]`

#Adding or removing commands to the REPL:

To add commands to the REPL, one can navigate to the path:

    edu/brown/cs/student/main/handlers

In this directory, one can add new commands in the form of a CommandHandler that
implements ICommandHandler. The final step is to navigate to:
    
    edu/brown/cs/student/main

where one needs to add a key-value pair to the CommandHashmap. The key corresponds to a 
String that represents the command key for the appropriate command, and the value corresponds to the 
ICommandHandler added to edu/brown/cs/student/main/handlers.

#Loading Student Data:
 
A user of the REPL can load student data by running:

    recsys_load

If the process is successful, the REPL will print the following informative statement:

    Loaded Recommender with k students

where k represents the total number of students in the database. Students' attributes are aggregated from 
an API call to https://runwayapi.herokuapp.com/integration and from an ORM processing of integration.sqlite3.

Qualitative attributes are stored and processed by a Bloom Filter, and quantitative attributes are 
stored and processed by a KDTree.

#Generating k recommendations for a particular student_id:

After running recsys_load, a user of the REPL can then get recommendations for a 
particular student_id by running:

    recsys_rec <num_recs> <student_id>

where <num_recs> corresponds to the number of recommendations you would like to 
make for the student (in our database, the max value is 60, since there are 61 
total students) and <student_id> is the student_id of the student you are 
interested in making recommendations for.

The output of this process will be of the form, where k represents <num_recs>:

    <matched_student_id_1>
    <matched_student_id_2>
    ...
    <matched_student_id_k

# 





















#Loading in databases:

To load in a Database of Objects (where the path to the database reflects the local path), use:
    
    database path/to/database.sqlite3


To load in a Database of Users (where the path to the database reflects the local path), use:

    users path/to/database.sqlite3


#Finding Most Similar Users:
From the database of Users that were loaded in, find the k most similar Users 
(based on height, weight, and age) to a given User with:

    similar <k> <some_user_id>
The User IDs of the k most similar Users will be returned in order starting with the nearest.

Similarly, find the k most similar Users to a given height, weight, and age with:

    similar <k> <weight in lbs> <height in inches> <age in years>
The User IDs of the k most similar Users will be returned in order starting with the nearest.

#Classifying Horoscopes:
To print out a count of how many of each of the Zodiac signs correspond with the
k most similar Users to a given User, use:
    
    classify <k> <some_user_id>
The results will list how many Users within the list of k each of the 12 Horoscope
signs correspond to. For example:
    
    >>> classify 5 151944
    Aries: 1
    Taurus: 1
    Gemini: 0
    Cancer: 0
    Leo: 1
    Virgo: 0
    Libra: 1
    Scorpio: 0
    Sagittarius: 0
    Capricorn: 0
    Aquarius: 1
    Pisces: 0

To do a similar Horoscope classifying process based on a given height, weight, 
and age, use:

    classify <k> <weight in lbs> <height in inches> <age in years>

The result will look similar to the example above, listing how many Users 
within the list of k each of the 12 Horoscope signs correspond to. For example:

    >>> classify 5 190 70 21
    Aries: 1
    Taurus: 1
    Gemini: 0
    Cancer: 0
    Leo: 2
    Virgo: 0
    Libra: 1
    Scorpio: 0
    Sagittarius: 0
    Capricorn: 0
    Aquarius: 0
    Pisces: 0


