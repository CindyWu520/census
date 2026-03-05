import java.io.Closeable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Implement the two methods below. We expect this class to be stateless and thread safe.
 */
public class Census {
    /**
     * Number of cores in the current machine.
     */
    private static final int CORES = Runtime.getRuntime().availableProcessors();

    /**
     * Output format expected by our tests.
     */
    public static final String OUTPUT_FORMAT = "%d:%d=%d"; // Position:Age=Total

    /**
     * Factory for iterators.
     */
    private final Function<String, Census.AgeInputIterator> iteratorFactory;

    /**
     * Creates a new Census calculator.
     *
     * @param iteratorFactory factory for the iterators.
     */
    public Census(Function<String, Census.AgeInputIterator> iteratorFactory) {
        this.iteratorFactory = iteratorFactory;
    }

    /**
     * Given one region name, call {@link #iteratorFactory} to get an iterator for this region and return
     * the 3 most common ages in the format specified by {@link #OUTPUT_FORMAT}.
     */
    public String[] top3Ages(String region) {
        Map<Integer, Integer> ageCountMap = getCountAges(region);
        List<Map.Entry<Integer, Integer>> top3List = findTop3Ages(ageCountMap);
        throw new UnsupportedOperationException();
    }

    private List<Map.Entry<Integer, Integer>> findTop3Ages(Map<Integer, Integer> ageCountMap) {
        // sort by count DESC, then tie-break by age
        List<Map.Entry<Integer, Integer>> sortedList = new ArrayList<>(ageCountMap.entrySet());
        sortedList.sort(Map.Entry.<Integer, Integer>comparingByValue(Comparator.reverseOrder())
                .thenComparing(Map.Entry.comparingByKey()));

        List<Map.Entry<Integer, Integer>> top3List = new ArrayList<>();
        // rank position: 0:1st, 1:2nd, 2:3rd
        int position = 0;
        // start from the highest count
        int currentCount = sortedList.isEmpty() ? 0 : sortedList.get(0).getValue();
        for (Map.Entry<Integer, Integer> entry : sortedList) {
            if (position < 2 & entry.getValue().equals(currentCount)) {
                // same count, same position
                top3List.add(entry);
            } else if (position < 2) {
                // lower count, lower position
                top3List.add(entry);
                position++;
                currentCount = entry.getValue();
            }
        }
        return top3List;
    }

    /**
     * calculate the age and count for a single region
     *
     * @param region a single region
     * @return return a map of age(key) and count(value)
     */
    private Map<Integer, Integer> getCountAges(String region) {
        Map<Integer, Integer> countAge = new HashMap<>();
        try (AgeInputIterator iterator = iteratorFactory.apply(region)) {
            while (iterator.hasNext()) {
                Integer age = iterator.next();
                if (countAge.containsKey(age)) {
                    countAge.put(age, countAge.get(age) + 1);
                } else {
                    countAge.put(age, 1);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to iterate region: " + region, e);
        }
        return countAge;
    }

    /**
     * Given a list of region names, call {@link #iteratorFactory} to get an iterator for each region and return
     * the 3 most common ages across all regions in the format specified by {@link #OUTPUT_FORMAT}.
     * We expect you to make use of all cores in the machine, specified by {@link #CORES).
     */
    public String[] top3Ages(List<String> regionNames) {

//        In the example below, the top three are ages 10, 15 and 12
//        return new String[]{
//                String.format(OUTPUT_FORMAT, 1, 10, 38),
//                String.format(OUTPUT_FORMAT, 2, 15, 35),
//                String.format(OUTPUT_FORMAT, 3, 12, 30)
//        };

        throw new UnsupportedOperationException();
    }


    /**
     * Implementations of this interface will return ages on call to {@link Iterator#next()}. They may open resources
     * when being instantiated created.
     */
    public interface AgeInputIterator extends Iterator<Integer>, Closeable {
    }
}