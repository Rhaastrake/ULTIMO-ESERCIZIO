package it.negozio.model;

import java.time.LocalDate;

public class Ordine {
    private int id;
    private Cliente cliente;
    private Prodotto prodotto;
    private int quantitaOrdinata;
    private LocalDate dataOrdine;

    public Ordine(int id, Cliente cliente, Prodotto prodotto, int quantitaOrdinata, LocalDate dataOrdine) {
        this.id = id;
        this.cliente = cliente;
        this.prodotto = prodotto;
        this.quantitaOrdinata = quantitaOrdinata;
        this.dataOrdine = dataOrdine;
    }

    public double getTotale(){
        return prodotto.getPrezzo() * quantitaOrdinata;
    }

    public int getId() {
        return id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Prodotto getProdotto() {
        return prodotto;
    }

    public int getQuantitaOrdinata() {
        return quantitaOrdinata;
    }

    public LocalDate getDataOrdine() {
        return dataOrdine;
    }

    @Override
    public String toString() {
        return "Ordine{" +
                "id=" + id +
                ", cliente=" + cliente +
                ", prodotto=" + prodotto +
                ", quantitaOrdinata=" + quantitaOrdinata +
                ", dataOrdine=" + dataOrdine +
                '}';
    }
}
