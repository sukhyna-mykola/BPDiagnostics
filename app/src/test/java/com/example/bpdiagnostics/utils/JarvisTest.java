package com.example.bpdiagnostics.utils;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.PointValue;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by mykola on 11.06.17.
 */
public class JarvisTest {
    @Test
    public void search() throws Exception {
        List<PointValue> inputValues = new ArrayList<>();
        inputValues.add(new PointValue(2, 2));
        inputValues.add(new PointValue(3, 4));
        inputValues.add(new PointValue(3, 6));
        inputValues.add(new PointValue(4, 4));
        inputValues.add(new PointValue(5, 3));
        inputValues.add(new PointValue(6, 5));

        List<PointValue> expectedValues = new ArrayList<PointValue>();
        expectedValues.add(new PointValue(2, 2));
        expectedValues.add(new PointValue(5, 3));
        expectedValues.add(new PointValue(6, 5));
        expectedValues.add(new PointValue(3, 6));
        expectedValues.add(new PointValue(2, 2));

        List<PointValue> actualValues = new Jarvis().search(inputValues);

        assertThat(actualValues, is(expectedValues));
    }


}