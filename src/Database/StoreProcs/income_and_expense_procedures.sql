-- get all income
DELIMITER //
CREATE PROCEDURE get_all_income()
	BEGIN
		SELECT p.idIncome, p.amount, p.date, u.idUser, u.firstname, u.lastname, u.username, c.idcategory, c.name as 'categoryname', t.idtransactiontype, t.name as 'transactionname'
		FROM income p, user u, category c, transactiontype t
		where p.category = c.idcategory and p.userid = u.idUser and p.transactionid = t.idtransactionType
        ORDER BY p.date ASC;
    END //
DELIMITER ;

-- get all expense
DELIMITER //
CREATE PROCEDURE get_all_expense()
	BEGIN
		SELECT e.idexpense, e.amount, e.date, u.idUser, u.firstname, u.lastname, u.username, c.idcategory, c.name as 'categoryname', t.idtransactionType, t.name as 'transactionname' FROM expense e, user u, category c, transactiontype t
		where e.categoryid = c.idcategory and e.userid = u.idUser and e.transactionid = t.idtransactionType;
    END //
DELIMITER ;

-- get all income created by a specific user
DELIMITER //
CREATE PROCEDURE get_income_by_userid(IN ID INTEGER)
	BEGIN
		SELECT p.idIncome, p.amount, p.date, c.idcategory, c.name as 'categoryname', t.idtransactiontype, t.name as 'transactionname'
		FROM income p, category c, transactiontype t
		where p.category = c.idcategory and p.transactionid = t.idtransactionType and p.userid = ID
        ORDER BY p.date ASC;
    END //
DELIMITER ;

-- get all expense created by a specific user
DELIMITER //
	CREATE PROCEDURE get_expense_by_userid(IN ID INTEGER)
    BEGIN
		SELECT e.idexpense, e.amount, e.date, c.idcategory, c.name as 'categoryname', t.idtransactionType, t.name as 'transactionname' FROM expense e, category c, transactiontype t
		where e.categoryid = c.idcategory and e.userid = ID and e.transactionid = t.idtransactionType;
    END //
DELIMITER ;

DELIMITER //
CREATE PROCEDURE get_income_by_month(IN D DATE)
	BEGIN
		SELECT p.idIncome, p.amount, p.date, u.idUser, u.firstname, u.lastname, u.username, c.idcategory, c.name as 'categoryname', t.idtransactiontype, t.name as 'transactionname'
		FROM income p, category c, transactiontype t, user u
		where p.category = c.idcategory and p.userid = u.idUser and p.transactionid = t.idtransactionType and MONTH(p.date) = MONTH(D) AND YEAR(p.date) = YEAR(D)
        ORDER BY p.date ASC;
    END //
DELIMITER ;

DELIMITER //
CREATE PROCEDURE get_expense_by_month(IN D DATE)
	BEGIN
		SELECT e.idexpense, e.amount, e.date, u.idUser, u.firstname, u.lastname, u.username, c.idcategory, c.name as 'categoryname', t.idtransactiontype, t.name as 'transactionname'
		FROM expense e, category c, transactiontype t, user u
		where e.categoryid = c.idcategory and e.userid = u.idUser and e.transactionid = t.idtransactionType and MONTH(e.date) = MONTH(D) AND YEAR(e.date) = YEAR(D)
        ORDER BY e.date ASC; 
    END //
DELIMITER ;


call get_income_by_month('');

-- get income by id
DELIMITER //
CREATE PROCEDURE get_income_by_id(IN ID INTEGER)
	BEGIN
		SELECT p.idIncome, p.amount, p.date, u.idUser, u.firstname, u.lastname, u.username, c.idcategory, c.name as 'categoryname', t.idtransactiontype, t.name as 'transactionname'
		FROM income p, user u, category c, transactiontype t
		where p.category = c.idcategory and p.userid = u.idUser and p.transactionid = t.idtransactionType and p.idIncome = ID;
    END //
DELIMITER ;

select * from income;

-- get expense by id
DELIMITER //
CREATE PROCEDURE get_expense_by_id(IN ID INTEGER)
	BEGIN
		SELECT e.idexpense, e.amount, e.date, u.idUser, u.firstname, u.lastname, u.username, c.idcategory, c.name as 'categoryname', t.idtransactionType, t.name as 'transactionname' FROM expense e, user u, category c, transactiontype t
		where e.categoryid = c.idcategory and e.userid = u.idUser and e.transactionid = t.idtransactionType and e.idexpense = ID;
    END //
DELIMITER ;

call get_expense_by_id(1);

-- add income
DELIMITER //
CREATE PROCEDURE add_income(IN A DECIMAL(65, 2), C INTEGER, D Date, UID INTEGER, TID INTEGER)
	BEGIN
		INSERT INTO income (amount, category, date, userid, transactionid)
			VALUES(A, C, D, uid, TID);
    END //
DELIMITER ;

call add_expense(50.00, 5, '2018-02-23', 1, 2);

call get_all_income();

-- add expense
DELIMITER //
CREATE PROCEDURE add_expense(IN A DECIMAL, C INTEGER, D DATETIME, UID INTEGER, TID INTEGER)
	BEGIN
		INSERT INTO expense(amount, categoryid, date, userid, transactionid)
			VALUES(A, C, D, UID, TID);
	END //
DELIMITER ;

-- delete income
DELIMITER //
CREATE PROCEDURE delete_income(IN ID INTEGER)
	BEGIN
		DELETE FROM income
        WHERE idIncome = ID;
    END //
DELIMITER ;

-- delete expense
DELIMITER //
CREATE PROCEDURE delete_expense(IN ID INTEGER)
	BEGIN
		DELETE FROM expense
        WHERE idexpense = ID;
    END //
DELIMITER ;

-- update income
DELIMITER //
CREATE PROCEDURE update_income(IN A DECIMAL, C INTEGER, D DATETIME, UID INTEGER, TID INTEGER, ID INTEGER)
	BEGIN
		UPDATE income i 
        SET i.amount = A, i.category = C, i.date = D, i.userid = UID, i.transactionid = TID
        WHERE i.idIncome = ID;
    END //
DELIMITER ;

-- update expense
DELIMITER //
CREATE PROCEDURE update_expense(IN A INTEGER, C INTEGER, D DATETIME, UID INTEGER, TID INTEGER, ID INTEGER)
	BEGIN
		UPDATE expense e 
        SET e.amount = A, e.categoryid = C, e.date = D, e.userid = UID, e.transactionid = TID
        WHERE e.idexpense = ID;
    END //
DELIMITER ;

-- get all expenses with the categories added up
DELIMITER //
CREATE PROCEDURE add_up_categories_in_month(IN d DATE)
	BEGIN
		SELECT e.categoryid, c.name as 'categoryname', sum(e.amount) as total from expense e, category c
	WHERE e.categoryid = c.idcategory AND MONTH(date) = MONTH(d) AND YEAR(date) = YEAR(d) AND categoryid = categoryid
	group by categoryid;
    END //
DELIMITER ;

call add_up_categories_in_month('2018-02-01');

call get_all_income();
call get_all_expense();






















