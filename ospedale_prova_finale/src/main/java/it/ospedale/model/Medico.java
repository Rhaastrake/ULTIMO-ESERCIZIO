package it.ospedale.model;

public class Medico {

   private int    id;
   private String nome;
   private String specializzazione;

   public Medico(int id, String nome, String specializzazione) {
      this.id               = id;
      this.nome             = nome;
      this.specializzazione = specializzazione;
   }

   public int    getId()               { return id; }
   public String getNome()             { return nome; }
   public String getSpecializzazione() { return specializzazione; }

   @Override
   public String toString() {
      return "Medico{id=" + id + ", nome='" + nome
              + "', specializzazione='" + specializzazione + "'}";
   }
}