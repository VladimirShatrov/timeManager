CREATE PROCEDURE prc_delete_user (
    userId INT
)
LANGUAGE plpgsql
AS $$
begin
    DELETE FROM task
    WHERE user_id = userId;

    DELETE FROM app_user
    WHERE id = userId;
end;
$$;