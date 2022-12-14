package de.dhia.todo;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import de.dhia.todo.enums.Status;
import de.dhia.todo.mapper.ToDoMapper;
import de.dhia.todo.repository.ToDoRepository;
import de.dhia.todo.service.ToDoService;
import de.dhia.todo.dto.ToDoRequest;
import de.dhia.todo.entity.ToDo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ToDoService.class})
class ToDoApplicationTests {

    @Autowired
    private ToDoService toDoService;

    @MockBean
    private ToDoMapper toDoMapper;

    @MockBean
    private ToDoRepository toDoRepository;

    private static final UUID ID = UUID.fromString("d27d54bc-3aa2-4a63-a746-41349e9cd3cc");
    private static final String DESCRIPTION = "Go to Pray";
    private static final LocalDateTime DUE_AT = LocalDateTime.now();
    private static final Status STATUS_NOT_DONE = Status.NOT_DONE;
    private static final LocalDateTime CREATE_ITEM_NOW = LocalDateTime.from(LocalDateTime.now());

    /**
     * {@link ToDoService#addItem(ToDoRequest)} }
     */
    @DisplayName("add New Item Test When The Response As Excpected")
    @Test
    public void addNewItemTestWhenTheResponseAsExcpected() {
        //Given
        ToDoRequest toDoRequest = new ToDoRequest(DESCRIPTION, DUE_AT);
        ToDo toDo =
                ToDo.builder().description(DESCRIPTION).status(STATUS_NOT_DONE).createdAt(CREATE_ITEM_NOW).dueAt(DUE_AT)
                    .doneAt(null).build();

        //When
        when(toDoMapper.toDoRequestIntializeToToDo(toDoRequest)).thenReturn(toDo);
        when(toDoRepository.save(toDo)).thenReturn(toDo);


        //Then
        assertAll(() -> {
            assertNotNull(toDo);
            assertEquals(toDo, toDoService.addItem(toDoRequest));
        });
    }

    /**
     * {@link ToDoService#addItem(ToDoRequest)} }
     */
    @DisplayName("test Throws Exception If ToDo Request Is Null When Create An Item")
    @Test
    void testThrowsExceptionIfToDoRequestIsNullWhenCreateAnItem() {
        ToDoRequest toDoRequest = null;

        //Test
        assertThrows(ResponseStatusException.class,
                () -> toDoService.addItem(toDoRequest));
    }

    /**
     * {@link ToDoService#getSpecificItem(UUID) }
     */
    @DisplayName("test Get Item By Id When Is As Excpected")
    @Test
    void testGetItemByIdWhenIsAsExcpected() {
        //Given
        ToDo toDo =
                ToDo.builder().description(DESCRIPTION).status(Status.DONE).createdAt(CREATE_ITEM_NOW).dueAt(DUE_AT)
                    .doneAt(LocalDateTime.now()).build();

        //When
        when(toDoRepository.findById(ID)).thenReturn(Optional.ofNullable(toDo));


        //Then
        assertAll(() -> {
            assertNotNull(toDo);
            assertEquals(toDo, toDoService.getSpecificItem(ID));
        });

    }

    /**
     * {@link ToDoService#getSpecificItem(UUID) }
     */
    @DisplayName("test Throws Exception If Item By Id Not Found")
    @Test
    void testThrowsExceptionIfItemByIdNotFound() {
        //Mock
        Mockito.when(toDoRepository.findById(ID))
               .thenReturn(Optional.empty());

        //Then
        assertThrows(ResponseStatusException.class,
                () -> toDoService.getSpecificItem(ID));
    }

}
