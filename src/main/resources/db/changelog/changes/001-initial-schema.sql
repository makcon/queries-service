CREATE TABLE users (
	id varchar(36) NOT NULL,
	created_at timestamp NOT NULL,
	updated_at timestamp NOT NULL,
	first_name varchar(255) NOT NULL,
	last_name varchar(255) NOT NULL,
	email varchar(255) NOT NULL,
	CONSTRAINT users_pkey PRIMARY KEY (id)
);
CREATE INDEX users_first_name_idx ON users (first_name);
CREATE INDEX users_last_name_idx ON users (last_name);
CREATE INDEX users_email_idx ON users (email);

CREATE TABLE products (
	id varchar(36) NOT NULL,
	created_at timestamp NOT NULL,
	updated_at timestamp NOT NULL,
	status varchar(50) NOT NULL,
	name varchar(255) NOT NULL,
	raw_data text NOT NULL,
	CONSTRAINT products_pkey PRIMARY KEY (id)
);
CREATE INDEX products_name_idx ON products USING btree (name);
CREATE INDEX products_status_idx ON products USING btree (status);

CREATE TABLE orders (
	id varchar(36) NOT NULL,
	created_at timestamp NOT NULL,
	updated_at timestamp NOT NULL,
	user_id varchar(36) NOT NULL,
	product_id varchar(36) NOT NULL,
	name varchar(255) NOT NULL,
	status varchar(50) NOT NULL,
	raw_data text NULL,
	CONSTRAINT orders_pkey PRIMARY KEY (id)
);
CREATE INDEX orders_user_id_idx ON orders USING btree (user_id);
CREATE INDEX orders_product_id_idx ON orders USING btree (product_id);
CREATE INDEX orders_name_idx ON orders USING btree (name);
CREATE INDEX orders_status_idx ON orders USING btree (status);