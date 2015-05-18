package com.pps1.guiame.guiame.dto;

import java.io.Serializable;

/**
 * Created by javi on 13/05/15.
 */
public class Curso implements Serializable {
    private Integer id;
    private String nombre;
    private String comision;
    private String aula;
    private String docente;
    private String diasYHorarios;

    public Curso(Integer id, String nombre, String comision, String aula, String docente, String diasYHorarios) {
        this.id = id;
        this.nombre = nombre;
        this.comision = comision;
        this.aula = aula;
        this.docente = docente;
        this.diasYHorarios = diasYHorarios;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getComision() {
        return comision;
    }

    public void setComision(String comision) {
        this.comision = comision;
    }

    public String getAula() {
        return aula;
    }

    public void setAula(String aula) {
        this.aula = aula;
    }

    public String getDocente() {
        return docente;
    }

    public void setDocente(String docente) {
        this.docente = docente;
    }

    public String getDiasYHorarios() {
        return diasYHorarios;
    }

    public void setDiasYHorarios(String diasYHorarios) {
        this.diasYHorarios = diasYHorarios;
    }

    @Override
    public String toString() {
        return this.nombre + " - Aula: " + this.aula + " - " + this.diasYHorarios + " Prof "+ this.docente;
    }
}
