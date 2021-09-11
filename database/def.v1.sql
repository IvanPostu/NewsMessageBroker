
CREATE TABLE [records] (
	_id           INTEGER PRIMARY KEY AUTOINCREMENT,
	_json         TEXT DEFAULT '',
	_is_in_queue  INTEGER NOT NULL -- bool
) ;

CREATE INDEX records_indexes ON records (_id, _is_in_queue);

