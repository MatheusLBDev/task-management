package com.mathdev.task_management.controller;

import com.mathdev.task_management.api.TaskDto;
import com.mathdev.task_management.service.TaskService;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.util.List;
import java.util.UUID;

@Controller("/")
public class TaskController {
    private final TaskService taskService;

    @Autowired
    public TaskController(final TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/")
    public ModelAndView home(@ModelAttribute("alertMessage") @Nullable String alertMessage) {
        ModelAndView mv = new ModelAndView("index");
        List<TaskDto> taskDtoList = taskService.getTaskList();
        mv.addObject("taskDtoList", taskDtoList);
        mv.addObject("alertMessage", alertMessage);
        return mv;
    }

    @GetMapping("/add-new-task")
    public ModelAndView pageNewTask (){
        ModelAndView mv = new ModelAndView("new-task");
        mv.addObject("taskDto", new TaskDto());
        mv.addObject("priorities", taskService.getPriorities());
        mv.addObject("statusList", taskService.getStatus());
        mv.addObject("alertMessage", "");
        return mv;
    }

    @PostMapping("/add-or-update-task")
    public ModelAndView addOrUpdateTask(final @Valid TaskDto taskDto, final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            ModelAndView mv = new ModelAndView("new-task");
            mv.addObject("taskDto", taskDto);
            mv.addObject("priorities", taskService.getPriorities());
            mv.addObject("statusList", taskService.getStatus());
            mv.addObject("alertMessage", "Error, please fill the form correctly");
            return mv;
        }
        if (taskDto.getId() == null){
            taskService.saveTask(taskDto);
            redirectAttributes.addFlashAttribute("alertMessage", "New task was been successfully" );
        } else {
            taskService.updateTask(taskDto);
            redirectAttributes.addFlashAttribute("alertMessage", "Task was been successfully updated" );
        }

        return new ModelAndView("redirect:/");
    }

    @GetMapping("/edit-task/{id}")
    public ModelAndView editTask(@PathVariable("id") UUID id, HttpSession session) {
        TaskDto taskDto = taskService.getTaskById(id);
        session.setAttribute("taskDto", taskDto);
        return new ModelAndView("redirect:/edit-task");
    }

    @GetMapping("/edit-task")
    public ModelAndView editTaskRedirect(HttpSession session) {
        TaskDto taskDto = (TaskDto) session.getAttribute("taskDto");
        if (taskDto == null) {
            taskDto = new TaskDto();
        }
        ModelAndView mv = new ModelAndView("new-task");
        mv.addObject("taskDto", taskDto);
        mv.addObject("priorities", taskService.getPriorities());
        mv.addObject("statusList", taskService.getStatus());
        mv.addObject("alertMessage", "");
        return mv;
    }

    @DeleteMapping("/delete-task/{id}")
    public ModelAndView deleteTask(@PathVariable UUID id){
        taskService.deleteTask(id);
        List<TaskDto> taskDtoList = taskService.getTaskList();
        ModelAndView mv = new ModelAndView("components/task-card");
        mv.addObject("taskDtoList", taskDtoList);
        return mv;
    }

}
