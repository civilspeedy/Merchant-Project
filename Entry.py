from tkinter import *
from tkinter.ttk import *
from tkinter import messagebox
import sqlite3
import matplotlib.pyplot as plt
import pandas as pd
import pandas_datareader as web
import datetime as dt
from decimal import *
from matplotlib.backends.backend_tkagg import FigureCanvasTkAgg


class HomeWindow:
    def __init__(self, master, userId):
        self.id = userId
        self.compList = ["TSLA", "FB", "AAPL", "GME", "AMZN", "SPOT", "QRTEA", "ZM", "MSFT", "ARM", "RR", "VOD", "EVE.L", "NFLX", "GOOGL"]
        self.comp = None
        self.values = None
        self.balance = 0
        self.getBal()
        # self.constructTable()
        self.master = master
        self.window = Toplevel(self.master)
        self.window.title('Merchant')
        self.window.geometry("1200x720")
        self.window.config(bg="black")
        self.window.iconbitmap("icon.ico")
        # https://tkdocs.com/tutorial/grid.html
        self.window.columnconfigure(0, weight=4)
        self.window.columnconfigure(1, weight=1)
        self.frame1 = Frame(self.window, padding=(5, 5, 5, 12))
        self.frame1.grid(row=0, columnspan=4)
        self.frame2 = Frame(self.window, padding=(5, 5, 5, 12))
        self.frame2.grid(row=1, columnspan=2)
        self.frame3 = Frame(self.window, padding=(5, 5, 5, 12))
        self.frame3.grid(row=1, column=1)

        # widgets
        self.button = Button(self.frame1, text="Select", command=self.comboFetch)
        self.button.grid(row=0, column=3)
        self.select = Combobox(self.frame1, values=self.compList, state="readonly")
        self.select.grid(row=0, column=2)
        self.value = Label(self.frame3, text="Value:\n£")
        self.value.grid(row=1, column=0)
        self.buy = Button(self.frame3, text="Buy", command=self.b)
        self.buy.grid(row=0, column=0)
        self.sell = Button(self.frame3, text="Sell", command=self.s)
        self.sell.grid(row=2, column=0)
        self.balanceLabel = Label(self.frame1, text=f"Balance:£{self.balance}")
        self.balanceLabel.grid(row=0, column=1)
        self.owned = Button(self.frame1, text="Owned Shares", command=self.view)
        self.owned.grid(row=0, column=0)

    def constructTable(self):
        id = self.id
        conn = sqlite3.connect("UserStore.db")
        c = conn.cursor()
        for i in range(len(self.compList)):
            c.execute("""INSERT INTO UserShares(USERID, Share, Volume)
            VALUES(?,?,?)""", (id, self.compList[i], 0,))
            conn.commit()

    def view(self):
        conn = sqlite3.connect('UserStore.db')
        c = conn.cursor()
        c.execute("""SELECT Share FROM UserShares
        WHERE USERID = ? AND Volume > 0""", (self.id,))
        shares = c.fetchall()
        if shares is None:
            messagebox.showinfo("Shares", "None")
        else:
            messagebox.showinfo("Shares", f"{shares}")
        conn.commit()

    def getBal(self):
        conn = sqlite3.connect('UserStore.db')
        c = conn.cursor()
        sql = """SELECT Balance FROM User
                    WHERE ID = ?"""
        c.execute(sql, (self.id,))
        self.balance = c.fetchone()
        self.balance = self.balance[0]
        self.balance = round(Decimal(self.balance), 2)
        conn.close()

    def comboFetch(self):
        self.comp = self.select.get()
        if self.comp == "":
            messagebox.showerror("No Entry", "Please select a company to continue")
        else:
            self.fetch()

    def fetch(self):
        # fetches prices
        start = dt.datetime(2021, 2, 1)
        end = dt.datetime.now()
        value = web.DataReader(f'{self.comp}', 'yahoo', start, end)
        value.to_csv("table.csv")
        close = pd.read_csv("table.csv", usecols=["Close"], skipinitialspace=True)
        find = close.iloc[len(close) - 1]
        self.values = round(Decimal(find[0]), 3)
        self.value.configure(text=f"Value:\n£{self.values:.3f}")

        # https://pythonprogramming.net/how-to-embed-matplotlib-graph-tkinter-gui/
        # draws graph
        figure1 = plt.Figure(figsize=(5, 5), dpi=90)
        ax1 = figure1.add_subplot(111)
        bar1 = FigureCanvasTkAgg(figure1, self.frame2)
        bar1.get_tk_widget().grid(row=1, column=1)
        close.plot(kind='line', legend=False, ax=ax1)

    def b(self):
        self.getBal()
        value = self.values
        comp = self.comp
        if self.values is None:
            messagebox.showerror(" ", "Please select from the menu!")
        if value > self.balance:
            messagebox.showerror("Value Too High", "Price exceeds your balance")
        else:
            self.balance = (Decimal(self.balance) - value)
            conn = sqlite3.connect('UserStore.db')
            self.balance = float(self.balance)
            c = conn.cursor()
            c.execute("""UPDATE User
                   SET Balance = ?
                   WHERE ID = ?""", (self.balance, self.id,))
            c.execute("""SELECT Balance FROM User
            WHERE ID = ?""", (self.id,))
            self.balance = c.fetchone()
            self.balanceLabel.configure(text=f"Balance:£{self.balance}")

            # Checks to see if a share has already been bought
            c.execute("""SELECT * FROM UserShares 
            WHERE USERID = ? AND Share = ?""", (self.id, comp,))
            record = c.fetchone()
            if record is None:
                c.execute("""INSERT INTO UserShares(USERID, Share, Volume)
                            VALUES(?,?,?)""", (self.id, comp, 1))
            else:
                volume = record[3] + 1
                userShareId = record[0]
                c.execute("""UPDATE UserShares
                SET Volume = ?
                WHERE ID = ?""", (volume, userShareId,))
            conn.commit()

    def s(self):
        conn = sqlite3.connect("UserStore.db")
        c = conn.cursor()
        c.execute("""SELECT * FROM UserShares
        WHERE USERID = ? AND Share = ?""", (self.id, self.comp,))
        hold = c.fetchall()
        conn.commit()
        if hold is True:
            hold = hold[0]
        if hold is None:
            messagebox.showerror("", "You do not own any shares in this company")
        else:
            c.execute("""SELECT Volume FROM UserShares
            WHERE USERID = ? AND Share = ?""", (self.id, self.comp,))
            volume = c.fetchone()
            if volume is None:
                volume = 0
            else:
                volume = volume[0]
            conn.commit()
            if volume < 1:
                messagebox.showerror("", "You do not own any shares in this company")
                c.execute("""UPDATE UserShares
                SET Volume = ?
                WHERE USERID = ? AND Share = ?""", (0, self.id, self.comp))
                conn.commit()
            else:
                volume = volume - 1
                c.execute("""UPDATE UserShares
                SET Volume = ?
                WHERE USERID = ? AND Share = ?""", (volume, self.id, self.comp))
                self.balance = Decimal(self.balance)
                self.balance = self.balance + self.values
                self.balance = float(self.balance)
                c.execute("""UPDATE User
                                   SET Balance = ?
                                   WHERE ID = ?""", (self.balance, self.id,))
                c.execute("""SELECT Balance FROM User
                            WHERE ID = ?""", (self.id,))
                conn.commit()
                self.balance = c.fetchone()
                self.balance = round(Decimal(self.balance[0]), 2)
                self.balanceLabel.configure(text=f"Balance:£{self.balance}")
