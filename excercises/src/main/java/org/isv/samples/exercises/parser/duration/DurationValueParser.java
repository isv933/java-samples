package org.isv.samples.exercises.parser.duration;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.nio.ByteBuffer;
import java.time.Duration;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Optional;

public class DurationValueParser {
    private final ByteBuffer expressionBuffer;
    private final ParserState parserState = ParserState.builder().hasEqual(false).build();
    private DurationValueParser(int maxExpressionLength) {
        this.expressionBuffer = ByteBuffer.allocate(maxExpressionLength);
    }

    public static DurationValueParser of(int maxExpressionLength){
        return new DurationValueParser(maxExpressionLength);
    }

    @Builder
    @Getter
    @Setter
    private static class ParserState {
        private boolean startedName;
        private boolean hasEqual;
        private boolean startedDuration;
        private int equalPos;
        public void reset(){
            startedName = false;
            hasEqual = false;
            startedDuration = false;
            equalPos = 0;
        }

    }

    public Collection<DurationValue> parseDurations(ByteBuffer inputBuffer) {
        var result = new LinkedList<DurationValue>();

        while(inputBuffer.hasRemaining()) {
            var nextValue = inputBuffer.get();

            if (!isValidInput(nextValue)) {
                expressionBuffer.clear();
                parserState.reset();
                continue;
            }

            if (isSpace(nextValue)) {
                if (parserState.isStartedDuration()) {
                    tryParseDuration().ifPresent(result::add);
                }
                continue;
            }

            expressionBuffer.put(nextValue);
            if (nextValue == '=') {
                parserState.setEqualPos(expressionBuffer.position()-1);
                parserState.setHasEqual(true);
                continue;
            }

            if (isLetter(nextValue)) {
                if (parserState.isHasEqual() ) {
                    parserState.setStartedDuration(true);
                } else {
                    parserState.setStartedName(true);
                }
            }
       }

        if (parserState.isStartedDuration()){
            tryParseDuration().ifPresent(result::add);
        }

        return result;
    }

    private Optional<DurationValue> tryParseDuration(){
        try {
            return  Optional.of(DurationValue.builder()
                    .durationName(buildString(expressionBuffer.array(), 0, parserState.getEqualPos() - 1))
                    .durationValue(buldDuration(expressionBuffer.array(),
                            parserState.getEqualPos() + 1, expressionBuffer.limit()-1)).build());
        }
        catch(Exception ex){
            return Optional.empty();
        }
        finally {
            expressionBuffer.clear();
            parserState.reset();
        }
    }

    private Duration buldDuration(byte[] data, int start, int end){
        return Duration.parse(buildString(data, start, end));
    }

    private String buildString(byte[] data, int start, int end){
        var builder = new StringBuilder();
        for(var i = start; i <= end; i++) {
            builder.append((char) (data[i] & 0xff));
        }
        return builder.toString().trim();
    }

    private boolean isValidInput(byte nextValue){

        if (nextValue == '=') {
            return !parserState.isHasEqual();
        }

        if (isSpace(nextValue) && parserState.isStartedName() && !parserState.isHasEqual()) {
            return false;
        }

        return isSpace(nextValue) || isLetter(nextValue) || isDigit(nextValue);
    }

    private boolean isSpace(byte value){
        return value == ' ' || value== '\t' || value == '\n' || value == '\r';
    }

    private boolean isLetter(byte value) {
        return ((value >= 'a' && value <='z') || (value >= 'A' && value <='Z'));
    }

    private boolean isDigit(byte value) {
        return ((value >= '0' && value <='9'));
    }

}
