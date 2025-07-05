CREATE TABLE IF NOT EXISTS `tb_order_item` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `quantity` int NOT NULL,
  `order_id` bigint DEFAULT NULL,
  `product_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_tb_product_id` FOREIGN KEY (`product_id`) REFERENCES `tb_product` (`id`),
  CONSTRAINT `FK_tb_order_id` FOREIGN KEY (`order_id`) REFERENCES `tb_order` (`id`)
)
