SELECT users.name, COUNT(*) as count
FROM chat.users AS users INNER JOIN messages ON users.id=messages.user_id GROUP BY(users.id) HAVING count>3;