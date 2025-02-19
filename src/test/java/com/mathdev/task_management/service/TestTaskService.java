package com.mathdev.task_management.service;

import com.mathdev.task_management.api.Priority;
import com.mathdev.task_management.api.Status;
import com.mathdev.task_management.api.TaskDto;
import com.mathdev.task_management.db.entity.TaskEntity;
import com.mathdev.task_management.db.repository.TaskRepository;
import com.mathdev.task_management.mapper.TaskConvert;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class TestTaskService {
    @Mock
    private TaskRepository taskRepository;
    @Mock
    private TaskConvert taskConvert;
    @InjectMocks
    private  TaskService taskService;

    public TestTaskService() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSaveTask(){
        final TaskDto taskDto = new TaskDto();
        final TaskEntity taskEntity = new TaskEntity();


        when(taskConvert.convertTaskDtoToTaskEntity(taskDto)).thenReturn(taskEntity);

        taskService.saveTask(taskDto);

        verify(taskConvert, times(1)).convertTaskDtoToTaskEntity(taskDto);
        verify(taskRepository, times(1)).save(taskEntity);

    }

    @Test
    public void testGetTaskById(){
        UUID id = UUID.randomUUID();
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setId(id);

        // Mock do repositório para retornar a tarefa
        when(taskRepository.findById(id)).thenReturn(Optional.of(taskEntity));

        TaskDto taskDto = new TaskDto();
        taskDto.setId(id);  // Garantir que o ID seja configurado corretamente

        // Mock da conversão
        when(taskConvert.convertTaskEntityToTaskDto(taskEntity)).thenReturn(taskDto);

        // Executar o metodo que estamos testando
        TaskDto taskDtoResult = taskService.getTaskById(id);

        // Verificar se o resultado não é nulo e se os objetos são iguais
        assertNotNull(taskDtoResult);
        assertEquals(taskDto, taskDtoResult);

        // Verificar se os mocks foram chamados corretamente
        verify(taskRepository, times(1)).findById(id);
        verify(taskConvert, times(1)).convertTaskEntityToTaskDto(taskEntity);
    }

    @Test
    public void testDeleteTask() {
        UUID id = UUID.randomUUID();
        taskService.deleteTask(id);
        verify(taskRepository, times(1)).deleteById(id);
    }

    @Test
    public void testUpdateTask() {
        TaskDto taskDto = new TaskDto();
        taskDto.setTitle("Teste");
        String date = Instant.now().toString();
        //taskDto.setCreatedOn(date);
        taskDto.setExpireOn(date);
        taskDto.setPriority(String.valueOf(Priority.High));
        taskDto.setStatus(Status.Ready);
        taskDto.setDescription("Teste");
        //taskDto.setUpdatedOn(date);
        UUID id = UUID.randomUUID();
        taskDto.setId(id);

        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setId(taskDto.getId());
        Instant expireOnInstant = Instant.now();
        when(taskConvert.convertStringToInstant(date)).thenReturn(expireOnInstant);
        taskEntity.setExpireOn(expireOnInstant);
        when(taskRepository.findById(taskDto.getId())).thenReturn(Optional.of(taskEntity));

        taskService.updateTask(taskDto);

        verify(taskRepository,times(1)).findById(taskDto.getId());
        verify(taskRepository,times(1)).save(taskEntity);
        verify(taskConvert, times(1)).convertStringToInstant(date);

        assertEquals("Teste", taskEntity.getTitle());
        assertEquals("Teste", taskEntity.getDescription());
        //assertEquals(date, taskEntity.getCreatedOn());
        assertEquals(expireOnInstant, taskEntity.getExpireOn());
        //assertEquals(date, taskEntity.getUpdatedOn());
        assertEquals(Priority.High, taskEntity.getPriority());
        assertEquals(Status.Ready, taskEntity.getStatus());
        assertEquals(id, taskEntity.getId());

    }

    @Test
    public void testGetTaskList(){
        List<TaskEntity> taskEntityList= Arrays.asList(new TaskEntity(), new TaskEntity(), new TaskEntity());
        when(taskRepository.findAllByOrderByCreatedOnDesc()).thenReturn(taskEntityList);

        TaskDto taskDto = new TaskDto();
        TaskEntity taskEntity = new TaskEntity();
        when(taskConvert.convertTaskEntityToTaskDto(taskEntity)).thenReturn(taskDto);

        List<TaskDto> taskDtosList = taskService.getTaskList();

        assertNotNull(taskDtosList);
        assertEquals(taskEntityList.size(), taskDtosList.size());
        verify(taskRepository, times(1)).findAllByOrderByCreatedOnDesc();
        verify(taskConvert, times(taskEntityList.size())).convertTaskEntityToTaskDto((TaskEntity) any());



    }




}
