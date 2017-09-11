import sqlite3
from random import randrange

class DataGenerator:
    def __init__(self, count):
        self.count = count
        self.generated = 0
        with open('words.txt', 'r') as f:
            self.words = f.readlines()

    def __iter__(self):
        return self

    def next(self):
        if self.generated == self.count:
            raise StopIteration
        self.generated += 1
        wordlen = len(self.words)
        title = map(lambda x: self.words[randrange(0, wordlen)].strip(), xrange(3))
        content = map(lambda x: self.words[randrange(0, wordlen)].strip(), xrange(30))
        return (' '.join(title), ' '.join(content))
        

if __name__ == '__main__':
    conn = sqlite3.connect('app/src/main/assets/database.sqlite')
    
    c = conn.cursor()
    c.execute('''
    CREATE TABLE IF NOT EXISTS data (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        title TEXT NOT NULL,
        content TEXT NOT NULL
    )
    ''')

    c.execute("DELETE FROM data")
    c.executemany("INSERT INTO data (title, content) VALUES (?, ?)", DataGenerator(100000))
    

    conn.commit()
    conn.close()
