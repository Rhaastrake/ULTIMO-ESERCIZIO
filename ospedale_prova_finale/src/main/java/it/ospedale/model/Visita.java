package it.ospedale.model;

import java.time.LocalDate;

public class Visita {

   private int       id;
   private Paziente  paziente;
   private Medico    medico;
   private LocalDate dataVisita;
   private String    descrizione;

   public Visita(int id, Paziente paziente, Medico medico,
                 LocalDate dataVisita, String descrizione) {
      this.id          = id;
      this.paziente    = paziente;
      this.medico      = medico;
      this.dataVisita  = dataVisita;
      this.descrizione = descrizione;
   }

   public int       getId()          { return id; }
   public Paziente  getPaziente()    { return paziente; }
   public Medico    getMedico()      { return medico; }
   public LocalDate getDataVisita()  { return dataVisita; }
   public String    getDescrizione() { return descrizione; }

   @Override
   public String toString() {
      return "Visita{id=" + id
              + ", paziente=" + paziente.getNome()
              + ", medico="   + medico.getNome()
              + ", data="     + dataVisita + "}";
   }
}