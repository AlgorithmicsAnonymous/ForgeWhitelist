package com.algorithmicsanonymous.forgewhitelist.common.util;

import java.util.*;

public class CachedSet<T> implements Set<T> {
    private final HashMap<T, Long> expireTimeMap = new HashMap<>();
    private final int expireTime;

    public CachedSet(int expireTime) {
        this.expireTime = expireTime;
    }

    public void cleanup() {
        Iterator<Map.Entry<T, Long>> i = expireTimeMap.entrySet().iterator();
        long now = System.currentTimeMillis();
        while (i.hasNext()) {
            Map.Entry<T, Long> e = i.next();
            if (e.getValue() > now) i.remove();
        }
    }

    @Override
    public int size() {
        cleanup();
        return expireTimeMap.size();
    }

    @Override
    public boolean isEmpty() {
        cleanup();
        return expireTimeMap.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        cleanup();
        return expireTimeMap.containsKey(o);
    }

    @Override
    public Iterator<T> iterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean add(T t) {
        return expireTimeMap.put(t, System.currentTimeMillis() + expireTime) != null;
    }

    @Override
    public boolean remove(Object o) {
        return expireTimeMap.remove(o) != null;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return expireTimeMap.keySet().containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        long time = System.currentTimeMillis() + expireTime;
        for (T t : c) expireTimeMap.put(t, time);
        return !c.isEmpty();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return expireTimeMap.keySet().retainAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return expireTimeMap.keySet().removeAll(c);
    }

    @Override
    public void clear() {
        expireTimeMap.clear();
    }
}
