package org.isv.samples.exercises.parser.duration;

import lombok.Builder;
import lombok.Data;

import java.time.Duration;

@Data
@Builder
public class DurationValue {
    private final String durationName;
    private final Duration durationValue;
}
