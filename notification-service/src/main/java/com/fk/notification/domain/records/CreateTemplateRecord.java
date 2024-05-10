package com.fk.notification.domain.records;
import java.util.Set;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateTemplateRecord(
    @NotNull String userId,
    @NotNull String topic,
    @NotNull @NotEmpty @Size.List( {
        @Size(min = 5, message = "message should at least contain 5 characters"),
        @Size(max = 250, message = "message must not exceed the length of 250")
    }) String message,
    @NotNull @Size.List({
        @Size(min = 5, message = "message should at least contain 5 characters"),
        @Size(max = 15, message = "message must not exceed the length of 15")
    }) String title,
    @NotEmpty
    Set<RuleRecord> rules){
}
