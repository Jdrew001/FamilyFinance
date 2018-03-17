-- Budget Stored Procedures

DELIMITER //
CREATE PROCEDURE get_all_budgets()
	BEGIN
		SELECT b.idBudget, b.date
        FROM budget;
    END //
DELIMITER ;

DELIMITER //
CREATE PROCEDURE get_budget_from_month(IN d DATE)
	BEGIN
		SELECT b.idBudget, b.date
        FROM budget b
        WHERE MONTH(b.date) = MONTH(d) AND YEAR(b.date) = YEAR(d);
    END //
DELIMITER ;

call get_budget_from_month('2018-03-01');

-- create new budget
DELIMITER //
CREATE PROCEDURE create_budget(IN d DATE)
	BEGIN
		INSERT INTO budget(date) 
			VALUES(d);
    END //
DELIMITER ;

-- delete budget
DELIMITER //
CREATE PROCEDURE delete_budget(IN id INT)
	BEGIN
		DELETE FROM budget
        WHERE idBudget = id;
    END //
DELIMITER ;

-- get all budget items for a month month
DELIMITER //
CREATE PROCEDURE get_budget_items_for_month(IN d DATE)
	BEGIN
		SELECT bc.id, b.idBudget, b.date, bc.amount, c.idcategory, c.name as 'categoryname'
        FROM budget b, budgetcategory bc, category c
        WHERE b.idbudget = bc.idBudget AND c.idcategory = bc.idCategory AND MONTH(b.date) = MONTH(d) AND YEAR(b.date) = YEAR(d)
        ORDER BY c.name;
    END //
DELIMITER ;

call get_budget_items_for_month('2018-03-01');

DELIMITER //
CREATE PROCEDURE get_budget_categories_for_month(IN d DATE)
	BEGIN
		SELECT b.idBudget, c.idcategory, c.name as 'categoryname', bc.amount
        FROM budget b, budgetcategory bc, category c
        WHERE b.idBudget = bc.idBudget AND c.idCategory = bc.idCategory AND MONTH(b.date) = MONTH(d) AND YEAR(b.date) = YEAR(d)
        ;
    END //
DELIMITER ;

call get_budget_categories_for_month('2018-03-01')
call get_expense_by_month('2018-03-01')

call get_budget_items_for_month('2018-03-01');
call get_budget_from_month('2018-03-11');

-- get budget item by id
DELIMITER //
CREATE PROCEDURE get_budget_item_by_id(IN id INT)
	BEGIN
		SELECT bc.id, b.idBudget, b.date, bc.amount, c.idcategory, c.name as 'categoryname'
        FROM budget b, budgetcategory bc, category c 
		WHERE b.idbudget = bc.idBudget AND c.idcategory = bc.idCategory AND bc.id = id;
    END //
DELIMITER ;

-- add new budget items in a given month
DELIMITER //
CREATE PROCEDURE create_budget_item(IN amount DOUBLE, idBudget INT, idCategory INT)
	BEGIN
		INSERT INTO budgetcategory(amount, idBudget, idCategory)
		VALUES(amount, idBudget, idCategory);
    END //
DELIMITER ;

call create_budget_item(400.00, 2, 5);
call get_all_categories();

-- update budget item
DELIMITER //
CREATE PROCEDURE update_budget_item(IN a DOUBLE, idC INT, id INT)
	BEGIN
		UPDATE budgetcategory
        SET amount = a, idCategory = idC
        WHERE budgetcategory.id = id;
    END //
DELIMITER ;

-- delete budget item
DELIMITER //
CREATE PROCEDURE delete_budget_item(IN id INT)
	BEGIN
		DELETE FROM budgetcategory
        WHERE budgetcategory.id = id;
    END //
DELIMITER ;

call delete_budget_item(6);

call update_budget_item(400, 11, 5);


call get_budget_items_for_month('2018-03-01');