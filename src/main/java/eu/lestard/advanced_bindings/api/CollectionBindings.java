package eu.lestard.advanced_bindings.api;

import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * This class contains custom binding implementations for collections.
 *
 * @author manuel mauky
 */
public class CollectionBindings {

    /**
     * Creates a number binding that contains the sum of the numbers of the given observable list of numbers.
     *
     * @param numbers the observable list of numbers.
     * @return a number binding.
     */
    public static NumberBinding sum(final ObservableList<? extends Number> numbers) {
        return Bindings.createDoubleBinding(() -> numbers.stream().mapToDouble(Number::doubleValue).sum(), numbers);
    }

    /**
     * Creates an observable list that represents the concatenated source lists. All elements from the all source lists
     * will be contained in the new list. If there is a change in any of the source lists this change will also be done
     * in the concatenated list.
     *
     * The order of elements will be maintained. All elements of the first source list will be located before all elements of
     * the second list and so on. So if an element is added to the first list, this element will be located
     * between the old elements of the first list and the second list's elements.
     *
     *
     * **Hint:** *At the moment this observable list is implemented with {@link javafx.beans.InvalidationListener}s on
     * the source lists and by clearing and recreating the concatenated list on every change. This should be keeped in mind
     * when using a {@link javafx.collections.ListChangeListener} on the concatenated list as it will react multiple times
     * when a change is done to one of the source lists.
     *
     * This behaviour will likely be changed in the future as it means a performance limitation too.
     *
     *
     * @param lists a var-args array of observable lists.
     * @param <T> the generic type of the lists.
     * @return a new observable list representing the concatenation of the source lists.
     */
    @SafeVarargs
    public static <T> ObservableList<T> concat(ObservableList<T> ... lists) {
        ObservableList<T> result = FXCollections.observableArrayList();

        // todo: think about a smarter solution
        for (ObservableList<T> list : lists) {
            list.addListener((Observable observable) -> {
                result.clear();

                for (ObservableList<T> ts : lists) {
                    result.addAll(ts);
                }
            });
        }

        return result;
    }
}
