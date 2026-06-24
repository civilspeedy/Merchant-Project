from tkinter import *
from tkinter.ttk import *
from tkinter import messagebox
import sqlite3
from Entry import HomeWindow


class Login_main:
    def __init__(self, master):
        # Constructor
        self.window = master
        self.window.title('login')
        self.window.iconbitmap('icon.ico')
        self.frame = Frame(self.window, padding=(5, 5, 5, 12))
        self.frame.grid(row=0, column=0, sticky=(N, W, E, S))
        self.username = ""
        self.id = None

        # Styles
        s = Style()
        s.configure('TFrame', background='black')
        s.configure('TLabel', foreground='white', background='black', font=("Trebuchet MS", 16))
        s.configure('TButton', font=("Trebuchet MS", 16), padding=-2)
        s.configure('TEntry', foreground='black', background='white', font=("Trebuchet MS", 16), padding=5)
        s.configure('TCheckbutton', foreground='white', background='black', font=("Trebuchet MS", 16), padding=5)

        # Widgets
        self.title = Label(self.frame, text="Login", font=("Trebuchet MS", 30)).grid(row=0, column=0)
        self.usernameLabel = Label(self.frame, text="Username:").grid(row=2, column=0, sticky=W)
        self.usernameEntry = Entry(self.frame, width=50)
        self.usernameEntry.grid(row=3, column=0, sticky=E + W)
        self.passwordLabel = Label(self.frame, text="Password:").grid(row=4, column=0, sticky=W)
        self.passwordEntry = Entry(self.frame, show='*', width=50)
        self.passwordEntry.grid(row=5, column=0)
        self.loginButton = Button(self.frame, text='Login:', command=self.validate).grid(row=6, column=0, sticky=W)
        self.signupButton = Button(self.frame, text='Signup', command=self.signupOpen).grid(row=6, column=0, sticky=E)

        # Methods

    def signupOpen(self):
        self.window.withdraw()
        Signup_main(self.window)

    def rememberMe(self):
        pass

    def validate(self):
        username = self.usernameEntry.get()
        password = self.passwordEntry.get()
        if username == '':
            messagebox.showerror('Username Missing', 'Please enter Username!')
            self.usernameEntry.focus_set()
        elif password == '':
            messagebox.showerror('Password Missing', 'Please enter Password')
        else:
            conn = sqlite3.connect('UserStore.db')
            c = conn.cursor()
            sql = """SELECT * FROM User
            WHERE Username = ? and Password = ?"""
            c.execute(sql, (username, password))
            user = c.fetchone()
            conn.close()
            self.username = username
            if user is None:
                messagebox.showerror("Login", "User not authenticated")
                self.usernameEntry.focus_set()
            else:
                userId = user[0]
                self.window.withdraw()
                HomeWindow(self.window, userId)


class Signup_main():
    def __init__(self, master):
        # Constructor
        self.master = master
        self.suWindow = Toplevel(self.master)
        self.suWindow.title('Sign Up')
        self.suWindow.iconbitmap('icon.ico')
        self.suWindow.protocol("WM_DELETE_WINDOW", self.close_window)
        self.frame = Frame(self.suWindow, padding=(5, 5, 5, 12))
        self.frame.grid(row=0, column=0, sticky=(N, W, E, S))

        # widgets
        Label(self.frame, text="Sign Up", font=("Trebuchet MS", 30)).grid(row=0, column=0)
        Label(self.frame, text="Username:").grid(row=1, column=0)
        self.usernameEntry = Entry(self.frame, width=50)
        self.usernameEntry.grid(row=2, column=0, sticky=E + W)
        Label(self.frame, text="Password:").grid(row=3, column=0)
        self.passwordEntry = Entry(self.frame, show='*', width=50)
        self.passwordEntry.grid(row=4, column=0)
        Button(self.frame, text='Sign Up', command=self.signup_create).grid(row=6, column=0,
                                                                            sticky=W)

    # Methods
    def signup_create(self):
        username = self.usernameEntry.get()
        password = self.passwordEntry.get()
        if username == '':
            messagebox.showerror('Username Missing', 'Please enter Username!')
            self.usernameEntry.focus_set()
        elif password == '':
            messagebox.showerror('Password Missing', 'Please enter Password')
        else:
            conn = sqlite3.connect('UserStore.db')
            c = conn.cursor()
            c.execute("""
            INSERT INTO User(Username, Password, Balance)
                VALUES(?, ?, 10000)""", (username, password))
            messagebox.showinfo("Register", "Details successfully added")
            conn.commit()
            self.close_window()

    def close_window(self):
        self.suWindow.destroy()
        self.master.deiconify()


def main():
    master = Tk()
    Login_main(master)
    master.mainloop()


main()
