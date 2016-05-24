SELECT messages.text, messages.date, users.name
FROM chat.users AS users INNER JOIN messages ON users.id=messages.user_id WHERE text LIKE "%2%";