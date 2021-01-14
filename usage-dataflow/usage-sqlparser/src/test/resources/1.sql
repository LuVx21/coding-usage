INSERT INTO TABLE dest.table3
SELECT
    t1.id as _id,
    t2.name as _name
FROM
    (
        SELECT
                id1 + id2 AS id
        FROM
            src.table1
    ) t1
        LEFT JOIN
    (
        SELECT
            id,
            name
        FROM
            (
                SELECT
                    id,
                    source_name AS name
                FROM
                    src.table2
            )
    ) t2
    ON t1.id = t2.id
;