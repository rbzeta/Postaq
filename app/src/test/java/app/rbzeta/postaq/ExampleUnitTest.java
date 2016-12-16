package app.rbzeta.postaq;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        /*int a[] = {5,1,7,3};
        int b[] = {9,0,2,1};
        //assertEquals("app.rbzeta.postaq", appContext.getPackageName());
        int res[] = mergeArrays(a,b);

        for (int i = 0; i < res.length; i++) {
            System.out.println(res[i]);
        }*/
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

    public void pascalTriangle(int k) {
        for (int row = 0; row < k; row++) {
            for (int col = 0; col < row ; col++) {
                int cell = factorial(row)/(factorial(col) * (row - col));
                System.out.print(cell);
                System.out.print(" ");
            }
            System.out.println();
            
        }

    }

    public int factorial(int n){
        int result;

        if (n <= 1){
            return 1;
        }else{
            return n * factorial(n-1);
        }

    }
}