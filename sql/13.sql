SELECT users.name, COUNT(*) as count
FROM chat.users AS users INNER JOIN messages ON users.id=messages.user_id WHERE date>=CURDATE() GROUP BY(users.id) HAVING count>3;