package com.mathdev.task_management.service;

import com.mathdev.task_management.api.Priority;
import com.mathdev.task_management.api.Status;
import com.mathdev.task_management.api.TaskDto;
import com.mathdev.task_management.db.entity.TaskEntity;
import com.mathdev.task_management.db.repository.TaskRepository;
import com.mathdev.task_management.exception.TaskNotFoundExceptions;
import com.mathdev.task_management.mapper.TaskConvert;
import jakarta.annotation.PostConstruct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.time.Instant;
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


/*    @PostConstruct
    private void generateRandomTask(){
        int x = 20;
        for(int i = 0; i < x; i++){
            TaskEntity taskEntity = new TaskEntity();
            taskEntity.setTitle("teste" + (i+1));
            taskEntity.setExpireOn(Instant.now());
            taskEntity.setPriority(Priority.Normal);
            taskEntity.setStatus(Status.Ready);
            taskEntity.setDescription("teste"+ (i+1) );
            taskEntity.setUpdatedOn(Instant.now());
            taskEntity.setCreatedOn(Instant.now());
            taskRepository.save(taskEntity);
        }
    }*/

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
                taskEntity.setExpireOn(taskConvert.convertStringToInstant(taskDto.getExpireOn()));
                taskEntity.setPriority(taskDto.getPriority());
                taskEntity.setStatus(taskDto.getStatus());
                taskEntity.setDescription(taskDto.getDescription());
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
        return List.of(Priority.Low.toString(), Priority.High.toString(), Priority.Normal.toString());
    }
    public List<String> getStatus() {
        return List.of(Status.Ready.toString(), Status.Progress.toString(), Status.Done.toString());
    }

    public List<TaskDto> getTaskListByStatus(String strStatus){
        Status status = taskConvert.convertStatus(strStatus);
        if(status == null){
            return getTaskList ();
        }
            List<TaskEntity> taskEntityList = taskRepository.findAllByStatusOrderByCreatedOnDesc(status);
            return taskEntityList.stream().map(taskConvert::convertTaskEntityToTaskDto).collect(Collectors.toList());
    }

    public Page<TaskDto> getTaskListPaginated(int pageNo, int pageSize, String status) {
        List<TaskDto> taskDtoList;
        Page<TaskDto> page;
        if (status == null || status.isEmpty() || status.equals("All") ) {
            taskDtoList = getTaskList();
        } else {
            taskDtoList = getTaskListByStatus(status);
            pageNo = 1;
        }
        if(!taskDtoList.isEmpty()){
            if(taskDtoList.size() < pageSize){
                pageSize = taskDtoList.size();
            }
            Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
            int start = (int) pageable.getOffset();
            int end = Math.min((start + pageable.getPageSize()), taskDtoList.size());
            List<TaskDto> subList = taskDtoList.subList(start, end);
            page = new PageImpl<>(subList, pageable, taskDtoList.size());

        } else{
            page = Page.empty();
        }
        return page;
    }

}
