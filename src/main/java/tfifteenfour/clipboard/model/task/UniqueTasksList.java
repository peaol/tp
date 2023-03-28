package tfifteenfour.clipboard.model.task;

import static java.util.Objects.requireNonNull;
import static tfifteenfour.clipboard.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Iterator;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import tfifteenfour.clipboard.model.task.exceptions.DuplicateTaskException;
import tfifteenfour.clipboard.model.task.exceptions.TaskNotFoundException;

/**
 * A list of tasks that enforces uniqueness between its elements and does not allow nulls.
 * A session is considered unique by comparing using {@code Task#isSameTask(Task)}. As such,
 * adding and updating of sessions uses Task#isSameTask(Task) for equality so as to ensure that
 * the task being added or updated is unique in terms of identity in the UniqueTasksList. However,
 * the removal of a session uses Task#equals(Object) so as to ensure that the session with exactly
 * the same fields will be removed.
 * Supports a minimal set of list operations.
 *
 * @see Task#isSameTask(Task)
 */
public class UniqueTasksList implements Iterable<Task> {

    private final ObservableList<Task> internalList = FXCollections.observableArrayList();
    private final ObservableList<Task> internalUnmodifiableList =
            FXCollections.unmodifiableObservableList(internalList);
    private final FilteredList<Task> filteredTasks = new FilteredList<>(internalList);

    /**
     * Returns true if the list contains an equivalent Task as the given argument.
     */
    public boolean contains(Task toCheck) {
        requireNonNull(toCheck);
        return internalList.stream().anyMatch(toCheck::isSameTask);
    }

    /**
     * Adds a Task to the list.
     * The Task must not already exist in the list.
     */
    public void add(Task toAdd) {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicateTaskException();
        }
        internalList.add(toAdd);
    }

    /**
     * Replaces the Task {@code target} in the list with {@code editedTask}.
     * {@code target} must exist in the list.
     * The Task identity of {@code editedTask} must not be the same as another existing Task in the list.
     */
    public void setTask(Task target, Task editedTask) {
        requireAllNonNull(target, editedTask);

        int index = internalList.indexOf(target);
        if (index == -1) {
            throw new TaskNotFoundException();
        }

        if (!target.isSameTask(editedTask) && contains(editedTask)) {
            throw new DuplicateTaskException();
        }

        internalList.set(index, editedTask);
    }

    /**
     * Removes the equivalent task from the list.
     * The task must exist in the list.
     */
    public void remove(Task toRemove) {
        requireNonNull(toRemove);
        if (!internalList.remove(toRemove)) {
            throw new TaskNotFoundException();
        }
    }

    public void setTasks(UniqueTasksList replacement) {
        requireNonNull(replacement);
        internalList.setAll(replacement.internalList);
    }

    /**
     * Replaces the contents of this list with {@code Tasks}.
     * {@code Tasks} must not contain duplicate tasks.
     */
    public void setSessions(List<Task> tasks) {
        requireAllNonNull(tasks);
        if (!tasksAreUnique(tasks)) {
            throw new DuplicateTaskException();
        }

        internalList.setAll(tasks);
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<Task> asUnmodifiableObservableList() {
        return internalUnmodifiableList;
    }

    public ObservableList<Task> asUnmodifiableFilteredList() {
        return FXCollections.unmodifiableObservableList(filteredTasks);
    }

    /**
     * Returns the backing list as a modifiable {@code ObservableList}.
     */
    public ObservableList<Task> asModifiableObservableList() {
        return internalList;
    }

    @Override
    public Iterator<Task> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof UniqueTasksList // instanceof handles nulls
                && internalList.equals(((UniqueTasksList) other).internalList));
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }

    /**
     * Returns true if {@code Sessions} contains only unique Sessions.
     */
    private boolean tasksAreUnique(List<Task> tasks) {
        for (int i = 0; i < tasks.size() - 1; i++) {
            for (int j = i + 1; j < tasks.size(); j++) {
                if (tasks.get(i).isSameTask(tasks.get(j))) {
                    return false;
                }
            }
        }
        return true;
    }
}
