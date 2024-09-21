CREATE PROCEDURE prc_start_task (
    userId INT,
    dateStart TIME,
    taskName varchar
)
    LANGUAGE plpgsql
AS $$
begin
INSERT INTO task(date_start, name, user_id) values (dateStart, taskname, userId);
end;
$$;