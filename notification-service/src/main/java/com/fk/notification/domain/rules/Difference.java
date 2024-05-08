package com.fk.notification.domain.rules;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Difference {
  ArrayList<Evaluation> evaluation = new ArrayList<>();
  Object oldValue;
  Object newValue;

  public Difference(Evaluation evaluation, Object oldValue, Object newValue) {
    this.evaluation.add(evaluation);
    this.oldValue = oldValue;
    this.newValue = newValue;
  }

  public Difference(List<Evaluation> evaluation, Object oldValue, Object newValue) {
    this.evaluation.addAll(evaluation);
    this.oldValue = oldValue;
    this.newValue = newValue;
  }

  public Difference addEvaluation(Evaluation eval){
    this.evaluation.add(eval);
    return this;
  }

  @Override
  public String toString() {
    return "\nevaluation: " + Arrays.toString(evaluation.toArray()) + "\n"
        + "oldValue: " + oldValue + "\n"
        + "newValue: " + newValue;
  }

}
