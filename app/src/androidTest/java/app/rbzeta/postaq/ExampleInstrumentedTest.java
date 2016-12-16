package app.rbzeta.postaq;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        int a[] = {5,1,7,3};
        int b[] = {9,0,2,1};
        //assertEquals("app.rbzeta.postaq", appContext.getPackageName());
        int res[] = mergeArrays(a,b);

        for (int i = 0; i < res.length; i++) {
            System.out.println(res[i]);
        }

    }

    public int[] mergeArrays(int[] a, int[] b) {
        int arrSize = a.length + b.length;
        int[] result = new int[arrSize];

        int count;
        for (count = 0; count < a.length; count++) {
            result[count] = a[count];
        }

        for (int i = count; i < arrSize; i++) {
            result[i] = b[i];
        }

        Arrays.sort(result);

        return result;
    }
}
