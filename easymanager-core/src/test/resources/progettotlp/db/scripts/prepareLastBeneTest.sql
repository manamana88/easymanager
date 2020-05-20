INSERT INTO azienda (id,p_iva,cod_fis,nome,via,civico,cap,citta,provincia,nazione,telefono,fax,email,principale)
VALUES
    (1,'01234567890','BRRVCN88M20G482K','CRTaglio','via A.Volta','1','65129','Pescara','PE','Italia','0854322029','0854322029','vinci.88@tisclai.it','Y'),
    (2,'01234567891','AAAAAAAAAAAAAAAA','azienda1','via A.Volta','1','65129','Pescara','PE','Italia','0854322029','0854322029','vincenzo.barrea88@gmail.com','N'),
    (3,'01234567892','BBBBBBBBBBBBBBBB','azienda2','via A.Volta','1','65129','Pescara','PE','Italia','0854322029','0854322029','vincenzo.barrea882@gmail.com','N');
INSERT INTO fattura (real_id,id,emissione,scadenza,cliente,netto,iva_perc,iva,totale) VALUES
    (1,4,'2012-03-30','2012-05-29',3,45,21,9.45,54.45),
    (2,1,'2011-03-30','2012-05-29',3,45,21,9.45,54.45);
INSERT INTO ddt (real_id,data,id,cliente,mezzo,causale,destinazione,vostro_ordine,vostro_ordine_del,tipo,aspetto_esteriore,colli,peso,porto,ritiro,annotazioni,progressivo,fattura,idx) VALUES
    (1,'2012-03-01',1,2,'Cessionario','Reso c/adesivazione',NULL,'',NULL,'','',0,0,'',NULL,'',0,NULL,0),
    (2,'2012-03-02',2,2,'Cessionario','Reso c/adesivazione',NULL,'',NULL,'','',0,0,'',NULL,'',0,NULL,1),
    (10,'2011-06-01',107,3,'Cessionario','Reso c/adesivazione',NULL,'',NULL,'','',0,0,'',NULL,'',0,2,0),
    (3,'2012-03-01',3,3,'','','','','','','',0,0,'','','',0,1,0),
    (4,'2012-03-02',4,3,'','','','','','','',0,0,'','','',0,1,1);
INSERT INTO bene VALUES
    (1,3,'0001','C0001','Abito',15,3,45,'Y','Y','Y','Y','Y',0),
    (2,10,'0001','C0001','Abito',15,1,15,'Y','Y','Y','Y','Y',0);
COMMIT;