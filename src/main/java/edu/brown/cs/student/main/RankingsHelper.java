package edu.brown.cs.student.main;

public class RankingsHelper {
  private String id;
  private double rank;

  public RankingsHelper(String id, double rank) {
    this.id = id;
    this.rank = rank;
  }

  public String getId() {
    return id;
  }

  public double getRank() {
    return rank;
  }

  @Override
  public String toString() {
    return "RankingsHelper{" +
        "id='" + id + '\'' +
        ", rank=" + rank +
        '}';
  }
}
