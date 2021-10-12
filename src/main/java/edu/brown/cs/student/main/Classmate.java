package edu.brown.cs.student.main;

import java.util.List;

public class Classmate {
    //shared info:
    int id;
    String name;

    //api info:
    int years_of_experience;
    String grade;
    String prefer_group;
    String horoscope;
    String preferred_language;
    List<String> meeting_times;
    List<String> marginalized_groups;

    //sql info:
    List<String> interests;
    List<String> negTraits;
    List<String> posTraits;
    int commenting;
    int testing;
    int oop;
    int algorithms;
    int teamwork;
    int frontend;

    public Classmate(String id, String name, String meeting, String grade,
                     String years_of_experience, String horoscope, String meeting_times,
                     String preferred_language, String marginalized_groups,
                     String prefer_group){
        //parse all inputs to the correct types to be stored (ie. ints, lists, etc)
    }

    // maybe we only need this if we want to set the API data after construction;
//    public void setAPIData() {
//
//    }
    public void setSQLData() {

    }
}
