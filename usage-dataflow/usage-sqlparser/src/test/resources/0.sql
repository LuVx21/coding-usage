-- * -> id, name
INSERT INTO TABLE dest.table3
SELECT
    t1.*,
    t1.*
FROM
    src.table1 AS t1
UNION
SELECT
    t2.*,
    t2.*
FROM
    (
        SELECT id, name
        FROM src.table2
    ) AS t2
UNION
SELECT
    t3.*,
    t4.*
FROM
    src.table3 AS t3 join
    (
        SELECT id, name
        FROM src.table4
    ) AS t4
    ON t3.id = t4.id
UNION
SELECT
    t5_6.*,
    t5_6.*
FROM
    (
        SELECT id, name
        FROM src.table5
        UNION
        SELECT *
        FROM src.table6
    ) AS t5_6
;