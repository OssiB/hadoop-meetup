WITH Rankings AS (
SELECT Id,Reputation, Ranking = ROW_NUMBER() OVER(ORDER BY Reputation DESC),Location
FROM Users
)
,Counts AS (
SELECT Count = COUNT(*)
FROM Users
WHERE Reputation > 1
)
SELECT Id,Location,Reputation, Ranking, CAST(Ranking AS decimal(20, 5)) / (SELECT Count FROM Counts) AS Percentile
FROM Rankings where 
Location LIKE  '%##state##%';
