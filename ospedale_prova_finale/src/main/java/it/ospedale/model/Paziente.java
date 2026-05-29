package it.ospedale.model;

import java.util.regex.Pattern;

public class Paziente {

   private static final Pattern EMAIL_PATTERN =
           Pattern.compile("^[\\w.+-]+@[\\w-]+\\.[a-zA-Z]{2,}$");

   private int    id;
   private String nome;
   private String email;

   public Paziente(int id, String nome, String email) {
      this.id   = id;
      this.nome = nome;
      if (!isEmailValida(email)) {
         throw new IllegalArgumentException("Email non valida: " + email);
      }
      this.email = email;
   }

   public static boolean isEmailValida(String email) {
      if (email == null || email.isBlank()) return false;
      return EMAIL_PATTERN.matcher(email).matches();
   }

   public int    getId()    { return id; }
   public String getNome()  { return nome; }
   public String getEmail() { return email; }

   @Override
   public String toString() {
      return "Paziente{id=" + id + ", nome='" + nome + "', email='" + email + "'}";
   }
}