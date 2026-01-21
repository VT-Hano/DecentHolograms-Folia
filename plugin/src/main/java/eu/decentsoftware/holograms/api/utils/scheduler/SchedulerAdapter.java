package eu.decentsoftware.holograms.api.utils.scheduler;

import org.bukkit.plugin.Plugin;
import java.util.function.Consumer;

public interface SchedulerAdapter {
    void executeSync(Plugin plugin, Runnable runnable);

    void executeAsync(Plugin plugin, Runnable runnable);

    void executeAsyncLater(Plugin plugin, Runnable runnable, long delay);

    int scheduleSyncDelayedTask(Plugin plugin, Runnable runnable, long delay);

    int scheduleSyncRepeatingTask(Plugin plugin, Runnable runnable, long delay, long interval);

    int scheduleAsyncRepeatingTask(Plugin plugin, Runnable runnable, long delay, long interval);

    void cancelTask(int taskId);

    void cancelTasks(Plugin plugin);
}
