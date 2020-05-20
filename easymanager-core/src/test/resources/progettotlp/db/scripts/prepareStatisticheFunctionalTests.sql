INSERT INTO azienda (id,p_iva,cod_fis,nome,via,civico,cap,citta,provincia,nazione,telefono,fax,email,principale)
VALUES
    (1,'01234567890','BRRVCN88M20G482K','CRTaglio','via A.Volta','1','65129','Pescara','PE','Italia','0854322029','0854322029','vinci.88@tisclai.it','Y'),
    (2,'01234567891','AAAAAAAAAAAAAAAA','azienda1','via A.Volta','1','65129','Pescara','PE','Italia','0854322029','0854322029','vincenzo.barrea88@gmail.com','N'),
    (3,'01234567892','BBBBBBBBBBBBBBBB','azienda2','via A.Volta','1','65129','Pescara','PE','Italia','0854322029','0854322029','vincenzo.barrea882@gmail.com','N');
INSERT INTO fattura (real_id,id,emissione,scadenza,cliente,netto,iva_perc,iva,totale) VALUES
    (1,1,'2012-03-30','2012-05-29',3,300,21,63,363),
    (2,2,'2012-03-30','2012-05-29',2,200,21,42,242),
    (3,3,'2012-04-01','2012-05-29',3,100,21,21,121),
    (4,1,'2011-03-30','2012-05-29',3,400,21,84,484);
INSERT INTO ddt (real_id,data,id,cliente,mezzo,causale,destinazione,vostro_ordine,vostro_ordine_del,tipo,aspetto_esteriore,colli,peso,porto,ritiro,annotazioni,progressivo,fattura,idx) VALUES
    (1,'2012-03-01',1,2,'Cessionario','Reso c/adesivazione',NULL,'',NULL,'','',0,0,'',NULL,'',0,3,0),
    (2,'2012-03-02',2,2,'Cessionario','Reso c/adesivazione',NULL,'',NULL,'','',0,0,'',NULL,'',0,2,1),
    (10,'2011-06-01',107,3,'Cessionario','Reso c/adesivazione',NULL,'',NULL,'','',0,0,'',NULL,'',0,NULL,0),
    (3,'2012-03-01',3,3,'','','','','','','',0,0,'','','',0,1,0),
    (4,'2012-03-02',4,3,'','','','','','','',0,0,'','','',0,1,1);
INSERT INTO bene (id,ddt,cod,commessa,descrizione,quantita,prezzo,tot,prototipo,campionario,primo_capo,piazzato,adesivato,idx) VALUES
    (1,1,'0001','C0001','Abito',15,0,0,'Y','Y','Y','Y','Y',0),
    (2,2,'0002','C0002','Abito',25,0,0,'Y','Y','N','Y','Y',0),
    (3,3,'0003','C0001','Abito',15,1,15,'Y','Y','Y','Y','Y',0),
    (4,4,'0004','C0002','Abito',25,null,30,'Y','Y','N','Y','Y',0);
COMMIT;