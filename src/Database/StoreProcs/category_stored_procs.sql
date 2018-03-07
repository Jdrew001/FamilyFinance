-- categories


-- get all categories
DELIMITER //
CREATE PROCEDURE get_all_categories()
	BEGIN
		SELECT c.idcategory, c.name AS 'categoryname'
        FROM category c;
    END //
DELIMITER ;

call get_all_categories();

-- get category by id
DELIMITER //
CREATE PROCEDURE get_category_by_id(IN ID INTEGER)
	BEGIN
		SELECT c.idcategory, c.name AS 'categoryname', t.idtransactionType, t.name AS 'transactionname'
        FROM category c
        WHERE c.idcategory = ID;
    END //
DELIMITER ;

-- create a new category
DELIMITER //
CREATE PROCEDURE create_category(IN N VARCHAR(45))
	BEGIN
		INSERT INTO category (name)
			VALUES(N);
    END //create_category
DELIMITER ;

-- update an exisiting category
DELIMITER //
CREATE PROCEDURE update_category(IN N VARCHAR(45), ID INTEGER)
	BEGIN
		UPDATE category c
        SET c.name = N
        WHERE c.idcategory = ID;
    END //
DELIMITER ;

-- delete a category
DELIMITER //
CREATE PROCEDURE delete_category(IN ID INTEGER)
	BEGIN
		DELETE FROM category
        WHERE idcategory = ID;
    END //
DELIMITER ;