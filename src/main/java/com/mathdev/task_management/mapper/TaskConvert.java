package com.mathdev.task_management.mapper;

import com.mathdev.task_management.api.TaskDto;
import com.mathdev.task_management.db.entity.TaskEntity;
import org.springframework.stereotype.Component;

@Component
public class TaskConvert {

    public TaskEntity ConvertTaskDtoToTaskEntity(final TaskDto taskDto){
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setTitle(taskDto.getTitle());
        taskEntity.setCreatedOn(taskDto.getCreatedOn());
        taskEntity.setExpireOn(taskDto.getExpireOn());
        taskEntity.setPriority(taskDto.getPriority());
        taskEntity.setStatus(taskDto.getStatus());
        taskEntity.setDescription(taskDto.getDescription());
        taskEntity.setUpdatedOn(taskDto.getUpdatedOn());
        return taskEntity;
    }
    public TaskDto ConvertTaskEntityToTaskDto(final TaskEntity taskEntity){
        TaskDto taskDto = new TaskDto();
        taskDto.setTitle(taskEntity.getTitle());
        taskDto.setCreatedOn(taskEntity.getCreatedOn());
        taskDto.setExpireOn(taskEntity.getExpireOn());
        taskDto.setPriority(taskEntity.getPriority());
        taskDto.setStatus(taskEntity.getStatus());
        taskDto.setDescription(taskEntity.getDescription());
        taskDto.setUpdatedOn(taskEntity.getUpdatedOn());
        taskDto.setId(taskEntity.getId());
        return taskDto;
    }
}
