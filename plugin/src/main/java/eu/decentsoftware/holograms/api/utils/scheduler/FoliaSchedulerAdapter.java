package eu.decentsoftware.holograms.api.utils.scheduler;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class FoliaSchedulerAdapter implements SchedulerAdapter {

    private final Map<Integer, Object> tasks = new ConcurrentHashMap<>();
    private int currentId = 0;

    private synchronized int getNextId() {
        return currentId++;
    }

    private Object getGlobalRegionScheduler() {
        try {
            Method method = Bukkit.class.getMethod("getGlobalRegionScheduler");
            return method.invoke(null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Object getAsyncScheduler() {
        try {
            Method method = Bukkit.class.getMethod("getAsyncScheduler");
            return method.invoke(null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void executeSync(Plugin plugin, Runnable runnable) {
        try {
            Object scheduler = getGlobalRegionScheduler();
            Method execute = scheduler.getClass().getMethod("execute", Plugin.class, Runnable.class);
            execute.invoke(scheduler, plugin, runnable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void executeAsync(Plugin plugin, Runnable runnable) {
        try {
            Object scheduler = getAsyncScheduler();
            Method runNow = scheduler.getClass().getMethod("runNow", Plugin.class, Consumer.class);
            runNow.invoke(scheduler, plugin, (Consumer<Object>) task -> runnable.run());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void executeAsyncLater(Plugin plugin, Runnable runnable, long delay) {
        try {
            Object scheduler = getAsyncScheduler();
            Method runDelayed = scheduler.getClass().getMethod("runDelayed", Plugin.class, Consumer.class, long.class,
                    TimeUnit.class);
            runDelayed.invoke(scheduler, plugin, (Consumer<Object>) task -> runnable.run(), delay * 50,
                    TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int scheduleSyncDelayedTask(Plugin plugin, Runnable runnable, long delay) {
        int id = getNextId();
        try {
            Object scheduler = getGlobalRegionScheduler();
            Method runDelayed = scheduler.getClass().getMethod("runDelayed", Plugin.class, Consumer.class, long.class);
            Object task = runDelayed.invoke(scheduler, plugin, (Consumer<Object>) t -> {
                runnable.run();
                tasks.remove(id);
            }, delay);
            tasks.put(id, task);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    @Override
    public int scheduleSyncRepeatingTask(Plugin plugin, Runnable runnable, long delay, long interval) {
        int id = getNextId();
        try {
            Object scheduler = getGlobalRegionScheduler();
            Method runAtFixedRate = scheduler.getClass().getMethod("runAtFixedRate", Plugin.class, Consumer.class,
                    long.class, long.class);
            Object task = runAtFixedRate.invoke(scheduler, plugin, (Consumer<Object>) t -> runnable.run(), delay,
                    interval);
            tasks.put(id, task);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    @Override
    public int scheduleAsyncRepeatingTask(Plugin plugin, Runnable runnable, long delay, long interval) {
        int id = getNextId();
        try {
            Object scheduler = getAsyncScheduler();
            Method runAtFixedRate = scheduler.getClass().getMethod("runAtFixedRate", Plugin.class, Consumer.class,
                    long.class, long.class, TimeUnit.class);
            Object task = runAtFixedRate.invoke(scheduler, plugin, (Consumer<Object>) t -> runnable.run(), delay * 50,
                    interval * 50, TimeUnit.MILLISECONDS);
            tasks.put(id, task);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    @Override
    public void cancelTask(int taskId) {
        Object task = tasks.remove(taskId);
        if (task != null) {
            try {
                Method cancel = task.getClass().getMethod("cancel");
                cancel.invoke(task);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void cancelTasks(Plugin plugin) {
        try {
            Object globalScheduler = getGlobalRegionScheduler();
            globalScheduler.getClass().getMethod("cancelTasks", Plugin.class).invoke(globalScheduler, plugin);

            Object asyncScheduler = getAsyncScheduler();
            asyncScheduler.getClass().getMethod("cancelTasks", Plugin.class).invoke(asyncScheduler, plugin);
        } catch (Exception e) {
            e.printStackTrace();
        }
        tasks.clear();
    }
}
