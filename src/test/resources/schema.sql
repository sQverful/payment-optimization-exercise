-- Create branch table only if it does not exist
CREATE TABLE IF NOT EXISTS branch
(
    id            INTEGER PRIMARY KEY AUTO_INCREMENT,
    name          VARCHAR(255) NOT NULL,
    transfer_cost INTEGER      NOT NULL,
    CONSTRAINT unique_name UNIQUE (name)
);

-- Create branch_connection table only if it does not exist
CREATE TABLE IF NOT EXISTS branch_connection
(
    id                    INTEGER PRIMARY KEY AUTO_INCREMENT,
    origin_branch_id      INTEGER,
    destination_branch_id INTEGER,
    FOREIGN KEY (origin_branch_id) REFERENCES branch (id),
    FOREIGN KEY (destination_branch_id) REFERENCES branch (id)
);
