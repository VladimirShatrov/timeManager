CREATE PROCEDURE prc_start_task (
    taskId INT,
    dateStop TIME
)
    LANGUAGE plpgsql
AS $$
begin
    UPDATE task
    SET
        date_stop = dateStop
    WHERE id = taskId;
end;
$$;