package com.github.chengtengfei.onvif.model;

public class UsernameToken {

    private String username;
    private String password;
    private String nonce;
    private String created;


    private UsernameToken(Builder builder) {
        setUsername(builder.username);
        setPassword(builder.password);
        setNonce(builder.nonce);
        setCreated(builder.created);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public UsernameToken() {
    }

    public UsernameToken(String username, String password, String nonce, String created) {
        this.username = username;
        this.password = password;
        this.nonce = nonce;
        this.created = created;
    }


    public static final class Builder {
        private String username;
        private String password;
        private String nonce;
        private String created;

        public Builder() {
        }

        public Builder username(String val) {
            username = val;
            return this;
        }

        public Builder password(String val) {
            password = val;
            return this;
        }

        public Builder nonce(String val) {
            nonce = val;
            return this;
        }

        public Builder created(String val) {
            created = val;
            return this;
        }

        public UsernameToken build() {
            return new UsernameToken(this);
        }
    }
}
