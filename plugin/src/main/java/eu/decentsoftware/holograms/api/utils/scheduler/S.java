package eu.decentsoftware.holograms.api.utils.scheduler;

import eu.decentsoftware.holograms.api.DecentHolograms;
import eu.decentsoftware.holograms.api.DecentHologramsAPI;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.plugin.IllegalPluginAccessException;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.concurrent.CompletableFuture;

@UtilityClass
public class S {

    private static final DecentHolograms DECENT_HOLOGRAMS = DecentHologramsAPI.get();
    private static final SchedulerAdapter ADAPTER;

    static {
        boolean isFolia = false;
        try {
            Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
            isFolia = true;
        } catch (ClassNotFoundException e) {
            // loose check
        }

        if (isFolia) {
            ADAPTER = new FoliaSchedulerAdapter();
        } else {
            ADAPTER = new BukkitSchedulerAdapter();
        }
    }

    public static void stopTask(int id) {
        ADAPTER.cancelTask(id);
    }

    public static void sync(Runnable runnable) {
        ADAPTER.executeSync(DECENT_HOLOGRAMS.getPlugin(), runnable);
    }

    public static BukkitTask sync(Runnable runnable, long delay) {
        int id = ADAPTER.scheduleSyncDelayedTask(DECENT_HOLOGRAMS.getPlugin(), runnable, delay);
        return new BukkitTask() {
            @Override
            public void cancel() {
                ADAPTER.cancelTask(id);
            }

            @Override
            public boolean isCancelled() {
                return false; // Not easily supported without more complex tracking
            }

            @Override
            public boolean isSync() {
                return true;
            }

            @Override
            public Plugin getOwner() {
                return DECENT_HOLOGRAMS.getPlugin();
            }

            @Override
            public int getTaskId() {
                return id;
            }
        };
    }

    public static BukkitTask syncTask(Runnable runnable, long interval) {
        int id = ADAPTER.scheduleSyncRepeatingTask(DECENT_HOLOGRAMS.getPlugin(), runnable, 0, interval);
        return new BukkitTask() {
            @Override
            public void cancel() {
                ADAPTER.cancelTask(id);
            }

            @Override
            public boolean isCancelled() {
                return false;
            }

            @Override
            public boolean isSync() {
                return true;
            }

            @Override
            public Plugin getOwner() {
                return DECENT_HOLOGRAMS.getPlugin();
            }

            @Override
            public int getTaskId() {
                return id;
            }
        };
    }

    public static void async(Runnable runnable) {
        ADAPTER.executeAsync(DECENT_HOLOGRAMS.getPlugin(), runnable);
    }

    public static void async(Runnable runnable, long delay) {
        ADAPTER.executeAsyncLater(DECENT_HOLOGRAMS.getPlugin(), runnable, delay);
    }

    public static BukkitTask asyncTask(Runnable runnable, long interval) {
        int id = ADAPTER.scheduleAsyncRepeatingTask(DECENT_HOLOGRAMS.getPlugin(), runnable, 0, interval);
        return new BukkitTask() {
            @Override
            public void cancel() {
                ADAPTER.cancelTask(id);
            }

            @Override
            public boolean isCancelled() {
                return false;
            }

            @Override
            public boolean isSync() {
                return false;
            }

            @Override
            public Plugin getOwner() {
                return DECENT_HOLOGRAMS.getPlugin();
            }

            @Override
            public int getTaskId() {
                return id;
            }
        };
    }

    public static BukkitTask asyncTask(Runnable runnable, long interval, long delay) {
        int id = ADAPTER.scheduleAsyncRepeatingTask(DECENT_HOLOGRAMS.getPlugin(), runnable, delay, interval);
        return new BukkitTask() {
            @Override
            public void cancel() {
                ADAPTER.cancelTask(id);
            }

            @Override
            public boolean isCancelled() {
                return false;
            }

            @Override
            public boolean isSync() {
                return false;
            }

            @Override
            public Plugin getOwner() {
                return DECENT_HOLOGRAMS.getPlugin();
            }

            @Override
            public int getTaskId() {
                return id;
            }
        };
    }

    /**
     * Cancel all tasks.
     */
    public static void cancelAll() {
        ADAPTER.cancelTasks(DECENT_HOLOGRAMS.getPlugin());
    }

}
