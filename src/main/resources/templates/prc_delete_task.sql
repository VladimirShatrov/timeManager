CREATE PROCEDURE prc_delete_task (
    taskId INT
)
LANGUAGE plpgsql
AS $$
begin
    DELETE FROM task
    WHERE id = taskId;
end;
$$;