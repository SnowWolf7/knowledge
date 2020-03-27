## 8.1 Optimization Overview

Database performance depends on several factors at the database level, such as tables, queries, and configuration settings. These software constructs result in CPU and I/O operations at the hardware level, which you must minimize and make as efficient as possible. As you work on database performance, you start by learning the high-level rules and guidelines for the software side, and measuring performance using wall-clock time. As you become an expert, you learn more about what happens internally, and start measuring things such as CPU cycles and I/O operations. 

Typical users aim to get the best database performance out of their existing software and hardware configurations. Advanced users look for opportunities to improve the MySQL software itself, or develop their own storage engines and hardware appliances to expand the MySQL ecosystem. 

+ Optimizing at the Database Level
+ Optimizing at the Hareware Level
+ Balancing Portability and Performance

**Optimizing at the Database Level**

The most important factor in making a database application fast is its basic design: 

+ Are the tables structured properly? In particular, do the columns have the right data types, and does each table have the appropriate columns for the type of work? For example, applications that perform frequent updates often have many tables with few columns, while applications that analyze large amounts of data often have few tables with many columns. 
+ Are the right indexes in place to make queries efficient? 
+ Are you using the appropriate storage engine for each table, and taking advantage of the strengths and features of each storage engine you use? In particular, the choice of a transactional storage engine such as InnoDB or a nontransactional one such as MyISAM can be very important for performance and scalability. 

> **Note**
>
> InnoDB is the default storage engine for new tables. In particular, the advanced InnoDB performance features mean that InnoDB tables often outperform the simpler MyISAM tables, especially for a busy database.

+ Does each table use an appropriate row format? This choice also depends on storage engine used for the table. In particular, compressed tables use less disk space and so require less disk I/O to read and write the data. Compression is available for all kinds of workloads with InnoDB tables, and for read-only MyISAM tables.
+ Does the application use an appropriate locking strategy? For example, by allowing shared access when possible so that database operations can run concurrently, and requesting exclusive access when appropriate so that critical operations get top priority. Again, the choice of storage engine is significant. The InnoDB storage engine handles most locking issues without involvement from you, allowing for better concurrency in the database and reducing the amount of experimentation and tuning for your code. 
+ Are all memory areas used for caching sized correctly? That is, large enough to hold frequently accessed data, but not so large that they overload physical memory and cause paging. The main memory areas to configure are the InnoDB buffer pool, the MyISAM key cache, and the MySQL query cache. 

## 8.2 Optimizing SQL Statements

The core logic of a database application is performed through SQL statements, whether issued directly through an interpreter or submitted behind the scenes through an API. The tuning guidelines in this section help to speed up all kinds of MySQL applications. The guidelines cover SQL operations that read and
 write data, the behind-the-scenes overhead for SQL operations in general, and operations used in specific scenarios such as database monitoring. 

### 8.2.1 Optimizing SELECT Statements

Queries, in the form of `SELECT` statements, perform all the lookup operations in the database. Tuning these statements is a top priority, whether to achieve sub-second response times for dynamic web pages, or to chop hours off the time to generate huge overnight reports. 

Besides `SELECT` statements, the tuning techniques for queries also apply to constructs such as `CREATE TABLE...AS SELECT`, `INSERT INTO...SELECT`, and `WHERE` clauses in `DELETE` statements. Those statements have additional performance considerations because they combine write operations with the read-oriented query operations. 

NDB Cluster supports a join pushdown optimization whereby a qualifying join is sent in its entirety to NDB Cluster data nodes, where it can be distributed among them and executed in parallel. For more information about this optimization, see Conditions for NDB pushdown joins.

The main considerations for optimizing queries are: 

+ To make a slow `SELECT ... WHERE` query faster, the first thing to check is whether you can add an *index*. Set up indexes on columns used in the `WHERE` clause, to speed up evaluation, filtering, and the final retrieval of results. To avoid wasted disk space, construct a small set of indexes that speed up many related queries used in your application. 

    Indexes are especially important for queries that reference different tables, using features such as *joins* and *foreign keys*. You can use the `EXPLAIN` statement to determine which indexes are used for a `SELECT`. See Section 8.3.1, “How MySQL Uses Indexes” and Section 8.8.1, “Optimizing Queries with EXPLAIN”. 

+ Isolate and tune any part of the query, such as a function call, that takes excessive time. Depending on how the query is structured, a function could be called once for every row in the result set, or even once for every row in the table, greatly magnifying any inefficiency. 

+ Minimize the number of full table scans in your queries, particularly for big tables. 

+ Keep table statistics up to date by using the `ANALYZE TABLE` statement periodically, so the optimizer 

    has the information needed to construct an efficient execution plan. 

+ Learn the tuning techniques, indexing techniques, and configuration parameters that are specific to
     the storage engine for each table. Both *InnoDB* and *MyISAM* have sets of guidelines for enabling and sustaining high performance in queries. For details, see Section 8.5.6, “Optimizing InnoDB Queries” and Section 8.6.1, “Optimizing MyISAM Queries”. 

+ You can optimize single-query transactions for *InnoDB* tables, using the technique in Section 8.5.3, “Optimizing InnoDB Read-Only Transactions”. 

+ Avoid transforming the query in ways that make it hard to understand, especially if the optimizer does some of the same transformations automatically. 

+ If a performance issue is not easily solved by one of the basic guidelines, investigate the internal details of the specific query by reading the `EXPLAIN` plan and adjusting your indexes, `WHERE` clauses, join clauses, and so on. (When you reach a certain level of expertise, reading the `EXPLAIN` plan might be your first step for every query.) 

+ Adjust the size and properties of the memory areas that MySQL uses for caching. With efficient use of the InnoDB buffer pool, MyISAM key cache, and the MySQL query cache, repeated queries run faster because the results are retrieved from memory the second and subsequent times. 

+ Even for a query that runs fast using the cache memory areas, you might still optimize further so that they require less cache memory, making your application more scalable. Scalability means that your application can handle more simultaneous users, larger requests, and so on without experiencing a big drop in performance. 

+ Deal with locking issues, where the speed of your query might be affected by other sessions accessing the tables at the same time. 

#### 8.2.1.1 WHERE Clause Optimization

This section discusses optimizations that can be made for processing `WHERE` clauses. The examples use `SELECT` statements, but the same optimizations apply for `WHERE` clauses in `DELETE` and `UPDATE` statements. 

You might be tempted to rewrite your queries to make arithmetic operations faster, while sacrificing readability. Because MySQL does similar optimizations automatically, you can often avoid this work, and leave the query in a more understandable and maintainable form. Some of the optimizations performed by MySQL follow: 

+ Removal of unnecessary parentheses: 

```sql
	((a AND b) AND c OR (((a AND b) AND (c AND D))))
->(a AND b AND c) OR (a AND b AND c AND d)
```

+ Constant folding:

```sql
	(a<b AND b=c) AND a=5
->b>5 AND b=c AND a=5
```

+ Constant condition removal: 

```sql
	(b>=5 AND b=5) OR (b=6 AND 5=5) OR (b=7 AND 5=6)
->b=5 OR b=6
```

+ Constant expressions used by indexes are evaluated only once. 

+ `COUNT(*)` on a single table without a `WHERE` is retrieved directly from the table information for *MyISAM* and *MEMORY* tables. This is also done for any `NOT NULL` expression when used with only one table. 

+ Early detection of invalid constant expressions. MySQL quickly detects that some `SELECT` statements 

    are impossible and returns no rows. 

+ `HAVING` is merged with `WHERE` if you do not use `GROUP BY` or aggregate functions (`COUNT()`, `MIN()`, and so on). 

+ For each table in a join, a simpler `WHERE` is constructed to get a fast `WHERE` evaluation for the table and also to skip rows as soon as possible. 

+ All constant tables are read first before any other tables in the query. A constant table is any of the following: 

    + An empty table or a table with one row. 
    + A table that is used with a WHERE clause on a PRIMARY KEY or a UNIQUE index, where all index parts are compared to constant expressions and are defined as NOT NULL. 

    All of the following tables are used as constant tables: 

    ```sql
    SELECT * FROM t WHERE primary_key=1; 
    SELECT * FROM t1,t2
    	WHERE t1.primary_key=1 AND t2.primary_key=t1.id;
    ```

+ The best join combination for joining the tables is found by trying all possibilities. If all columns in `ORDER BY` and `GROUP BY` clauses come from the same table, that table is preferred first when joining. 

- If there is an `ORDER BY` clause and a different `GROUP BY` clause, or if the `ORDER BY` or `GROUP BY` contains columns from tables other than the first table in the join queue, a temporary table is created. 

- If you use the `SQL_SMALL_RESULT` modifier, MySQL uses an in-memory temporary table. 
- Each table index is queried, and the best index is used unless the optimizer believes that it is more efficient to use a table scan. At one time, a scan was used based on whether the best index spanned more than 30% of the table, but a fixed percentage no longer determines the choice between using an index or a scan. The optimizer now is more complex and bases its estimate on additional factors such as table size, number of rows, and I/O block size. 
- In some cases, MySQL can read rows from the index without even consulting the data file. If all columns used from the index are numeric, only the index tree is used to resolve the query. 

- Before each row is output, those that do not match the HAVING clause are skipped. 

Some examples of queries that are very fast: 

```sql
SELECT COUNT(*) FROM tbl_name;
SELECT MIN(key_part1),MAX(key_part1) FROM tbl_name;
SELECT MAX(key_part2) FROM tbl_name WHERE key_part1=constant;
SELECT ... FROM tbl_name
	ORDER BY key_part1,key_part2,... LIMIT 10;
SELECT ... FROM tbl_name
	ORDER BY key_part1 DESC, key_part2 DESC, ... LIMIT 10;
```

MySQL resolves the following queries using only the index tree, assuming that the indexed columns are numeric: 

```sql
SELECT key_part1,key_part2 FROM tbl_name WHERE key_part1=val; 
SELECT COUNT(*) FROM tbl_name
	WHERE key_part1=val1 AND key_part2=val2;
SELECT key_part2 FROM tbl_name GROUP BY key_part1;
```

The following queries use indexing to retrieve the rows in sorted order without a separate sorting pass: 

```sql
SELECT ... FROM tbl_name
	ORDER BY key_part1,key_part2,... ;
SELECT ... FROM tbl_name
	ORDER BY key_part1 DESC, key_part2 DESC, ... ;
```

#### 8.2.1.2 Range Optimization

#### 8.2.1.3 Index Merge Optimization

*The Index Merge* access method retrieves rows with multiple *range* scans and merges their results into one. This access method merges index scans from a single table only, not scans across multiple tables. The merge can produce unions, intersections, or unions-of-intersections of its underlying scans. 

Example queries for which Index Merge may be used: 

```sql
SELECT * FROM tbl_name WHERE key1 = 10 OR key2 = 20;

SELECT * FROM tbl_name
	WHERE (key1 = 10 OR key2 = 20) AND non_key = 30;
	
SELECT * FROM t1, t2
	WHERE (t1.key1 IN (1,2) OR t1.key2 LIKE 'value%')
	AND t2.key1 = t1.some_col;
	
SELECT * FROM t1, t2
	WHERE t1.key1 = 1
	AND (t2.key1 = t1.some_col OR t2.key2 = t1.some_col2);
```

*未完待续*

#### 8.2.1.4 Engine Condition Pushdown Optimization

#### 8.2.1.5 Index Condition Pushdown Optimization

#### 8.2.1.6 Nested-Loop Join Algorithms

#### 8.2.1.7 Nested Join Optimization

#### 8.2.1.8 Outer Join Optimization

#### 8.2.1.9 Outer Join Simplification

#### 8.2.1.10 Multi-Range Read Optimization

#### 8.2.1.11 Block Nested-Loop and Batched Key Access Joins

#### 8.2.1.12 IS NULL Optimization

#### 8.2.1.13 ORDER BY Optimization

#### 8.2.1.14 GROUP BY Optimization

#### 8.2.1.15 DISTINCT Optimization

#### 8.2.1.16 LIMIT Query Optimization

#### 8.2.1.17 Function Call Optimization

#### 8.2.1.18 Row Constructor Expression Optimization

#### 8.2.1.19 Avoiding Full Table Scans

## 8.3 Optimization and Indexes

The best way to improve the performance of `SELECT` operations is to create indexes on one or more of the columns that are tested in the query. The index entries act like pointers to the table rows, allowing the query to quickly determine which rows match a condition in the `WHERE` clause, and retrieve the other column values for those rows. All MySQL data types can be indexed. 

Although it can be tempting to create an indexes for every possible column used in a query, unnecessary indexes waste space and waste time for MySQL to determine which indexes to use. Indexes also add to the cost of inserts, updates, and deletes because each index must be updated. You must find the right balance to achieve fast queries using the optimal set of indexes. 

### 8.3.1 How MySQL Uses Indexes

Indexes are used to find rows with specific column values quickly. Without an index, MySQL must begin with the first row and then read through the entire table to find the relevant rows. The larger the table, the more this costs. If the table has an index for the columns in question, MySQL can quickly determine the position to seek to in the middle of the data file without having to look at all the data. This is much faster than reading every row sequentially. 

Most MySQL indexes (PRIMARY KEY, UNIQUE, INDEX, and FULLTEXT) are stored in B-trees. Exceptions: Indexes on spatial data types use R-trees; MEMORY tables also support hash indexes; InnoDB uses inverted lists for FULLTEXT indexes. 

In general, indexes are used as described in the following discussion. Characteristics specific to hash indexes (as used in MEMORY tables are described in Section 8.3.8, “Comparison of B-Tree and Hash Indexes”. 

MySQL uses indexes for these operations: 

+ To find the rows that matching a `WHERE` clause quickly.

+ To eliminate rows from consideration. If there is a choice between multiple indexes, MySQL normally uses the index that finds the smallest number of rows (the most selective index).

+ If the table has a multiple-column index, any leftmost prefix of the index can be used by the optimizer to look up rows. For example, if you have a three-column index on (col1, col2, col3), you have indexed search capabilities on `(col1)`, `(col1, col2)`, and `(col1, col2, col3)`. For more information, see Section 8.3.5, “Multiple-Column Indexes”. 

+ To retrieve rows from other tables when performing joins. MySQL can use indexes on columns more efficiently if they are declared as the same type and size. In this context, VARCHAR and CHAR are considered the same if they are declared as the same size. For example, VARCHAR(10) and CHAR(10) are the same size, but `VARCHAR(10)` and `CHAR(15)` are not. 

    For comparisons between nonbinary string columns, both columns should use the same character set. For example, comparing a utf8 column with a latin1 column precludes use of an index.

    Comparison of dissimilar columns (comparing a string column to a temporal or numeric column, for example) may prevent use of indexes if values cannot be compared directly without conversion. For a given value such as 1 in the numeric column, it might compare equal to any number of values in the string column such as '1', ' 1', '00001', or '01.e1'. This rules out use of any indexes for the string column. 

+ To find the `MIN()` or `MAX()` value for a specific indexed column `key_col`. This is optimized by a preprocessor that checks whether you are using `WHERE key_part_N = constant` on all key parts that occur before `key_col` in the index. In this case, MySQL does a single key lookup for each `MIN()` or `MAX()` expression and replaces it with a constant. If all expressions are replaced with constants, the query returns at once. For example: 

    ```sql
    SELECT MIN(key_part1), MAX(key_part2)
    	FROM tb1_name WHERE key_part1=10
    ```

+ To sort or group a table if the sorting or grouping is done on a leftmost prefix of a usable index (for example, ORDER BY key_part1, key_part2). If all key parts are followed by DESC, the key is read in reverse order. See Section 8.2.1.13, “ORDER BY Optimization”, and Section 8.2.1.14, “GROUP BY 

    Optimization”.

+ In some cases, a query can be optimized to retrieve values without consulting the data rows. (An index that provides all the necessary results for a query is called a covering index.) If a query uses from a table only columns that are included in some index, the selected values can be retrieved from the index tree for greater speed: 

    ```sql
    SELECT key_part3 FROM tb1_name
    	WHERE key_part1=1
    ```

    Indexes are less important for queries on small tables, or big tables where report queries process most or all of the rows. When a query needs to access most of the rows, reading sequentially is faster than working through an index. Sequential reads minimize disk seeks, even if not all the rows are needed for the query. See Section 8.2.1.19, “Avoiding Full Table Scans” for details. 

### 8.3.2 Primary Key Optimization

### 8.3.3 Foreign Key Optimization

### 8.3.4 Column Indexes

### 8.3.5 Multiple-Column Indexes

### 8.3.6 Verifying Index Usage

### 8.3.7 InnoDB and MyISAM Index Statistics Collection

### 8.3.8 Comparison of B-Tree and Hash Indexes

### 8.3.9 Use of Index Extensions

### 8.3.10 Indexed Lookups from TIMESTAMP Columns

Temporal values are stored in TIMESTAMP columns as UTC values, and values inserted into and retrieved from TIMESTAMP columns are converted between the session time zone and UTC. (This is the same
 type of conversion performed by the CONVERT_TZ() function. If the session time zone is UTC, there is effectively no time zone conversion.) 

Due to conventions for local time zone changes such as Daylight Saving Time (DST), conversions between UTC and non-UTC time zones are not one-to-one in both directions. UTC values that are distinct may not be distinct in another time zone. The following example shows distinct UTC values that become identical in a non-UTC time zone: 

```sql
mysql> CREATE TABLE tstable (ts TIMESTAMP); 
mysql> SET time_zone = 'UTC'; -- insert UTC values 
mysql> INSERT INTO tstable VALUES
       	('2018-10-28 00:30:00'),
				('2018-10-28 01:30:00');
mysql> SELECT ts FROM tstable;
+---------------------+
| ts 									|
+---------------------+
| 2018-10-28 00:30:00 |
| 2018-10-28 01:30:00 |
+---------------------+
mysql> SET time_zone = 'MET'; -- retrieve non-UTC values 
mysql> SELECT ts FROM tstable;
+---------------------+
| ts 									|
+---------------------+
| 2018-10-28 02:30:00 |
| 2018-10-28 02:30:00 |
+---------------------+
```

> **Note**
>
> To use named time zones such as `'MET'` or `'Europe/Amsterdam'`, the time zone tables must be properly set up. For instructions, see Section 5.1.12, “MySQL Server Time Zone Support”. 

You can see that the two distinct UTC values are the same when converted to the `'MET'` time zone. This phenomenon can lead to different results for a given `TIMESTAMP` column query, depending on whether the optimizer uses an index to execute the query. 

Suppose that a query selects values from the table shown earlier using a WHERE clause to search the ts column for a single specific value such as a user-provided timestamp literal: 

```sql
SELECT ts FROM tstable
WHERE ts = 'literal';
```

Suppose further that the query executes under these conditions: 

+ The session time zone is not UTC and has a DST shift. For example: 

    ```sql
    SET time_zone = 'MET';
    ```

+ Unique UTC values stored in the `TIMESTAMP` column are not unique in the session time zone due to DST shifts. (The example shown earlier illustrates how this can occur.) 
+ The query specifies a search value that is within the hour of entry into DST in the session time zone. 

Under those conditions, the comparison in the `WHERE` clause occurs in different ways for nonindexed and indexed lookups and leads to different results: 

+ If there is no index or the optimizer cannot use it, comparisons occur in the session time zone. The optimizer performs a table scan in which it retrieves each ts column value, converts it from UTC to the session time zone, and compares it to the search value (also interpreted in the session time zone): 
+ 

## 8.4 Optimizing Database Structure

## 8.5 Optimizing for InnoDB Tables

## 8.6 Optimizing for MyISAM Tables

## 8.7 Optimizing for MEMORY Tables

## 8.8 Understanding the Query Execution Plan

## 8.9 Controlling the Query Optimizer

## 8.10 Buffering and Caching

## 8.11 Optimizing Locking Operations

## 8.12 Optimizing the MySQL Server

## 8.13 Measuring Performance (Benchmarking)

## 8.14 Examining Thread Information

