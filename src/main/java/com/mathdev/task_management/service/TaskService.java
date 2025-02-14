package com.mathdev.task_management.service;

import com.mathdev.task_management.api.Priority;
import com.mathdev.task_management.api.Status;
import com.mathdev.task_management.api.TaskDto;
import com.mathdev.task_management.db.entity.TaskEntity;
import com.mathdev.task_management.db.repository.TaskRepository;
import com.mathdev.task_management.exception.TaskNotFoundExceptions;
import com.mathdev.task_management.mapper.TaskConvert;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private  final TaskRepository taskRepository;
    private final TaskConvert taskConvert;

    public TaskService(final TaskRepository taskRepository, final TaskConvert taskConvert) {
        this.taskRepository = taskRepository;
        this.taskConvert = taskConvert;
    }

    public void saveTask(final TaskDto taskDto){
        try {
            final TaskEntity taskEntity = taskConvert.convertTaskDtoToTaskEntity(taskDto);
            taskRepository.save(taskEntity);
        } catch (RuntimeException re){
            throw re;
        }
    }

    public TaskDto getTaskById(final UUID id) {
        final Optional<TaskEntity> optionalTaskEntity = taskRepository.findById(id);
        if (optionalTaskEntity.isPresent()) {
            return taskConvert.convertTaskEntityToTaskDto(optionalTaskEntity.get());
        } else {
            throw new TaskNotFoundExceptions("Task with id" + id + "not found");
        }
    }

    public void deleteTask(final UUID id) {
        taskRepository.deleteById(id);
    }

    public void updateTask(final TaskDto taskDto){
        try {
            final Optional<TaskEntity> optionalTaskEntity = taskRepository.findById(taskDto.getId());
            if (optionalTaskEntity.isPresent()) {
                TaskEntity taskEntity = optionalTaskEntity.get();
                taskEntity.setTitle(taskDto.getTitle());
                //taskEntity.setCreatedOn(taskDto.getCreatedOn());
                //taskEntity.setExpireOn(taskDto.getExpireOn());
                taskEntity.setPriority(taskDto.getPriority());
                taskEntity.setStatus(taskDto.getStatus());
                taskEntity.setDescription(taskDto.getDescription());
                //taskEntity.setUpdatedOn(taskDto.getUpdatedOn());
                taskRepository.save(taskEntity);
            } else {
                throw new TaskNotFoundExceptions("Task with id" + taskDto.getId() + "not found");
            }
        } catch (final RuntimeException re){
            throw re;
        }
    }

    public List<TaskDto> getTaskList () {
        return taskRepository.findAllByOrderByCreatedOnDesc()
                .stream()
                .map(taskConvert::convertTaskEntityToTaskDto)
                .collect(Collectors.toList());
    }
    public List<String> getPriorities() {
        return Arrays.asList(Priority.NORMAL.toString(), Priority.HIGH.toString(), Priority.LOW.toString());
    }
    public List<String> getStatus() {
        return Arrays.asList(Status.READY.toString(), Status.PROGRESS.toString(), Status.DONE.toString());
    }


}
