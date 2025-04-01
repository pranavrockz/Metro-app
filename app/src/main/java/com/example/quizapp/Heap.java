package com.example.quizapp;
import java.util.ArrayList;
import java.util.HashMap;

public class Heap<T extends Comparable<T>>
{
    private ArrayList<T> data;
    private HashMap<T, Integer> map;

    public Heap() {
        this.data = new ArrayList<>();
        this.map = new HashMap<>();
    }

    public void add(T item)
    {
        data.add(item);
        int index = data.size() - 1;
        map.put(item, index);
        upheapify(index);
    }

    private int upheapify(int ci)
    {
        int exchangeCount = 0;
        int pi = (ci - 1) / 2;
        while (ci > 0 && data.get(ci).compareTo(data.get(pi)) < 0) {
            swap(ci, pi);
            exchangeCount++;
            ci = pi;
            pi = (ci - 1) / 2;
        }
        return exchangeCount;
    }

    private void swap(int i, int j)
    {
        T ith = data.get(i);
        T jth = data.get(j);

        data.set(i, jth);
        data.set(j, ith);
        map.put(ith, j);
        map.put(jth, i);
    }

    public void display()
    {
        System.out.println(data);
    }

    public int size()
    {
        return this.data.size();
    }

    public boolean isEmpty()
    {
        return this.size() == 0;
    }

    public T remove()
    {
        if (data.isEmpty()) {
            throw new IllegalStateException("Heap is empty");
        }
        T removedItem = data.get(0);
        T lastItem = data.remove(data.size() - 1);
        map.remove(removedItem);

        if (!data.isEmpty()) {
            data.set(0, lastItem);
            map.put(lastItem, 0);
            downheapify(0);
        }

        return removedItem;
    }

    private int downheapify(int pi)
    {
        int exchangeCount = 0;
        int size = data.size();
        while (true) {
            int minIdx = pi;
            int lci = 2 * pi + 1;
            int rci = 2 * pi + 2;

            if (lci < size && data.get(lci).compareTo(data.get(minIdx)) < 0) {
                minIdx = lci;
            }
            if (rci < size && data.get(rci).compareTo(data.get(minIdx)) < 0) {
                minIdx = rci;
            }
            if (minIdx != pi) {
                swap(minIdx, pi);
                exchangeCount++;
                pi = minIdx;
            } else {
                break;
            }
        }
        return exchangeCount;
    }

    public T get()
    {
        return this.data.get(0);
    }

    public int isLarger(T t, T o)
    {
        return t.compareTo(o);
    }

    public int updatePriority(T item)
    {
        int exchangeCount = 0;
        Integer index = map.get(item);
        if (index != null) {
            exchangeCount += upheapify(index);
            exchangeCount += downheapify(index);
        }
        return exchangeCount;

    }
}
