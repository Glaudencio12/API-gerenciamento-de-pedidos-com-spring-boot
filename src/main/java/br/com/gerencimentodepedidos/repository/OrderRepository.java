package br.com.gerencimentodepedidos.repository;

import br.com.gerencimentodepedidos.model.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
}
