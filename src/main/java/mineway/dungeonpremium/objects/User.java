package mineway.dungeonpremium.objects;

import java.time.LocalDateTime;

public class User {

    private LocalDateTime lastReward;

    public User() {
        this.lastReward = LocalDateTime.now().plusDays(-1);
    }

    public User(LocalDateTime lastReward) {
        this.lastReward = lastReward;
    }

    public LocalDateTime getLastReward() {
        return lastReward;
    }

    public void setLastReward(LocalDateTime lastReward) {
        this.lastReward = lastReward;
    }


}
