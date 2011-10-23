CREATE
    TABLE brand
    (
        id BIGINT NOT NULL AUTO_INCREMENT,
        description LONGTEXT,
        erased BIT NOT NULL,
        name VARCHAR(255) NOT NULL,
        version INT,
        PRIMARY KEY (id)
    )
    ENGINE=InnoDB ;
CREATE
    TABLE category
    (
        id BIGINT NOT NULL AUTO_INCREMENT,
        description LONGTEXT,
        erased BIT NOT NULL,
        name VARCHAR(255) NOT NULL,
        version INT,
        PRIMARY KEY (id)
    )
    ENGINE=InnoDB ;
CREATE
    TABLE certificate_type
    (
        id BIGINT NOT NULL AUTO_INCREMENT,
        description LONGTEXT,
        erased BIT NOT NULL,
        name VARCHAR(255) NOT NULL,
        version INT,
        PRIMARY KEY (id)
    )
    ENGINE=InnoDB ;
CREATE
    TABLE COMMENT
    (
        id BIGINT NOT NULL AUTO_INCREMENT,
        content LONGTEXT,
        creation_date DATETIME NOT NULL,
        erased BIT NOT NULL,
        version INT,
        owner BIGINT NOT NULL,
        reply_to BIGINT,
        PRIMARY KEY (id),
        INDEX FK38A5EE5F74417EDB (reply_to),
        INDEX FK38A5EE5FE4387E6D (owner)
    )
    ENGINE=InnoDB ;
CREATE
    TABLE employee
    (
        id BIGINT NOT NULL AUTO_INCREMENT,
        account_enabled BIT NOT NULL,
        account_non_expired BIT NOT NULL,
        account_non_locked BIT NOT NULL,
        credentials_non_expired BIT NOT NULL,
        email VARCHAR(255) NOT NULL,
        identity_number VARCHAR(100) NOT NULL,
        type INT NOT NULL,
        erased BIT NOT NULL,
        name LONGTEXT NOT NULL,
        password VARCHAR(255) NOT NULL,
        phone_number VARCHAR(255),
        start_date DATETIME NOT NULL,
        version INT,
        image_id BIGINT,
        title BIGINT NOT NULL,
        PRIMARY KEY (id),
        CONSTRAINT email UNIQUE (email),
        INDEX FK4722E6AEAB050D16 (title),
        INDEX FK4722E6AEE757F366 (image_id)
    )
    ENGINE=InnoDB ;
CREATE
    TABLE employee_authorities
    (
        employee BIGINT NOT NULL,
        authority VARCHAR(100),
        INDEX FK7F03C31025077268 (employee)
    )
    ENGINE=InnoDB ;
CREATE
    TABLE employee_certificates
    (
        employee BIGINT NOT NULL,
        expired BIT,
        given_date DATETIME,
        type BIGINT NOT NULL,
        PRIMARY KEY (employee, type),
        INDEX FK422A3FAD25077268 (employee),
        INDEX FK422A3FAD481E061F (type)
    )
    ENGINE=InnoDB ;
CREATE
    TABLE employee_title
    (
        id BIGINT NOT NULL AUTO_INCREMENT,
        description LONGTEXT,
        erased BIT NOT NULL,
        name VARCHAR(255) NOT NULL,
        version INT,
        PRIMARY KEY (id)
    )
    ENGINE=InnoDB ;
CREATE
    TABLE equipment
    (
        id BIGINT NOT NULL AUTO_INCREMENT,
        best_before_date DATETIME NOT NULL,
        erased BIT NOT NULL,
        product_code LONGTEXT,
        product_name LONGTEXT,
        production_date DATETIME NOT NULL,
        stock_entrance DATETIME NOT NULL,
        version INT,
        brand BIGINT NOT NULL,
        category BIGINT NOT NULL,
        PRIMARY KEY (id),
        INDEX FK4027E58EAF8EA33A (brand),
        INDEX FK4027E58ED0F3EE50 (category)
    )
    ENGINE=InnoDB ;
CREATE
    TABLE equipment_images
    (
        equipment BIGINT NOT NULL,
        images BIGINT NOT NULL,
        PRIMARY KEY (equipment, images),
        CONSTRAINT images UNIQUE (images),
        INDEX FKFCCDDE49D3EEEF5F (images),
        INDEX FKFCCDDE49132FE148 (equipment)
    )
    ENGINE=InnoDB ;
CREATE
    TABLE equipment_inspection_reports
    (
        equipment BIGINT NOT NULL,
        inspection_reports BIGINT NOT NULL,
        PRIMARY KEY (equipment, inspection_reports),
        CONSTRAINT inspection_reports UNIQUE (inspection_reports),
        INDEX FK3F197C4550BE9A10 (inspection_reports),
        INDEX FK3F197C45132FE148 (equipment)
    )
    ENGINE=InnoDB ;
CREATE
    TABLE image
    (
        id BIGINT NOT NULL AUTO_INCREMENT,
        description VARCHAR(255),
        erased BIT NOT NULL,
        image_data LONGBLOB NOT NULL,
        mime_type VARCHAR(255) NOT NULL,
        thumbnail_data LONGBLOB,
        title VARCHAR(255),
        upload_id VARCHAR(255) NOT NULL,
        version INT,
        PRIMARY KEY (id)
    )
    ENGINE=InnoDB ;
CREATE
    TABLE inspection_report
    (
        id BIGINT NOT NULL AUTO_INCREMENT,
        erased BIT NOT NULL,
        inspection_date DATETIME NOT NULL,
        report LONGTEXT,
        status INT NOT NULL,
        version INT,
        inspector BIGINT NOT NULL,
        PRIMARY KEY (id),
        INDEX FK4440FA5FD900C6B1 (inspector)
    )
    ENGINE=InnoDB ;
CREATE
    TABLE inspection_report_images
    (
        inspection_report BIGINT NOT NULL,
        images BIGINT NOT NULL,
        PRIMARY KEY (inspection_report, images),
        CONSTRAINT images UNIQUE (images),
        INDEX FK40EBEC585121427B (inspection_report),
        INDEX FK40EBEC58D3EEEF5F (images)
    )
    ENGINE=InnoDB ;
CREATE
    TABLE work_definition
    (
        id BIGINT NOT NULL AUTO_INCREMENT,
        customer LONGTEXT NOT NULL,
        end_date DATETIME,
        erased BIT NOT NULL,
        name LONGTEXT NOT NULL,
        start_date DATETIME NOT NULL,
        version INT,
        supervisor BIGINT NOT NULL,
        PRIMARY KEY (id),
        INDEX FK26295F4178BADF22 (supervisor)
    )
    ENGINE=InnoDB ;
CREATE
    TABLE work_definition_comments
    (
        work_definition BIGINT NOT NULL,
        comments BIGINT NOT NULL,
        PRIMARY KEY (work_definition, comments),
        INDEX FKE1144932F4F725CE (work_definition),
        INDEX FKE114493269F4DCDF (comments)
    )
    ENGINE=InnoDB ;
CREATE
    TABLE work_definition_equipments
    (
        work_definition BIGINT NOT NULL,
        equipments BIGINT NOT NULL,
        PRIMARY KEY (work_definition, equipments),
        INDEX FK7C636063F4F725CE (work_definition),
        INDEX FK7C63606397DCC85F (equipments)
    )
    ENGINE=InnoDB ;
CREATE
    TABLE work_definition_workers
    (
        work_definition BIGINT NOT NULL,
        workers BIGINT NOT NULL,
        PRIMARY KEY (work_definition, workers),
        INDEX FKDF3251B7F4F725CE (work_definition),
        INDEX FKDF3251B738CCA86F (workers)
    )
    ENGINE=InnoDB ;
