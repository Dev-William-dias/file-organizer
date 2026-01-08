BEGIN TRANSACTION;
CREATE TABLE IF NOT EXISTS "files" (
	"id"	INTEGER,
	"name"	TEXT NOT NULL,
	"category"	TEXT,
	"description"	TEXT,
	"file_path"	TEXT NOT NULL,
	"numberPages"	INTEGER,
	"fileSize"	REAL,
	PRIMARY KEY("id" AUTOINCREMENT)
);
CREATE TABLE IF NOT EXISTS "category" (
	"id"	INTEGER,
	"name"	TEXT NOT NULL,
	PRIMARY KEY("id" AUTOINCREMENT)
);
COMMIT;
