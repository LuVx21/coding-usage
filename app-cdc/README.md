<details>
<summary>点击展开目录</summary>
<!-- TOC -->

- [mysql-binlog-connector](#mysql-binlog-connector)
- [Debezium](#debezium)
- [Canal](#canal)

<!-- /TOC -->
</details>


## mysql-binlog-connector


```xml
<dependency>
    <groupId>com.zendesk</groupId>
    <artifactId>mysql-binlog-connector-java</artifactId>
    <version>0.23.2</version>
</dependency>
```

> [mysql-binlog-connector-java](https://github.com/osheroff/mysql-binlog-connector-java)

binlog:
```
+------------------+------+----------------+-----------+-------------+--------------------------------------+
| Log_name         | Pos  | Event_type     | Server_id | End_log_pos | Info                                 |
+------------------+------+----------------+-----------+-------------+--------------------------------------+
| mysql-bin.000001 | 1838 | Anonymous_Gtid |         1 |        1903 | SET @@SESSION.GTID_NEXT= 'ANONYMOUS' |
| mysql-bin.000001 | 1903 | Query          |         1 |        1977 | BEGIN                                |
| mysql-bin.000001 | 1977 | Table_map      |         1 |        2031 | table_id: 265 (boot.user)            |
| mysql-bin.000001 | 2031 | Update_rows    |         1 |        2115 | table_id: 265 flags: STMT_END_F      |
| mysql-bin.000001 | 2115 | Xid            |         1 |        2146 | COMMIT /* xid=510 */                 |
+------------------+------+----------------+-----------+-------------+--------------------------------------+
```

json:
```json
{
    "header": {
        "dataLength": 46,
        "eventLength": 65,
        "eventType": "ANONYMOUS_GTID",
        "flags": 0,
        "headerLength": 19,
        "nextPosition": 1903,
        "position": 1838,
        "serverId": 1,
        "timestamp": 1598336354000
    }
}

{
    "data": {
        "database": "boot",
        "errorCode": 0,
        "executionTime": 0,
        "sql": "BEGIN",
        "threadId": 395
    },
    "header": {
        "dataLength": 55,
        "eventLength": 74,
        "eventType": "QUERY",
        "flags": 8,
        "headerLength": 19,
        "nextPosition": 1977,
        "position": 1903,
        "serverId": 1,
        "timestamp": 1598336354000
    }
}

{
    "data": {
        "columnMetadata": [
            0,
            1020,
            1020,
            0
        ],
        "columnNullability": {
            "empty": false
        },
        "columnTypes": "CA8PAw==",
        "database": "boot",
        "table": "user",
        "tableId": 265
    },
    "header": {
        "dataLength": 35,
        "eventLength": 54,
        "eventType": "TABLE_MAP",
        "flags": 0,
        "headerLength": 19,
        "nextPosition": 2031,
        "position": 1977,
        "serverId": 1,
        "timestamp": 1598336354000
    }
}

{
    "data": {
        "includedColumns": {
            "empty": false
        },
        "includedColumnsBeforeUpdate": {
            "empty": false
        },
        "rows": [
            {
                [
                    1,
                    "foo1",
                    "bar",
                    1
                ]: [
                    1,
                    "foo1",
                    "bar",
                    11
                ]
            }
        ],
        "tableId": 265
    },
    "header": {
        "dataLength": 65,
        "eventLength": 84,
        "eventType": "EXT_UPDATE_ROWS",
        "flags": 0,
        "headerLength": 19,
        "nextPosition": 2115,
        "position": 2031,
        "serverId": 1,
        "timestamp": 1598336354000
    }
}

{
    "data": {
        "xid": 510
    },
    "header": {
        "dataLength": 12,
        "eventLength": 31,
        "eventType": "XID",
        "flags": 0,
        "headerLength": 19,
        "nextPosition": 2146,
        "position": 2115,
        "serverId": 1,
        "timestamp": 1598336354000
    }
}
```


## Debezium



## Canal


