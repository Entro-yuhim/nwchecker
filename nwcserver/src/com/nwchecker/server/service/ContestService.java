/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nwchecker.server.service;

import com.nwchecker.server.dao.ContestDAO;
import com.nwchecker.server.model.Contest;

import java.util.List;

/**
 * @author Роман
 */
public interface ContestService {

    public void setContestDAO(ContestDAO dao);

    public void setUserService(UserService userService);

    public void addContest(Contest c);

    public void updateContest(Contest c);

    public void mergeContest(Contest c);

    public List<Contest> getContests();

    public Contest getContestByID(int id);

    public List<Contest> getContestByStatus(Contest.Status status);

    public boolean checkIfUserHaveAccessToContest(String username, int ContestId);

}
