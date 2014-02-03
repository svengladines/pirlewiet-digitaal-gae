INSERT INTO credentials (username,password,enabled,emailadres) VALUES ('d','3c363836cf4e16666669a25da280a1865c2d2874',1,'none@no.be');
INSERT INTO credentials_roles (credentials_id,roles) VALUES (1,'ROLE_DIENST');
INSERT INTO credentials (username,password,enabled,emailadres) VALUES ('s','a0f1490a20d0211c997b44bc357e1972deab8ae3',1,'none@no.zem');
INSERT INTO credentials_roles (credentials_id,roles) VALUES (2,'ROLE_SECRETARIAAT');
INSERT INTO credentials (username,password,enabled,emailadres) VALUES ('p','516b9783fca517eecbd1d064da2d165310b19759',1,'none@no.one.be');
INSERT INTO credentials_roles (credentials_id,roles) VALUES (3,'ROLE_SUPERUSER');
INSERT INTO credentials (username,password,enabled,emailadres) VALUES ('dienst1','3c363836cf4e16666669a25da280a1865c2d2874',1,'none@no.be');
INSERT INTO credentials_roles (credentials_id,roles) VALUES (4,'ROLE_DIENST');
INSERT INTO credentials (username,password,enabled,emailadres) VALUES ('dienst2','3c363836cf4e16666669a25da280a1865c2d2874',1,'none@no.be');
INSERT INTO credentials_roles (credentials_id,roles) VALUES (5,'ROLE_DIENST');
INSERT INTO credentials (username,password,enabled,emailadres) VALUES ('dienst3','3c363836cf4e16666669a25da280a1865c2d2874',1,'none@no.be');
INSERT INTO credentials_roles (credentials_id,roles) VALUES (6,'ROLE_DIENST');
INSERT INTO credentials (username,password,enabled,emailadres) VALUES ('dienst4','3c363836cf4e16666669a25da280a1865c2d2874',1,'none@no.be');
INSERT INTO credentials_roles (credentials_id,roles) VALUES (7,'ROLE_DIENST');
INSERT INTO credentials (username,password,enabled,emailadres) VALUES ('dienst5','3c363836cf4e16666669a25da280a1865c2d2874',1,'none@no.be');
INSERT INTO credentials_roles (credentials_id,roles) VALUES (8,'ROLE_DIENST');


INSERT INTO vakantietype (id, naam, displaynaam) VALUES (1,'PAASKINDERKAMP','Paaskinderkamp');
INSERT INTO vakantietype (id, naam, displaynaam) VALUES (2,'PAASGEZINSVAKANTIE','Paasgezinsvakantie');
INSERT INTO vakantietype (id, naam, displaynaam) VALUES (3,'KINDERKAMP','Kinderkamp');
INSERT INTO vakantietype (id, naam, displaynaam) VALUES (4,'TIENERKAMP','Tienerkamp');
INSERT INTO vakantietype (id, naam, displaynaam) VALUES (5,'GEZINSVAKANTIE','Gezinsvakantie');
INSERT INTO vakantietype (id, naam, displaynaam) VALUES (6,'VOLWASSENENVAKANTIE','Vakantie onder volwassenen');
INSERT INTO vakantietype (id, naam, displaynaam) VALUES (7,'DRIEDAAGSE','Driedaagse');
INSERT INTO vakantietype (id, naam, displaynaam) VALUES (8,'BEGELEIDWONENBRUSSEL','Begeleid wonen brussel');
INSERT INTO vakantietype (id, naam, displaynaam) VALUES (9,'DUMMYVAKANTIE','Dummy; Onbekend vakantietype bij import uit excel.');

INSERT INTO vakantieproject (vakantietype_id,beginDatum,eindDatum,beginInschrijving, eindInschrijving) VALUES(3,'2012-07-11','2012-08-11','2012-01-01','2012-05-31');
INSERT INTO vakantieproject (vakantietype_id,beginDatum,eindDatum,beginInschrijving, eindInschrijving) VALUES(5,'2012-09-02','2012-09-16','2012-03-01','2012-08-15');
INSERT INTO vakantieproject (vakantietype_id,beginDatum,eindDatum,beginInschrijving, eindInschrijving) VALUES(6,'2012-08-05','2012-08-16','2012-02-01','2012-06-30');

INSERT INTO dienst (gemeente,nummer,postcode,straat,emailadres,naam,telefoonnummer,credentials_id, afdeling) VALUES ('Heist-op-den-Berg','20','2220','Stationstraat','ocmw@heist-op-den-berg.be','OCMW Heist-op-den-Berg','015/123456',1, 'OCMW 1');
INSERT INTO dienst (gemeente,nummer,postcode,straat,emailadres,naam,telefoonnummer,credentials_id, afdeling) VALUES ('OCMW Heist-op-den-Berg','22','3600','Kerkstraat','ocmw@heist-op-den-berg.be','ocmw Heist-op-den-Berg','015/654321',4, 'ocmw 2 brussel');
INSERT INTO dienst (gemeente,nummer,postcode,straat,emailadres,naam,telefoonnummer,credentials_id, afdeling) VALUES ('Antwerpen','22','3600','Kerkstraat','ocmw@heist-op-den-berg.be','Antwerpen','015/654321',5, 'OCMW Kruis');
INSERT INTO dienst (gemeente,nummer,postcode,straat,emailadres,naam,telefoonnummer,credentials_id, afdeling) VALUES ('Brugge','22','3600','Kerkstraat','ocmw@heist-op-den-berg.be','Brugge','015/654321',6, 'OCMW afdeling');
INSERT INTO dienst (gemeente,nummer,postcode,straat,emailadres,naam,telefoonnummer,credentials_id, afdeling) VALUES ('Brussel','22','3600','Kerkstraat','ocmw@heist-op-den-berg.be','Brussel rode kruis','015/654321',7, 'ocmw 2');
INSERT INTO dienst (gemeente,nummer,postcode,straat,emailadres,naam,telefoonnummer,credentials_id, afdeling) VALUES ('Genk','22','3600','Kerkstraat','ocmw@heist-op-den-berg.be','kruis','015/654321',8, 'rode kruis');
INSERT INTO persoon (discriminator,id,familienaam,voornaam,telefoonnr,email,gsmnr,rijksregisternr,geboortedatum) VALUES ('D',1,'Van Woensel','Gerd','015/242811','gerdvanwoensel@pirlewiet.be','0496077199','254-12 23.23.23','1989-10-02');
INSERT INTO persoon (discriminator,id,familienaam,voornaam,telefoonnr,email,gsmnr,rijksregisternr,geboortedatum,ffn) VALUES ('D',4,'Truyens','Peter','015/242812','tp@pirlewiet.be','0496077198','254-12 24.24.24','1988-10-02','truyens');
INSERT INTO persoon (discriminator,id,familienaam,voornaam,telefoonnr,email,gsmnr,rijksregisternr,geboortedatum) VALUES ('D',5,'Haesevoet','Joris','015/242813','hj@pirlewiet.be','0496077197','254-12 25.25.25','1987-10-02');
INSERT INTO persoon (discriminator,id,familienaam,voornaam,telefoonnr,email,gsmnr,rijksregisternr,geboortedatum,dienst_id,isPassive) VALUES ('C',2,'Van Goeddoel','Sofie','015/241325','s.vg@pirlewiet.be','0496012398','254-11 98.87.23','1989-10-02',1,false);
INSERT INTO persoon (discriminator,id,familienaam,voornaam,telefoonnr,email,gsmnr,rijksregisternr,geboortedatum,dienst_id,isPassive,functie) VALUES ('C',3,'Van Goeddoel','Frans','015/242832','f.vg@pirlewiet.be','0496012399','254-12 98.87.23','1989-10-03',1,false,'verantwoordelijke');
INSERT INTO persoon (discriminator,id,familienaam,voornaam,credentials_id, displayed) VALUES ('S',6,'werker','mede',1, true);
INSERT INTO persoon (discriminator,id,familienaam,voornaam,credentials_id, displayed) VALUES ('S',7,'marco','not displayed',2, true);

INSERT INTO aanvraaginschrijving (id,vakantieproject_id,gezinsnummer,contactpersoon_id,inschrijvingsdatum,contactType,straat,nummer,gemeente) VALUES (1,1,1,2,'2012-07-17',1,'Goorse baan',22,'Goor');
INSERT INTO aanvraaginschrijving_persoon (inschrijvingen_id,deelnemers_id) VALUES (1,1);

INSERT INTO aanvraaginschrijving (id,vakantieproject_id,gezinsnummer,contactpersoon_id,status,contactType) VALUES (2,2,1,2,1,0);
INSERT INTO aanvraaginschrijving_persoon (inschrijvingen_id,deelnemers_id) VALUES (2,5);
INSERT INTO aanvraaginschrijving_persoon (inschrijvingen_id,deelnemers_id) VALUES (2,1);
INSERT INTO aanvraaginschrijving_persoon (inschrijvingen_id,deelnemers_id) VALUES (2,4);

INSERT INTO aanvraaginschrijving (id,vakantieproject_id,gezinsnummer,contactpersoon_id,status,contactType) VALUES (3,3,1,3,2,1);
INSERT INTO aanvraaginschrijving_persoon (inschrijvingen_id,deelnemers_id) VALUES (3,1);

INSERT INTO aanvraaginschrijving (id,vakantieproject_id,gezinsnummer,contactpersoon_id,status,contactType) VALUES (4,1,1,3,3,0);
INSERT INTO aanvraaginschrijving_persoon (inschrijvingen_id,deelnemers_id) VALUES (4,4);
