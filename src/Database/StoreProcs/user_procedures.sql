-- getuserbyId
DELIMITER //
CREATE PROCEDURE get_user_by_id(IN num INTEGER)
	BEGIN
		select * FROM user WHERE idUser = num;
    END //
    
DELIMITER ;

DELIMITER //
CREATE PROCEDURE get_user_login(IN uName VARCHAR(45), uPassword VARCHAR(45))
	BEGIN
		SELECT * 
        FROM user u 
        WHERE u.username = uNAME AND u.password = uPassword; 
    END //
DELIMITER ;

-- get all users
DELIMITER //
CREATE PROCEDURE get_all_users()
	BEGIN
		SELECT * FROM user;
    END //
DELIMITER ;

-- update user
DELIMITER //
CREATE PROCEDURE update_user(IN FNAME varchar(45), LNAME varchar(45), uName varchar(45), pWord varchar(45), mail varchar(45), NUM INTEGER)
	BEGIN
        UPDATE user 
        SET firstname = FNAME, lastname = LNAME, username = uName, password = pWord, email = mail
        WHERE idUser = num;
	END //
DELIMITER ;

-- new user
DELIMITER //
	CREATE PROCEDURE insert_user(IN FNAME varchar(45), LNAME varchar(45), uName varchar(45), pWord varchar(45), mail varchar(45))
		BEGIN
			INSERT INTO user(firstname, lastname, username, password, email)
			VALUES(FNAME, LNAME, uName, pWord, mail);
		END //
DELIMITER ;

INSERT INTO user(firstname, lastname, username, password, email)
	VALUES('Drew', 'Atkison', 'dtatkison','Onegodchurch1','dtatkison@gmail.com');
    
call get_all_users();

-- delete user
DELIMITER //
CREATE PROCEDURE delete_user_by_id(IN num INTEGER)
	BEGIN
		DELETE FROM user WHERE idUser = num;
    END //
    
DELIMITER ;

call delete_user_by_id(3);

call get_user_by_id(1);
call get_all_users();