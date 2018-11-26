/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufpr.tads.dao;

import br.ufpr.tads.bean.Mutante;
import br.ufpr.tads.bean.Skill;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ArtVin
 */
public class SkillDAO {
    private Connection con;
    private ResultSet rs;
    
    public SkillDAO(){
        con = ConnectionFactory.getConnection();
    }

    public List<Skill> getListSkillsOfMutante(int idMutante) {
        List<Skill> listaSkill = null;
        PreparedStatement st;
        try {
            st = con.prepareStatement(
                    "SELECT Skill_idSkill FROM Mutante_has_Skill WHERE Mutante_idMutante = ?"
            );
            st.setInt(1, idMutante);
                          
            rs = st.executeQuery();
            if(rs != null){
                listaSkill = new ArrayList<Skill>();
                Skill s;
                while(rs.next()){
                    s = null;
                    s = getSkill(rs.getInt("Skill_idSkill"));
                    if(s != null){
                        listaSkill.add(s);
                    }
                }  
            }
        } catch (SQLException ex) {
            Logger.getLogger(SkillDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        return listaSkill;
    }
    
    
    public Skill getSkill(int idSkill){
        Skill s = null;
        PreparedStatement st;
        try {
            st = con.prepareStatement(
                    "SELECT skillName FROM Skill WHERE idSkill = ?"
            );
            if(idSkill > 0){
                st.setInt(1, idSkill);
                
                rs = st.executeQuery();
                
                while(rs.next()){
                    s = new Skill();
                    s.setId(idSkill);
                    s.setSkillName(rs.getString("skillName"));
                }
            }            
        } catch (SQLException ex) {
            Logger.getLogger(SkillDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return s;
    }
    
    public Skill getSkill(String skillName){
        Skill s = null;
        PreparedStatement st;
        try {
            st = con.prepareStatement(
                    "SELECT idSkill FROM Skill WHERE skillName = '%?%'"
            );
            if(skillName != null && !skillName.isEmpty()){
                st.setString(1, skillName);
                
                rs = st.executeQuery();
                
                while(rs.next()){
                    s = new Skill();
                    s.setId(rs.getInt("idSkill"));
                    s.setSkillName(skillName);
                }
            }            
        } catch (SQLException ex) {
            Logger.getLogger(SkillDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return s;
    }
    
    public int insertSkill(Skill s) {
        PreparedStatement st;
        if(s != null && s.isValid()){
            try {
                st = con.prepareStatement(
                        "INSERT INTO Skill(skillName) VALUES(?)",Statement.RETURN_GENERATED_KEYS
                );
                st.setString(1, s.getSkillName());
                st.executeUpdate();
                if(st != null){
                    rs = st.getGeneratedKeys();
                    if(rs.next()){
                        s.setId(rs.getInt(1));
                    }
                }
                return s.getId();
            } catch (SQLException ex) {
                Logger.getLogger(SkillDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return 0;
    }

    public boolean deleteSkill(int idSkill) {
        MutanteDAO mutanteDAO = new MutanteDAO();
        List<Mutante> list = mutanteDAO.getListMutantesWithSkill(idSkill);
     
        if(list != null && list.size() <= 0){
            PreparedStatement st;
            try {
                st = con.prepareStatement(
                        "DELETE FROM Skill WHERE idSkill = ?"
                );
                st.setInt(1, idSkill);

                if(st.executeUpdate() !=0) return true;

            } catch (SQLException ex) {
                Logger.getLogger(SkillDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return false;
        
    }

    public boolean insertSkillIntoMutante(int idMutante, int idSkill) {
        PreparedStatement st;
        try {
            st = con.prepareStatement(
                    "INSERT INTO Mutante_has_Skill(Mutante_idMutante,Skill_idSkill) VALUES(?,?)",Statement.RETURN_GENERATED_KEYS
            );
            st.setInt(1, idMutante);
            st.setInt(2, idSkill);
            st.executeUpdate();
            if(st != null){
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(SkillDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
    }

}
