package com.example.concorrencia.controller;

import org.springframework.http.ResponseEntity;
import java.util.Optional;
import com.example.concorrencia.domain.ContaVersionada;
import com.example.concorrencia.domain.ContaVersionadaRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

@RestController
@RequestMapping("/contas-versionadas")
public class ContaVersionadaRest {
    @Autowired private ContaVersionadaRepo repo;

    @PostMapping("/{id}/deposito")
    @Transactional
    public ResponseEntity<?> depositar(@PathVariable Long id, @RequestParam BigDecimal valor) {
        Optional<ContaVersionada> opt = repo.findById(id);

        if (opt.isPresent()) {
            ContaVersionada conta = opt.get();
            conta.setSaldo(conta.getSaldo().add(valor));

            try {
                ContaVersionada salva = repo.save(conta);
                return ResponseEntity.ok(salva);
            } catch (ObjectOptimisticLockingFailureException e) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Conflito detectado: Este registro foi atualizado por outro usuário.");
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/saque")
    @Transactional
    public ResponseEntity<?> sacar(@PathVariable Long id, @RequestParam BigDecimal valor) {
        Optional<ContaVersionada> opt = repo.findById(id);

        if (opt.isPresent()) {
            ContaVersionada conta = opt.get();

            if (conta.getSaldo().compareTo(valor) < 0) {
                return ResponseEntity.badRequest().body("Saldo insuficiente.");
            }

            conta.setSaldo(conta.getSaldo().subtract(valor));

            try {
                ContaVersionada salva = repo.save(conta);
                return ResponseEntity.ok(salva);
            } catch (ObjectOptimisticLockingFailureException e) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Conflito detectado: Este registro foi atualizado por outro usuário.");
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
