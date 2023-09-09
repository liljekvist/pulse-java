package se.bth.pulse.Utility;

public class PasswordPair {
    PasswordPair(byte[] _hashed_password, byte[] _salt){
        this.hashed_password = _hashed_password;
        this.salt = _salt;
    }
    private byte[] hashed_password;
    private byte[] salt;

    public byte[] getHashed_password() {
        return hashed_password;
    }

    public void setHashed_password(byte[] hashed_password) {
        this.hashed_password = hashed_password;
    }

    public byte[] getSalt() {
        return salt;
    }

    public void setSalt(byte[] salt) {
        this.salt = salt;
    }
}
