INSERT INTO TABLE dest.dest
SELECT
    t0.id as _id,
    t0.name as _name
FROM
    src.table0 as t0
UNION
(
    SELECT
        t1.id as _id1,
        t1.name as _name1
    FROM
        src.table1 as t1
)
UNION
SELECT
    t2.id1 + t2.id2 as _id,
    concat(t2.name1, t2.name2)
FROM
    src.table2 as t2
UNION
SELECT
    t3.id1 + t4.id1 + t4.id2 as _id,
    concat(t3.name1, t4.name1, t4.name2)
FROM
    src.table3 as t3 join
    src.table4 as t4 on t3.id1 = t4.id1
UNION
SELECT
    sub1.id1 + sub2.id2 as _id,
    concat(sub2.name, '_3') as _name
FROM
    (
        SELECT
                id1 + id2 AS id1
        FROM
            src.table98
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
                    src.table99
            ) as sub2_1
    ) sub2
    ON sub1.id = sub2.id
;