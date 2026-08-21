// Дана закодированная строка вида: 2[aa]2[bb]b которую нужно раскодировать в строку
// Считаем что формат правильный(нет лишних пробелов, скобок хватает) и на ошибки проверять не нужно
// 2[aa]2[bb]c  -> aabbc
// 2[aa2bb]]c   -> aabbbbaabbbbcc


package org.isv.samples.algorithms;

import java.util.function.Predicate;
import java.util.stream.Stream;

public class EncodeStringUsingRecursion {

    record ParseState (int pos, String data) {
        public int asInt() {
            return Integer.parseInt(data());
        }
    }

    public static String encodeString(String input) {
        var result = new StringBuilder();

        var left = 0;
        while (left < input.length()) {

            if (Character.isDigit(input.charAt(left))) {
                var resultData = parseEncodedData(input,left);
                result.append(resultData.data());
                left = resultData.pos();
            } else {
                result.append(input.charAt(left));
                left++;
            }
        }

        return result.toString();
    }

    private static ParseState parseEncodedData(String input, int pos) {
        if (pos >=input.length()) {
            return new ParseState(pos,"");
        }

        var count = parseData(input, pos, Character::isDigit);
        var data  = parseData(input, count.pos()+1,
                ch-> ch!='[' && ch !=']' && !Character.isDigit(ch));


        if (Character.isDigit(input.charAt(data.pos()))) {
            var childData = parseEncodedData(input, data.pos());
            return new ParseState(childData.pos()+1,
                    Stream.generate(
                            data::data)
                            .limit(count.asInt()).reduce("",
                                        (x,y)->x+y+ childData.data()));
            }

        // else next char is [

        return new ParseState(data.pos()+1,
                Stream.generate(data::data).limit(count.asInt())
                        .reduce("", (x,y)->x+y));
    }

    private static ParseState parseData(String data, int startPos,
                                            Predicate<Character> isContinue) {
        var right = startPos;

        while(right < data.length() &&  isContinue.test(data.charAt(right))){
            right++;
        }

        return  new ParseState(right, data.substring(startPos, right));
    }

    public static void test(){
        System.out.println(encodeString("2[a]3[b]"));
        System.out.println(encodeString("2[a]3[b]cde"));
        System.out.println(encodeString("2[a3[b]]cde"));
        System.out.println(encodeString("2[ae3[b2[rt]]]cde"));
    }

}

