package edu.brown.cs.student.main;

import org.checkerframework.checker.units.qual.A;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Classmate {
    //shared info:
    private final int id;
    private final String name;

    //api info:
    private final String meeting;
    private final int years_of_experience;
    private final String grade;
    private final String prefer_group;
    private final String horoscope;
    private final String preferred_language;
    private final List<String> meeting_times;
    private final List<String> marginalized_groups;

    //sql info (can't be final, since assigning in sql constructor)
    private List<String> interests;
    private List<String> negTraits;
    private List<String> posTraits;
    private int commenting;
    private int testing;
    private int oop;
    private int algorithms;
    private int teamwork;
    private int frontend;

    /**
     * Takes in API data and constructs a new object. Main constructor needs to take in API data
     * because the class is being passed to process JSON from the API call.
     *
     * @param id - person's unique ID
     * @param name - person's name (String)
     * @param meeting - person's pref meeting loc (String)
     * @param grade - person's year (String)
     * @param years_of_experience - person's exper (int)
     * @param horoscope - person's horoscope (String)
     * @param meeting_times - person's pref meeting
     * @param preferred_language - person's pref lang (String)
     * @param marginalized_groups - person's marg groups (List)
     * @param prefer_group - person prefs group or not (String)
     */
    public Classmate(String id, String name, String meeting, String grade,
                     String years_of_experience, String horoscope, String meeting_times,
                     String preferred_language, String marginalized_groups,
                     String prefer_group) {
        this.id = Integer.parseInt(id);
        this.name = name;
        this.meeting = meeting;
        this.grade = grade;
        this.years_of_experience = Integer.parseInt(years_of_experience);
        this.horoscope = horoscope;

        // convert meeting_times into list:
        String[] meeting_times_split = meeting_times.split(";");
        ArrayList<String> meetingTimesList = new ArrayList<>();
        for (String item: meeting_times_split) {
            meetingTimesList.add(item.trim());
        }
        this.meeting_times = meetingTimesList;
        this.preferred_language = preferred_language;
        this.prefer_group = prefer_group;

        // convert marginalized_groups into list:
        String[] marginalized_groups_split = marginalized_groups.split(";");
        ArrayList<String> marginalizedGroupList = new ArrayList<>();
        for (String item: marginalized_groups_split) {
            marginalizedGroupList.add(item.trim());
        }
        this.marginalized_groups = marginalizedGroupList;
    }

    /**
     * Sets fields from SQL ResultSet
     *
     * @param rs - set containing data read in from SQL
     */
    public void setSQLData(ResultSet rs) throws SQLException {
        // convert interests into a list:
        String interests = rs.getString("interests");
        String[] interests_split = interests.split(",");
        ArrayList<String> interestsList = new ArrayList<>();
        for (String item: interests_split) {
            interestsList.add(item.trim());
        }
        this.interests = interestsList;
        // convert negative to a list:
        String negatives = rs.getString("negative");
        String[] negative_split = negatives.split(",");
        ArrayList<String> negativesList = new ArrayList<>();
        for (String item: negative_split) {
            negativesList.add(item.trim());
        }
        this.negTraits = negativesList;
        // convert negative to a list:
        String positives = rs.getString("positive");
        String[] pos_split = positives.split(",");
        ArrayList<String> posList = new ArrayList<>();
        for (String item: pos_split) {
            posList.add(item.trim());
        }
        this.posTraits = posList;

        // set remaining fields
        this.commenting = rs.getInt("commenting");
        this.testing = rs.getInt("testing");
        this.oop = rs.getInt("OOP");
        this.algorithms = rs.getInt("algorithms");
        this.teamwork = rs.getInt("teamwork");
        this.frontend = rs.getInt("frontend");
    }

    public List<String> getInterests() {
        return interests;
    }

    public List<String> getNegTraits() {
        return negTraits;
    }

    public List<String> getPosTraits() {
        return posTraits;
    }

    public int getCommenting() {
        return commenting;
    }

    public int getTesting() {
        return testing;
    }

    public int getOop() {
        return oop;
    }

    public int getAlgorithms() {
        return algorithms;
    }

    public int getTeamwork() {
        return teamwork;
    }

    public int getFrontend() {
        return frontend;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getMeeting() {
        return meeting;
    }
}
