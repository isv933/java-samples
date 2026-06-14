package org.isv.samples.exercises.parser;

import java.nio.channels.ReadableByteChannel;

public interface
ChannelDetector {
    boolean isMyChannel(ReadableByteChannel channel);
}
