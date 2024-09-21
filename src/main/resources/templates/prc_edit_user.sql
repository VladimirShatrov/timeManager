CREATE PROCEDURE prc_create_user (
    userId INT,
    userEmail varchar DEFAULT NULL,
    userLogin varchar DEFAULT NULL,
    userPassword varchar DEFAULT NULL
)
LANGUAGE plpgsql
AS $$
begin
    UPDATE app_user
    SET
        email = COALESCE(userEmail, email),
        login = COALESCE(userLogin, login),
        password = COALESCE(userPassword, password)
    WHERE id = userId;
end;
$$;