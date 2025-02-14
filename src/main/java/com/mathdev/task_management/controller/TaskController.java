package com.mathdev.task_management.controller;

import com.mathdev.task_management.api.TaskDto;
import com.mathdev.task_management.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller("/")
public class TaskController {
    private final TaskService taskService;

    @Autowired
    public TaskController(final TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/")
    public ModelAndView home() {
        ModelAndView mv = new ModelAndView("index");
        List<TaskDto> taskDtoList = taskService.getTaskList();
        mv.addObject("taskDtoList", taskDtoList);
        return mv;
    }

    @GetMapping("/add-new-task")
    public ModelAndView pageNewTask (){
        ModelAndView mv = new ModelAndView("new-task");
        mv.addObject("taskDto", new TaskDto());
        mv.addObject("priorities", taskService.getPriorities());
        mv.addObject("statusList", taskService.getStatus());
        return mv;
    }

    @PostMapping("/add-or-update-task")
    public ModelAndView addOrUpdateTask(final TaskDto taskDto) {
        String title = taskDto.getTitle();
        return null;
    }

}
