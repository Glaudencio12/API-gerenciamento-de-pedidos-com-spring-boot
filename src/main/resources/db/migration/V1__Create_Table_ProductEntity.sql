CREATE TABLE IF NOT EXISTS `tb_product` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `category` enum('BEBIDA','ENTRADA','PRATO_PRINCIPAL','SOBREMESA') NOT NULL,
  `name` varchar(80) NOT NULL,
  `price` double NOT NULL,
  PRIMARY KEY (`id`)
)



