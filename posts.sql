WITH Rankings AS (
SELECT Id,Reputation, Ranking = ROW_NUMBER() OVER(ORDER BY Reputation DESC),Location
FROM Users
)
,Counts AS (
SELECT Count = COUNT(*)
FROM Users
WHERE Reputation > 10
)
SELECT * FROM POSTS WHERE OwnerUserId in
(SELECT Id
FROM Rankings where 
Location LIKE  '%##state##%') and score >10;
