package com.project;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Ciutat implements Serializable {

    private long ciutatId;
    private String nom;
    private String pais;
    private int codiPostal;

    private Set<Ciutada> Ciutadans = new HashSet<>();

    public Ciutat() {}

    public Ciutat(String nom, String pais, int codiPostal) {
        this.nom = nom;
        this.pais = pais;
        this.codiPostal = codiPostal;
    }

    public long getCiutatId() {
        return ciutatId;
    }

    public String getNom() {
        return nom;
    }

    public String getPais() {
        return pais;
    }

    public int getCodiPostal() {
        return codiPostal;
    }

    public Set<Ciutada> getCiutadans() {
        return Ciutadans;
    }

    public void setCiutatId(long ciutatId) {
        this.ciutatId = ciutatId;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public void setCodiPostal(int codiPostal) {
        this.codiPostal = codiPostal;
    }

    public void setCiutadans(Set<Ciutada> ciutadans) {
        Ciutadans = ciutadans;
    }
}