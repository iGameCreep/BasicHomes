package fr.gamecreep.basichomes.entities.accounts;

import lombok.Data;

@Data
public class PlayerAccount {
    private int userId;
    private String password;

    public PlayerAccount(int userId, String password) {
        setUserId(userId);
        setPassword(password);
    }

    public PlayerAccount(int userId) {
        setUserId(userId);
    }
}
