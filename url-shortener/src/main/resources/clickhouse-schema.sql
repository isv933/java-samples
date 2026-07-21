CREATE TABLE IF NOT EXISTS shortener_url (id  String, url String,timestamp Datetime DEFAULT now())
ENGINE = ReplacingMergeTree
ORDER BY id;
