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
import java.util.SortedSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ArtVin
 */
public class MutanteDAO {
    private Connection con;
    private ResultSet rs;
    
    public MutanteDAO(){
        con = ConnectionFactory.getConnection();
    }
    
    public List<Mutante> getListMutantes(){
        List<Mutante> lista = null;
        PreparedStatement st;
        try {
            st = con.prepareStatement(
                    "SELECT idMutante FROM Mutante"
            );
                          
            rs = st.executeQuery();
            if(rs != null){
                lista = new ArrayList<>();
                Mutante m;
                while(rs.next()){
                    m = null;
                    m = getMutante(rs.getInt("IDMUTANTE"));
                    if(m != null){
                        lista.add(m);
                    }
                }  
            }
        } catch (SQLException ex) {
            Logger.getLogger(MutanteDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return lista;
    }
    
    public List<Mutante> getListMutantesWithName(String name){
        List<Mutante> lista = null;
        PreparedStatement st;
        try {
            st = con.prepareStatement(
                    "SELECT idMutante FROM Mutante WHERE mutanteName LIKE '%?%'"
            );
            if( name != null && !name.isEmpty()){ 
                st.setString(1, name);
                rs = st.executeQuery();
                if(rs != null){
                    lista = new ArrayList<>();
                    Mutante m;
                    while(rs.next()){
                        m = null;
                        m = getMutante(rs.getInt("IDMUTANTE"));
                        if(m != null){
                            lista.add(m);
                        }
                    }  
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(MutanteDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return lista;
    }
    
    
    public Mutante getMutante(int idMutante){
        Mutante m = null;
        PreparedStatement st;
        try {
            st = con.prepareStatement(
                    "SELECT MUTANTENAME FROM MUTANTE WHERE idMutante = ?"
            );
            st.setInt(1, idMutante);
                          
            rs = st.executeQuery();
            if(rs != null){
                SkillDAO skillDAO = new SkillDAO();
                while(rs.next()){
                    m = new Mutante();
                    m.setId(idMutante);
                    m.setMutanteName(rs.getString("MUTANTENAME"));
                    m.setSkills(skillDAO.getListSkillsOfMutante(idMutante));
                }  
            }
        } catch (SQLException ex) {
            Logger.getLogger(MutanteDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return m;
    }
    public Mutante getMutante(String nomeMutante){
        Mutante m = null;
        PreparedStatement st;
        try {
            st = con.prepareStatement(
                    "SELECT idMutante FROM MUTANTE WHERE MUTANTENAME LIKE '%?%'"
            );
            st.setString(1, nomeMutante);
                          
            rs = st.executeQuery();
            if(rs != null){
                SkillDAO skillDAO = new SkillDAO();
                while(rs.next()){
                    m = new Mutante();
                    m.setId(rs.getInt("idMutante"));
                    m.setMutanteName(nomeMutante);
                    m.setSkills(skillDAO.getListSkillsOfMutante(m.getId()));
                }  
            }
        } catch (SQLException ex) {
            Logger.getLogger(MutanteDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return m;
    }
    public Mutante getMutanteEqualName(String nomeMutante){
        Mutante m = null;
        PreparedStatement st;
        try {
            st = con.prepareStatement(
                    "SELECT idMutante FROM MUTANTE WHERE MUTANTENAME = ?"
            );
            st.setString(1, nomeMutante);
                          
            rs = st.executeQuery();
            if(rs != null){
                SkillDAO skillDAO = new SkillDAO();
                while(rs.next()){
                    m = new Mutante();
                    m.setId(rs.getInt("idMutante"));
                    m.setMutanteName(nomeMutante);
                    m.setSkills(skillDAO.getListSkillsOfMutante(m.getId()));
                }  
            }
        } catch (SQLException ex) {
            Logger.getLogger(MutanteDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return m;
    }
    
    public int insertMutante(Mutante m){
        PreparedStatement st;
        if(validateMutante(m)){
            try {
                st = con.prepareStatement(
                        "INSERT INTO Mutante(mutanteName) VALUES(?)",Statement.RETURN_GENERATED_KEYS
                );
                st.setString(1, m.getMutanteName());
                st.executeUpdate();
                if(st != null){
                    rs = st.getGeneratedKeys();
                    if(rs.next()){
                        m.setId(rs.getInt(1));
                        if(insertSkills(m)){
                            return m.getId();
                        }
                    }
                    
                }
            } catch (SQLException ex) {
                Logger.getLogger(MutanteDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
            deleteMutante(m);
        }
        return 0;
    }
    
    public boolean deleteMutante(int idMutante){
        return deleteMutante(getMutante(idMutante));
    }
    
    private boolean deleteMutante(Mutante m) {
        PreparedStatement st;
        if(m.getId() > 0){
            if (deleteMutanteSkills(m.getId())){
                SkillDAO skillDao = new SkillDAO();
                m.getSkills().stream().noneMatch((s) -> ( !skillDao.deleteSkill(s.getId())));
                try {
                    st = con.prepareStatement(
                            "DELETE FROM Mutante WHERE idMutante = ?"
                    );
                    st.setInt(1, m.getId());

                    if(st.executeUpdate() !=0) return true;

                } catch (SQLException ex) {
                    Logger.getLogger(MutanteDAO.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }
        return false;
    }
    
    private boolean deleteMutanteSkills(int id) {
        PreparedStatement st;
        try {
            st = con.prepareStatement(
                    "DELETE FROM Mutante_has_Skill WHERE Mutante_idMutante = ?"
            );
            st.setInt(1, id);

            if(st.executeUpdate() !=0) return true;

        } catch (SQLException ex) {
            Logger.getLogger(MutanteDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public List<Mutante> getListMutantesWithSkill(int idSkill) {
        List<Mutante> listaMutantes = null;
        PreparedStatement st;
        try {
            st = con.prepareStatement(
                    "SELECT Mutante_idMutante FROM Mutante_has_Skill WHERE Skill_idSkill = ?"
            );
            st.setInt(1, idSkill);
                          
            rs = st.executeQuery();
            if(rs != null){
                listaMutantes = new ArrayList<>();
                while(rs.next()){
                    listaMutantes.add(getMutante(rs.getInt("Mutante_idMutante")));
                }  
            }
        } catch (SQLException ex) {
            Logger.getLogger(MutanteDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return listaMutantes;
    }
    
    public List<Mutante> getListMutantesWithSkill(String skillName) {
        List<Mutante> listaMutantes = null;
        PreparedStatement st;
        try {
            st = con.prepareStatement(
                    "SELECT m.idMutante,m.mutanteName FROM Mutante_has_Skill "
                            + "INNER JOIN mutante m ON m.idMutante = Mutante_idMutante"
                            + "INNER JOIN skill s ON s.idSkill = Skill_idSkill "
                            + "WHERE s.skillName LIKE '%?%'"
            );
            st.setString(1, skillName);
                          
            rs = st.executeQuery();
            if(rs != null){
                listaMutantes = new ArrayList<>();
                Mutante m;
                while(rs.next()){
                    m = new Mutante();
                    m.setId(rs.getInt("m.idMutante"));
                    m.setMutanteName("m.mutanteName");
                    listaMutantes.add(m);
                }  
            }
        } catch (SQLException ex) {
            Logger.getLogger(MutanteDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return listaMutantes;
    }
    
    public boolean updateMutante(Mutante m){
        PreparedStatement st;
        if(validateMutante(m) && m.getId() > 0){
            deleteMutanteSkills(m.getId());
            if(insertSkills(m)){
                try {
                    st = con.prepareStatement(
                            "UPDATE Mutante SET mutanteName = ? WHERE idMutante = ?"
                    );
                    st.setString(1, m.getMutanteName());
                    st.setInt(2, m.getId());
                    st.executeUpdate();

                    int rowsAffected = st.executeUpdate();
                    if(rowsAffected > 0)return true;

                } catch (SQLException ex) {
                    Logger.getLogger(MutanteDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return false;   
    }
    private boolean insertSkills(Mutante m){
        SkillDAO skillDao = new SkillDAO();
        Skill tmpSkill;
        boolean hasError = false;
        for(Skill s : m.getSkills()){
            tmpSkill = skillDao.getSkill(s.getSkillName());
            if(tmpSkill != null && tmpSkill.getId() > 0) s.setId(tmpSkill.getId());
            if(s.getId() <= 0){
                s.setId(skillDao.insertSkill(s));
            }
            hasError =  hasError && !skillDao.insertSkillIntoMutante(m.getId(),s.getId());
        }
        return !hasError;
    }
    
    private boolean validateMutante(Mutante m){
        if(m == null || !m.isValid()) return false;
        return getMutanteEqualName(m.getMutanteName()) == null;
    }

    

    
}