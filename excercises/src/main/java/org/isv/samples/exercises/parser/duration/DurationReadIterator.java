package org.isv.samples.exercises.parser.duration;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class DurationReadIterator implements Iterator<DurationValue> {
    private final ReadableByteChannel channel;
    private final ByteBuffer readBuffer = ByteBuffer.allocate(1024);
    private final DurationValueParser durationValueParser;
    private final List<DurationValue> parsedValues = new LinkedList<>();

    public static DurationReadIterator of(ReadableByteChannel channel, int maxExpressionLength){
        return new DurationReadIterator(channel, maxExpressionLength);
    }

    private DurationReadIterator(ReadableByteChannel channel, int maxExpressionLength){
        this.channel = channel;
        this.durationValueParser = DurationValueParser.of(maxExpressionLength);
    }

    @Override
    public boolean hasNext() {
        return !parsedValues.isEmpty() || readNext();
    }

    @Override
    public DurationValue next() {
        if (parsedValues.isEmpty() && !readNext()) {
            throw new RuntimeException("No more values");
        }

        return parsedValues.remove(0);
    }

    private boolean readNext() {
        if (channel.isOpen()) {
            return false;
        }
        try {
            while ( parsedValues.isEmpty() || channel.read(readBuffer) > 0) {
                readBuffer.flip();
                parsedValues.addAll(durationValueParser.parseDurations(readBuffer));
                readBuffer.clear();
            }
        }
        catch (IOException e){
            throw new RuntimeException(e);
        }

        return true;
    }
}
