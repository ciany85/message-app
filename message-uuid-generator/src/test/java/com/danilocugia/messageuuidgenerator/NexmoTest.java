package com.danilocugia.messageuuidgenerator;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
public class NexmoTest {

    @Test
    public void testMaxNumber() {
        StringBuilder finalIntAsString = new StringBuilder();
        char[] chars = Integer.toString(123).toCharArray();
        while (chars.length > 0) {
            int indexMaxInt = getIndexMaxInt(chars);
            finalIntAsString.append(chars[indexMaxInt]);
            StringBuilder newCharArraySB = new StringBuilder();
            for(int i =0; i< chars.length; i++) {
                if (i != indexMaxInt) {
                    char c = chars[i];
                    newCharArraySB.append(c);
                }
            }
            chars = newCharArraySB.toString().toCharArray();

        }
        int i = Integer.parseInt(finalIntAsString.toString());
        assertTrue(i == 321);
    }

    @Test
    @Ignore
    public void testSiblings() {
        char[] chars = Integer.toString(100).toCharArray();
        List<Integer> permutes = permute(chars, 0);
        for (Integer permute: permutes) {
            System.out.println(permute);
        }
        assertEquals(1, permutes.size());
    }


    public List<Integer> permute(char[] chars, int index) {
        List<Integer> listOfPermutes = new ArrayList<>();

        for(int i = index; i < chars.length; i++) {
            chars = swap(chars, i, index).toCharArray();
            listOfPermutes.add(Integer.parseInt(new String(chars)));
            List<Integer> permutes = permute(chars, index + 1);
            for (Integer permute: permutes) {
                if (!listOfPermutes.contains(permute) && String.valueOf(permute).length() == chars.length) {
                    listOfPermutes.add(permute);
                }
            }
            chars = swap(chars, index, i).toCharArray();
        }

        if (index == chars.length -1) {
            int permute = Integer.parseInt(new String(chars));
            if (!listOfPermutes.contains(permute) && String.valueOf(permute).length() == chars.length) {
                listOfPermutes.add(permute);
            }
        }

        return listOfPermutes;
    }

    public String swap(char[] chars, int i1, int i2) {
        char c1 = chars[i1];
        char c2 = chars[i2];
        chars[i1] = c2;
        chars[i2] = c1;
        return new String(chars);
    }


    public int getIndexMaxInt(char[] chars) {
        Integer max = null;
        Integer index = null;
        for (int i =0; i< chars.length; i++) {
            char c = chars[i];
            int numericValue = Character.getNumericValue(c);
            if (max == null || numericValue > max) {
                max = numericValue;
                index = i;
            }
        }
        return index;
    }
}
