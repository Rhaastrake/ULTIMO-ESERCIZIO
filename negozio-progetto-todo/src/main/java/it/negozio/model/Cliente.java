package it.negozio.model;

import java.util.regex.Pattern;

public class Cliente {
    private int id;
    private String nome;
    private String email;

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[\\w.+-]+@[\\w-]+\\.[a-zA-Z]{2,}$");

    public Cliente(int id, String nome, String email) {
        this.id = id;
        this.nome = nome;
        if(!isEmailValida(email)){
            throw new IllegalArgumentException("Email non valida");
        }
        this.email = email;
    }

    public static boolean isEmailValida(String email){
        if(email == null || email.isBlank()) return false;
        return EMAIL_PATTERN.matcher(email).matches();
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
