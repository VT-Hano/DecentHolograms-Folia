package eu.decentsoftware.holograms.api.utils.scheduler;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

public class BukkitSchedulerAdapter implements SchedulerAdapter {

    @Override
    public void executeSync(Plugin plugin, Runnable runnable) {
        Bukkit.getScheduler().runTask(plugin, runnable);
    }

    @Override
    public void executeAsync(Plugin plugin, Runnable runnable) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, runnable);
    }

    @Override
    public void executeAsyncLater(Plugin plugin, Runnable runnable, long delay) {
        Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, runnable, delay);
    }

    @Override
    public int scheduleSyncDelayedTask(Plugin plugin, Runnable runnable, long delay) {
        return Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, runnable, delay);
    }

    @Override
    public int scheduleSyncRepeatingTask(Plugin plugin, Runnable runnable, long delay, long interval) {
        return Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, runnable, delay, interval);
    }

    @Override
    public int scheduleAsyncRepeatingTask(Plugin plugin, Runnable runnable, long delay, long interval) {
        return Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, runnable, delay, interval).getTaskId();
    }

    @Override
    public void cancelTask(int taskId) {
        Bukkit.getScheduler().cancelTask(taskId);
    }

    @Override
    public void cancelTasks(Plugin plugin) {
        Bukkit.getScheduler().cancelTasks(plugin);
    }
}
