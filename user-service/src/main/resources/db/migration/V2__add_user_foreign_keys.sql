-- V2__add_user_foreign_keys.sql (For User Service)
ALTER TABLE user_addresses ADD CONSTRAINT fk_user_addresses_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE;
ALTER TABLE authorities ADD CONSTRAINT fk_authorities_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE;