-- Insert demo data into branch table
INSERT INTO branch (id, name, transfer_cost)
VALUES (1, 'A', 5),
       (2, 'B', 50),
       (3, 'C', 10),
       (4, 'D', 10),
       (5, 'E', 20),
       (6, 'F', 5);

-- Insert demo data into branch_connection table
INSERT INTO branch_connection (id, origin_branch_id, destination_branch_id)
VALUES (1, 1, 2),
       (2, 1, 3),
       (3, 3, 2),
       (4, 2, 4),
       (5, 3, 5),
       (6, 4, 5),
       (7, 5, 4),
       (8, 4, 6),
       (9, 5, 6);

-- Ensure the next auto-increment values are set correctly
ALTER TABLE branch
    ALTER COLUMN id RESTART WITH 7;
ALTER TABLE branch_connection
    ALTER COLUMN id RESTART WITH 10;
