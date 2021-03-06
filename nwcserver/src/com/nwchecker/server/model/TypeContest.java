package com.nwchecker.server.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "type_contest")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class TypeContest {

    @Id
    @Column(name = "id")
    @GeneratedValue
    @JsonProperty("id")
    private int id;

    @Column(name = "name")
    @JsonProperty("name")
    private String name;

    @Column(name = "dynamic")
    @JsonProperty("dynamic")
    private Boolean dynamic;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "typeContest")
    @JsonIgnore
    private List<Rule> rules;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "typeContest")
    @JsonIgnore
    private List<Contest> contests;

    public TypeContest() {
        this.name = "N/A";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Rule> getRules() {
        return rules;
    }

    public void setRules(List<Rule> rules) {
        this.rules = rules;
    }

    public void addRule(Rule rule) {
        if (rules == null) {
            rules = new ArrayList<Rule>();
        }
        this.rules.add(rule);
    }

    public List<Contest> getContests() {
        return contests;
    }

    public void setContests(List<Contest> contests) {
        this.contests = contests;
    }

    public void addContest(Contest contest) {
        if (contests == null) {
            contests = new ArrayList<Contest>();
        }
        this.contests.add(contest);
    }

    public Boolean isDynamic() {
        return dynamic;
    }

    public void setDynamic(Boolean dynamic) {
        this.dynamic = dynamic;
    }

    public Boolean getDynamic() {
        return dynamic;
    }
}
