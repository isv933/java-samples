package org.isv.samples.exercises.parser.duration;

import org.isv.samples.exercises.parser.ChannelParser;

import java.nio.channels.ReadableByteChannel;
import java.util.Iterator;

public class DurationsParser implements ChannelParser<DurationValue> {
    @Override
    public Iterator<DurationValue> parse(ReadableByteChannel channel) {
        return null;
    }
}
