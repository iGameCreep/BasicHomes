package fr.gamecreep.basichomes.entities.accounts;

import lombok.Data;

@Data
public class PlayerAccount {
    private int userId;
    private String password;
    private String rank;

    public PlayerAccount(int userId, String password, String rank) {
        setUserId(userId);
        setPassword(password);
        setRank(rank);
    }

    public PlayerAccount(int userId) {
        setUserId(userId);
    }
}
