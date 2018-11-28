package Cache;

public interface LevelCache {
    long CacheSize = 0;

    public int getRemainingCapacity();
    public int putObject(Object object);
    public Object getObject(int hashCode);

}
