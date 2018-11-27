/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufpr.tads.bean;

import java.util.List;

/**
 *
 * @author ArtVin
 */
public class Mutante {
    
    private int id;
    private String mutanteName;
    private List<Skill> skills;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMutanteName() {
        return mutanteName;
    }

    public void setMutanteName(String mutanteName) {
        this.mutanteName = mutanteName;
    }

    public List<Skill> getSkills() {
        return skills;
    }

    public void setSkills(List<Skill> skills) {
        this.skills = skills;
    }

    public boolean isValid() {
        if(mutanteName == null || mutanteName.isEmpty()) return false;
        if(skills == null || skills.size() > 0) return false;
        return skills.stream().noneMatch((s) -> ( !s.isValid() ));
    }
    
    
}
