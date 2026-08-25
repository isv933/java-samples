// Дана закодированная строка вида: 2[aa]2[bb]b которую нужно раскодировать в строку
// Считаем что формат правильный(нет лишних пробелов, скобок хватает) и на ошибки проверять не нужно
// 2[aa]2[bb]c  -> aabbc
// 2[aa2bb]]c   -> aabbbbaabbbbcc

package org.isv.samples.algorithms;

import java.util.ArrayDeque;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class EncodeStringUsingStack {

    record RepeatString (int count, String data){  }
    record ParseState(RepeatString data, int pos) {}

    public static String encodeString(String input) {
        var queue = new ArrayDeque<RepeatString>();
        var resultString = new StringBuilder();
        var left = 0;

        while (left < input.length()) {
            var leftChar = input.charAt(left);

            if (Character.isDigit(leftChar)) {
                var parseState = parseRepeatString(input, left);
                queue.addFirst(parseState.data());
                left = parseState.pos();
            } else if (leftChar == ']') {
                var latestData = queue.pollFirst();
                assert(latestData!=null);

                if (!queue.isEmpty()){
                    var prevData = queue.pollFirst();
                    queue.addFirst(new RepeatString( prevData.count(),
                    Stream.generate(latestData::data)
                            .limit(latestData.count()).reduce(prevData.data(),
                                    (first,second)->first+second)));

                } else {

                    resultString.append(Stream.generate(latestData::data)
                            .limit(latestData.count()).reduce("",
                                    (first,second)->first+second));
                }

                left++;

            } else {
                resultString.append(leftChar);
                left++;
            }

        }

        return resultString.toString();
    }


    private static ParseState parseRepeatString(String input, int left) {
        var pos = left;

        var countString = parseUntil(input,pos,Character::isDigit);

        pos+=countString.length();
        var dataString = parseUntil(input,pos+1,(ch)->
                ch!=']' && !Character.isDigit(ch));

        pos+=dataString.length()+1;

        return new ParseState(new RepeatString(Integer.parseInt(countString),
                dataString),pos);

    }


    private static String parseUntil(String input, int left,
                                        Predicate<Character> isContinue){
        var right = left;

        while(right < input.length()){
            if (!isContinue.test(input.charAt(right))) {
                break;
            }

            right++;
        }

        return input.substring(left,right);
    }


    public static void test(){
        System.out.println(encodeString("2[a]3[b]"));
        System.out.println(encodeString("2[a]3[b]cde"));
        System.out.println(encodeString("2[a3[b]]cde"));
        System.out.println(encodeString("2[ae3[b2[rt]]]cde"));
    }



}