package com.nwchecker.server.controller;

import com.nwchecker.server.model.*;
import com.nwchecker.server.service.ContestPassService;
import com.nwchecker.server.service.ContestService;
import com.nwchecker.server.service.TaskService;
import com.nwchecker.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Роман on 11.02.2015.
 */
@Controller("contestPassController")
public class ContestPassController {

    @Autowired
    private UserService userService;
    @Autowired
    private ContestService contestService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private ContestPassService contestPassService;


    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/passTask.do", method = RequestMethod.GET)
    public String getTaskForPass(Principal pricnipal, @RequestParam("id") int taskId,
                                 Model model) {
        Task currentTask = taskService.getTaskById(taskId);
        model.addAttribute("currentTask", currentTask);
        User user = userService.getUserByUsername(pricnipal.getName());
        //check if contest status provide passing:
        if (!(currentTask.getContest().getStatus() == Contest.Status.GOING ||
                currentTask.getContest().getStatus() == Contest.Status.ARCHIVE)) {
            return "access/accessDenied403";
        }
        //if contest is going:
        boolean goingContest = false;
        ContestPass currentContestPass = null;
        if (currentTask.getContest().getStatus() == Contest.Status.GOING) {
            goingContest = true;
            //check if user has contestPass for this contest:
            boolean contains = false;
            for (ContestPass c : user.getContestPassList()) {
                if (c.getContest().equals(currentTask.getContest())) {
                    contains = true;
                    currentContestPass = c;
                    break;
                }
            }
            if (!contains) {
                ContestPass contestPass = new ContestPass();
                contestPass.setContest(currentTask.getContest());
                contestPass.setUser(user);
                contestPassService.saveContestPass(contestPass);
            }
        }
        //get contest tasks titles
        Map<Integer, String> taskTitles = new HashMap<>();
        for (Task task : currentTask.getContest().getTasks()) {
            taskTitles.put(task.getId(), task.getTitle());
        }
        model.addAttribute("taskTitles", taskTitles);

        //get list of passed/failed tasks, and forward it to UI:
        if (currentContestPass != null) {
            Map<Integer, Boolean> taskResults = new LinkedHashMap<>();
            for (TaskPass taskPass : currentContestPass.getTaskPassList()) {
                //if not contains:
                if (!taskResults.containsKey(taskPass.getTask().getId())) {
                    taskResults.put(taskPass.getTask().getId(), taskPass.isPassed());
                    continue;
                }
                //if contains and new result if success:
                if ((!taskResults.get(taskPass.getTask().getId())) && taskPass.isPassed()) {
                    taskResults.put(taskPass.getTask().getId(), taskPass.isPassed());
                }
            }
            model.addAttribute("taskResults", taskResults);
        }
        return "contests/contestPass";
    }

    @RequestMapping(value = "/submitTask", method = RequestMethod.POST)
    @PreAuthorize("hasRole('ROLE_USER')")
    public
    @ResponseBody
    Map<String, Object> submitTask(Principal principal, @RequestParam(value = "id") int taskId,
                                   @RequestParam(value = "compilerId") int compilerId,
                                   @RequestParam("file") MultipartFile file) throws IOException {
        Map<String, Object> result = new LinkedHashMap<>();
        Task task = taskService.getTaskById(taskId);
        User user = userService.getUserByUsername(principal.getName());
        //check access:
        ContestPass contestPass = null;
        if (task.getContest().getStatus() == Contest.Status.GOING) {
            //check if user contains contestPass:
            for (ContestPass c : user.getContestPassList()) {
                if (c.getContest().equals(task.getContest())) {
                    contestPass = c;
                }
            }
        }
        if (task.getContest().getStatus() == Contest.Status.GOING && contestPass != null) {
            result = contestPassService.checkTask(true, contestPass, task, compilerId, file.getBytes());
        } else if (task.getContest().getStatus() == Contest.Status.ARCHIVE) {
            //archive:
            result = contestPassService.checkTask(false, contestPass, task, compilerId, file.getBytes());
        } else {
            result.put("accessDenied", true);
        }
        return result;
    }
}
