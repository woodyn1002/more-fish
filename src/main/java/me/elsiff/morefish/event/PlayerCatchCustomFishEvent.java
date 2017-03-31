package me.elsiff.morefish.event;

import me.elsiff.morefish.CaughtFish;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerFishEvent;

public class PlayerCatchCustomFishEvent extends PlayerEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    private CaughtFish fish;
    private PlayerFishEvent fishEvent;

    public PlayerCatchCustomFishEvent(Player who, CaughtFish fish, PlayerFishEvent fishEvent) {
        super(who);
        this.fish = fish;
        this.fishEvent = fishEvent;
    }

    public CaughtFish getFish() {
        return fish;
    }

    public PlayerFishEvent getPlayerFishEvent() {
        return fishEvent;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }
}
