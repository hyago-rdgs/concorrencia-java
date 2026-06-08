package com.example.concorrencia.controller;

import org.springframework.http.ResponseEntity;
import java.util.Optional;
import com.example.concorrencia.domain.Conta;
import com.example.concorrencia.domain.ContaRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

@RestController
@RequestMapping("/contas")
public class ContaRest {
    @Autowired private ContaRepo repo;

    @PostMapping("/{id}/deposito")
    @Transactional
    public void depositar(@PathVariable Long id, @RequestParam BigDecimal valor) throws Exception {
        Conta c = repo.findById(id).orElseThrow();
        Thread.sleep(200);
        c.setSaldo(c.getSaldo().add(valor));
        repo.save(c);
    }

    @PostMapping("/{id}/saque")
    @Transactional
    public ResponseEntity<?> saque(@PathVariable Long id, @RequestParam BigDecimal valor) throws Exception {
        Conta c = repo.findById(id).orElseThrow();
        Thread.sleep(200);

        // Validação: saldo não pode ficar negativo
        if (c.getSaldo().compareTo(valor) < 0) {
            return ResponseEntity.badRequest().body("Saldo insuficiente para saque.");
        }

        c.setSaldo(c.getSaldo().subtract(valor));
        repo.save(c);
        return ResponseEntity.ok(c);
    }
}
