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

where <num_recs> corresponds to the number of recommendations you would like to make for the student (in our database, 
the max value is 60, since there are 61 total students) and <student_id> is the student_id of the student you are 
interested in making recommendations for. The ranking is based on an average rank from both the KDTree output 
(primarily quantitative attributes, i.e. how similar different classmates are on numerical traits) and the BloomFilter 
output (primarily qualitative attributes).

The output of this process will be of the form, where k represents <num_recs>:

    <matched_student_id_1>
    <matched_student_id_2>
    ...
    <matched_student_id_k

#Generating Groups of size k:

After running recsys_load, a user of the REPL can also get group recommendations of size k by 
running the following command:

    recsys_gen_groups <team_size>

where <team_size> is the desired number of members in each team. If the <team_size> number does not
divide evenly into the number of classmates pulled in from the API and SQLite database, then some groups may have one
extra group mate to compensate for this.

The resulting output of this command is in the following format:

    [group_1_student_1, group_1_student_2, … group_1_student_k]
    [group_2_student_1, group_2_student_2, … group_2_student_k]
    ...
    [group_n_student_1, group_n_student_2, … group_n_student_k]

#Questions

Matchmaker Results: how minorities may be impacted

    An issue we see with our matchmaker system, especially considering the findings in the study by the National Bureau
    on Economic Development, is that we cannot account for how inputs may be biased higher or lower depending on which
    group, a minority or majority, a given responder is apart of. Our matchmaker uses a purely quantitative approach,
    even attempting to quantify qualitative attributes from the survery (i.e. BloomFilter's representation of 
    interests and positive/negative traits). With this in mind, there is no way to control for race/minority status, 
    which may severly skew our groupings/recommendation. We fear that members of the majority may be group with 
    other members of the majority, whereas members of minorities may be grouped together, which would create a
    dynamic not as diverse as desired (minimal diversity among the majority and minorities).

Larger-reaching impacts

    
    

As you work through the data, think about how it reflects biases within society even though it hasn’t been 
manipulated nor collected with specific motivations in mind. As seen in a study by the National Bureau on Economic 
Development (read this summary), even when providing information on themselves - the topic which an individual can 
speak to best - different groups act differently, with minorities far less likely to rate their own abilities well 
than their equally skilled peers in the majority. From this angle, how might your matchmaker’s results end up treating
minorities differently than their more privileged counterparts? 

    ...


What larger-reaching impacts might this have (e.g. are biases reiterated? Do members of minority groups receive 
as well-suited matches as their peers might? etc.):

    ...
    
In the questions section of your README, write a paragraph describing these impacts and explaining how your system 
design was influenced by this context.