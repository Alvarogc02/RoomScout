package rsObjetos;

public class Usuario {
    private String nick;
    private String password;
    private boolean isAdmin;

    public Usuario(String nick, String password, boolean isAdmin) {
        this.nick = nick;
        this.password = password;
        this.isAdmin = isAdmin;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    @Override
    public String toString() {
        return nick;
    }
}