import sqlite3


def main(dbname):
    conn = sqlite3.connect(dbname)
    c = conn.cursor()
    c.execute("""
    CREATE TABLE User(
        ID INTEGER PRIMARY KEY AUTOINCREMENT, 
        Username VARCHAR(20),
        Password VARCHAR(20),
        Balance FLOAT);""")
    c.execute("""CREATE TABLE UserShares(
                            ID INTEGER PRIMARY KEY AUTOINCREMENT,
                            USERID INTEGER,
                            Share Varchar(20),
                            Volume INTEGER,
                            UNIQUE(ID, Share, Volume));""")
    conn.commit()
    conn.close()


main('UserStore.db')
