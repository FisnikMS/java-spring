package com.fk.notification.domain.records;
import java.util.Set;

import jakarta.validation.constraints.NotEmpty;

public record RuleRecord(
    //@TODO Validation: either field or exactValue, not both.
    String field,
    String exactValue,
    //@TODO Validation: rules of type Evaluation
    @NotEmpty Set<String> conditions) {
}
