CREATE KEYSPACE IF NOT EXISTS "example";

USE "example";

CREATE TABLE token_tracker(id uuid, reference_id uuid, reference_code text, updated_date timestamp, primary key (id));

-- insert into token_tracker(id, reference_id, reference_code, updated_date) values(uuid(), uuid(), 'F0', dateof(now())) using ttl 30;