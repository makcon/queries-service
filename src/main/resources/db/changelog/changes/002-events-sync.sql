CREATE TABLE events_sync (
	id varchar(50) NOT NULL,
	created_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
	updated_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
	last_created_at timestamp NULL,
	synced bool NOT NULL DEFAULT false,
	CONSTRAINT events_sync_pkey PRIMARY KEY (id)
);
CREATE INDEX events_sync_idx ON events_sync (synced);

INSERT INTO events_sync (id) VALUES ('USERS');
INSERT INTO events_sync (id) VALUES ('PRODUCTS');
INSERT INTO events_sync (id) VALUES ('ORDERS');