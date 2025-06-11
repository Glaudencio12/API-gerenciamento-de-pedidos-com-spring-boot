package br.com.gerencimentodepedidos.repository;

import br.com.gerencimentodepedidos.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
