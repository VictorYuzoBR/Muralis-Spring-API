package com.muralis.api_cliente.Repositories;

import com.muralis.api_cliente.Model.ContatoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContatoRepository extends JpaRepository<ContatoModel, Long> {
}
