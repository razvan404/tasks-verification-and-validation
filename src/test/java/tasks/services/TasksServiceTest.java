package tasks.services;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import tasks.model.ArrayTaskList;
import tasks.model.Task;
import tasks.model.TasksOperations;

import java.util.Arrays;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TasksServiceTest {
    @Mock
    private ArrayTaskList tasks;

    @InjectMocks
    private TasksService service;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getObservableListTest() {
        Task task1 = new Task("Task 1", new Date());
        Task task2 = new Task("Task 2", new Date());
        Mockito.when(tasks.getAll()).thenReturn(Arrays.asList(task1, task2));

        ObservableList<Task> observableList = service.getObservableList();

        Mockito.verify(tasks, times(1)).getAll();

        assert this.tasks.getAll().size() == 2;

        Mockito.verify(tasks, times(2)).getAll();

        assertNotNull(observableList);
        assertEquals(2, observableList.size());
        assertTrue(observableList.contains(task1));
        assertTrue(observableList.contains(task2));
    }

    @Test
    public void filterTasksTest() {
        Date now = new Date();
        Task task1 = new Task("Task 1", now);
        task1.setActive(true);
        task1.setTime(now, new Date(now.getTime() + 3600 * 1000), 60);
        Task task2 = new Task("Task 2", now);
        task2.setActive(true);
        task2.setTime(now, new Date(now.getTime() + 3600 * 2 * 1000), 60);

        Mockito.when(tasks.getAll()).thenReturn(Arrays.asList(task1, task2));

        Date start = new Date(now.getTime() - 100000);  // 100 secunde
        Date end = new Date(now.getTime() + 100000);    // 100 secunde

        Iterable<Task> filteredTasks = service.filterTasks(start, end);
        Mockito.verify(tasks, times(1)).getAll();

        assertNotNull(filteredTasks);
        assertTrue(filteredTasks.iterator().hasNext());
        assertEquals(task1, filteredTasks.iterator().next());

        verify(tasks, times(1)).getAll();
    }
}