package com.fk.notification.domain.rules;

public enum Evaluation {
  GREATER {
    @Override
    public String toString() {
      return "Greater";
    }
  },
  UNEQUAL {
    @Override
    public String toString() {
      return "Unequal";
    }
  },

  LESS {
    @Override
    public String toString() {
      return "Less";
    }
  },

  UPDATE {
    @Override
    public String toString() {
      return "Update";
    }
  },
  ADD {
    @Override
    public String toString() {
      return "Add";
    }
  },

  REMOVE {
    @Override
    public String toString() {
      return "Remove";
    }
  }
}
