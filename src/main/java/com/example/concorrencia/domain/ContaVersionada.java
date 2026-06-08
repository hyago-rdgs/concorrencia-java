package com.example.concorrencia.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Version;

@Entity
public class ContaVersionada implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BigDecimal saldo;

    @Version // O segredo para o controle de concorrência otimista
    private Integer versao;

    public ContaVersionada() {}
    public ContaVersionada(BigDecimal saldo) { this.saldo = saldo; }

    // Getters e Setters
    public Long getId() { return id; }
    public BigDecimal getSaldo() { return saldo; }
    public void setSaldo(BigDecimal saldo) { this.saldo = saldo; }
    public Integer getVersao() { return versao; }
}