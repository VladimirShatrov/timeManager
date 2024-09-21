CREATE PROCEDURE prc_delete_all_user_task (
    userId INT
)
LANGUAGE plpgsql
AS $$
begin
    DELETE FROM task
    WHERE user_id = userId;
end;
$$;