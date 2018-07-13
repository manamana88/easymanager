DROP TABLE IF EXISTS `account`;
DROP TABLE IF EXISTS `fattura`;
DROP TABLE IF EXISTS `bene`;
DROP TABLE IF EXISTS `ddt`;
DROP TABLE IF EXISTS `azienda`;
commit;
CREATE TABLE `azienda` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `p_iva` varchar(11) NOT NULL,
  `cod_fis` varchar(16) NOT NULL,
  `nome` varchar(50) NOT NULL,
  `via` varchar(60) DEFAULT NULL,
  `civico` varchar(10) DEFAULT NULL,
  `cap` varchar(6) DEFAULT NULL,
  `citta` varchar(50) DEFAULT NULL,
  `provincia` varchar(50) DEFAULT NULL,
  `nazione` varchar(50) DEFAULT NULL,
  `telefono` varchar(20) DEFAULT NULL,
  `fax` varchar(20) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `principale` varchar(1) NOT NULL DEFAULT 'N',
  `tassabile` varchar(1) NOT NULL DEFAULT 'Y',
  `num_aut` varchar(45) DEFAULT NULL,
  `data_aut` date DEFAULT NULL,
  `num_reg` varchar(45) DEFAULT NULL,
  `data_reg` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uc_piva` (`p_iva`),
  UNIQUE KEY `uc_codfis` (`cod_fis`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
CREATE TABLE `ddt` (
  `real_id` int(11) NOT NULL AUTO_INCREMENT,
  `data` date NOT NULL,
  `id` int(11) NOT NULL,
  `cliente` int(11) NOT NULL,
  `mezzo` varchar(12) DEFAULT NULL,
  `causale` varchar(45) DEFAULT NULL,
  `destinazione` varchar(255) DEFAULT NULL,
  `vostro_ordine` varchar(255) DEFAULT NULL,
  `vostro_ordine_del` varchar(255) DEFAULT NULL,
  `tipo` varchar(45) DEFAULT NULL,
  `aspetto_esteriore` varchar(45) DEFAULT NULL,
  `colli` int(10) unsigned DEFAULT NULL,
  `peso` double DEFAULT NULL,
  `porto` varchar(45) DEFAULT NULL,
  `ritiro` varchar(255) DEFAULT NULL,
  `annotazioni` varchar(250) DEFAULT NULL,
  `progressivo` int(10) DEFAULT NULL,
  `fatturabile` varchar(1) DEFAULT 'Y',
  `fattura` int(11) DEFAULT NULL,
  `idx` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`real_id`) USING BTREE,
  KEY `cliente` (`cliente`),
  CONSTRAINT `ddt_ibfk_1` FOREIGN KEY (`cliente`) REFERENCES `azienda` (`id`) ON DELETE NO ACTION ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
CREATE TABLE `bene` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ddt` int(11) NOT NULL,
  `cod` varchar(255) DEFAULT NULL,
  `commessa` varchar(50) DEFAULT NULL,
  `descrizione` varchar(50) NOT NULL,
  `quantita` int(11) NOT NULL,
  `prezzo` float DEFAULT NULL,
  `tot` float DEFAULT NULL,
  `prototipo` varchar(1) NOT NULL DEFAULT 'N',
  `campionario` varchar(1) NOT NULL DEFAULT 'N',
  `primo_capo` varchar(1) NOT NULL DEFAULT 'N',
  `piazzato` varchar(1) NOT NULL DEFAULT 'N',
  `adesivato` varchar(1) NOT NULL DEFAULT 'N',
  `idx` int(11) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `ddt` (`ddt`),
  CONSTRAINT `FK_bene_1` FOREIGN KEY (`ddt`) REFERENCES `ddt` (`real_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
CREATE TABLE `fattura` (
  `real_id` int(11) NOT NULL AUTO_INCREMENT,
  `id` int(11) NOT NULL,
  `emissione` date NOT NULL,
  `scadenza` date NOT NULL,
  `cliente` int(11) NOT NULL,
  `netto` float NOT NULL,
  `iva_perc` float NOT NULL,
  `iva` float NOT NULL,
  `totale` float NOT NULL,
  `bollo` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`real_id`),
  KEY `cliente` (`cliente`),
  CONSTRAINT `fattura_ibfk_1` FOREIGN KEY (`cliente`) REFERENCES `azienda` (`id`) ON DELETE NO ACTION ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
CREATE TABLE `account` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  `smtp` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
commit;
