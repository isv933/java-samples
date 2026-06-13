package  org.isv.samples.exercises.parser.duration;

import org.isv.samples.exercises.parser.duration.DurationValueParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

public class DurationValueParserTest {

    @Test
    public void onValidDurationInText() {

        var res = DurationValueParser.of(100).parseDurations(
                ByteBuffer.wrap("hello world duration=PT20S newduration=PT30S".getBytes()));

        Assertions.assertFalse(res.isEmpty());

   }

}
