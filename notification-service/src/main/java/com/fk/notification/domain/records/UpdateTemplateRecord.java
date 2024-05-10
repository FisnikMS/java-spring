package com.fk.notification.domain.records;

import java.util.HashSet;

import org.springframework.data.mongodb.core.mapping.Unwrapped.Empty;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateTemplateRecord(
    @NotNull String id,
    @Empty String topic,
    String userId,
    @Size.List( {
        @Size(min = 5, message = "message should at least contain 5 characters"),
        @Size(max = 250, message = "message must not exceed the length of 250")
    }) String message,
    @Size.List({
        @Size(min = 5, message = "message should at least contain 5 characters"),
        @Size(max = 15, message = "message must not exceed the length of 15")
    }) String title,
    HashSet<RuleRecord> rules
    ){
}
