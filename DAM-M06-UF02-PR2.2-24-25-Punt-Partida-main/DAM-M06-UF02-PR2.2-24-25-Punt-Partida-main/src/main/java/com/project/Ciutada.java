package com.project;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "ciutada")
public class Ciutada  implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="ciutadaId", unique=true, nullable=false)
    private long id;
    @ManyToOne
    @JoinColumn(name="ciutatId")
    private Ciutat ciutat;
    private String nom;
    private String cognom;
    private int edat;


    public Ciutada() {}


    public Ciutada(String nom, String cognom, int edat) {
        this.nom = nom;
        this.cognom = cognom;
        this.edat = edat;
    }

    public long getCiutadaId() {
        return id;
    }

    public Ciutat getCiutat() {
        return ciutat;
    }

    public String getNom() {
        return nom;
    }

    public String getCognom() {
        return cognom;
    }
    
    public int getEdat() {
        return edat;
    }

    public void setCiutadaId(long ciutadaId) {
        this.id = ciutadaId;
    }

    public void setCiutat(Ciutat ciutat) {
        this.ciutat = ciutat;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setCognom(String cognom) {
        this.cognom = cognom;
    }

    public void setEdat(int edat) {
        this.edat = edat;
    }

    @Override
    public String toString() {
        return "Ciutada - " + this.getCiutadaId() + ": " + this.getNom() + " "+ this.getCognom() + " , edat " + this.getEdat() + " anys";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        Ciutada ciutada = (Ciutada) o;
        return id == ciutada.id;
    }
    
    @Override
    public int hashCode() {
        return Long.hashCode(id);
    }
    
}