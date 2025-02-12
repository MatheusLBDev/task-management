package com.mathdev.task_management.service;

import com.mathdev.task_management.api.Priority;
import com.mathdev.task_management.api.Status;
import com.mathdev.task_management.api.TaskDto;
import com.mathdev.task_management.db.entity.TaskEntity;
import com.mathdev.task_management.db.repository.TaskRepository;
import com.mathdev.task_management.mapper.TaskConvert;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class TaskService {

    private  final TaskRepository taskRepository;
    private final TaskConvert taskConvert;

    public TaskService(final TaskRepository taskRepository, final TaskConvert taskConvert) {
        this.taskRepository = taskRepository;
        this.taskConvert = taskConvert;
    }

    // Garante que a tarefa de exemplo seja criada ao iniciar a aplicação
    @PostConstruct
    private void initializeSampleTask() {
        TaskDto taskDto = new TaskDto();
        taskDto.setTitle("Histórico para vaga");
        taskDto.setDescription("Realizar o envio do histórico para vaga e assistir o video para dar continuação no processo seletivo");
        taskDto.setStatus(Status.READY);
        taskDto.setPriority(Priority.HIGH);
        taskDto.setUpdatedOn(Instant.now());
        taskDto.setExpireOn(Instant.now());
        taskDto.setCreatedOn(Instant.now());
        saveTask(taskDto);
    }

    public void saveTask(final TaskDto taskDto){
        try {
            final TaskEntity taskEntity = taskConvert.convert(taskDto);
            taskRepository.save(taskEntity);
        } catch (RuntimeException re){
            throw re;
        }
    }

}
