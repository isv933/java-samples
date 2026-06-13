package org.isv.samples.exercises.parser;

import java.nio.channels.ReadableByteChannel;
import java.util.Iterator;

public interface ChannelParser<T> {
    Iterator<T> parse(ReadableByteChannel channel);
}
