package edu.brown.cs.student.main;

import edu.brown.cs.student.bloom.recommender.Item;
import edu.brown.cs.student.kdtree.coordinates.Coordinate;
import edu.brown.cs.student.orm.table.ITable;
import org.checkerframework.checker.units.qual.A;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Classmate implements Coordinate<String>, Item, ITable {
    //shared info:
    private final String id;
    private final String name;

    //api info:
    private final String meeting;
    private final double years_of_experience;
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
    private double commenting;
    private double testing;
    private double oop;
    private double algorithms;
    private double teamwork;
    private double frontend;

    private final List<Double> coordinates;

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
        this.id = id;
        System.out.println("ITS HERE======================================"+this.id);
        this.name = name;
        this.meeting = meeting;
        this.grade = grade;
        this.years_of_experience = Double.parseDouble(years_of_experience);
        this.horoscope = horoscope;

        System.out.println(meeting_times);
        // convert meeting_times into list:
        this.meeting_times = new ArrayList<>();
        for (String item: meeting_times.split(";")) {
            this.meeting_times.add(item.trim());
        }
        this.preferred_language = preferred_language;
        this.prefer_group = prefer_group;

        System.out.println(marginalized_groups);
        // convert marginalized_groups into list:
        this.marginalized_groups = new ArrayList<>();
        for (String item: marginalized_groups.split(";")) {
            this.marginalized_groups.add(item.trim());
        }
        this.coordinates = new ArrayList<>();
        coordinates.add(this.years_of_experience);
    }



    public Classmate(Map<String,String> map) {
        this.id = map.get("id");
        this.name = map.get("name");
        this.meeting = map.get("meeting");
        this.grade = map.get("grade");
        this.years_of_experience = Double.parseDouble(map.get("years_of_experience"));
        this.horoscope = map.get("horoscope");

        // convert meeting_times into list:
        this.meeting_times = new ArrayList<>();
        for (String item: map.get("meeting_times").split(";")) {
            this.meeting_times.add(item.trim());
        }
        this.preferred_language = map.get("preferred_language");
        this.prefer_group = map.get("prefer_group");

        // convert marginalized_groups into list:
        this.marginalized_groups = new ArrayList<>();
        for (String item: map.get("marginalized_groups").split(";")) {
            this.marginalized_groups.add(item.trim());
        }
        this.coordinates = new ArrayList<>();
        coordinates.add(this.years_of_experience);
    }

    /**
     * Sets fields from SQL ResultSet
     *
     * @param rs - set containing data read in from SQL
     */
    @Override
    public Classmate populateFields(ResultSet rs) throws SQLException {
        // convert interests into a list:
        this.interests = new ArrayList<>();
        for (String item: rs.getString("interests").split(","))
            this.interests.add(item.trim());

        // convert negative to a list:
        this.negTraits = new ArrayList<>();
        for (String item:  rs.getString("negative").split(","))
            this.negTraits.add(item.trim());

        // convert positive to a list:
        this.posTraits = new ArrayList<>();
        for (String item: rs.getString("positive").split(",")) {
            this.posTraits.add(item.trim());
        }

        // set remaining fields
        this.commenting = rs.getDouble("commenting");
        this.testing = rs.getDouble("testing");
        this.oop = rs.getDouble("OOP");
        this.algorithms = rs.getDouble("algorithms");
        this.teamwork = rs.getDouble("teamwork");
        this.frontend = rs.getDouble("frontend");

        // add numeric data to list of coords
        this.coordinates.add(this.commenting);
        this.coordinates.add(this.testing);
        this.coordinates.add(this.oop);
        this.coordinates.add(this.algorithms);
        this.coordinates.add(this.teamwork);
        this.coordinates.add(this.frontend);

        return this;
    }

    @Override
    public String iTableName() {return "classmate";}

    public List<String> getInterests() {
        return interests;
    }

    public List<String> getNegTraits() {
        return negTraits;
    }

    public List<String> getPosTraits() {
        return posTraits;
    }

    public double getCommenting() {
        return commenting;
    }

    public double getTesting() {
        return testing;
    }

    public double getOop() {
        return oop;
    }

    public double getAlgorithms() {
        return algorithms;
    }

    public double getTeamwork() {
        return teamwork;
    }

    public double getFrontend() {
        return frontend;
    }

    @Override
    public List<String> getVectorRepresentation() {
        List<String> vector = new ArrayList<>();
        vector.add(meeting);
        vector.add(grade);
        vector.add(prefer_group);
        vector.add(horoscope);
        vector.add(preferred_language);
        vector.addAll(meeting_times);
        vector.addAll(marginalized_groups);
        for (String i: interests)
            vector.add("interest:"+i);
        for (String i: posTraits)
            vector.add("pos:"+i);
        for (String i: negTraits)
            vector.add("neg:"+i);
        return vector;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getMeeting() {
        return meeting;
    }

    @Override
    public Double getCoordinateVal(int dim) {
        return coordinates.get(dim);
    }
    @Override
    public List<Double> getCoordinates() {
        return this.coordinates;
    }

    @Override
    public String toString() {
        return "["+this.getId()+"]:" + this.getName();
    }
}
