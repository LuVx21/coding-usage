INSERT OVERWRITE TABLE dest_db.dest_table PARTITION ( pt_dt = '20190901' )
SELECT
     src0.id as _id
     ,IF(s1.fee_amount IS NULL, 'foo', 'bar') as _name
     ,s1.fee_amount + s2.fee_amount as fee_amount
FROM
  src_db.src0
  LEFT OUTER JOIN
  (
    SELECT
          id
          ,SUM(fee_amount) AS fee_amount
      FROM src_db.src1
      WHERE service_id = '1'
    GROUP BY id
    UNION ALL
    SELECT
          id
          ,SUM(fee_amount)   AS fee_amount
      FROM src_db.src2
      WHERE service_id = '1'
    GROUP BY id
  ) s1 on src0.id= s1.id
  LEFT OUTER JOIN 
  (
    SELECT id
          ,SUM(fee_amount) AS fee_amount
    FROM src_db.src3
    WHERE service_id = '1'
    GROUP BY id
  ) s2
  ON src0.id = s2.id
  and case
      when s2.id = 1 and (s2.fee_amount > 0 or s2.fee_amount < 10) then
        1 = 1
      else 1 = 1
    end
;
