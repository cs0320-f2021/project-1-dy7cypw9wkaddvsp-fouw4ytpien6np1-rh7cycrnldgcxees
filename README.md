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


#Notes on Implementations:

User Story 1: Generic REPL/Adding and Removing Commands

    

User Story 2: Load Recommender with k Students

    The Set data method in the PartnerRecommender Class aggregates data from integration.sqlite3
    and from an API call to the appropriate integration website. In order to aggregate the data to be used in the
    recsys_rec <num_recs> <student_id> and recsys_gen_groups <team_size> commands, our implementation uses the
    Classmate class whose main constructor takes in the appropriate data from the generic API implementation.
    In setData, we have a separate process to take in a ResultSet from a query, i.e. matching the ID fields of the 
    JSON objects of the API call with those objects from the integration.sqlite3 database. We accomplished this by 
    creating an extendible method in our database class that could arbitrarily take in a table and match on a 
    shared field in both the ORM and API data to get the fields of the objects' whose IDs match (this does assume
    that objects are relatable somehow between SQL and the API). From here, our Classmate object and other Obejcts that
    extend IStudent (main constructor relates to API and it must implement setSQLData). Here is a point where we wish
    we had more time to implement this component more generically. We had to use the Classmate class in our 
    PartnerRecommender class, which is not ideal, however, we did try to create a skeleton for how we would make the 
    implementation with a class to store attributes more generically. This is a primary focus for us on our next 
    project. Moreover, when aggregating the SQL tables we had to run a portion of the code to make those new tables
    only once (and subsequently comment it out) given that those changes we made to the SQL database would remain
    after the program would finish running. We tried to troubleshoot this, but could not find an elegant solution.
    From there, setData would load users into the KDTree and BloomFilter so that we could make recommendations.
    The REPL command to load the data was recsys_load, which used the RecsysLoadHandler to actually load the data
    and catch any erros. 

User Story 3: get k recommendations for a particular student's ID. 

    This was mainly handled by the getRecsFromStudentID method within PartnerRecommender.java. We also had a 
    RecsysRecHandler to handle the REPL inputs associated with this command and to catch any erros like invalid
    k number of recommendations or a studentID that did not exist. For the particular StudentID 
    inputted, we took the recommendations from the BloomFilter and KD for all other 60 students. 
    We then used a helper class to aggregate those two rankings for one overall ranking. We then sorted by the average
    ranking and in the RecsysRecHandler we then printed out the top k results. Again to run this, one can type:
    recsys_rec <num_recs> <student_id>.
    

User Story 4: get groups of size groupSize


#Distribution of Woke:

Izzy:

Emma:

Henry:

    I spent most of my time on user stories 2 and 3 as well as helping others with miscellaneous things as they 
    came up. Most of my work involved extending our database class to handle the aggregation of the 
    data from SQL and the API call, so that we could create one aggregate Object, titled Classmate, which
    would then be used in PartnerRecommender to actually make recommendations. I also wrote a large portion
    of the README file and our thoughts below on how the Biases of our recommender system may negatively    
    impact minorities.

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

    If a biased system such as this were to be used in a larger context, or even in the current context, members
    of minority groups would rarely be grouped up with others with similar traits or with complementing skills, etc.
    Although we didn't have time to fully address these concerns, we believe that the data collection process may 
    be helped with a disclaimer/educational resource that could be presented before a user responds to the survey.
    There are coding-relating decision we can make to have our system be more unbiased, such as perhaps taking race 
    into account and making sure we don't have clusters of people from similar backgrounds in the same group, but a
    disclaimer up front could be a first step. We could summarize the findings from the study above on the cover page
    before a user inputs their responses, as well as create added checks in our code to make sure people are grouped
    up with who they should be grouped up with and that race or minority status does not skew those results.