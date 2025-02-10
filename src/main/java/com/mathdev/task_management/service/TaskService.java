package com.mathdev.task_management.service;

import com.mathdev.task_management.api.Priority;
import com.mathdev.task_management.api.Status;
import com.mathdev.task_management.db.entity.TaskEntity;
import com.mathdev.task_management.db.repository.TaskRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class TaskService {

    private  final TaskRepository taskRepository;

    public TaskService(final TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    // Garante que a tarefa de exemplo seja criada ao iniciar a aplicação
    @PostConstruct
    private void initializeSampleTask() {
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setTitle("Apresentação para vaga - Gupy");
        taskEntity.setDescription("Realizar gravação de apresentação para vaga");
        taskEntity.setStatus(Status.READY);
        taskEntity.setPriority(Priority.HIGH);
        taskEntity.setUpdatedOn(Instant.now());
        taskEntity.setExpireOn(Instant.now());
        taskEntity.setCreatedOn(Instant.now());
        saveTask(taskEntity);
    }

    public void saveTask(final TaskEntity taskEntity){
        taskRepository.save(taskEntity);
    }

}
