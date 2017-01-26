/*
* examples for announcement insertion using mongo shell
*/

//low
db.announcements.insert({"versions": "[0, 1.0.0]", "category": "app", "priority": "low", "content": {"de": "<html><body><h2>test</h2><\/body><\/html>", "en": "<html><body><h2>test en</h2><\/body><\/html>"}})

//medium
db.announcements.insert({"versions": "[0, 1.0.0]", "category": "app", "priority": "medium", "content": {"de": "<html><body><h2>test</h2><\/body><\/html>", "en": "<html><body><h2>test en</h2><\/body><\/html>"}})

//high
db.announcements.insert({"versions": "[0, 1.0.0]", "category": "app", "priority": "high", "content": {"de": "<html><body><h2>test</h2><\/body><\/html>", "en": "<html><body><h2>test en</h2><\/body><\/html>"}})
