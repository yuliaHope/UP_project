SELECT users.name, COUNT(*) 
FROM chat.users AS users INNER JOIN messages ON users.id=messages.user_id WHERE date="2016-05-09" GROUP BY(users.id);