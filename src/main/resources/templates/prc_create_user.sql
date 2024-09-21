CREATE PROCEDURE prc_create_user (userEmail varchar, userLogin varchar, userPassword varchar)
    LANGUAGE plpgsql
AS $$
begin
INSERT INTO app_user (email, login, password) values (userEmail, userLogin, userPassword);
end;
$$;