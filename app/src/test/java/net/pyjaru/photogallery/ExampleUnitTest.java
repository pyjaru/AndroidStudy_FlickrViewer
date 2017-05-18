package net.pyjaru.photogallery;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 1);
    }

    @Test
    public void test_primitives() throws Exception {

    }

    @Test
    public void TestInteger() throws Exception {
        assertEquals(new Integer(null),new Integer(0)); // Failed. Occured Exception < java.lang.NumberFormatException : Null >
    }
}