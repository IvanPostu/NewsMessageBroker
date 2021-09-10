
CREATE TABLE [records] (
	_id           INTEGER PRIMARY KEY,
	_data         TEXT DEFAULT '',
	_is_in_queue  INTEGER NOT NULL,
	_author       TEXT NOT NULL,
	_category     TEXT NOT NULL
) WITHOUT ROWID;

CREATE INDEX records_indexes ON records (_author, _category, _is_in_queue);

