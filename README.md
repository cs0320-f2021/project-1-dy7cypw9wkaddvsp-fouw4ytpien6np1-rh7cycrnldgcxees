# README
To build use:
`mvn package`

To run use:
`./run`

To start the server use:
`./run --gui [--port=<port>]`

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




