package br.com.gerencimentodepedidos.repository;

import br.com.gerencimentodepedidos.model.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> { }
