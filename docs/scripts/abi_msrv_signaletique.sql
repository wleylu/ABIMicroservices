create or replace FUNCTION           ABI_MSRV_SIGNALETIQUE(P_COMPTE VARCHAR2, P_AGENCE VARCHAR2) RETURN CLOB IS
--
-- First Implementation 27/08/2014
-- COULIBALY MELARGA
--
    V_INFOS         VARCHAR(503);
	V_VARSCORE      CLOB := NULL;
    V_STATUS        VARCHAR(3);
    V_COMPTE        VARCHAR(11);
    V_AGENCE        VARCHAR(5);
    V_DATFRM        DATE;
    V_TYP           VARCHAR(1);
    V_NCG           VARCHAR(6);
    V_TITMAN        VARCHAR(1);
    V_AGENCP        VARCHAR(5);
    V_NOM        	VARCHAR(80);
    V_PRENOMS       VARCHAR(120);
    V_CLIENT        VARCHAR(6);
    V_CLERIB        VARCHAR(2);
    V_CODBANK       VARCHAR(5);
	
	

BEGIN
    V_COMPTE := substr(P_COMPTE,1,11);
    V_AGENCE := substr(P_AGENCE,1,5);
    
    -- Verification des tables de travail
    BEGIN
        V_STATUS := 'OK';
        execute immediate 'truncate table w_ed_MSRV_SIGNALETIQUE';
        commit;
    EXCEPTION
        WHEN OTHERS THEN
        RETURN '97'; -- Sortir immediatement car la table de travail n'est pas cree
    END;    
    
    -- Verifions si le compte et l'agence sont valides
    BEGIN
        SELECT 'OK',DATFRM,TYP,NCG,AGENCE
        INTO V_STATUS,V_DATFRM,V_TYP,V_NCG,V_AGENCP  
        FROM CPT 
        WHERE COMPTE=V_COMPTE;
        
        IF  V_AGENCE <> V_AGENCP THEN
            RETURN '93'; -- COMPTE NON GERÉ PAR VOTRE AGENCE
        ELSE
            IF SUBSTR(V_NCG,1,4) NOT IN ('2511','2531') THEN
                RETURN '92'; -- La rubrique comptable du compte est inappropriee
            ELSE
                IF V_DATFRM IS NOT NULL THEN
                    RETURN '91'; -- COMPTE FERME
                ELSE
                    IF V_TYP NOT IN ('C','P') THEN
                        RETURN '90'; -- TYPE DE COMPTE DIFFERENT DE C ET P
                    END IF;
                END IF;
            END IF;
        END IF;
        
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            RETURN '94'; -- COMPTE INEXISTANT
            
        WHEN OTHERS THEN
            RETURN '89'; -- AUTRE ANOMALIE SUR COMPTE
    END;    
          
    -- Determinons si le compte passe en parametre est celui d'une entreprise ou
    -- d'un client Ordinaire
    BEGIN
        SELECT 'OK' INTO V_STATUS 
        FROM CPT CP, TITU T 
        WHERE CP.COMPTE=V_COMPTE AND CP.CLIENT=T.CLIENT AND T.IDP  IS NULL AND T.IDM IS NOT NULL;
    EXCEPTION
        WHEN OTHERS THEN
        V_STATUS := 'NOK';
    END;
    
    IF V_STATUS = 'OK'  THEN
 -- DBMS_OUTPUT.PUT_LINE ('Dans la boucle OK');
 
        BEGIN
            insert into w_ed_MSRV_SIGNALETIQUE 
            SELECT 
              DECODE(s.codbnq,'CI034','000343',
              '10082','000343',
              '10034','000697',
              'BJ115','101151',
              'TG138','901388',
              'BF134','201342',
              'NE136','701366',
              'ML135','301355',
              'SN137','901377','      '), 
              CL.AGENCE,
              CP.COMPTE COMPTE,
              CP.AGENCE,
              NVL(CP.NCG,' ') 
              ,NVL(I.CODCIVIL,' ') 
              ,SUBSTR(NVL(RPAD(TRIM(T.LIB12),40,' '),'                                        '),1,40)      
              ,SUBSTR(RPAD(TRIM(NVL(T.LIB14,' ') ||' '||NVL(T.LIB15,' ')),40,' '),1,40)      AS PRENOM
              ,SUBSTR(NVL(RPAD(TRIM(I.SEXE),1,' '),' '),1,1)
              ,SUBSTR(NVL(RPAD(TRIM(CL.PAYS),3,' '),'   '),1,3),
              ' ' 
              ,SUBSTR(NVL(RPAD(TRIM(I.SITU),1,' '),' '),1,1)
              ,SUBSTR(NVL(RPAD(TRIM(I.NUMID),16,' '),'                '),1,16)
              ,NVL(TO_CHAR(I.DATVALID,'YYYYMMDD'), '        '),
              '                              ' --VILLE CNI 30 POSITIONS  
              ,SUBSTR(NVL(RPAD(TRIM(I.PROFESSION),4,'0'),'0000'),1,4)
              ,SUBSTR(NVL(RPAD(decode(TRIM(CL.CODAPE),'**','00',TRIM(CL.CODAPE)),4,'0'),'0000'),1,4)
              ,SUBSTR(NVL(RPAD(TRIM(CP.DEVISE),3,' '),'   '),1,3),
              DECODE(ck.client,null,SUBSTR(NVL(RPAD(TRIM(CL.TYPCPT),1,' '),' '),1,1),'P'),
              SUBSTR(NVL(RPAD(TRIM(CL.LANGUE),3,' '),'   '),1,3),
              NVL(TO_CHAR(I.DATNAIS,'YYYYMMDD'), '        '),
              SUBSTR(NVL(RPAD(TRIM(I.COMMNAIS),30,' '),'                              '),1,30),
              SUBSTR(NVL(RPAD(TRIM(I.PAYSNAIS),30,' '),'                              '),1,30),
              SUBSTR(NVL(RPAD(TRIM(I.ADR1),30,' '),'                              '),1,30),
              SUBSTR(NVL(RPAD(TRIM(I.ADR2),30,' '),'                              '),1,30),
              SUBSTR(NVL(RPAD(TRIM(I.ADR3),30,' '),'                              '),1,30),
              SUBSTR(NVL(RPAD(TRIM(I.ADR4),30,' '),'                              '),1,30),
              SUBSTR(NVL(LPAD(TRIM(I.DEPRES),5,'0'),'00000'),1,5),
              '          ', -- CODE POSTAL NON DEFINI
              SUBSTR(NVL(RPAD(TRIM(I.PAYRES),2,' '),'   '),1,2),
              SUBSTR(NVL(RPAD(TRIM(I.TEL),16,' '),'                '),1,16),
              SUBSTR(NVL(RPAD(TRIM(I.MAIL),35,' '),'                                   '),1,35),
              SUBSTR(NVL(RPAD(TRIM(I.ADR5),30,' '),'                              '),1,30),
              NVL(T.IDP,'01'),
              CP.TYP
              ,SUBSTR(NVL(RPAD(TRIM(CP.NOM),40,' '),'                '),1,40)
              
        FROM CPT CP, CLI CL,IDP I,TITU T,state s,compackadh ck
        WHERE CP.COMPTE = V_COMPTE
          AND CP.AGENCE = V_AGENCE
          AND CL.CLIENT = CP.CLIENT
          AND CL.CLIENT = ck.CLIENT(+)
          AND T.CLIENT = CL.CLIENT
          AND SUBSTR(CP.NCG,1,4) IN ('2511','2531')
          AND CP.typ    IN ('C','P')
          AND cp.datfrm is null
          AND T.IDM    IS NOT NULL
          AND T.IDP    IS NULL
          AND I.IDP = (SELECT MIN(D.IDPSIGN) IDPSIGN
                          FROM IDMMAND D WHERE D.IDM=T.IDM );

        COMMIT;
        
        EXCEPTION            
            WHEN OTHERS THEN
            RETURN '96'; -- AUTRES TYPE D'ANOMALIE SUR LE COMPTE PERSONNE MORALE        
        END;
    ELSE
      
        BEGIN
            insert into w_ed_MSRV_SIGNALETIQUE
            SELECT 
              DECODE(s.codbnq,'CI034','000343',
              '10082','000343',
              '10034','000697',
              'BJ115','101151',
              'TG138','901388',
              'BF134','201342',
              'NE136','701366',
              'ML135','301355',
              'SN137','901377','      '),
              CL.AGENCE,
              CP.COMPTE COMPTE,
              CP.AGENCE,
              NVL(CP.NCG,' ') 
              ,NVL(I.CODCIVIL,' ') 
              ,SUBSTR(NVL(RPAD(TRIM(T.LIB12),40,' '),'                                        '),1,40)      
              ,SUBSTR(RPAD(TRIM(NVL(T.LIB14,' ') ||' '||NVL(T.LIB15,' ')),40,' '),1,40)      AS PRENOM
              ,SUBSTR(NVL(RPAD(TRIM(I.SEXE),1,' '),' '),1,1)
              ,SUBSTR(NVL(RPAD(TRIM(CL.PAYS),3,' '),'   '),1,3),
              ' ' 
              ,SUBSTR(NVL(RPAD(TRIM(I.SITU),1,' '),' '),1,1)
              ,SUBSTR(NVL(RPAD(TRIM(I.NUMID),16,' '),'                '),1,16)
              ,NVL(TO_CHAR(I.DATVALID,'YYYYMMDD'), '        '),
              '                              ' --VILLE CNI 30 POSITIONS  
              ,SUBSTR(NVL(RPAD(TRIM(I.PROFESSION),4,'0'),'0000'),1,4)
              ,SUBSTR(NVL(RPAD(decode(TRIM(CL.CODAPE),'**','00',TRIM(CL.CODAPE)),4,'0'),'0000'),1,4)
              ,SUBSTR(NVL(RPAD(TRIM(CP.DEVISE),3,' '),'   '),1,3),
              DECODE(ck.client,null,SUBSTR(NVL(RPAD(TRIM(CL.TYPCPT),1,' '),' '),1,1),'P'),
              SUBSTR(NVL(RPAD(TRIM(CL.LANGUE),3,' '),'   '),1,3),
              NVL(TO_CHAR(I.DATNAIS,'YYYYMMDD'), '        '),
              SUBSTR(NVL(RPAD(TRIM(I.COMMNAIS),30,' '),'                              '),1,30),
              SUBSTR(NVL(RPAD(TRIM(I.PAYSNAIS),30,' '),'                              '),1,30),
              SUBSTR(NVL(RPAD(TRIM(I.ADR1),30,' '),'                              '),1,30),
              SUBSTR(NVL(RPAD(TRIM(I.ADR2),30,' '),'                              '),1,30),
              SUBSTR(NVL(RPAD(TRIM(I.ADR3),30,' '),'                              '),1,30),
              SUBSTR(NVL(RPAD(TRIM(I.ADR4),30,' '),'                              '),1,30),
              SUBSTR(NVL(LPAD(TRIM(I.DEPRES),5,'0'),'00000'),1,5),
              '          ', -- CODE POSTAL NON DEFINI
              SUBSTR(NVL(RPAD(TRIM(I.PAYRES),2,' '),'   '),1,2),
              SUBSTR(NVL(RPAD(TRIM(I.TEL),16,' '),'                '),1,16),
              SUBSTR(NVL(RPAD(TRIM(I.MAIL),35,' '),'                                   '),1,35),
              SUBSTR(NVL(RPAD(TRIM(I.ADR5),30,' '),'                              '),1,30),
              NVL(T.IDP,'01'),
              CP.TYP
              ,SUBSTR(NVL(RPAD(TRIM(CP.NOM),40,' '),'                '),1,40)
        FROM  cpt  CP,CLI CL, IDP I, titu T, state s,compackadh ck
             ,(SELECT A1.CLIENT,MIN(A1.NUM) NUM FROM TITU A1 
                WHERE A1.TITMAN = 'T' AND A1.IDP IS NOT NULL GROUP BY A1.CLIENT) TIT 
        WHERE CP.COMPTE = V_COMPTE
          AND CP.AGENCE = V_AGENCE
          AND    CP.typ in ('C','P')
          and CP.datfrm is null
          AND SUBSTR(CP.NCG,1,4) IN ('2511','2531')
          AND t.client=TIT.client 
          AND T.NUM = TIT.NUM
          AND  T.client = CL.client 
          AND T.idp = I.idp
          AND CP.client = cl.client
          AND CL.CLIENT = ck.CLIENT(+);


        COMMIT;    
        
        EXCEPTION        
            WHEN OTHERS THEN
            RETURN '95'; -- AUTRES TYPE D'ANOMALIE SUR LE COMPTE PERSONNE PHYSIQUE
            
        END;    
    END IF;
    
    
    -- FORMATAGE ET ENVOIS DES INFORMATIONS SI PAS EXCEPTION GENEREE
    --
    BEGIN
	
        SELECT 
			ct.client,
			ct.compte,
			ct.ncg,
			ct.clerib,
			s.codbnq,
			cp.agence,
			cp.nomcpt,
			cp.prenoms
		
    /*     ||substr(RPAD(trim(cp.agence),6,' '),1,6)
         ||substr(RPAD(TRIM(cp.COMPTE),24,' '),1,24)
         ||substr(RPAD(trim(cp.agence),6,' '),1,6)  
         ||RPAD(decode(substr(trim(cp.ncg),1,3),'253', '10','00') ,2, ' ') 
         ||substr(RPAD(decode(trim(cp.idp),'01','STE',decode(trim(cp.codcivil),'2','MR','3','MME','4','MLLE','MR')),4,' '),1,4)
         ||UPPER(decode(trim(cp.idp),'01',substr(cp.nomcpt,1,40)||substr(cp.nomcpt,1,40),decode(cp.prenoms,null,substr(cp.nom,1,40)||substr(cp.nom,1,40) ,decode(length(trim(cp.prenoms)),0,substr(cp.nom,1,40)||substr(cp.nom,1,40),substr(cp.nom,1,40)||substr(cp.prenoms,1,40)))))
         ||substr(decode(trim(cp.idp),'01','N',decode(cp.sexe,' ','N',cp.sexe)),1,1)
         -- ||decode((select 'OK' from pays p4  where trim(p4.iso)=trim(cp.payres)),'OK',(select substr(RPAD(NVL(trim(pa.ISO3N),'000'),3,' '),1,3) from pays pa where trim(pa.iso)=trim(cp.payres)),DECODE(s.codbnq,'CI034','384','10082','384','BJ115','204','TG138','768', 'BF134','854','NE136','562','ML135','466','SN137','686','10034','120','384'))
         ||decode((select 'OK' from pays p4  where trim(p4.iso)=trim(cp.payres)),'OK',(select substr(RPAD(NVL(trim(pa.ISO3N),DECODE(s.codbnq,'CI034','384','10082','384','BJ115','204','TG138','768', 'BF134','854','NE136','562','ML135','466','SN137','686','10034','120','384')),3,' '),1,3) from pays pa where trim(pa.iso)=trim(cp.payres)),DECODE(s.codbnq,'CI034','384','10082','384','BJ115','204','TG138','768', 'BF134','854','NE136','562','ML135','466','SN137','686','10034','120','384'))
         ||substr(decode(trim(cp.idp),'01','0','0'),1,1)
         ||substr(decode(trim(cp.idp),'01','N',decode(trim(cp.situ),'C','C','M','M','C')),1,1)
         ||substr(cp.numid,1,16)
         ||RPAD(substr(decode(trim(cp.idp),'01','20991231',cp.datvalid),1,8),8,' ')
         ||substr(cp.villecin,1,30)
         ||substr(decode(trim(cp.idp),'01','0099',cp.profession),1,4)
         ||substr(decode(trim(cp.idp),'01','0990',cp.codape),1,4)
         ||substr(cp.devise,1,3)
         ||decode(cp.typcpt,'P','P',substr(decode(trim(cp.idp),'01','C',decode(cp.typcpt,'1',decode(cp.typ,'P','S','I'),'2',decode(cp.typ,'P','S','I'),'3','C',cp.typcpt)),1,1))
         ||substr(decode(trim(cp.langue),'F','FRE','ENG'),1,3)
         ||substr(cp.datnais,1,8)
         ||substr(cp.commnais,1,30)
         ||substr(cp.paysnais,1,30)
         ||substr(cp.adr1,1,30)
         ||substr(cp.adr2,1,30)
         ||substr(cp.adr3,1,30)
         ||substr(cp.adr4,1,30)
         ||decode((select 'OK' from pays p  where trim(p.iso)=trim(cp.payres)),'OK',decode((select 'NOK' from dual where trim(cp.payres) not in ('CI','BJ','TG','BF','NE','ML','SN') ),'NOK','00000',substr(decode(LENGTH(TRIM(TRANSLATE(cp.depres, ' +-.0123456789',' '))),null,cp.depres,'00000'),1,5)),'00000')
         ||substr(cp.codpost,1,10)
         ||decode((select 'OK' from pays p2  where trim(p2.iso)=trim(cp.payres)),'OK',(select substr(RPAD(NVL(trim(p3.ISO3N),DECODE(s.codbnq,'CI034','384','10082','384','BJ115','204','TG138','768', 'BF134','854','NE136','562','ML135','466','SN137','686','10034','120','384')),3,' '),1,3) from pays p3 where trim(p3.iso)=trim(cp.payres)),DECODE(s.codbnq,'CI034','384','10082','384','BJ115','204','TG138','768', 'BF134','854','NE136','562','ML135','466','SN137','686','10034','120','384'))
         ||substr(cp.tel,1,16)
         ||substr(cp.mail,1,35)
         ||decode(( select substr(RPAD(NVL(trim(f.y1),' '),30,'                              '),1,30) from fx5y8 f where tname='TITU' and model='DEPNAIS'  
             and x1=(substr(decode(LENGTH(TRIM(TRANSLATE(cp.depres, ' +-.0123456789',' '))),null,lpad(substr(trim(cp.depres),4,5),2,'0'),'00'),1,2))),'','INCONNU                       ',( select substr(RPAD(NVL(trim(f.y1),' '),30,'                              '),1,30) from fx5y8 f where tname='TITU' and model='DEPNAIS'  
             and x1=(substr(decode(LENGTH(TRIM(TRANSLATE(cp.depres, ' +-.0123456789',' '))),null,lpad(substr(trim(cp.depres),4,5),2,'0'),'00'),1,2))))  
         || RPAD(NVL(trim(ct.client),' '),7,' ') LIGNE
	
		*/
		
        INTO V_CLIENT,V_COMPTE,V_NCG,V_CLERIB,V_CODBANK,V_AGENCE,V_NOM,V_PRENOMS
        FROM  w_ed_MSRV_SIGNALETIQUE cp, state s, cpt ct    
        WHERE  substr(cp.compte,1,11)=ct.compte and substr(cp.compte,1,11)=V_COMPTE and rownum <2;
        
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            IF V_STATUS = 'OK' THEN
                RETURN '88'; -- PAS DE TITULAIRE SUR LE COMPTE PERSONNE MORALE
            ELSE
                RETURN '87'; -- PAS DE TITULAIRE SUR LE COMPTE PERSONNE PHYSIQUE
            END IF;
        WHEN OTHERS THEN
        
        RETURN '01'; -- PAS DE DONNEES
    
    END;
	
    V_VARSCORE :='{ "success": true, "client":"'||V_CLIENT
    ||'","compte": "'||V_COMPTE
    ||'","rubriqueComptable": "'||V_NCG 
    ||'","clerib": "'||V_CLERIB
    ||'","codebanque": "'||V_CODBANK
    ||'","agence": "'||V_AGENCE 
    ||'","nom": "'||V_NOM 
    ||'","prenoms": "'||V_PRENOMS
    ||'" }';
       
	
   RETURN V_VARSCORE;

END ABI_MSRV_SIGNALETIQUE;
/