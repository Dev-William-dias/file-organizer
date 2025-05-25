BEGIN TRANSACTION;
CREATE TABLE IF NOT EXISTS "files" (
	"id"	INTEGER,
	"name"	TEXT NOT NULL,
	"category"	TEXT,
	"description"	TEXT,
	"file"	BLOB NOT NULL,
	"numberPages"	INTEGER,
	"fileSize"	REAL,
	PRIMARY KEY("id" AUTOINCREMENT)
);
COMMIT;
