package br.com.gerencimentodepedidos.repository;

import br.com.gerencimentodepedidos.model.OrderItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItemEntity, Long> {
}
