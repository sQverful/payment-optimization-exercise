-- Create branch table
CREATE TABLE branch
(
    id            INTEGER PRIMARY KEY AUTO_INCREMENT,
    name          VARCHAR(255) NOT NULL,
    transfer_cost INTEGER      NOT NULL,
    CONSTRAINT unique_name UNIQUE (name)
);

CREATE INDEX idx_branch_name ON branch (name);

-- Create branch_connection table
CREATE TABLE branch_connection
(
    id                    INTEGER PRIMARY KEY AUTO_INCREMENT,
    origin_branch_id      INTEGER,
    destination_branch_id INTEGER,
    FOREIGN KEY (origin_branch_id) REFERENCES branch (id),
    FOREIGN KEY (destination_branch_id) REFERENCES branch (id)
);