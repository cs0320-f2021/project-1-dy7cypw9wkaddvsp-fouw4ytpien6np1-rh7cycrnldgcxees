package edu.brown.cs.student.main;

public class RankingsHelper {
  private String id;
  private double rank;

  /**
   * Helper class to sort ranks while keeping track of ID
   *
   * @param id - classmate ID
   * @param rank - rank relative to the inputted user in PartnerRecommender
   */
  public RankingsHelper(String id, double rank) {
    this.id = id;
    this.rank = rank;
  }

  /**
   * Get ID as String
   *
   * @return String id
   */
  public String getId() {
    return id;
  }

  /**
   * Get rank as a double
   *
   * @return double rank
   */
  public double getRank() {
    return rank;
  }

  /**
   * Convert RankingsHelper to String
   *
   * @return String - RankingsHelper as String
   */
  @Override
  public String toString() {
    return "RankingsHelper{" +
        "id='" + id + '\'' +
        ", rank=" + rank +
        '}';
  }
}
