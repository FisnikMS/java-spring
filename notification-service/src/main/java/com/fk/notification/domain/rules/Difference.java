package com.fk.notification.domain.rules;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Difference {
  private ArrayList<Evaluation> evaluation = new ArrayList<>();
  private Object oldValue;
  private Object newValue;

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

}
