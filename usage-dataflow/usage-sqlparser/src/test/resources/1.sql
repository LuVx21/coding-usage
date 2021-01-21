INSERT INTO TABLE dest.dest
SELECT
    t0.id as _id,
    t0.name as _name
FROM
    src.table0 as t0
UNION
SELECT
    sub1.id1 + sub2.id2 as _id,
    concat(sub2.name, '_3') as _name
FROM
    (
        SELECT
                id1 + id2 AS id1
        FROM
            src.table1
    ) sub1
        LEFT JOIN
    (
        SELECT
            id2,
            concat(new_name, '_bar') as name
        FROM
            (
                SELECT
                    id2,
                    concat(source_name, '_foo') AS new_name
                FROM
                    src.table2
            ) as sub2_1
    ) sub2
    ON sub1.id = sub2.id
UNION
(
    SELECT
        t3.id as _id,
        t3.name as _name
    FROM
        src.table3 as t3
)
;