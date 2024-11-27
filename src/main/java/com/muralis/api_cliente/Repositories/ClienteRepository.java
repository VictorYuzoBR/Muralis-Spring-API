package com.muralis.api_cliente.Repositories;

import com.muralis.api_cliente.Model.ClienteModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<ClienteModel, Long> {
}
