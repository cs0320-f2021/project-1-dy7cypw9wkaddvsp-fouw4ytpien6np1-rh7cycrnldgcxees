package edu.brown.cs.student.bloom.recommender;

import java.util.List;

public interface Item {
  List<String> getVectorRepresentation();
  String getId();
}
