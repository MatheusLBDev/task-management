package com.mathdev.task_management.mapper;

import com.mathdev.task_management.api.Priority;
import com.mathdev.task_management.api.Status;
import com.mathdev.task_management.api.TaskDto;
import com.mathdev.task_management.db.entity.TaskEntity;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

@Component
public class TaskConvert {

    public TaskEntity convertTaskDtoToTaskEntity(final TaskDto taskDto){
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setTitle(taskDto.getTitle());
        taskEntity.setExpireOn(convertStringToInstant(taskDto.getExpireOn()));
        taskEntity.setPriority(taskDto.getPriority());
        taskEntity.setStatus(taskDto.getStatus());
        taskEntity.setDescription(taskDto.getDescription());
        return taskEntity;
    }

    public TaskDto convertTaskEntityToTaskDto(final TaskEntity taskEntity){
        TaskDto taskDto = new TaskDto();
        taskDto.setTitle(taskEntity.getTitle());
        taskDto.setExpireOn(convertInstantToString(taskEntity.getExpireOn()));
        taskDto.setPriority(taskEntity.getPriority());
        taskDto.setStatus(taskEntity.getStatus());
        taskDto.setDescription(taskEntity.getDescription());
        taskDto.setCreatedOn(convertInstantToString(taskEntity.getCreatedOn()));
        taskDto.setUpdatedOn(convertInstantToString(taskEntity.getUpdatedOn()));
        taskDto.setId(taskEntity.getId());
        taskDto.setStatusClass(getStatusClass(taskEntity.getStatus()));
        taskDto.setPriorityClass(getPriorityClass(taskEntity.getPriority()));
        return taskDto;
    }

    private String convertInstantToString(final Instant dateInstant){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(Date.from(dateInstant));
    }

  public Instant convertStringToInstant(final String dateString){
        try{
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = dateFormat.parse(dateString);
            return date.toInstant();
        } catch ( ParseException pe) {
            throw new IllegalArgumentException("Error during date parse" + pe.getMessage());
        }
    }

    private String getStatusClass(Status status){
        return switch (status) {
            case Progress -> "badge badge-primary";
            case Done -> "badge badge-success";
            default -> "badge badge-secondary";
        } ;
    }

    private String getPriorityClass(Priority priority){
        return switch (priority) {
            case Normal -> "badge badge-primary";
            case High -> "badge badge-danger";
            default -> "badge badge-secondary";
        } ;
    }

    public Status convertStatus(String status){
        switch (status){
            case "Ready" -> {
                return Status.Ready;
            }
            case "Done" -> {
                return Status.Done;
            }
            case "Progress" -> {
                return Status.Progress;
            }

        }
     return null;
    }

}
