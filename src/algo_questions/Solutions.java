package algo_questions;

/**
 * Class contains all solutions to the algo questions for this project.
 * @author Dor Messica.
 */
public class Solutions {
    /**
     * Method computing the maximal amount of tasks out of n tasks that can be completed with m time slots.
     * A task can only be completed in a time slot if the length of the time slot is grater than the no. of
     * hours needed to complete the task.
     * @param tasks array of integers of length n. tasks[i] is the time in hours required to complete task i.
     * @param timeSlots array of integers of length m. timeSlots[i] is the length in hours of the slot i.
     * @return maximal amount of tasks that can be completed
     */
    public static int alotStudyTime(int[] tasks, int[] timeSlots) {
        int tasksCompleted = 0;
        int currentTimeSlot = timeSlots.length - 1;
        java.util.Arrays.sort(tasks);
        java.util.Arrays.sort(timeSlots);
        for(int i = tasks.length - 1; i >= 0; i--) {
            if(currentTimeSlot == -1) {
                break;
            }
            if(tasks[i] <= timeSlots[currentTimeSlot]) {
                tasksCompleted++;
                currentTimeSlot--;
            }
        }
        return tasksCompleted;
    }

    /**
     * Method computing the nim amount of leaps a frog needs to jump across n waterlily leaves,
     * from leaf 1 to leaf n. The leaves vary in size and how stable they are, so some leaves allow larger
     * leaps than others. leapNum[i] is an integer telling you how many leaves ahead you can jump from leaf i.
     * If leapNum[3]=4, the frog can jump from leaf 3, and land on any of the leaves 4, 5, 6 or 7.
     * @param leapNum array of ints. leapNum[i] is how many leaves ahead you can jump from leaf i.
     * @return minimal no. of leaps to last leaf.
     */
    public static int minLeap(int[] leapNum) {
        int minJumps = 0, currentLeaf = 0, maxVal, maxValLeaf;
        if(leapNum.length <= 1) { return 0; }
        while(currentLeaf + leapNum[currentLeaf] < leapNum.length-1) {
            maxVal = 0; maxValLeaf = 0;
            for(int i = 1; i <= leapNum[currentLeaf]; i++) {
                if(leapNum[currentLeaf+i] >= maxVal) {
                    maxValLeaf = currentLeaf+i;
                    maxVal = leapNum[maxValLeaf];
                }
                if((currentLeaf+i) + leapNum[currentLeaf+i] >= leapNum.length-1) { return minJumps+2; }
            }
            currentLeaf = maxValLeaf;
            minJumps++;
        }
        return minJumps + 1;
    }

    /**
     * Method computing the solution to the following problem: A boy is filling the water trough for
     * his father's cows in their village. The trough holds n liters of water. With every trip to the
     * village well, he can return using either the 2 bucket yoke, or simply with a single bucket.
     * A bucket holds 1 liter. In how many different ways can he fill the water trough? n can be assumed to
     * be greater or equal to 0, less than or equal to 48.
     * @param n Liters of water the trough holds.
     * @return Valid output of algorithm.
     */
    public static int bucketWalk(int n) {
        int[] littersArray = new int[n+2];
        littersArray[0] = 1;
        littersArray[1] = 1;

        for (int i = 2; i <= n; i++) {
            littersArray[i] = littersArray[i-1] + littersArray[i-2];
        }

        return littersArray[n];
    }

    /**
     * Method computing the solution to the following problem: Given an integer n, return the number of
     * structurally unique BST's (binary search trees) which has exactly n nodes of unique values from 1 to n.
     * You can assume n is at least 1 and at most 19.
     * (Definition: two trees S and T are structurally distinct
     * if one can not be obtained from the other by renaming of the nodes).
     * @param n Number to calculate.
     * @return Valid output of algorithm.
     */
    public static int numTrees(int n) {
        int[] distinctBST = new int[n+2];
        distinctBST[0] = 1;
        distinctBST[1] = 1;

        for (int i = 2; i <= n; i++) {
            distinctBST[i] = 0;
            for (int j = 0; j < i; j++) {
                distinctBST[i] += distinctBST[j] * distinctBST[i - j - 1];
            }
        }
        return distinctBST[n];
    }
}
