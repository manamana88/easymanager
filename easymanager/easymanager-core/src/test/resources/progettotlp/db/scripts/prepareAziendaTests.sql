INSERT INTO azienda (`id`,`p_iva`,`cod_fis`,`nome`,`via`,`civico`,`cap`,`citta`,`provincia`,`nazione`,`telefono`,`fax`,`email`,`principale`,`tassabile`)
VALUES
    (1,"01234567890","BRRVCN88M20G482K","CRTaglio","via A.Volta","1","65129","Pescara","PE","Italia","0854322029","0854322029","vinci.88@tisclai.it",'Y','Y'),
    (2,"01234567891","AAAAAAAAAAAAAAAA","ABTaglio","via A.Volta","1","65129","Pescara","PE","Italia","0854322029","0854322029","vincenzo.barrea88@gmail.com",'N','N'),
    (3,"01234567892","BBBBBBBBBBBBBBBB","CDTaglio","via A.Volta","1","65129","Pescara","PE","Italia","0854322029","0854322029","vincenzo.barrea882@gmail.com",'N','Y');

INSERT INTO fattura (`real_id`,`id`,`emissione`,`scadenza`,`cliente`,`netto`,`iva_perc`,`iva`,`totale`) VALUES
    (1,1,'2012-06-01','2012-08-01',3,10.0,21,2.1,12.1);

INSERT INTO ddt VALUES
    (1,'2012-06-01',106,2,'Cessionario','Reso c/adesivazione',NULL,'',NULL,'','',0,0,'',NULL,'',0,NULL,0),
    (2,'2012-06-01',107,3,'Cessionario','Reso c/adesivazione',NULL,'',NULL,'','',0,0,'',NULL,'',0,NULL,0),
    (3,'2012-06-01',108,3,'Cessionario','Reso c/adesivazione',NULL,'',NULL,'','',0,0,'',NULL,'',0,1,0),
    (4,'2012-06-01',107,3,'Cessionario','Reso c/adesivazione',NULL,'',NULL,'','',0,0,'',NULL,'',0,NULL,0);
INSERT INTO bene VALUES
    (1,1,"cod","com","descr",10,21.0,0,'N','N','N','N','Y',0),
    (2,1,"cod2","com2","descr2",11,0,0,'Y','N','N','N','Y',1);

COMMIT;