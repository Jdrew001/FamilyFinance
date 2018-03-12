-- journal stored procedures

-- get all
DELIMITER //
CREATE PROCEDURE get_all_from_journal()
	BEGIN
		SELECT j.idjournal, j.date, j.amount, j.description, c.name AS 'categoryname', t.name AS 'transactionname' FROM journal j, category c, transactiontype t
        WHERE j.category = c.idcategory AND j.transactionid = t.idtransactionType;
    END //
DELIMITER ;

call get_all_from_journal();

-- get by id
DELIMITER //
CREATE PROCEDURE get_from_journal_by_id(IN ID INTEGER)
	BEGIN
		SELECT j.idjournal, j.date, j.amount, j.description, c.name AS 'categoryname', t.name AS 'transactionname' 
        FROM journal j, category c, transactiontype t
        WHERE J.category = c.idcategory AND j.idjournal = t.idtransactionType;
    END //
DELIMITER ;

call get_from_journal_by_id(1);

-- get by month
DELIMITER //
CREATE PROCEDURE get_journal_from_month(IN D DATETIME)
	BEGIN
		SELECT j.idjournal, j.date, j.amount, j.description, c.name AS 'categoryname', t.name AS 'transactionname' 
        FROM journal j, category c, transactiontype t
        WHERE j.category = c.idcategory AND j.transactionid = t.idtransactionType AND MONTH(j.date) = MONTH(D) AND YEAR(j.date) = YEAR(D);
    END //
DELIMITER ;

-- create journal
DELIMITER //
CREATE PROCEDURE create_journal(IN D DATETIME, C INTEGER, DES VARCHAR(85), A INTEGER, TID INTEGER)
	BEGIN
		INSERT INTO journal(date, category, description, amount, transactionid)
			VALUES(D, C, DES, A, TID);
    END //
DELIMITER ;

-- update journal
DELIMITER //
CREATE PROCEDURE update_journal(IN D DATETIME, C INTEGER, DES VARCHAR(85), A INTEGER, TID INTEGER, ID INTEGER)
	BEGIN
		UPDATE journal j
        SET j.date = D, j.category = C, j.description = DES, j.amount = A, j.transactionid = TID
        WHERE j.idjournal = ID;
    END //
DELIMITER ;

-- delete journal
DELIMITER //
CREATE PROCEDURE delete_journal(IN ID INTEGER)
	BEGIN
		DELETE FROM journal
        WHERE idjournal = ID;
    END //
DELIMITER ;